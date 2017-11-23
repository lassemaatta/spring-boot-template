package com.example.feature.base.tenant.connector;

import com.example.feature.base.condition.H2Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

@Conditional(H2Condition.class)
@Component
public class H2SqlSchemaConnector extends SchemaConnector {

    private static final String SCHEMA_STMT = "SET SCHEMA ";

    @Autowired
    public H2SqlSchemaConnector(@Nonnull final DataSource dataSource) {
        super(dataSource, SCHEMA_STMT);
    }
}
