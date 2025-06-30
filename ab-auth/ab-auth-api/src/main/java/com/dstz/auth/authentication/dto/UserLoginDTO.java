package com.dstz.auth.authentication.dto;

public class UserLoginDTO implements java.io.Serializable {

    /**
     * Account
     */
    private String account;

    /**
     * Password
     */
    private String password;

    /**
     * Verification Code
     */
    private String captcha;


    /**
     * The receiver, pc/mobile/android"
     */
    private String audience = "pc";


    /**
     * Remember me
     */
    private String rememberMe;


    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }
}
