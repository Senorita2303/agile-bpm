package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;

public class UpdateUserPassWorldDTO implements java.io.Serializable {

    /**
     * Name
     */
    private String fullname;

    /**
     * Account
     */
    @NotBlank(message = "The account cannot be empty!")
    private String account;

    /**
     * Original password
     */
    @NotBlank(message = "The original password cannot be empty!")
    private String oldPassword;

    /**
     * New Password
     */
    @NotBlank(message = "New password cannot be empty!")
    private String newPassword;

    /**
     * Confirm Password
     */
    @NotBlank(message = "Confirm password cannot be empty!")
    private String confirmPassword;


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "UpdateUserPassWorldDTO{" +
                "fullname='" + fullname + '\'' +
                ", account='" + account + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
