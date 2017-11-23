package com.example.feature.base.tenant.connector;

import com.example.feature.base.tenant.Schema;
import com.example.feature.base.tenant.Tenant;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public abstract class SchemaConnector implements MultiTenantConnectionProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaConnector.class);

    @Nonnull private final DataSource dataSource;
    @Nonnull private final String changeSchemaQuery;

    protected SchemaConnector(@Nonnull final DataSource dataSource,
                              @Nonnull final String changeSchemaQuery) {
        this.dataSource = Objects.requireNonNull(dataSource);
        this.changeSchemaQuery = Objects.requireNonNull(changeSchemaQuery);
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(final Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(final String tenantIdentifier) throws SQLException {
        LOG.debug("Get connection for {}", tenantIdentifier);
        return getConnectionToSchema(Schema.of(Tenant.of(tenantIdentifier)));
    }

    @Nonnull
    private Connection getConnectionToSchema(@Nonnull final Schema schema) throws SQLException {
        final String query = changeSchemaQuery + schema;
        LOG.debug("Changing schema: {}", query);
        final Connection connection = getAnyConnection();
        try (final Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            return connection;
        } catch (final SQLException e) {
            throw new HibernateException(String.format("Could not activate schema '%s'", schema), e);
        }
    }

    @Override
    public void releaseConnection(final String tenantIdentifier, final Connection connection) throws SQLException {
        LOG.debug("Closing connection for {}", tenantIdentifier);
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(final Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(final Class<T> unwrapType) {
        return null;
    }
}
