package com.ciklon.friendtracker.common.auth.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ADMIN");

    private final String value;
    @Override
    public String getAuthority() {
        return value;
    }

    public static final Role BASE_ROLE = Role.ADMIN;
}
