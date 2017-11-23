package com.example.feature.base.tenant.connector;

import com.example.feature.base.condition.PostgreSqlCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

@Conditional(PostgreSqlCondition.class)
@Component
public class PostgreSqlSchemaConnector extends SchemaConnector {

    private static final String SCHEMA_STMT = "SET search_path TO ";

    @Autowired
    public PostgreSqlSchemaConnector(@Nonnull final DataSource dataSource) {
        super(dataSource, SCHEMA_STMT);
    }
}
