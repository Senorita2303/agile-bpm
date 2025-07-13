
package com.dstz.springboot.autoconfigure.oauth2;

import cn.hutool.extra.spring.SpringUtil;
import com.dstz.auth.authentication.AbAuthenticationTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

@Configuration
public class TokenConfig {


    @Autowired
    private DataSource dataSource;

    /**
     * Default injected memory storage
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "ab.oauth.token-store-type",havingValue = "InMemory", matchIfMissing = true)
    public TokenStore tokenStoreInMemory() {
        InMemoryTokenStore store = new InMemoryTokenStore();
        store.setAuthenticationKeyGenerator(new AbAuthenticationTokenGenerator());
        return store;
    }

    @Bean
    @ConditionalOnProperty(name = "ab.oauth.token-store-type",havingValue = "redis")
    public TokenStore tokenStoreInRedis() {
        RedisTokenStore store = new RedisTokenStore(SpringUtil.getBean(RedisConnectionFactory.class));
        store.setPrefix("ab:token:");
        store.setAuthenticationKeyGenerator(new AbAuthenticationTokenGenerator());
        return store;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // Symmetric key, which the resource server uses to authenticate
        converter.setSigningKey(SpringUtil.getProperty("ab.oauth.login-token-signing-key"));
        return converter;
    }
}

