package com.dstz.auth.utils;

import com.dstz.base.common.constats.StrPool;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Ignore authentication address check
 *
 */
public class IngoreChecker {

    /**
     * Ignore Address
     */
    private String[] ignoreUrls;

    public void setIgnoreUrls(String[] ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    /**
     * Determines whether the current URL is among the ignored addresses.
     *
     * @param request ask
     * @return
     */
    public boolean isIngores(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        // It will jump to index.html again, so just ignore it
        return StrPool.SLASH.equals(servletPath) || PatternMatchUtils.simpleMatch(ignoreUrls, servletPath);
    }

}
