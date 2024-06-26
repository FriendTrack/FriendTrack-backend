package com.ciklon.friendtracker.common.security.constant;

public class SecurityConstants {
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String AUTH_HEADER = "Authorization";

    public static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/favicon.ico"
    };

    public static final String LOGIN = "login";
}
