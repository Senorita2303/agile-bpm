package com.dstz.auth.authentication;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.dstz.auth.authentication.api.SysApplicationApi;
import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.auth.authentication.vo.SysApplicationVO;
import com.dstz.auth.exception.Auth2CustomizeExceptionTranslator;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * Client Information
 *
 */
@Component
public class AbClientDetailsServiceImpl extends JdbcClientDetailsService {

    @Autowired
    private SysApplicationApi sysApplicationApi;

    public AbClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        try {
            SysApplicationVO sysApplication = sysApplicationApi.getByCode(clientId);
            Assert.notNull(sysApplication, () -> new BusinessException(AuthStatusCode.NO_FIND_APP.formatDefaultMessage(clientId)));

            BaseClientDetails baseClientDetails = new BaseClientDetails();
            // Client clientId
            baseClientDetails.setClientId(sysApplication.getCode());
            // Client Key
            baseClientDetails.setClientSecret("{noop}" + sysApplication.getSecret());
            // Method-level permission control specifies the scope of permissions requested by the client
            baseClientDetails.setScope(StrUtil.split(sysApplication.getScope(), StrPool.COMMA));
            // The resource ID set that the client can access. Multiple resources are separated by commas (,). If not set, all permissions are granted.
            baseClientDetails.setResourceIds(StrUtil.isNotEmpty(sysApplication.getResourceIds()) ? StrUtil.split(sysApplication.getResourceIds(), StrPool.COMMA) : null);
            // Supported authentication types
            baseClientDetails.setAuthorizedGrantTypes(StrUtil.isNotEmpty(sysApplication.getGrantTypes()) ? StrUtil.split(sysApplication.getGrantTypes(), StrPool.COMMA) : CollUtil.newArrayList());

            // Callback address
            baseClientDetails.setRegisteredRedirectUri(StrUtil.isNotEmpty(sysApplication.getRedirectUri()) ? CollUtil.newHashSet(StrUtil.split(sysApplication.getRedirectUri(), StrPool.COMMA)) : null);

            // Set whether the user automatically approves the operation. The default value is 'false'. This field is only applicable to grant_type="authorization_code". When the user logs in successfully, if the value is 'true' or a supported scope value, the user Approve page will be skipped and authorization will be granted directly.
            baseClientDetails.setAutoApproveScopes(Collections.singletonList(sysApplication.getAutoapprove().toString()));

            // Token validity period
            baseClientDetails.setAccessTokenValiditySeconds(sysApplication.getAccessTokenValidity());
            // Refresh token validity period
            baseClientDetails.setRefreshTokenValiditySeconds(sysApplication.getRefreshTokenValidity());
            return baseClientDetails;
        } catch (Exception e) {
            throw new Auth2CustomizeExceptionTranslator.CustomizeException(AuthStatusCode.LOAD_CLIENT_BY_CLIENTID_ERROR.formatDefaultMessage(e.getMessage()));
        }
    }
}
