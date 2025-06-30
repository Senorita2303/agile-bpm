package com.dstz.component.pubsub.local;

import com.dstz.base.common.utils.CastUtils;
import com.dstz.component.pubsub.AbMessagePublisher;
import com.dstz.component.pubsub.AbMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Publish and subscribe based on local implementation, pure memory
 *
 */
public class AbLocalMessagePublishProvider implements AbMessagePublisher {

	private static final Logger logger = LoggerFactory.getLogger(AbLocalMessagePublishProvider.class);

	private final Collection<AbMessageSubscriber<?>> subscribers;

	public AbLocalMessagePublishProvider(Collection<AbMessageSubscriber<?>> subscribers) {
		this.subscribers = subscribers;
	}

	@Override
	public void publish(String channel, Object body) {
		for (AbMessageSubscriber<?> subscriber : subscribers) {
			if (subscriber.getChannel().equals(channel)) {
				try {
					subscriber.subscribe(CastUtils.cast(body));
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
}
