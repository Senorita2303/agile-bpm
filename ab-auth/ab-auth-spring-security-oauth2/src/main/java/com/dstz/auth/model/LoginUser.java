package com.dstz.auth.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.org.api.UserApi;
import com.dstz.org.api.model.IUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.function.SingletonSupplier;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * Login User
 *
 * To obtain more properties of AbUser, please refer to the getPhoto method, proxy acquisition
 *
 */
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 4962121675615445357L;

	private String username;

	private String userId;

	private String fullName;

	private transient volatile IUser delegateUser;

	private Boolean superAdmin;

	private Collection<? extends GrantedAuthority> authorities;

	public void setDelegateUser(IUser delegateUser) {
		this.delegateUser = delegateUser;
		this.username = delegateUser.getUsername();
		this.userId = delegateUser.getUserId();
		this.fullName = delegateUser.getFullName();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return ObjectUtil.defaultIfNull(authorities, Collections.emptyList());
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return username;
	}

    @Override
    public boolean isAccountNonExpired() {
        Date expireDate = delegateUser().getAttrValue("expireDate", Date.class);
        return expireDate == null || SystemClock.now() <= expireDate.getTime();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return delegateUser().getAttrValue("password", String.class);
    }

    @Override
    public boolean isEnabled() {
        Integer status = delegateUser().getAttrValue("status", Integer.class);
        // 1： Enable
        return NumberPool.INTEGER_ONE.equals(status);
    }

    /**
     * Get user source
     *
     * @return User Source
     */
    public String getFrom() {
        return delegateUser().getAttrValue("from", String.class);
    }

	public Boolean getSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(Boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	/**
	 * Get proxy user
	 *
	 * @return Proxy User
	 */
	public IUser delegateUser() {
		if (Objects.isNull(delegateUser)) {
			synchronized (this) {
				if (Objects.isNull(delegateUser)) {
					this.delegateUser = new ProxyUser(this.username, this.userId, this.fullName);
				}
			}
		}
		return delegateUser;
	}

	private static final class ProxyUser implements IUser {

		private static final long serialVersionUID = -6505859170491860133L;

		private final String username;

		private final String userId;

		private final String fullName;

		private final SingletonSupplier<IUser> attributes;

		public ProxyUser(String username, String userId, String fullName) {
			this.username = username;
			this.userId = userId;
			this.fullName = fullName;
			this.attributes = SingletonSupplier.of(() -> SpringUtil.getBean(UserApi.class).getByUsername(username));
		}

		@Override
		public String getUsername() {
			return username;
		}

		@Override
		public String getUserId() {
			return userId;
		}


		@Override
		public String getFullName() {
			return fullName;
		}

		@Override
		public <T> T getAttrValue(String attrName, Class<T> tClass) {
			PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(getClass(), attrName);
			if (propertyDescriptor != null) {
				return Convert.convert(tClass, ReflectUtil.invoke(this, propertyDescriptor.getReadMethod()));
			}
			IUser attributes = this.attributes.get();
			return attributes == null ? null : attributes.getAttrValue(attrName, tClass);
		}
	}
}
