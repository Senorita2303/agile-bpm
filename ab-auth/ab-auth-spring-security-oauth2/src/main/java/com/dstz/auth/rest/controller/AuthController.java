package com.dstz.auth.rest.controller;

import com.dstz.auth.login.AbLoginService;
import com.dstz.auth.login.AbTokenService;
import com.dstz.auth.login.model.RefreshTokenReqVO;
import com.dstz.auth.login.model.TokenReqVO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.dstz.auth.authentication.api.constant.AuthApiConstant.OAUTH_TOKEN_PASSWORD;

/**
 * Get a token
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.AUTH_SERVICE_PREFIX)
public class AuthController {

    @Autowired
    AbTokenService abTokenService;

    @Autowired
    AbLoginService loginService;

    /**
     * Get token interface
     *
     * @param tokenReqVO
     * @return
     */
    @RequestMapping("/getToken")
    public ApiResponse<OAuth2AccessToken> getToken(@Valid @RequestBody TokenReqVO tokenReqVO) {
        // Get a token
        tokenReqVO.setGrantType(OAUTH_TOKEN_PASSWORD);
        ApiResponse<OAuth2AccessToken> result = abTokenService.getToken(tokenReqVO);
        // Caching Data
        loginService.loginPostposition(result);
        return result;
    }


    /**
     * Refresh token interface
     *
     * @param refreshTokenReqVO
     * @return
     */
    @RequestMapping("/refreshToken")
    public ApiResponse<OAuth2AccessToken> refreshToken(@Valid @RequestBody RefreshTokenReqVO refreshTokenReqVO) {
        // Refresh token
        ApiResponse<OAuth2AccessToken> result = abTokenService.refreshToken(refreshTokenReqVO);
        // Caching Data
        loginService.loginPostposition(result);
        return result;
    }
}
