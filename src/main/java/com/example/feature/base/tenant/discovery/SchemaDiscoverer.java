package com.example.feature.base.tenant.discovery;

import com.example.feature.base.tenant.Schema;
import com.google.common.collect.ImmutableSet;
import org.assertj.core.util.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Set;

public abstract class SchemaDiscoverer {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaDiscoverer.class);

    @Nonnull private final DataSource dataSource;
    @Nonnull private final String readSchemasQuery;
    @Nonnull private final String createSchemaQuery;

    protected SchemaDiscoverer(@Nonnull final DataSource dataSource,
                               @Nonnull final String readSchemasQuery,
                               @Nonnull final String createSchemaQuery) {
        this.dataSource = Objects.requireNonNull(dataSource);
        this.readSchemasQuery = readSchemasQuery;
        this.createSchemaQuery = createSchemaQuery;
    }

    public void createSchema(@Nonnull final Schema schema) {
        final String query = createSchemaQuery + schema;
        LOG.warn("Creating schema: {}", query);
        try (final Connection connection = dataSource.getConnection();
             final Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        } catch (final SQLException ex) {
            LOG.error("Failed to create schema", ex);
        }
    }

    @Nonnull
    public ImmutableSet<Schema> getSchemas() {
        LOG.debug("Looking for schemas..");
        final Set<Schema> results = Sets.newHashSet();
        try (final Connection connection = dataSource.getConnection();
             final Statement stmt = connection.createStatement();
             final ResultSet rs = stmt.executeQuery(readSchemasQuery)){
            while (rs.next()) {
                final String schemaName = rs.getString(1);
                if (schemaName != null) {
                    LOG.debug("Found candidate schema: {}", schemaName);
                    try {
                        final Schema schema = Schema.of(schemaName);
                        if (schema != null) {
                            results.add(schema);
                        }
                    } catch (final IllegalStateException ignored) {
                        // ignore non-tenant schemas
                    }
                }
            }
        } catch (final SQLException ex) {
            LOG.error("Failed to read schemas", ex);
        }
        return ImmutableSet.copyOf(results);
    }
}
