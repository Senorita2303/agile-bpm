package com.dstz.auth.exception;

import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.codes.IBaseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;
/**
 * Custom oauth exception
 *
 */
public class Auth2CustomizeExceptionTranslator implements WebResponseExceptionTranslator {

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity translate(Exception e) throws Exception {

        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
        // Wrong username or password
        if (ase instanceof InvalidGrantException) {
            return dealResponseEntity(AuthStatusCode.LOGIN_ERROR);
        }

        ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                causeChain);
        // Login username not found
        if (ase instanceof UsernameNotFoundException) {
            return dealResponseEntity(AuthStatusCode.ACCOUNT_NOT_FIND);
        }

        ase = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        // No access
        if (ase instanceof AccessDeniedException) {
            return dealResponseEntity(AuthStatusCode.APP_CONFIG_ERROR.getCode(), AuthStatusCode.APP_CONFIG_ERROR.getMessage() + e.getMessage());
        }

        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(
                HttpRequestMethodNotSupportedException.class, causeChain);
        // Method not allowed
        if (ase instanceof HttpRequestMethodNotSupportedException) {
            return dealResponseEntity(AuthStatusCode.METHOD_NOT_ALLOWED.getCode(), AuthStatusCode.METHOD_NOT_ALLOWED.getMessage() + e.getMessage());

        }
        // Other abnormalities
        return dealResponseEntity(AuthStatusCode.SERVICE_ERROR.getCode(), AuthStatusCode.SERVICE_ERROR.getMessage() + e.getMessage());


    }

    private static ResponseEntity dealResponseEntity(IBaseCode baseCode) {
        return dealResponseEntity(baseCode.getCode(), baseCode.getMessage());
    }

    private static ResponseEntity dealResponseEntity(String code, String message) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.fail(code, message));
    }

    public static class CustomizeException extends MyOAuth2Exception {
        String code = "";

        public CustomizeException(IBaseCode baseCode) {
            super(baseCode.getMessage());
            this.code = baseCode.getCode();
        }

        public CustomizeException(String code, String msg) {
            super(msg);
            this.code = code;
        }

        @Override
        public String getOAuth2ErrorCode() {
            return code;
        }
    }


}
