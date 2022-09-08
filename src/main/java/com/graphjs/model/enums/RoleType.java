package com.graphjs.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {
    ADMIN,
    CLIENT;
    public String getAuthority() {
        return name();
    }
    }
