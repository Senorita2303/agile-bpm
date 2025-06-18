package com.dstz.auth.authentication.api.model;

public interface ISysApplication {

    /**
     * Return Primary Key
     *
     * @return
     */
    String getId();

    /**
     * Return System Name
     *
     * @return
     */
    String getName();

    /**
     * Return System Alias
     *
     * @return
     */
    String getCode();


    /**
     * Key
     */
    String getSecret();

    /**
     * Refresh seconds
     */
    Integer getRefreshTokenValidity();

    /**
     * Validity
     */
    Integer getAccessTokenValidity();

    /**
     * System address, if empty, it is the current system
     */
    String getUrl();

    /**
     * Callback address
     */
    String getRedirectUri();

    /**
     * Open
     */
    String getOpenType();


    /**
     * Returns whether it is available: 1 for available, 0 for unavailable
     *
     * @return
     */
    Integer getEnabled();

    /**
     * Is it default
     */
    Integer getIsDefault();

    /**
     * Description Notes
     */
    String getDesc();

    /**
     * Extended Configuration
     */
    String getConfig();

    /**
     * Is the app available?
     * @return
     */
    Integer getAppType();


}