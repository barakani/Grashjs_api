package com.grash.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleType implements GrantedAuthority {
    SUPER_ADMIN,
    CLIENT;

    public String getAuthority() {
        return name();
    }
}
