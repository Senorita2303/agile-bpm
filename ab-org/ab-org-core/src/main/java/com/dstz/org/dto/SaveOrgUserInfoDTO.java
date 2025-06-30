package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;


/**
 * requestDto Save user information
 */
public class SaveOrgUserInfoDTO implements java.io.Serializable {

    private String id;

    /**
     * photo
     */
    private String photo;

    /**
     * signature
     */
    private String signature;

    /**
     * Name
     */
    @NotBlank(message = "Name cannot be empty!")
    private String fullname;

    /**
     * account
     */
    @NotBlank(message = "The account number cannot be empty!")
    private String account;

    /**
     * Mail
     */
    private String email;

    /**
     * mobile
     */
    private String mobile;

    /**
     * WeChat
     */
    private String weixin;

    /**
     * gender
     */
    private String sex;

    /**
     * address
     */
    private String address;


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "SaveOrgUserDTO{" +
                "id='" + id + '\'' +
                ", photo='" + photo + '\'' +
                ", signature='" + signature + '\'' +
                ", fullname='" + fullname + '\'' +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", weixin='" + weixin + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
