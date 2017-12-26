package com.jazasoft.mtdb.tenant;

import com.jazasoft.mtdb.IConstants;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Component
public class CurrentTennatIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    private final Logger logger = LoggerFactory.getLogger(CurrentTenantIdentifierResolver.class);

    @Override
    public String resolveCurrentTenantIdentifier() {
        logger.debug("resolveCurrentTenantIdentifier");
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            String identifier = (String) requestAttributes.getAttribute(IConstants.CURRENT_TENANT_IDENTIFIER,
                    RequestAttributes.SCOPE_REQUEST);
            if (identifier != null) {
                return identifier;
            }
        }
        return IConstants.UNKNOWN_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
