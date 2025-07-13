package com.dstz.component.pubsub;

/**
 * Message Subscribers
 *
 */
public interface AbMessageSubscriber<T> {

	/**
	 * Get the message subscription channel
	 *
	 * @return Message subscription channel
	 */
	String getChannel();

	/**
	 * Subscribe to News
	 * 
	 * @param body Subscription message body
	 */
	void subscribe(T body);
}
