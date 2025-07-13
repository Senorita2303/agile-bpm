package com.dstz.auth.login.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class TokenReqVO implements Serializable {

    @NotBlank(message = "Username cannot be empty!")
    private String userName;

    @NotBlank(message = "Password cannot be empty!")
    private String password;

    private String grantType;

    @NotBlank(message = "Application id cannot be empty!")
    private String clientId;

    @NotBlank(message = "Application key cannot be empty!")
    private String clientSecret;

    // Verification Code
    private String captcha;

    public TokenReqVO() {
    }

    public TokenReqVO(@NotBlank(message = "Username cannot be empty!") String userName, @NotBlank(message = "Password cannot be empty!") String password, @NotBlank(message = "Validation type cannot be empty!") String grantType, @NotBlank(message = "Application id cannot be empty!") String clientId, @NotBlank(message = "Application key cannot be empty!") String clientSecret) {
        this.userName = userName;
        this.password = password;
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
