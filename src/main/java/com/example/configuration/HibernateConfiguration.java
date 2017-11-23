package com.example.configuration;

import com.google.common.collect.ImmutableMap;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Nonnull final DataSource dataSource,
                                                                       @Nonnull final MultiTenantConnectionProvider provider,
                                                                       @Nonnull final CurrentTenantIdentifierResolver resolver) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.feature");

        em.setJpaVendorAdapter(jpaVendorAdapter());

        em.setJpaPropertyMap(ImmutableMap.of(
                AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.SCHEMA,
                AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, provider,
                AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, resolver,
                AvailableSettings.FORMAT_SQL, true
        ));
        return em;
    }
}
