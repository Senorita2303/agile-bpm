package com.dstz.auth.forbidden;

import com.dstz.auth.authentication.api.constant.AuthApiConstant;
import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Return No permission
 *
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        // Login timeout records the current redirect URL
        request.getSession().setAttribute(AuthApiConstant.SSO_REDIRECTURL, getBackUrl(request));

        byte[] dataBytes = JsonUtils.toJSONString(ApiResponse.fail(AuthStatusCode.LOGIN_TIMEOUT.getCode(), ex.getMessage())).getBytes();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(dataBytes.length);
        response.getOutputStream().write(dataBytes);
    }

    private String getBackUrl(HttpServletRequest request) {
        // Server address
        StringBuffer strBackUrl = new StringBuffer("http://").append(request.getServerName())

                .append(":")

                .append(request.getServerPort()) // Port Number

                .append(request.getContextPath())     // Project Name

                .append(request.getServletPath())     // Request a page or other address

                .append("?").append((request.getQueryString()));


        return strBackUrl.toString();
    }

}