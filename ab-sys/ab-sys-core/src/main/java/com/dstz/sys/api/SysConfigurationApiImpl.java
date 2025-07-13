package com.dstz.sys.api;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.property.SysConfigurationApi;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.sys.core.entity.SysConfiguration;
import com.dstz.sys.core.manager.SysConfigurationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dstz.base.common.enums.ErrorLogLeve.ERROR;
import static com.dstz.base.common.events.AbErrorLogEvent.createErrorLog;
import static com.dstz.sys.api.constant.SysApiCodes.SYS_CONFIG_NOT_FOUND;

@Service
public class SysConfigurationApiImpl implements SysConfigurationApi {

    @Autowired
    SysConfigurationManager sysConfigurationManager;


    @Override
    public <T> T getConfigByCode(String code, Class<T> typeClass) {
        SysConfiguration sysConfiguration = sysConfigurationManager.getConfByCode(code);
        if(ObjectUtil.isNull(sysConfiguration)) {
            SpringUtil.publishEvent(createErrorLog(new BusinessException(SYS_CONFIG_NOT_FOUND.formatDefaultMessage(code)), ERROR));
        }
        if (ObjectUtil.isNotNull(sysConfiguration) && StrUtil.isNotEmpty(sysConfiguration.getJson()) && NumberPool.INTEGER_ONE.equals(sysConfiguration.getIsEnable())) {
            return JsonUtils.parseObject(sysConfiguration.getJson(), typeClass);
        }
        return null;
    }
    @Override
    public String getConfByCode(String code) {
        return ObjectUtil.isNotNull(sysConfigurationManager.getConfByCode(code)) ? sysConfigurationManager.getConfByCode(code).getJson() : "";
    }

    @Override
    public void saveConf(String json) {
        sysConfigurationManager.save(JsonUtils.parseObject(json, SysConfiguration.class));
    }


    @Override
    public void deleteByCode(String code) {
        sysConfigurationManager.deleteByCode(code);
    }
}
