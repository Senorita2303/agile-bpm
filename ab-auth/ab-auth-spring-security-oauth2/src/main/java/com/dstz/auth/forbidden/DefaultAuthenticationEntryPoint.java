package com.dstz.auth.forbidden;

import cn.hutool.core.util.StrUtil;
import com.dstz.auth.authentication.api.constant.AuthApiConstant;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Timeout access
 *
 */
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Login timeout records the current redirect URL
        String referer = request.getHeader("Referer");
        String Url = getBackUrl(request);
        if (StrUtil.isNotEmpty(referer) && StrUtil.isNotEmpty(request.getContentType())) {
            request.getSession().setAttribute(AuthApiConstant.SSO_REDIRECTURL, referer);
        }

        byte[] dataBytes = JsonUtils.toJSONString(ApiResponse.fail(GlobalApiCodes.ACCESS_FORBIDDEN.getCode(), "Login access timed out!")).getBytes();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(dataBytes.length);
        response.getOutputStream().write(dataBytes);
    }

    private String getBackUrl(HttpServletRequest request) {
        // Server address
        StringBuffer strBackUrl = new StringBuffer("http://").append(request.getServerName())

                .append(":")
                // Port Number
                .append(request.getServerPort())
                // Project Name
                .append(request.getContextPath())

                .append(request.getServletPath());

        if (StrUtil.isNotEmpty(request.getQueryString())) {
            strBackUrl.append("?").append((request.getQueryString()));
        }


        return strBackUrl.toString();
    }


}