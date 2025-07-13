package com.dstz.auth.filter;

import cn.hutool.extra.servlet.ServletUtil;
import com.dstz.auth.utils.IngoreChecker;
import com.dstz.auth.utils.RefererCsrfChecker;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.utils.JsonUtils;
import org.springframework.http.MediaType;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Prevent CSRF cross-site request attacks.<br>
 * This is mainly to prevent external links from connecting to this system.
 *
 */
public class RefererCsrfFilter extends RefererCsrfChecker implements Filter {


    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        // Determine whether it is an external link.
        String referer = req.getHeader("Referer");
        String serverName = request.getServerName();
        // The request did not come from this website.
        if (null != referer && referer.indexOf(serverName) < 0) {
            // Whether to include the current URL
            boolean isIngoreUrl = this.isIngores(referer);
            if (isIngoreUrl) {
                chain.doFilter(request, response);
            } else {
                String msg = String.format("The system does not support access to the current domain name, please contact the administrator! <br> Server: %s, current domain name: %s", serverName, referer);
                response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                ServletUtil.write((HttpServletResponse) response, JsonUtils.toJSONString(ApiResponse.fail(GlobalApiCodes.INTERNAL_ERROR.getCode(), msg)), MediaType.APPLICATION_JSON_VALUE);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig config) {
    }

}
