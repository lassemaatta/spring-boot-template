package com.example.security;

import com.example.feature.base.tenant.Tenant;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant";
    public static final String TENANT_ATTRIBUTE = "com.example.security.tenant";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(TENANT_HEADER);
        if (header != null) {
            try {
                request.setAttribute(TENANT_ATTRIBUTE, Tenant.of(header));
            } catch (final IllegalArgumentException ignored) {
                // Ignore invalid tenant
            }
        }
        filterChain.doFilter(request, response);
    }
}
