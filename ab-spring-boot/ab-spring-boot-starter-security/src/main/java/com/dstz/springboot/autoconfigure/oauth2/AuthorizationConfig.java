
package com.dstz.springboot.autoconfigure.oauth2;

import com.dstz.auth.authentication.AbClientDetailsServiceImpl;
import com.dstz.auth.authentication.AbDaoAuthenticationProvider;
import com.dstz.auth.constant.AuthConstant;
import com.dstz.auth.exception.Auth2CustomizeExceptionTranslator;
import com.dstz.auth.login.CustomPwdEncoder;
import com.dstz.auth.login.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Collections;

import static com.dstz.auth.authentication.api.constant.AuthApiConstant.*;


/**
 * Issuing token configuration
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ClientDetailsService clientDetailsService;


    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;



    @Autowired
    private TokenStore tokenStore;

    CustomPwdEncoder customPwdEncoder = new CustomPwdEncoder();

    @Bean
    public AbDaoAuthenticationProvider abDaoAuthenticationProvider() {
        AbDaoAuthenticationProvider abDaoAuthenticationProvider = new AbDaoAuthenticationProvider();
        abDaoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        abDaoAuthenticationProvider.setUserDetailsService(userDetailsService());
        abDaoAuthenticationProvider.setPasswordEncoder(customPwdEncoder);
        return abDaoAuthenticationProvider;
    }

    @Bean("userDetailsService")
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean("authenticationManager")
    public AuthenticationManager authenticationManagerBean() {
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(abDaoAuthenticationProvider()));
        providerManager.setAuthenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationContext));
        return providerManager;
    }


    /**
     * Configure token endpoint security constraints
     *
     * @param security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)  {
        security.tokenKeyAccess(TOKEN_SERVER_SECURITY_CONFIGURER)
                .checkTokenAccess(TOKEN_SERVER_SECURITY_CONFIGURER)
                .allowFormAuthenticationForClients();
    }


    /**
     * Configure client details service
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        AbClientDetailsServiceImpl clientDetailsService = new AbClientDetailsServiceImpl(dataSource);
        clientDetailsService.setSelectClientDetailsSql(AuthConstant.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(AuthConstant.DEFAULT_FIND_STATEMENT);
        clients.withClientDetails(clientDetailsService);
    }


    /**
     * Configure token access endpoint, token service
     *
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        endpoints
                //Customize the authorization consent page
                .pathMapping(TOKEN_SERVER_AUTH_DEFAULTPATH, TOKEN_SERVER_AUTH_CUSTOMPATH)
                //Authentication Manager
                .authenticationManager(authenticationManagerBean())
                //Custom exception handling
                //.exceptionTranslator(new Auth2CustomizeExceptionTranslator())
                //User information management in password mode
                .userDetailsService(userDetailsService())
                //Authorization Code Service
                .authorizationCodeServices(authorizationCodeServices())
                //Token Management Services
                .tokenServices(tokenService())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET);
    }


    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        // Client Details Service
        service.setClientDetailsService(clientDetailsService);
        // Whether to support token refresh
        service.setSupportRefreshToken(true);
        // Token storage strategy
        service.setTokenStore(tokenStore);
        // Token Enhancement
       /* TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);*/
        // The default validity period of the token is 2 hours
        service.setAccessTokenValiditySeconds(ACCESSTOKEN_VALIDITY_SECONDS);
        // The refresh token is valid for 3 days by default.
        service.setRefreshTokenValiditySeconds(REFRESHTOKEN_VALIDITY_SECONDS);
        return service;
    }
}

