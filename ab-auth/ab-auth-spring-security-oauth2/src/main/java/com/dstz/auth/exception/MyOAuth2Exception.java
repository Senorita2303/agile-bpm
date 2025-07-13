package com.dstz.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * Custom exception classes
 *
 */
@JsonSerialize(using = CustomizeOauthExceptionSerializer.class)
@JsonDeserialize(using = CustomizeOAuthExceptionDeserializer.class)
public class MyOAuth2Exception extends OAuth2Exception {
    public MyOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public MyOAuth2Exception(String msg) {
        super(msg);
    }

}
