package com.dstz.component.mq.engine.consumer;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.api.JmsHandler;
import com.dstz.component.mq.api.constants.JmsDestinationConstant;
import com.dstz.component.mq.api.model.JmsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Redis message queue consumption
 *
 */
@SuppressWarnings("ALL")
public class RedisMessageQueueConsumer extends AbstractMessageQueue implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageQueueConsumer.class);

    /**
     * Name in the reidsTemplate container
     */
    private String redisTemplateBeanName;

    /**
     * Message Queues
     */
    private BoundListOperations<String, Object> messageQueue;

    /**
     * thread poll executor
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * redis queue listener
     */
    private RedisQueueListener redisQueueListener;


    public void setRedisTemplateBeanName(String redisTemplateBeanName) {
        this.redisTemplateBeanName = redisTemplateBeanName;
    }

    @Override
    public void destroy() throws Exception {
        redisQueueListener.interrupt();
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void containerInitialCompleteAfter() {
        if (StrUtil.isEmpty(this.redisTemplateBeanName)) {
            this.messageQueue = getApplicationContext().getBean(RedisTemplate.class).boundListOps(JmsDestinationConstant.DEFAULT_NAME);
        } else {
            this.messageQueue = getApplicationContext().getBean(redisTemplateBeanName, RedisTemplate.class).boundListOps(JmsDestinationConstant.DEFAULT_NAME);
        }
        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            try {
                if (!executor.isShutdown()) {
                    executor.getQueue().put(r);
                }
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        };
        threadPoolExecutor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2 + 1, 5, TimeUnit.MINUTES, new SynchronousQueue<>(), ThreadFactoryBuilder.create().setNamePrefix("redis-queue-handler-").build(), rejectedExecutionHandler);
        redisQueueListener = new RedisQueueListener();
        redisQueueListener.setDaemon(true);
        redisQueueListener.start();
    }


    /**
     * Redis Queue Listener
     */
    private class RedisQueueListener extends Thread {


        private void submitData(Object jmsDTO) {
            if (Objects.isNull(jmsDTO)) {
                LOGGER.warn("redis queue {} pop data is null", JmsDestinationConstant.DEFAULT_NAME);
                return;
            }
            if (!(jmsDTO instanceof JmsDTO)) {
                LOGGER.warn("Types of data that cannot be processed class: {}", jmsDTO.getClass());
                return;
            }
            JmsDTO<Serializable> data = (JmsDTO<Serializable>) jmsDTO;
            JmsHandler<Serializable> jmsHandler = getJmsHandler(data.getType());
            if (Objects.isNull(jmsHandler)) {
                LOGGER.warn("{} no handler", data.getType());
                return;
            }
            threadPoolExecutor.execute(() -> {
                try {
                    jmsHandler.handlerMessage(data);
                } catch (Exception e) {
                    LOGGER.error("{}, msgType: {} processing error, processing data: {}", Thread.currentThread().getName(), data.getType(), JsonUtils.toJSONString(data), e);
                }
            });
        }


        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    submitData(messageQueue.leftPop(RandomUtil.getRandom().nextInt(800, 1000), TimeUnit.MILLISECONDS));
                } catch (Exception e) {
                    LOGGER.warn("listen redis queue {} exception", JmsDestinationConstant.DEFAULT_NAME, e);
                }
            }
        }
    }
}
