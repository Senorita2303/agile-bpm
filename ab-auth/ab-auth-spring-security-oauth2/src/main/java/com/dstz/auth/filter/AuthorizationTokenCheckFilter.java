package com.dstz.auth.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.dstz.auth.utils.IngoreChecker;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.dstz.auth.authentication.api.constant.AuthApiConstant.AUTHORIZATION;
import static com.dstz.auth.authentication.api.constant.AuthStatusCode.NO_AUTH_TOKEN;

/**
 * Authorization parameter filtering
 *
 */
public class AuthorizationTokenCheckFilter extends IngoreChecker implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // Handling anonymous interfaces
        if (this.isIngores(req)) {
            chain.doFilter(request, response);
            return;
        }
        String token = ServletUtil.getHeader(req, AUTHORIZATION, StrPool.EMPTY);
        if (StrUtil.isBlank(token) || !token.trim().startsWith(OAuth2AccessToken.BEARER_TYPE)) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            ServletUtil.write((HttpServletResponse) response, JsonUtils.toJSONString(ApiResponse.fail(NO_AUTH_TOKEN.getCode(), NO_AUTH_TOKEN.getMessage())), MediaType.APPLICATION_JSON_VALUE);
        } else {
            chain.doFilter(request, response);
        }
    }
}

