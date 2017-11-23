package com.example.feature.base.tenant.discovery;

import com.example.feature.base.condition.PostgreSqlCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

@Conditional(PostgreSqlCondition.class)
@Component
public class PostgreSqlSchemaDiscoverer extends SchemaDiscoverer {

    private static final String READ_SCHEMAS =
            "SELECT schema_name FROM information_schema.schemata";
    private static final String CREATE_SCHEMA =
            "CREATE SCHEMA IF NOT EXISTS ";

    @Autowired
    public PostgreSqlSchemaDiscoverer(@Nonnull final DataSource dataSource) {
        super(dataSource, READ_SCHEMAS, CREATE_SCHEMA);
    }
}
