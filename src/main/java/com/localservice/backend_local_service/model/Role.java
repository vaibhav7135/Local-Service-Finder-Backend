package com.localservice.backend_local_service.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_JOBSEEKER,
    ROLE_SERVICE_PROVIDER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}