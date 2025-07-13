package com.dstz.auth.login.model;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class RefreshTokenReqVO implements Serializable {

    @NotBlank(message = "Application id cannot be empty!")
    private String clientId;

    @NotBlank(message = "Application key cannot be empty!")
    private String clientSecret;


    @NotBlank(message = "Refresh token cannot be empty!")
    private String refreshToken;


    public RefreshTokenReqVO() {
    }

    public RefreshTokenReqVO(@NotBlank(message = "Application id cannot be empty!") String clientId, @NotBlank(message = "Application key cannot be empty!") String clientSecret, @NotBlank(message = "Refresh token cannot be empty!") String refreshToken) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
    }

    public String getClientId() {
        return clientId;
    }


    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
