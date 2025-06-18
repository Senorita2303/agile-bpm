package com.dstz.component.msg.api;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.component.msg.api.dto.ExternalMsgDTO;
import com.dstz.component.msg.api.dto.MsgDTO;

import java.util.List;

/**
 * Message sending implementation
 *
 */
public interface MsgApi {

	/**
	 * Send message
	 *
	 * @param msgDTO
	 */
	void sendMsg(MsgDTO msgDTO);

	/**
	 * Send messages in batches
	 *
	 * @param msgDTOList
	 */
	void sendMsg(List<MsgDTO> msgDTOList);

	/**
	 * Send external messages, not dependent on current user information
	 * @param extMsgDTO
	 */
	void sendExtMsg(ExternalMsgDTO extMsgDTO);

	/**
	 * Send external messages in batches, not dependent on current user information
	 * @param extMsgDTOList
	 */
	void sendExtMsg(List<ExternalMsgDTO> extMsgDTOList);

	/**
	 * Send message synchronously
	 *
	 * @param msgDTO
	 */
	void syncSendMsg(MsgDTO msgDTO);

	/**
	 * Send messages in batches synchronously
	 *
	 * @param msgDTOList
	 */
	void syncSendMsg(List<MsgDTO> msgDTOList);

	/**
	 * Send external messages synchronously, not dependent on current user information
	 * @param extMsgDTO
	 */
	void syncSendExtMsg(ExternalMsgDTO extMsgDTO);

	/**
	 * Synchronous batch sending of external messages, not dependent on current user information
	 * @param extMsgDTOList
	 */
	void syncSendExtMsg(List<ExternalMsgDTO> extMsgDTOList);

	/**
	 * Update callback status according to business id
	 * @param businessId Business id
	 * @param businessType Business type
	 * @return Number of rows affected
	 */
	void updateMsgLogStatusByBusinessId(String businessId, String businessType);

	/**
	 * Template conversion
	 * eg Convert "Please review ${myParam}" to "Please review xxx leave application"
	 * @param templateStr The string to be parsed
	 * @param templateCode Template code
	 * @param obj System parameters
	 * @return
	 */
	String convertTemplateStr(String templateStr, String templateCode, Object obj);
}
