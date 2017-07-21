package com.jazasoft.mtdb.audit;

import com.jazasoft.mtdb.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by mdzahidraza on 01/07/17.
 */
public class UsernameAuditorAware implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return ((User) authentication.getPrincipal()).getName();
    }
}
