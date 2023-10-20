package com.tuto.example.config;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
@PersistenceUnitExtension(value = "tenant")
public class CustomTenantResolver implements TenantResolver
{

    @Override
    public String getDefaultTenantId()
    {
        return "tenant1";
    }

    @Override
    public String resolveTenantId()
    {
        return CurrentTenant.get().orElseThrow(() -> new RuntimeException("Tenant not provided"));
    }

}
