package com.cinema.security;

import com.cinema.models.User;
import java.util.Arrays;

public class AuthorizationService {
    public void require(Role... allowed) {
        User u = SecurityContext.getCurrentUser();
        if (u == null) throw new AccessDeniedException("Please login first.");

        boolean ok = Arrays.stream(allowed)
                .anyMatch(r -> r.name().equalsIgnoreCase(u.getRole()));
        if (!ok) throw new AccessDeniedException("Access denied for role: " + u.getRole());
    }
}
