package com.dstz.component.msg.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 消息模板dto
 * </p>
 *
 * @author lightning
 * @since 2022-11-17
 */
public class MessageTemplateVO implements Serializable {

    /**
     * 模板编码
     */
    private String code;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板描述
     */
    private String desc;

    /**
     * html模板配置
     */
    private String htmlTemplate;

    /**
     * 应用模板配置
     */
    private String appTemplate;

    /**
     * 卡片模板配置
     */
    private String cardTemplate;

    /**
     * 模板参数
     */
    private String templateParamJson;

    /**
     * 短信供应商模板code
     */
    private String smsTemplateCode;

    public String getSmsTemplateCode() {
        return smsTemplateCode;
    }

    public void setSmsTemplateCode(String smsTemplateCode) {
        this.smsTemplateCode = smsTemplateCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(String htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }


    public String getAppTemplate() {
        return appTemplate;
    }

    public void setAppTemplate(String appTemplate) {
        this.appTemplate = appTemplate;
    }


    public String getTemplateParamJson() {
        return templateParamJson;
    }

    public void setTemplateParamJson(String templateParamJson) {
        this.templateParamJson = templateParamJson;
    }

    public String getCardTemplate() {
        return cardTemplate;
    }

    public void setCardTemplate(String cardTemplate) {
        this.cardTemplate = cardTemplate;
    }
}



