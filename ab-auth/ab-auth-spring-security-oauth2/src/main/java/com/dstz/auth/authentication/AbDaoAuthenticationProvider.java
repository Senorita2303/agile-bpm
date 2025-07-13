package com.dstz.auth.authentication;

import cn.hutool.core.util.StrUtil;
import com.dstz.auth.authentication.api.MultipleFromAuthentication;
import com.dstz.auth.authentication.api.constant.AuthApiConstant;
import com.dstz.auth.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Rewrite the login based on system source
 *
 */
public class AbDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private SessionAuthenticationStrategy sessionStrategy;



    private Map<String, MultipleFromAuthentication> multipleFromAuthenticationBeanMap;

    /**
     * Convert the source to lowercase and match case insensitively
     *
     * @param from Account Source
     * @return Lowercase characters source
     */
    private String fromToLowerCase(String from) {
        return StrUtil.swapCase(from);
    }

    private MultipleFromAuthentication getMultipleFromAuthenticationImplement(String from) {
        Map<String, MultipleFromAuthentication> localMap = multipleFromAuthenticationBeanMap;
        if (localMap == null) {
            synchronized (this) {
                localMap = multipleFromAuthenticationBeanMap;
                if (localMap == null) {
                    Collection<MultipleFromAuthentication> multipleFromAuthenticationBeans = applicationContext.getBeansOfType(MultipleFromAuthentication.class).values();
                    localMap = new HashMap<>(multipleFromAuthenticationBeans.size());
                    for (MultipleFromAuthentication multipleFromAuthenticationBean : multipleFromAuthenticationBeans) {
                        localMap.put(fromToLowerCase(multipleFromAuthenticationBean.getFrom()), multipleFromAuthenticationBean);
                    }
                    multipleFromAuthenticationBeanMap = localMap;
                }
            }
        }
        return localMap.get(fromToLowerCase(from));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken
            authentication) {
        if (!(userDetails instanceof LoginUser)) {
            super.additionalAuthenticationChecks(userDetails, authentication);
        } else {
            LoginUser loginUser = (LoginUser) userDetails;
            if (StrUtil.isEmpty(loginUser.getFrom()) || AuthApiConstant.SYSTEM.equalsIgnoreCase(loginUser.getFrom())) {
                super.additionalAuthenticationChecks(userDetails, authentication);
            } else {
                multipleFromAuthentication(loginUser, authentication);
            }
        }
        // Compatible with other versions without injection
        if (sessionStrategy != null) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            sessionStrategy.onAuthentication(new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null, userDetails.getAuthorities()), requestAttributes.getRequest(), requestAttributes.getResponse());
        }
    }

    private void multipleFromAuthentication(LoginUser userDetails, UsernamePasswordAuthenticationToken
            authentication) {
        MultipleFromAuthentication multipleFromAuthentication = getMultipleFromAuthenticationImplement(userDetails.getFrom());
        if (multipleFromAuthentication == null) {
            super.additionalAuthenticationChecks(userDetails, authentication);
        } else {
            multipleFromAuthentication.authentication(userDetails, authentication);
        }
    }
}
