
package com.dstz.component.mq.msg.engine.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.enums.ErrorLogLeve;
import com.dstz.base.common.events.AbErrorLogEvent;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.property.SysConfigurationApi;
import com.dstz.base.common.utils.AESUtil;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.msg.engine.dto.MsgImplDTO;
import com.dstz.component.msg.handler.AbsNotifyMessageHandler;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.SysFileApi;
import com.dstz.sys.api.dto.MailSendConfigDTO;
import com.dstz.sys.api.dto.SysFileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.dstz.component.mq.api.constants.JmsTypeEnum.EMAIL;
import static com.dstz.component.mq.msg.engine.constants.MsgEngineStatusCode.EMAIL_SEND_FAIL;
import static com.dstz.component.mq.msg.engine.constants.MsgEnginesConstant.EMAIL_FILE_IDS;
import static com.dstz.component.mq.msg.engine.constants.MsgEnginesConstant.EMAIL_RECEIVERS;


/**
 * Mail message handler.
 *
 */

@Component("mailHandler")
public class MailHandler extends AbsNotifyMessageHandler<MsgImplDTO> {


    private static final Logger LOGGER = LoggerFactory.getLogger(MailHandler.class);

    @Autowired
    private SysFileApi sysFileApi;
    @Override
    public String getType() {
        return EMAIL.getType();
    }


    @Override
    public String getTitle() {
        return "Email";
    }

    @Override
    public boolean getIsDefault() {
        return true;
    }


    @Override
    public boolean getSupportHtml() {
        return true;
    }

    @Override
    public boolean sendMessage(MsgImplDTO notifMessage) {
        MailAccount mailAccount = createMailAccount();
        if (CollUtil.isNotEmpty(notifMessage.getReceivers())) {
            for (IUser reciver : identityConvert.convert2Users(notifMessage.getReceivers())) {
                sendEmail(mailAccount, reciver.getAttrValue(EMAIL.getType(), String.class), notifMessage);
            }
        } else {
            if (MapUtil.isNotEmpty(notifMessage.getExtendVars()) && notifMessage.getExtendVars().containsKey(EMAIL_RECEIVERS)) {
                sendEmail(mailAccount, String.join(",", (List) notifMessage.getExtendVars().get(EMAIL_RECEIVERS)), notifMessage);
            }
        }
        return true;
    }

    private void sendEmail(MailAccount mailAccount, String email, MsgImplDTO notifMessage) {
        if (StrUtil.isEmpty(email)) {
            return;
        }
        String text = notifMessage.getHtmlTemplate().replace(PC_URL_CHART, PropertyEnum.PC_URL.getPropertyValue(String.class));
        List<String> paths = new ArrayList<>();
        try {
            File[] fileList = new File[0];

            if(notifMessage.getExtendVars().containsKey(EMAIL_FILE_IDS)){

                String ids = notifMessage.getExtendVars().get(EMAIL_FILE_IDS).toString();
                if(StrUtil.isNotEmpty(ids)){
                    String[] fileIds = ids.split(StrPool.COMMA);
                    fileList = new File[fileIds.length];
                    int i = 0;
                    for (String fileId : fileIds) {
                        InputStream inputStream = sysFileApi.download(fileId);
                        if(ObjectUtil.isNotEmpty(inputStream)){
                            SysFileDTO sysFileDTO = sysFileApi.getById(fileId);
                            String path = sysFileDTO.getName();
                            File file = FileUtil.writeFromStream(inputStream,path);
                            fileList[i] = file;
                            paths.add(path);
                            i++;
                        }
                    }
                }
            }
            if(fileList.length>0) {
                MailUtil.send(mailAccount, CollUtil.newArrayList(email.split(StrPool.COMMA)), notifMessage.getSubject(), text, true, fileList);
            }else{
                MailUtil.send(mailAccount, CollUtil.newArrayList(email.split(StrPool.COMMA)), notifMessage.getSubject(), text, true);
            }
            paths.forEach(FileUtil::del);
        } catch (Throwable ex) {
            String formatMessage = StrUtil.format("Sending mail exception, sending parameters {}, tos: {}, notifyMessage: {}", mailAccount, email, JsonUtils.toJSONString(notifMessage));
            SpringUtil.publishEvent(AbErrorLogEvent.createErrorLog(new IllegalStateException(formatMessage, ex), ErrorLogLeve.ERROR));
            throw new BusinessException(EMAIL_SEND_FAIL.formatDefaultMessage(ex.getMessage()));
        }
    }

    private MailAccount createMailAccount() {
        MailSendConfigDTO mailSendConfigDTO = SpringUtil.getBean(SysConfigurationApi.class).getConfigByCode("mailSendConfig", MailSendConfigDTO.class);
        if (ObjectUtil.isNull(mailSendConfigDTO)){
            LOGGER.error("Not configured system configuration  mailSendConfig");
            throw new BusinessException(EMAIL_SEND_FAIL.formatDefaultMessage("No valid email configuration was found"));
        };
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(mailSendConfigDTO.getHost());
        mailAccount.setPort(mailSendConfigDTO.getPort());
        mailAccount.setFrom(mailSendConfigDTO.getAddress());
        mailAccount.setUser(mailSendConfigDTO.getNickname());
        mailAccount.setPass(AESUtil.decrypt(mailSendConfigDTO.getPassword()));
        mailAccount.setSslEnable(mailSendConfigDTO.getSsl());
        return mailAccount;
    }
}

