package com.dstz.auth.model.dto;

import java.util.List;

/**
 * <pre>
 * Description: WeChat enterprise member details
 * </pre>
 *
 */
public class QyWxUserDTO {

    private String userid; // Member UserID. Corresponding to the account of the management end
    private String name;  // Member name; cannot be obtained by third parties, userid is returned instead of name when called; for members not created by third parties, third-party address book applications cannot obtain them either; third-party pages need to display names through address book display components
    private String[] department; // List of department IDs to which members belong. Only department IDs that the app has permission to view are returned.
    private String[] order; // The ranking value within the department, the default value is 0. The number must be consistent with the department, the larger the value, the higher the ranking. The value range is [0, 2^32)
    private String position; // Position information; only the address book application of third parties can obtain it; for members not created by third parties, third-party address book applications cannot obtain it
    private String mobile; // Phone number
    private String gender; // Gender. 0 means undefined, 1 means male, 2 means female
    private String email; // Email address: Only the address book app of a third party can obtain it. For members not created by a third party, the third-party address book app cannot obtain it either.
    private List<Integer> is_leader_in_dept; // Indicates whether the person is a superior in the department. 0-No; 1-Yes. It is a list, and the number must be the same as department. Only the third-party address book application can obtain it; for members not created by a third party, the third-party address book application cannot obtain it either.
    private String avatar; // Avatar URL. Only the third-party address book app can obtain it; for members not created by a third party, the third-party address book app cannot obtain it either.
    private String thumb_avatar; // Avatar thumbnail URL. Only the third-party address book app can obtain it; for members not created by a third party, the third-party address book app cannot obtain it either.
    private String telephone;  // Landline.
    private String alias; // Aliases;
    private int status; // Activation status: 1=activated, 2=disabled, 4=inactive, 5=exited from enterprise.
    private String address; // Address.
    private int hide_mobile;
    private String english_name;
    private String open_userid; // Globally unique. For the same service provider, different applications can obtain the same open_userid of the same member in the enterprise, with a maximum of 64 bytes. Only third-party applications can obtain it.
    private int main_department; // Main Department

    private String qr_code; // Employee's personal QR code, scan to add as external contact (note that the returned code is a URL, which can be opened on the browser to display the QR code); third-party contacts can only be obtained through the address book application; for members not created by third parties, third-party address book applications cannot obtain
    private String external_position; // External position. If this value is set, it will be used as the external position, otherwise it will be displayed as position. Only the third-party address book application can obtain it; for members not created by a third party, the third-party address book application cannot obtain it either.

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getDepartment() {
        return department;
    }

    public void setDepartment(String[] department) {
        this.department = department;
    }

    public String[] getOrder() {
        return order;
    }

    public void setOrder(String[] order) {
        this.order = order;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getIs_leader_in_dept() {
        return is_leader_in_dept;
    }

    public void setIs_leader_in_dept(List<Integer> is_leader_in_dept) {
        this.is_leader_in_dept = is_leader_in_dept;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getThumb_avatar() {
        return thumb_avatar;
    }

    public void setThumb_avatar(String thumb_avatar) {
        this.thumb_avatar = thumb_avatar;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHide_mobile() {
        return hide_mobile;
    }

    public void setHide_mobile(int hide_mobile) {
        this.hide_mobile = hide_mobile;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getOpen_userid() {
        return open_userid;
    }

    public void setOpen_userid(String open_userid) {
        this.open_userid = open_userid;
    }

    public int getMain_department() {
        return main_department;
    }

    public void setMain_department(int main_department) {
        this.main_department = main_department;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getExternal_position() {
        return external_position;
    }

    public void setExternal_position(String external_position) {
        this.external_position = external_position;
    }
}
