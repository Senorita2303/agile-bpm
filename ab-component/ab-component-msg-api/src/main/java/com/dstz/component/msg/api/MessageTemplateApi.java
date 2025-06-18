package com.dstz.component.msg.api;

import com.dstz.component.msg.api.vo.MessageTemplateVO;

/**
 * <p>
 * 消息模板api
 * </p>
 *
 * @author lightning
 * @since 2022-11-18
 */
public interface MessageTemplateApi {


    /**
     * 根据code查询模板信息
     *
     * @param code
     * @return
     */
    MessageTemplateVO getTemplateByCode(String code);
}
