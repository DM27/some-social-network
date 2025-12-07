package com.training.some_social_network.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public class SecurityHelper {

    public static Optional<UUID> getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(auth -> (UserDetails) auth.getPrincipal())
                .map(UserDetails::getUsername)
                .map(UUID::fromString);
    }
}
