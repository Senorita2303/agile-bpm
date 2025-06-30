package com.dstz.auth.exception;

import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Authentication failed return information
 *
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        byte[] responseBytes = JsonUtils.toJSONBytes(ApiResponse.fail(AuthStatusCode.TOKEN_INVALID.getCode(), AuthStatusCode.TOKEN_INVALID.getMessage()));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setContentLength(responseBytes.length);
        response.getOutputStream().write(responseBytes, 0, responseBytes.length);
        response.getOutputStream().flush();
    }
}
