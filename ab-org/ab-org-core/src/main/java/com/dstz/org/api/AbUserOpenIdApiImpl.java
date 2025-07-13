package com.dstz.org.api;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.org.api.model.IUser;
import com.dstz.org.core.entity.OrgUser;
import com.dstz.org.core.manager.OrgUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dstz.org.core.constant.OrgStatusCode.NOT_FIND_USER_BY_OPENID;

@Component("userOpenIdApi")
public class AbUserOpenIdApiImpl implements UserOpenIdApi {

    @Autowired
    private OrgUserManager orgUserManager;


    @Override
    public IUser getByOpenId(String openId) {
        OrgUser orgUser = orgUserManager.selectOne(Wrappers.lambdaQuery(OrgUser.class).eq(OrgUser::getOpenid, openId));
        Assert.notNull(orgUser, ()-> new BusinessMessage(NOT_FIND_USER_BY_OPENID));
        return new AbUser(orgUser);
    }

    @Override
    public void saveOpenIdByAccount(String account, String openId) {
        OrgUser orgUser = orgUserManager.selectOne(Wrappers.lambdaQuery(OrgUser.class).eq(OrgUser::getAccount, account));
        Assert.notNull(orgUser, "No user information found for account %s", StrUtil.nullToEmpty(account));
        orgUser.setOpenid(openId);
        orgUserManager.update(orgUser);
    }
}
