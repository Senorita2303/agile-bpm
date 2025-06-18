package com.dstz.component.j2cache;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2CacheBuilder;
import net.oschina.j2cache.J2CacheConfig;
import org.springframework.beans.factory.FactoryBean;

import java.util.Properties;

/**
 * cacheChannel bean factory
 *
 */
public class J2CacheChannelFactoryBean implements FactoryBean<CacheChannel> {

    /**
     * Serialization
     */
    private String serialization;

    /**
     * Broadcast
     */
    private String broadcastProviderClass;

    /**
     * First-level cache implementation class
     */
    private String cacheL1ProviderClass;

    /**
     * Second level cache implementation class
     */
    private String cacheL2ProviderClass;

    /**
     * Whether to cache empty objects
     */
    private Boolean defaultCacheNullObject;

    /**
     * First-level cache configuration properties
     */
    private Properties cacheL1Properties;

    /**
     * Second level cache configuration
     */
    private Properties cacheL2Properties;

    /**
     * Broadcast Configuration
     */
    private Properties broadcastProperties;

    @Override
    public CacheChannel getObject() {
        J2CacheConfig config = new J2CacheConfig();
        config.setSerialization(serialization);
        config.setBroadcast(broadcastProviderClass);
        config.setL1CacheName(cacheL1ProviderClass);
        config.setL2CacheName(cacheL2ProviderClass);
        config.setSyncTtlToRedis(Boolean.FALSE);
        config.setDefaultCacheNullObject(Boolean.TRUE.equals(defaultCacheNullObject));
        config.setL1CacheProperties(cacheL1Properties);
        config.setL2CacheProperties(cacheL2Properties);
        config.setBroadcastProperties(broadcastProperties);
        return J2CacheBuilder.init(config).getChannel();
    }

    @Override
    public Class<?> getObjectType() {
        return CacheChannel.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getSerialization() {
        return serialization;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public String getBroadcastProviderClass() {
        return broadcastProviderClass;
    }

    public void setBroadcastProviderClass(String broadcastProviderClass) {
        this.broadcastProviderClass = broadcastProviderClass;
    }

    public String getCacheL1ProviderClass() {
        return cacheL1ProviderClass;
    }

    public void setCacheL1ProviderClass(String cacheL1ProviderClass) {
        this.cacheL1ProviderClass = cacheL1ProviderClass;
    }

    public String getCacheL2ProviderClass() {
        return cacheL2ProviderClass;
    }

    public void setCacheL2ProviderClass(String cacheL2ProviderClass) {
        this.cacheL2ProviderClass = cacheL2ProviderClass;
    }

    public Boolean getDefaultCacheNullObject() {
        return defaultCacheNullObject;
    }

    public void setDefaultCacheNullObject(Boolean defaultCacheNullObject) {
        this.defaultCacheNullObject = defaultCacheNullObject;
    }

    public Properties getCacheL1Properties() {
        return cacheL1Properties;
    }

    public void setCacheL1Properties(Properties cacheL1Properties) {
        this.cacheL1Properties = cacheL1Properties;
    }

    public Properties getCacheL2Properties() {
        return cacheL2Properties;
    }

    public void setCacheL2Properties(Properties cacheL2Properties) {
        this.cacheL2Properties = cacheL2Properties;
    }

    public Properties getBroadcastProperties() {
        return broadcastProperties;
    }

    public void setBroadcastProperties(Properties broadcastProperties) {
        this.broadcastProperties = broadcastProperties;
    }
}
