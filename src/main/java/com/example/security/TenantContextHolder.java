package com.example.security;

import com.example.feature.base.tenant.Tenant;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static com.example.security.TenantFilter.TENANT_ATTRIBUTE;

@Component
public class TenantContextHolder implements CurrentTenantIdentifierResolver {

    private static final Logger LOG = LoggerFactory.getLogger(TenantContextHolder.class);

    @Override
    public String resolveCurrentTenantIdentifier() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final Tenant tenant = (Tenant) requestAttributes.getAttribute(TENANT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
            if (tenant != null) {
                LOG.debug("Got tenant {} from attribute", tenant);
                return tenant.toString();
            }
        }
        LOG.debug("Context has no tenant");
        return "default";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
