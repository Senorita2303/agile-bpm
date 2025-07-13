package com.dstz.component.j2cache.redis;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.j2cache.constant.J2CacheBroadcastPropertyKeyConstant;
import net.oschina.j2cache.CacheProviderHolder;
import net.oschina.j2cache.Command;
import net.oschina.j2cache.cluster.ClusterPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Properties;

/**
 * Spring Redis key invalidation notification strategy
 *
 */
public class RedisPubSubClusterPolicy implements ClusterPolicy, MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisPubSubClusterPolicy.class);

    private final int LOCAL_COMMAND_ID = Command.genRandomSrc();

    private RedisTemplate<String, Object> redisTemplate;

    private CacheProviderHolder holder;

    /**
     * Message and subscription channel names
     */
    private String channel;

    /**
     * Broadcast on
     */
    private boolean broadcastOpen;

    @SuppressWarnings("unchecked")
    @Override
    public void connect(Properties props, CacheProviderHolder holder) {
        this.holder = holder;
        this.broadcastOpen = BooleanUtil.toBoolean(props.getProperty(J2CacheBroadcastPropertyKeyConstant.OPEN));
        // Get the message channel name
        this.channel = props.getProperty(J2CacheBroadcastPropertyKeyConstant.CHANNEL);
        if (this.broadcastOpen) {
            redisTemplate = RedisTemplateUtils.createRedisTemplate();
            // Registering a Listener
            final String redisMessageListenerContainer = props.getProperty(J2CacheBroadcastPropertyKeyConstant.REDIS_MESSAGE_LISTENER_CONTAINER_BEAN_ID);
            (StrUtil.isEmpty(redisMessageListenerContainer) ?
                    SpringUtil.getBean(RedisMessageListenerContainer.class) :
                    SpringUtil.getBean(redisMessageListenerContainer, RedisMessageListenerContainer.class)).addMessageListener(this, ChannelTopic.of(this.channel));
        }
    }

    @Override
    public void publish(Command cmd) {
        cmd.setSrc(LOCAL_COMMAND_ID);
        if (logger.isDebugEnabled()) {
            logger.debug("broadcastOpen: {}, publish cmd:{}", broadcastOpen, JsonUtils.toJSONString(cmd));
        }
        if (broadcastOpen) {
            redisTemplate.convertAndSend(channel, JsonUtils.toJSONBytes(cmd));
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (logger.isDebugEnabled()) {
            logger.debug("receive command {}", StringRedisSerializer.UTF_8.deserialize(message.getBody()));
        }
        handleCommand(JsonUtils.parseObject(message.getBody(), Command.class));
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void evict(String region, String... keys) {
        holder.getLevel1Cache(region).evict(keys);
    }

    @Override
    public void clear(String region) {
        holder.getLevel1Cache(region).clear();
    }

    @Override
    public boolean isLocalCommand(Command cmd) {
        return cmd.getSrc() == LOCAL_COMMAND_ID;
    }
}
