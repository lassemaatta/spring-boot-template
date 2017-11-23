package com.example.feature.base;

import com.example.feature.base.tenant.Schema;
import com.example.feature.base.tenant.Tenant;
import com.example.feature.base.tenant.discovery.SchemaDiscoverer;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
public class MultiTenantMigration extends MultiTenantSpringLiquibase {

    private static final Logger LOG = LoggerFactory.getLogger(MultiTenantMigration.class);
    private static final String CHANGELOG =
            "classpath:db/changelog/db.changelog-master.yaml";

    @Nonnull
    private final DataSource dataSource;

    @Nonnull
    private final SchemaDiscoverer schemaProvider;

    @Autowired
    public MultiTenantMigration(@Nonnull final DataSource dataSource,
                                @Nonnull final SchemaDiscoverer schemaProvider) {
        this.dataSource = Objects.requireNonNull(dataSource);
        this.schemaProvider = Objects.requireNonNull(schemaProvider);

        setDataSource(dataSource);
        setChangeLog(CHANGELOG);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final ImmutableSet<Schema> existingSchemas = schemaProvider.getSchemas();
        final List<Schema> schemasToMigrate = Lists.newArrayList(existingSchemas);

        if (!existingSchemas.contains(Schema.DEFAULT_SCHEMA)) {
            schemaProvider.createSchema(Schema.DEFAULT_SCHEMA);
            schemasToMigrate.add(Schema.DEFAULT_SCHEMA);
        }

        LOG.debug("Migrating: {}", Joiner.on(", ").join(schemasToMigrate));
        setSchemas(schemasToMigrate.stream().map(Schema::toString).collect(toList()));

        // Perform the migration
        super.afterPropertiesSet();
    }

    public void createAndMigrate(@Nonnull final Tenant tenant) {
        final Schema schema = Schema.of(tenant);
        if (schemaProvider.getSchemas().contains(schema)) {
            LOG.debug("Not creating schema '{}', already exists", schema);
        } else {
            schemaProvider.createSchema(schema);
        }

        final SpringLiquibase slb = new SpringLiquibase();
        slb.setChangeLog(CHANGELOG);
        slb.setDataSource(dataSource);
        slb.setDefaultSchema(schema.toString());
        slb.setResourceLoader(new DefaultResourceLoader());

        try {
            LOG.debug("Migrating {}", schema);
            slb.afterPropertiesSet();
        } catch (final LiquibaseException e) {
            LOG.error("Failed to migrate", e);
            throw new RuntimeException(e);
        }
    }
}
