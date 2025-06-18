package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;

public class SetPwdByEmailDTO {
    /**
     * Account
     */
    @NotBlank(message = "The account cannot be empty!")
    private String account;

    /**
     * New Password
     */
    @NotBlank(message = "Password cannot be empty!")
    private String newPassword;

    /**
     * Verification Code
     */
    @NotBlank(message = "Verification code cannot be empty!")
    private String captcha;

    /**
     * Confirm Password
     */
    @NotBlank(message = "Confirm password cannot be empty!")
    private String confirmPassword;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "SetPwdByEmailDTO{" +
                "account='" + account + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", captcha='" + captcha + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
