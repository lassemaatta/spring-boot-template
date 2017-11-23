package com.example.feature.base.tenant.discovery;

import com.example.feature.base.condition.H2Condition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

@Conditional(H2Condition.class)
@Component
public class H2SchemaDiscoverer extends SchemaDiscoverer {

    private static final String READ_SCHEMAS =
            "SHOW SCHEMAS";
    private static final String CREATE_SCHEMA =
            "CREATE SCHEMA IF NOT EXISTS ";

    public H2SchemaDiscoverer(@Nonnull final DataSource dataSource) {
        super(dataSource, READ_SCHEMAS, CREATE_SCHEMA);
    }
}
