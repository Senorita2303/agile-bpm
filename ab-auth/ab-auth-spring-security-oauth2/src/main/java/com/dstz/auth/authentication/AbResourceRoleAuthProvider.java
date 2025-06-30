package com.dstz.auth.authentication;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dstz.auth.authentication.api.SysResourceApi;
import com.dstz.auth.constant.PlatformConsts;
import com.dstz.base.common.utils.UserContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Access the decision manager. The current resource requires roles to determine whether the user has these roles. If so,
 *
 */
public class AbResourceRoleAuthProvider implements AccessDecisionManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysResourceApi sysResourceApi;

    /**
     * Determine whether the role of the current user meets the role required by the current resource
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes.contains(PlatformConsts.ROLE_CONFIG_ANONYMOUS)) {
            return;
        }
        if (authentication.isAuthenticated()) {
            FilterInvocation filterInvocation = (FilterInvocation) object;
            final String servletPath = filterInvocation.getRequest().getServletPath();

            if (UserContextUtils.isSuperAdmin()) {
                logger.trace("Path: {}, the current access user is a super administrator, skip resource control", servletPath);
                return;
            }

            Set<String> accessRoleByUrl = sysResourceApi.getAccessRoleByUrl(servletPath);
            if (CollUtil.isEmpty(accessRoleByUrl)) {
                logger.trace("Path: {}, no authorization role found, no permission check will be performed for the current access", servletPath);
                return;
            }

            // Traverse the roles of the current user. If the role corresponding to the current page contains a role of the current user, then the page has permission to access.
            for (GrantedAuthority grantedAuthority : ObjectUtil.<Collection<? extends GrantedAuthority>>defaultIfNull(authentication.getAuthorities(), Collections.emptyList())) {
                if (accessRoleByUrl.contains(grantedAuthority.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("Sorry, you do not have access rights to this resource");
    }


    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
