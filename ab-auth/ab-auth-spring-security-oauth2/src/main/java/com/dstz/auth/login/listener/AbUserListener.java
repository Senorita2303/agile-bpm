package com.dstz.auth.login.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.dstz.auth.login.AbTokenService;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.events.AbUserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbUserListener implements ApplicationListener<AbUserEvent> {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ICache cache;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AbTokenService abTokenService;

    @Override
    public void onApplicationEvent(AbUserEvent event) {
        // Update User
        if (AbUserEvent.EventType.UPDATE_USER.equals(event.getEventType())) {
            if (CollectionUtil.isNotEmpty(event.getUserAccountList())) {
                event.getUserAccountList().forEach(e -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(e);
                    List<OAuth2AccessToken> auth2AccessTokenList = abTokenService.getOAuth2AccessTokenByAccount(e);
                    auth2AccessTokenList.forEach(o ->{
                        if (ObjectUtil.isNotEmpty(o)) {
                            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(o);
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getUsername(), userDetails.getAuthorities());
                            // Copy the variable information when logging in
                            authenticationToken.setDetails(oAuth2Authentication.getUserAuthentication().getDetails());
                            tokenStore.storeAccessToken(o, new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), authenticationToken));
                        }
                    });

                });
            }
        }

        // Deleting a User
        if (AbUserEvent.EventType.DELETE_USER.equals(event.getEventType())) {
            if (CollectionUtil.isNotEmpty(event.getUserAccountList())) {
                event.getUserAccountList().forEach(e -> {
                    abTokenService.removeAccessToken(e);
                });
            }
        }
    }


}
