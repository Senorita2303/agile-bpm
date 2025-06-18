package com.dstz.base.web.filter;

import com.dstz.base.common.utils.ContextCleanUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ab context cleanup filter
 *
 */
@Component("abContextCleanFilter")
public class AbContextCleanFilter extends OncePerRequestFilter implements Ordered {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            ContextCleanUtils.executeAll();
            filterChain.doFilter(request, response);
        } finally {
            ContextCleanUtils.executeAll();
        }
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
