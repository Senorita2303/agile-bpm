package com.dstz.org.api;

import com.dstz.org.api.model.IUser;
import com.dstz.org.core.entity.OrgUser;

/**
 * ab User implementation
 *
 */
class AbUser extends AbDelegate<OrgUser> implements IUser {

	public AbUser(OrgUser delegate) {
		super(delegate);
	}

	@Override
	public String getUserId() {
		return delegate.getId();
	}

	@Override
	public String getUsername() {
		return delegate.getAccount();
	}

	@Override
	public String getFullName() {
		return delegate.getFullname();
	}

	public String getEmail() {
		return delegate.getEmail();
	}

	public String getMobile() {
		return delegate.getMobile();
	}

	public String getAddress() {
		return delegate.getAddress();
	}

	public String getPhoto() {
		return delegate.getPhoto();
	}

	public String getSex() {
		return delegate.getSex();
	}

	public String getWeixin() {
		return delegate.getWeixin();
	}

	public Integer getStatus() {
		return delegate.getStatus();
	}

	public String getOpenid() {
		return delegate.getOpenid();
	}

	public String getSignature() {
		return delegate.getSignature();
	}
}
