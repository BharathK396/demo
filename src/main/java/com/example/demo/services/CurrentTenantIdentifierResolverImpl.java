package com.example.demo.services;

import java.util.Optional;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        return Optional.ofNullable(getCurrentTenant()).orElse("default_tenant");
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

