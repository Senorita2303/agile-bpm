package com.dstz.component.msg.api;

import com.dstz.component.msg.api.vo.MessageTemplateVO;

/**
 * <p>
 * Message template api
 * </p>
 *
 */
public interface MessageTemplateApi {


    /**
     * Query template information based on code
     *
     * @param code
     * @return
     */
    MessageTemplateVO getTemplateByCode(String code);
}
