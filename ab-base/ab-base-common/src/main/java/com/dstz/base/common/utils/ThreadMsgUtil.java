package com.dstz.base.common.utils;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Message Util tool class, add messages to thread variables, and use list to store messages.
 * 
 */
public class ThreadMsgUtil {
	private static ThreadLocal<List<String>> localMsg = new ThreadLocal<>();
	
	static {
		ContextCleanUtils.register(localMsg::remove, ContextCleanUtils.Phase.THREAD, ContextCleanUtils.Phase.REQUEST_COMPLETE);
	}
	
	/**
	 * Add a message.
	 *
	 * @param msg
	 */
	public static void addMsg(String msg) {
		List<String> list = localMsg.get();
		if (CollectionUtil.isEmpty(list)) {
			list = new ArrayList<>();
			localMsg.set(list);
		}
		list.add(msg);
	}

	/**
	 * Get the message data and clear the data in the message directly.
	 *
	 * @return
	 */
	public static List<String> getMsg() {
		return getMsg(true);
	}

	/**
	 * Get message data.
	 *
	 * @param clean
	 * @return
	 */
	public static List<String> getMsg(boolean clean) {
		List<String> list = localMsg.get();
		if (clean) {
			localMsg.remove();
		}
		return list;
	}

	/**
	 * Returns the process message.
	 *
	 * @return
	 */
	public static String getMessage() {
		return getMessage(true);
	}

	/**
	 * Get the message.
	 *
	 * @param clean
	 * @return
	 */
	public static String getMessage(boolean clean) {
		return getMessage(clean, "\r\n");
	}

	public static String getMessage(boolean clean, String lineBreak) {
		List<String> list = getMsg(clean);
		if (CollectionUtil.isEmpty(list)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String msg : list) {
			sb.append(msg).append(lineBreak);
		}
		return sb.toString();
	}

	/**
	 * Clear message.
	 */
	public static void clean() {
		localMsg.remove();
	}
}
