package com.dstz.auth.login;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dstz.auth.model.LoginUser;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.UserApi;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Implement the UserDetailsService interface to obtain the UserDetails interface instance object.
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private GroupApi groupApi;

    @Autowired
    private UserApi userApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        IUser user = userApi.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("%s does not exist", username));
        }
        // Get Role
        Collection<GrantedAuthority> authorities = ObjectUtil.defaultIfNull(groupApi.getByGroupTypeAndUserId(GroupType.ROLE.getType(), user.getUserId()), Collections.<IGroup>emptyList())
                                                             .stream().map(IGroup::getGroupCode).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        LoginUser loginUser = new LoginUser();
        loginUser.setDelegateUser(user);
        loginUser.setAuthorities(authorities);
        loginUser.setSuperAdmin(ArrayUtil.contains(PropertyEnum.ADMIN_ACCOUNTS.getPropertyValue(String[].class), user.getUsername()));
        return loginUser;
    }
}
