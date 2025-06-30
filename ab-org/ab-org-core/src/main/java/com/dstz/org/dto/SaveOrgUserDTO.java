package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * requestDto Save user information
 */
public class SaveOrgUserDTO implements java.io.Serializable {

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
	 * Account
	 */
	@NotBlank(message = "The account number cannot be empty!")
	private String account;

	/**
	 * Password
	 */
	private String password;

	/**
	 * Mail
	 */
	private String email;

	/**
	 * Cell phone
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
	 * status
	 */
	private Integer status;

	/**
	 * address
	 */
	private String address;

	/**
	 * User Relationship
	 */
	private List<OrgRelationDTO> orgRelationList;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<OrgRelationDTO> getOrgRelationList() {
		return orgRelationList;
	}

	public void setOrgRelationList(List<OrgRelationDTO> orgRelationList) {
		this.orgRelationList = orgRelationList;
	}

	@Override
	public String toString() {
		return "SaveOrgUserDTO{" + "id='" + id + '\'' + ", photo='" + photo + '\'' + ", signature='" + signature + '\''
				+ ", fullname='" + fullname + '\'' + ", account='" + account + '\'' + ", password='" + password + '\''
				+ ", email='" + email + '\'' + ", mobile='" + mobile + '\'' + ", weixin='" + weixin + '\'' + ", sex='"
				+ sex + '\'' + ", status=" + status + ", address='" + address + '\'' + ", orgRelationList="
				+ orgRelationList + '}';
	}
}
