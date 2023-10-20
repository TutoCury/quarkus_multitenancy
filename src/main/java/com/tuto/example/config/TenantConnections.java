package com.tuto.example.config;

import static io.agroal.api.configuration.AgroalConnectionPoolConfiguration.ConnectionValidator.defaultValidator;
import static java.time.Duration.ofSeconds;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.AgroalDataSourceConfiguration;
import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.agroal.api.security.NamePrincipal;
import io.agroal.api.security.SimplePassword;
import io.quarkus.arc.Unremovable;
import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.customized.QuarkusConnectionProvider;
import io.quarkus.hibernate.orm.runtime.tenant.TenantConnectionResolver;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Unremovable
@PersistenceUnitExtension(value = "tenant")
public class TenantConnections implements TenantConnectionResolver
{

    private final Map<String, ConnectionProvider> cache = new HashMap<>();

    @ConfigProperty(name = "quarkus.datasource.tenant.jdbc.url")
    String jdbcUrl;

    @ConfigProperty(name = "quarkus.datasource.tenant.username")
    String username;

    @ConfigProperty(name = "quarkus.datasource.tenant.password")
    String password;

    private AgroalDataSourceConfiguration createDataSourceConfiguration(String tenant)
    {
        return new AgroalDataSourceConfigurationSupplier()
                .dataSourceImplementation(AgroalDataSourceConfiguration.DataSourceImplementation.AGROAL)
                .metricsEnabled(false)
                .connectionPoolConfiguration(cp -> cp
                        .minSize(0)
                        .maxSize(5)
                        .initialSize(0)
                        .connectionValidator(defaultValidator())
                        .acquisitionTimeout(ofSeconds(5))
                        .leakTimeout(ofSeconds(5))
                        .validationTimeout(ofSeconds(50))
                        .reapTimeout(ofSeconds(500))
                        .connectionFactoryConfiguration(cf -> cf
                                .jdbcUrl(jdbcUrl + tenant)
                                .connectionProviderClassName("com.mysql.cj.jdbc.Driver")
                                .principal(new NamePrincipal(username))
                                .credential(new SimplePassword(password))))
                .get();
    }

    @Override
    public ConnectionProvider resolve(String tenant)
    {

        if (cache.containsKey(tenant))
            return cache.get(tenant);

        try
        {
            AgroalDataSource agroalDataSource = AgroalDataSource.from(createDataSourceConfiguration(tenant));
            QuarkusConnectionProvider quarkusConnectionProvider = new QuarkusConnectionProvider(agroalDataSource);
            cache.put(tenant, quarkusConnectionProvider);
            return quarkusConnectionProvider;
        }
        catch (SQLException ex)
        {
            throw new IllegalStateException("Failed to create a new data source based on the tenantId: " + tenant, ex);
        }
    }

}
