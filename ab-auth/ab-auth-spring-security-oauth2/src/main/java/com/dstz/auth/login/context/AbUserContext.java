package com.dstz.auth.login.context;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import com.dstz.auth.model.LoginUser;
import com.dstz.base.common.context.UserContext;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.ContextCleanUtils;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;

import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Ab User Context
 *
 */
@Component("abUserContext")
public class AbUserContext implements UserContext, InitializingBean {

	private static final ThreadLocal<ThreadLocalVariable> THREAD_LOCAL = new ThreadLocal<>();

	private GroupApi groupApi;


	@Autowired
	public void setGroupApi(GroupApi groupApi) {
		this.groupApi = groupApi;
	}

	private static class ThreadLocalVariable {
		Optional<IUser> user;
		Optional<IGroup> org;
		Collection<String> authorizes;
		Boolean superAdmin;
	} 

	private static ThreadLocalVariable getThreadLocalVariable() {
		ThreadLocalVariable threadLocalVariable = THREAD_LOCAL.get();
		if (threadLocalVariable == null) {
			THREAD_LOCAL.set(threadLocalVariable = new ThreadLocalVariable());
			LoginUser loginUser = Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).filter(o -> o instanceof LoginUser).map(LoginUser.class::cast).orElse(null);
			if (loginUser != null) {
				threadLocalVariable.user = Optional.of(loginUser.delegateUser());
				threadLocalVariable.superAdmin = loginUser.getSuperAdmin();
				threadLocalVariable.authorizes = ObjectUtil.defaultIfNull(loginUser.getAuthorities(), Collections.<GrantedAuthority>emptyList()).stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
			} else {
				threadLocalVariable.user = Optional.empty();
				threadLocalVariable.org = Optional.empty();
				threadLocalVariable.superAdmin = Boolean.FALSE;
			}
		}
		return threadLocalVariable;
	}

	@Override
	public Object duplicate() {
		return THREAD_LOCAL.get();
	}

	@Override
	public void fill(Object duplicate) {
		THREAD_LOCAL.set((ThreadLocalVariable) duplicate);
	}

	@Override
	public Optional<IUser> getUser() {
		ThreadLocalVariable threadLocalVariable = getThreadLocalVariable();
		return threadLocalVariable.user;
	}

	@Override
	public void setUser(IUser user) {
		getThreadLocalVariable().user = Optional.of(user);
	}

	@Override
	public Optional<IGroup> getOrg() {
		ThreadLocalVariable threadLocalVariable = getThreadLocalVariable();
		if (Objects.isNull(threadLocalVariable.org)) {
			threadLocalVariable.org = Optional.empty();
			IGroup org = null;
			if (Objects.nonNull(threadLocalVariable.user) && threadLocalVariable.user.isPresent()) {
				org = CollUtil.getFirst(groupApi.getByGroupTypeAndUserId(GroupType.ORG.getType(), threadLocalVariable.user.get().getUserId()));
			}
			threadLocalVariable.org = Optional.ofNullable(org);
		}
		return threadLocalVariable.org;
	}

	@Override
	public void setOrg(IGroup org) {
		getThreadLocalVariable().org = Optional.ofNullable(org);
	}

	@Override
	public Collection<String> getAuthorities() {
		return THREAD_LOCAL.get().authorizes;
	}

	@Override
	public void setAuthorities(Collection<String> authorities) {
		THREAD_LOCAL.get().authorizes = authorities;
	}

	@Override
	public boolean isSuperAdmin() {
		ThreadLocalVariable threadLocalVariable = getThreadLocalVariable();
		if (threadLocalVariable.superAdmin == null) {
			String username = getUser().map(IUser::getUsername).orElseThrow(() -> new IllegalArgumentException("Current username null"));
			threadLocalVariable.superAdmin = ArrayUtil.contains(PropertyEnum.ADMIN_ACCOUNTS.getPropertyValue(String[].class), username);
		}
		return threadLocalVariable.superAdmin;
	}

	@Override
	public void setSuperAdmin(boolean superAdmin) {
		THREAD_LOCAL.get().superAdmin = superAdmin;
	}

	@Override
	public void clear() {
		THREAD_LOCAL.remove();
	}

	@Override
	public void afterPropertiesSet() {
		ContextCleanUtils.register(this::clear, ContextCleanUtils.Phase.values());
	}

	@Override
	public Set<String> getFilterUserIds(String userId) {
		return SetUtils.hashSet(userId);
	}
}
