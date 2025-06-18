package com.dstz.component.msg.api;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.component.msg.api.dto.ExternalMsgDTO;
import com.dstz.component.msg.api.dto.MsgDTO;

import java.util.List;

/**
 * 消息发送实现
 *
 * @author lightning
 */
public interface MsgApi {

    /**
     * 发送消息
     *
     * @param msgDTO
     */
    void sendMsg(MsgDTO msgDTO);

    /**
     * 批量发送消息
     *
     * @param msgDTOList
     */
    void sendMsg(List<MsgDTO> msgDTOList);


    /**
     * 发送外部消息，不依赖当前用户信息
     * @param extMsgDTO
     */
    void sendExtMsg(ExternalMsgDTO extMsgDTO);



    /**
     * 批量发送外部消息，不依赖当前用户信息
     * @param extMsgDTOList
     */
    void sendExtMsg(List<ExternalMsgDTO> extMsgDTOList);



    /**
     * 同步发送消息
     *
     * @param msgDTO
     */
    void syncSendMsg(MsgDTO msgDTO);

    /**
     * 同步批量发送消息
     *
     * @param msgDTOList
     */
    void syncSendMsg(List<MsgDTO> msgDTOList);


    /**
     * 同步发送外部消息，不依赖当前用户信息
     * @param extMsgDTO
     */
    void syncSendExtMsg(ExternalMsgDTO extMsgDTO);



    /**
     * 同步批量发送外部消息，不依赖当前用户信息
     * @param extMsgDTOList
     */
    void syncSendExtMsg(List<ExternalMsgDTO> extMsgDTOList);

    /**
     * 根据业务id更新回调状态
     * @param businessId 业务id
     * @param businessType 业务类型
     * @return 受影响行数
     */
    void updateMsgLogStatusByBusinessId(String businessId,String businessType);

    /**
     * 模板转换
     * eg 把  请审阅 ${myParam}  转换为 请审阅 xxx请假申请
     * @param templateStr 要解析的字符串
     * @param templateCode 模板编码
     * @param obj 系统参数
     * @return
     */
    String convertTemplateStr(String templateStr, String templateCode, Object obj);
}
