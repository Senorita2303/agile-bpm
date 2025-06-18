package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;

public class SendCaptchaEmailDTO implements java.io.Serializable {

    /**
     * Account
     */
    @NotBlank(message = "The account cannot be empty!")
    private String account;


    /**
     * Mail
     */
    @NotBlank(message = "Email address cannot be empty!")
    private String email;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "SendCaptchaEmailDTO{" +
                "account='" + account + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
