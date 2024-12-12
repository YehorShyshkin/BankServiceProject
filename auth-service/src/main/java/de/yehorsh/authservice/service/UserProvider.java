package de.yehorsh.authservice.service;

import de.yehorsh.authservice.model.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * This class provides methods to retrieve the currently authenticated user.
 */
@Component
public class UserProvider {
    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the currently authenticated {@link User}
     */
    public User getCurrentUser() {
        return ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .user();
    }
}
