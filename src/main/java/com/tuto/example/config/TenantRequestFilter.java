package com.tuto.example.config;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class TenantRequestFilter implements ContainerRequestFilter
{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {
        String tenantId = requestContext.getHeaderString("tenant");
        if (tenantId != null && !tenantId.isBlank())
            CurrentTenant.set(tenantId);
        else
            CurrentTenant.clear();
    }

}
