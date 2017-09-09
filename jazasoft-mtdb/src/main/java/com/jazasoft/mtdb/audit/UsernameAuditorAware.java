package com.jazasoft.mtdb.audit;

import com.jazasoft.mtdb.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by mdzahidraza on 01/07/17.
 */
public class UsernameAuditorAware implements AuditorAware<String> {
    private final Logger logger = LoggerFactory.getLogger(UsernameAuditorAware.class);
    @Override
    public String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        if (authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getName();
        } else if (authentication.getPrincipal() instanceof String){
            logger.debug("princilpal = {}", authentication.getPrincipal());
            return (String) authentication.getPrincipal();
        }
        logger.debug("princilpal = {}", authentication.getPrincipal());
        return null;
    }
}
