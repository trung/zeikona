package org.mdkt.zeikona.security;

import org.mdkt.zeikona.api.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by trung on 13/11/14.
 */
public class SecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String accessToken = getAccessToken(httpServletRequest);
        httpServletRequest.getUserPrincipal();

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private String getAccessToken(HttpServletRequest httpServletRequest) {
        String authorizationValue = httpServletRequest.getHeader("Authorization");
        if (authorizationValue != null && authorizationValue.startsWith(Constants.BEARER)) {
            return authorizationValue.replaceFirst(Constants.BEARER, "");
        }
        return null;
    }
}
