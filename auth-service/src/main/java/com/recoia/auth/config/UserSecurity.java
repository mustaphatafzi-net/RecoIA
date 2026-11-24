
package com.recoia.auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    public boolean isSelf(Long userId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object authenticatedUserId = authentication.getDetails();

        return authenticatedUserId instanceof Long
                && authenticatedUserId.equals(userId);
    }
}
