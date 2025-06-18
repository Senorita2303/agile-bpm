package com.dstz.auth.filter;

import com.dstz.auth.utils.IngoreChecker;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
/**
 * csrf security filter.
 *
 */
public class SecurityRequestCsrfMatcher extends IngoreChecker implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {

        boolean isIngoreUrl = isIngores(request);

        if (isIngoreUrl) {
            return true;
        }

        return false;
    }

}
