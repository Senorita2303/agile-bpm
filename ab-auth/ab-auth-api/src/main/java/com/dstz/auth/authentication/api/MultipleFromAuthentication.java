package com.dstz.auth.authentication.api;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public interface MultipleFromAuthentication {

    /**
     * Get the source
     *
     * @return source
     */
    String getFrom();

    /**
     * Authentication
     *
     * @param userDetails    Login User Details
     * @param authentication User password authentication string
     * @throws AuthenticationException Authentication exception
     */
    void authentication(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException;

}
