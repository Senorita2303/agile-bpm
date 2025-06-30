package com.dstz.auth.constant;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

/**
 * Permission Type
 *
 */
public class PlatformConsts {

    /**
     * Anonymous level
     */
    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * Verify permissions
     */
    private final static String ROLE_AUTH = "ROLE_AUTH";

    public static final ConfigAttribute ROLE_CONFIG_ANONYMOUS = new SecurityConfig(ROLE_ANONYMOUS);

    public static final ConfigAttribute URL_AUTH = new SecurityConfig(ROLE_AUTH);
}
