package com.dstz.auth.rest.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.core.lang.Assert;
import cn.hutool.extra.servlet.ServletUtil;
import com.dstz.auth.login.AbLoginService;
import com.dstz.auth.login.AbTokenService;
import com.dstz.auth.login.model.TokenReqVO;
import com.dstz.auth.utils.LoginUtils;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.AbRequestUtils;
import com.dstz.org.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.dstz.auth.authentication.api.constant.AuthStatusCode.PARAM_CLIENT_ID_IS_NOT_FOUND;

/**
 * Login Controller
 *
 * @owner Shenzhen Dashitongzhou Information Technology Co., Ltd.
 */
@RestController
@RequestMapping(AbAppRestConstant.AUTH_SERVICE_PREFIX)
public class    LoginController {


    @Autowired
    UserApi userApi;

    @Autowired
    ICache cache;

    @Autowired
    AbTokenService abTokenService;

    @Autowired
    AbLoginService loginService;

    /**
     * Caching Data
     *
     * @param tokenReqVO
     * @return
     */
    @RequestMapping("/login")
    public ApiResponse<OAuth2AccessToken> login(@Valid @RequestBody TokenReqVO tokenReqVO, HttpServletResponse response) {
        // Pre-login verification information
        loginService.authLoginParam(tokenReqVO);
        // Get a token
        ApiResponse<OAuth2AccessToken> result = abTokenService.getToken(tokenReqVO);
        // Caching Data
        loginService.loginPostposition(result);
        return result;
    }

    /**
     * Log out
     *
     * @return
     */
    @RequestMapping("/logout")
    public ApiResponse logout() {
        abTokenService.removeCurrentAccessToken();
        return ApiResponse.success();
    }

    @RequestMapping("/login/getCode")
    public void getCode() throws IOException {
        String sessionId = AbRequestUtils.getHttpServletRequest().getSession().getId();
        // Define the length, width, number of characters, and width of the interference line of the graphic verification code
        GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(120, 40, 4);
        // Get randomly generated content
        gifCaptcha.createCode();
        String code = gifCaptcha.getCode();
        cache.put(AbCacheRegionConstant.LOGIN_CACHE_REGION, sessionId, code);
        // Output Stream
        ServletOutputStream os = AbRequestUtils.getHttpServletResponse().getOutputStream();
        gifCaptcha.write(os);
        os.flush();
        os.close();
    }

    @RequestMapping("/login/queryCaptchaSwitch")
    public ApiResponse<List<Boolean>> queryCaptchaSwitch() {
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(0, LoginUtils.queryCaptchaSwitch());
        booleanList.add(1, LoginUtils.queryGetBackPwdSwitch());
        return ApiResponse.success(booleanList);
    }


}
