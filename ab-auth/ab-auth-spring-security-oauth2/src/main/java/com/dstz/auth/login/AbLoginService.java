package com.dstz.auth.login;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.dstz.auth.login.model.TokenReqVO;
import com.dstz.auth.model.LoginUser;
import com.dstz.auth.utils.LoginUtils;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.encrypt.EncryptUtil;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.AESUtil;
import com.dstz.base.common.utils.AbRequestUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.org.api.UserApi;
import com.dstz.org.api.model.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.dstz.auth.authentication.api.constant.AuthCacheKeyConstant.*;
import static com.dstz.auth.authentication.api.constant.AuthStatusCode.*;
import static com.dstz.base.common.constats.AbCacheRegionConstant.LOGIN_CACHE_REGION;

@Service("abLoginService")
public class AbLoginService {

    private final String API_SUCCESS = "Success";
    @Autowired
    ICache iCache;
    @Autowired
    AbTokenService tokenService;
    @Autowired
    UserApi userApi;

    /**
     * Process data after login
     *
     * @param apiResponse
     */
    public void loginPostposition(ApiResponse<OAuth2AccessToken> apiResponse) {
        try {
            if (apiResponse.getIsOk() && API_SUCCESS.equals(apiResponse.getCode())) {
                String token = apiResponse.getData().getValue();
                OAuth2Authentication oAuth2Authentication = tokenService.getOAuth2AuthenticationByToken(token);
                // Set the current thread login context information
                SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
                UserContextUtils.getUserContext().clear();
            }
        } catch (Exception e) {
            throw new BusinessMessage(LOGIN_TIMEOUT);

        }
    }

    /**
     * 登录验证
     *
     * @param tokenReqVO
     */
    public void authLoginParam(TokenReqVO tokenReqVO) {
        Assert.notBlank(tokenReqVO.getGrantType(),()-> new BusinessMessage(GRANTTYPE_CANNOT_BE_EMPTY));
        //校验验证码
        if (LoginUtils.queryCaptchaSwitch()) {
            Assert.notBlank(tokenReqVO.getCaptcha(), () -> new BusinessMessage(CAPTCHA_CANNOT_BE_EMPTY));

            String sessionId = AbRequestUtils.getHttpServletRequest().getSession().getId();
            Assert.isTrue(iCache.exists(LOGIN_CACHE_REGION, sessionId), () -> new BusinessMessage(CAPTCHA_ERROR));

            String captcha = iCache.getIfPresent(LOGIN_CACHE_REGION, sessionId);
            Assert.isTrue(tokenReqVO.getCaptcha().equalsIgnoreCase(captcha), () -> new BusinessMessage(CAPTCHA_ERROR));
            iCache.invalidate(LOGIN_CACHE_REGION, sessionId);
        }

        IUser user = userApi.getByUsername(tokenReqVO.getUserName());
        Assert.notNull(user, () -> new BusinessMessage(ACCOUNT_NOT_FIND));
        Assert.isTrue(user.getAttrValue("status", Integer.class) == NumberPool.BOOLEAN_TRUE, () -> new BusinessMessage(ACCOUNT_DISABLED));

        // 检查历史失败过几次
        String passordErrorMsg = LoginUtils.chckLoginFailedTimes(tokenReqVO.getUserName(), AbRequestUtils.getRequestIp());
        // 判断用户名密码是否匹配
        Assert.isTrue(ObjectUtil.isNotNull(user) && EncryptUtil.encryptSha256(tokenReqVO.getPassword()).equals(user.getAttrValue("password", String.class)), () -> new BusinessMessage(USER_NAME_OR_PASSORD_ERROR.formatDefaultMessage().formatDefaultMessage(passordErrorMsg)));
        // 如果为默认密码，且系统配置 【默认密码需要重置】，则走重置页面
        if (LoginUtils.queryIsResetPwd()) {
            // 如果没有过期时间则为默认密码
            Assert.isTrue(ObjectUtil.isNotNull(user.getAttrValue("expireDate", Date.class)), () -> new BusinessMessage(PASSWORD_NEEDS_TO_BE_CHANGED));
        }
        //登录成功清除缓存
        iCache.invalidate(LOGIN_CACHE_REGION, USER_LOGIN_COUNT + tokenReqVO.getUserName());
    }
}
