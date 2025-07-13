package com.dstz.component.pubsub;

/**
 * ab message News Release
 *
 */
public interface AbMessagePublisher {

	/**
	 * News Release
	 *
	 * @param channel Release Channel
	 * @param body    Message body
	 */
	void publish(String channel, Object body);

}
