package com.dstz.auth.login;

import cn.hutool.core.util.StrUtil;
import com.dstz.auth.authentication.api.SysApplicationApi;
import com.dstz.auth.authentication.api.model.ISysApplication;
import com.dstz.auth.login.model.RefreshTokenReqVO;
import com.dstz.auth.login.model.TokenReqVO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.UserContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dstz.auth.authentication.api.constant.AuthApiConstant.*;
import static com.dstz.auth.authentication.api.constant.AuthStatusCode.APPLICATION_GET_TOKEN_ERROR;
import static com.dstz.auth.authentication.api.constant.AuthStatusCode.APPLICATION_REFRESH_TOKEN_ERROR;

@Service("abTokenService")
public class AbTokenService {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    ICache iCache;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TokenEndpoint tokenEndpoint;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    SysApplicationApi sysApplicationApi;

    /**
     * Get token based on username
     *
     * @param account
     * @return
     */
    public List<OAuth2AccessToken> getOAuth2AccessTokenByAccount(String account) {
        return Optional.ofNullable(sysApplicationApi.getAllCode())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(clientId -> tokenStore.findTokensByClientIdAndUserName(clientId, account))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Remove tokens based on account
     *
     * @param account
     */
    public void removeAccessToken(String account) {
        getOAuth2AccessTokenByAccount(account).forEach(tokenStore::removeAccessToken);
    }

    /**
     * Get token based on loginvo
     *
     * @param tokenReqVO
     * @return
     */
    public ApiResponse<OAuth2AccessToken> getToken(TokenReqVO tokenReqVO) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(OAUTH_TOKEN_CLIENT_KEY, tokenReqVO.getClientId());
        paramMap.put(OAUTH_TOKEN_GRANT_TYPE, tokenReqVO.getGrantType());
        paramMap.put(OAUTH_TOKEN_USER_NAME, tokenReqVO.getUserName());
        paramMap.put(OAUTH_TOKEN_PASSWORD, tokenReqVO.getPassword());
        paramMap.put(OAUTH_TOKEN_CLIENT_SECRET, tokenReqVO.getClientSecret());

        User userDetail = new User(tokenReqVO.getClientId(), tokenReqVO.getClientSecret(), new ArrayList<>());
        Authentication request_token = new UsernamePasswordAuthenticationToken(userDetail, null, new ArrayList<>());
        try {
            return (ApiResponse<OAuth2AccessToken>) tokenEndpoint.postAccessToken(request_token, paramMap).getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new ApiException(APPLICATION_GET_TOKEN_ERROR.formatDefaultMessage(tokenReqVO.getUserName(), tokenReqVO.getClientId()), e);
        }
    }

    /**
     * Remove the current login account token
     */
    public void removeCurrentAccessToken() {
        String account = UserContextUtils.getAccount();
        Boolean isExitSystem = PropertyEnum.CHANGE_PWD_iS_Exit_SYSTEM.getPropertyValue(Boolean.class);
        if (StrUtil.isNotEmpty(account) && isExitSystem) {
            removeAccessToken(UserContextUtils.getAccount());
        } else {
            tokenStore.removeAccessToken(getCurrentOAuth2AccessToken());
        }
    }


    /**
     * Get the current thread token string
     */
    public String getCurrentAccessTokenStr() {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return details.getTokenValue();
    }

    /**
     * Get the current thread token object
     */
    public OAuth2AccessToken getCurrentOAuth2AccessToken() {
        return tokenStore.readAccessToken(getCurrentAccessTokenStr());
    }

    /**
     * Refresh token
     *
     * @param refreshTokenReqVO
     * @return
     */
    public ApiResponse<OAuth2AccessToken> refreshToken(RefreshTokenReqVO refreshTokenReqVO) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(OAUTH_TOKEN_CLIENT_KEY, refreshTokenReqVO.getClientId());
        paramMap.put(OAUTH_TOKEN_CLIENT_SECRET, refreshTokenReqVO.getClientSecret());
        paramMap.put(OAUTH_TOKEN_GRANT_TYPE, OAUTH_TOKEN_REFRESH_TOKEN);
        paramMap.put(OAUTH_TOKEN_REFRESH_TOKEN, refreshTokenReqVO.getRefreshToken());

        try {
            User userDetail = new User(refreshTokenReqVO.getClientId(), refreshTokenReqVO.getClientSecret(), new ArrayList<>());
            Authentication request_token = new UsernamePasswordAuthenticationToken(userDetail, null, new ArrayList<>());
            return (ApiResponse<OAuth2AccessToken>) tokenEndpoint.postAccessToken(request_token, paramMap).getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new ApiException(APPLICATION_REFRESH_TOKEN_ERROR.formatDefaultMessage(refreshTokenReqVO.getClientId()), e);
        }
    }

    /**
     * Get a token based on the application
     *
     * @param sysApplication
     * @return
     */
    private ApiResponse<OAuth2AccessToken> getToken(ISysApplication sysApplication) {
        // Get user information based on user account
        UserDetails userDetails = userDetailsService.loadUserByUsername(UserContextUtils.getAccount());
        TokenReqVO tokenReqVO = new TokenReqVO(userDetails.getUsername(), userDetails.getPassword(), OAUTH_TOKEN_PASSWORD, sysApplication.getCode(), sysApplication.getSecret());
        return getToken(tokenReqVO);
    }

    /**
     * Get OAuth2Authentication based on token
     *
     * @param token
     * @return OAuth2Authentication
     */
    public OAuth2Authentication getOAuth2AuthenticationByToken(String token) {
        // Token is put into cache
        return tokenStore.readAuthentication(token);
    }

}
