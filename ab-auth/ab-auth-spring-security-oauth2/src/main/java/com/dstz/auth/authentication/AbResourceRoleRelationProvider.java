package com.dstz.auth.authentication;

import cn.hutool.core.collection.CollUtil;
import com.dstz.auth.constant.PlatformConsts;
import com.dstz.auth.utils.IngoreChecker;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Get the list of roles assigned to it based on the current URL
 *
 */
public class AbResourceRoleRelationProvider extends IngoreChecker implements FilterInvocationSecurityMetadataSource {

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = ((FilterInvocation) object);
        HttpServletRequest request = filterInvocation.getRequest();
        // Anonymous access
        if (isIngores(request)) {
            return CollUtil.newHashSet(PlatformConsts.ROLE_CONFIG_ANONYMOUS);
        }
        return CollUtil.newHashSet(PlatformConsts.URL_AUTH);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
