package com.dstz.auth.login.logout;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.dstz.auth.authentication.api.constant.AuthApiConstant.AUTHORIZATION;

/**
 * Log out by default
 *
 */
public class DefualtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenStore tokenStore = SpringUtil.getBean(TokenStore.class);
        String token = ServletUtil.getHeader(request, AUTHORIZATION, "");
        if (StrUtil.isNotBlank(token)) {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(token.replace(OAuth2AccessToken.BEARER_TYPE, "").trim());
            if (accessToken != null) {
                tokenStore.removeAccessToken(accessToken);
            }
        }
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        ServletUtil.write(response, JsonUtils.toJSONString(ApiResponse.success("Logout successful")), MediaType.APPLICATION_JSON_VALUE);
    }

}
