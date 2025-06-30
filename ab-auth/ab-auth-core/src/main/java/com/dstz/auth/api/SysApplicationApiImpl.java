package com.dstz.auth.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.auth.authentication.api.SysApplicationApi;
import com.dstz.auth.authentication.vo.SysApplicationVO;
import com.dstz.auth.core.entity.SysApplication;
import com.dstz.auth.core.manager.SysApplicationManager;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanCopierUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dstz.auth.authentication.api.constant.AuthStatusCode.APPCLICATION_ENABLE_DEFAULT_MOBILE_APP_NOT_FIND;

/**
 * System application interface implementation
 *
 */
@Service("sysApplicationApi")
public class SysApplicationApiImpl implements SysApplicationApi {

    private final SysApplicationManager sysApplicationManager;

    public SysApplicationApiImpl(SysApplicationManager sysApplicationManager) {
        this.sysApplicationManager = sysApplicationManager;
    }

    @Override
    public SysApplicationVO getByCode(String code) {
        SysApplication sysApplication = sysApplicationManager.getByAlias(code);
        return BeanCopierUtils.transformBean(sysApplication, SysApplicationVO.class);
    }

    @Override
    public List<String> getAllCode() {
        LambdaQueryWrapper<SysApplication> queryWrapper = Wrappers.lambdaQuery(SysApplication.class)
                .select(SysApplication::getCode);
        List<SysApplication> sysApplicationList = sysApplicationManager.selectByWrapper(queryWrapper);
        return CollUtil.map(sysApplicationList, SysApplication::getCode, false);
    }

    @Override
    public SysApplicationVO getEnabledMobileApp() {
        LambdaQueryWrapper<SysApplication> queryWrapper = Wrappers.lambdaQuery(SysApplication.class)
                .eq(SysApplication::getEnabled, NumberPool.INTEGER_ONE)
                .eq(SysApplication::getIsDefault, NumberPool.INTEGER_ONE)
                .eq(SysApplication::getAppType, NumberPool.INTEGER_ONE);
        List<SysApplication> sysApplicationList = sysApplicationManager.selectByWrapper(queryWrapper);
        Assert.isTrue(CollUtil.isNotEmpty(sysApplicationList),()-> new BusinessMessage(APPCLICATION_ENABLE_DEFAULT_MOBILE_APP_NOT_FIND));
        return CollUtil.isNotEmpty(sysApplicationList) ? BeanCopierUtils.transformBean(sysApplicationList.get(0), SysApplicationVO.class) : null;
    }
}
