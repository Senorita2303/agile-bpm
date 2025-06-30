package com.dstz.base.common.constats;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description Enumeration constants for CMS station messages
 */
public enum InnerMsgEnum {
	/**
	 * Internal message type - News
	 */
	NEWS("news", "News", 0),

	/**
	 * 站内信类型-公告
	 */
	NOTIFY("notify", "公告", 0),

	/**
	 * 站内信类型-行程安排
	 */
	SCHEDULE("schedule", "行程安排", 0),

	/**
	 * 站内信类型-流程的评论
	 */
	WF_COMMENT("wfComment", "流程评论", 0),

	/**
	 * 站内信类型-流程待办
	 */
	WF_TODO("wfTodo", "流程待办", 1),

	/**
	 * 站内信类型-流程催办
	 */
	WF_URGE("wfUrge", "流程催办", 0),
	/**
	 * 站内信类型-流程督办
	 */
	WF_SUPERVISE("wfSupervise", "流程督办", 0),

	/**
	 * 站内信类型-流程抄送
	 */
	WF_COPY("wfCopy", "流程抄送", 0),

	/**
	 * 站内信类型-消息通知
	 */
	WF_NODE_MESSAGE("wfNodeMessage", "节点消息通知", 1),

	/**
	 * 站内信类型-流程驳回
	 */
	WF_REJECT("wfReject", "流程驳回", 1),

	/**
	 * 站内信类型-流程结束
	 */
	WF_OVER("wfOver", "流程结束", 1),

	/**
	 * 站内信类型-流程任务转办
	 */
	WF_TASK_TURN("taskTurn", "任务转办", 1),

	WF_CARBON_COPY("carbonCopy", "流程传阅", 0),

	WF_RECALL("wfRecall", "流程撤回", 0),

	SYS_ERROR("sysError", "System abnormality alarm", 0),

	/**
	 * Tenant visits
	 */
	TENANT_PV("tenantPv", "Tenant visits", 0);

	private final String key;
	private final String value;
	private final int type;

	/**
	 * Construction method
	 *
	 * @param key   Type code (value stored in the database)
	 * @param value Type text description
	 * @param type  Notification classification by type (the seven sub-types are further divided into two major categories: 0 Notification 1 To-do)
	 */
	InnerMsgEnum(String key, String value, int type) {
		this.key = key;
		this.value = value;
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}

	public int getType() {
		return type;
	}

	public static InnerMsgEnum getByKey(String key) {
		for (InnerMsgEnum e : InnerMsgEnum.values()) {
			if (key.equals(e.key + "")) {
				return e;
			}
		}
		return null;
	}

	public static List<String> getKeyListByType(int type) {
		return Arrays.stream(InnerMsgEnum.values()).filter(s -> s.getType() == type).map(InnerMsgEnum::getKey)
				.collect(Collectors.toList());
	}

}
