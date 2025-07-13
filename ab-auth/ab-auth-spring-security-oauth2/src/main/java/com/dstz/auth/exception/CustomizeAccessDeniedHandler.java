package com.dstz.auth.exception;

import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.utils.JsonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Insufficient permissions to return information
 *
 */
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(JsonUtils.toJSONString(ApiResponse.fail(AuthStatusCode.NOT_PERMISSION.getCode(), AuthStatusCode.NOT_PERMISSION.getMessage())));
    }
}
