package com.dstz.base.common.utils;


import cn.hutool.core.util.StrUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Cookie operation class
 *
 */
public class CookieUitl {
    // Automatic expiration
    public static final int cookie_auto_expire = -1;
    // Not expired
    public static final int cookie_no_expire = 60 * 60 * 24 * 365;

    /**
     * Add a cookie. The life cycle of the cookie is to disappear when the browser is closed
     *
     * @param name
     * @param value
     */
    public static void addCookie(String name, String value) {
        addCookie(name, value, -1, getContextPath(), AbRequestUtils.getHttpServletResponse());
    }

    public static void addCookie(String name, String value, int timeout) {
        addCookie(name, value, timeout, getContextPath(), AbRequestUtils.getHttpServletResponse());
    }

    public static String getContextPath() {
        String contextPath = AbRequestUtils.getHttpServletRequest().getContextPath();
        if (StrUtil.isEmpty(contextPath)) {
            contextPath = "/";
        }
        return contextPath;
    }


    public static void addCookie(String name, String value, int maxAge, String contextPath, HttpServletResponse response) {

        Cookie cookie = new Cookie(name, value);
        cookie.setPath(contextPath);
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }


    /**
     * Delete cookies
     *
     * @param name
     * @param response
     */
    public static void delCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        if (request == null) return;

        Cookie uid = new Cookie(name, null);
        uid.setPath("/");
        uid.setMaxAge(0);
        response.addCookie(uid);
    }


    public static String getValueByName(String cookieName, HttpServletRequest request) {
        if (request == null) return null;

        Cookie cookies[] = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        return null;
    }

    public static String getValueByName(String name) {
        return getValueByName(name, AbRequestUtils.getHttpServletRequest());
    }

}
