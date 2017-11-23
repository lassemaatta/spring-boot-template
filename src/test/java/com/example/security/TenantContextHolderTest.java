package com.example.security;

import com.example.feature.base.MultiTenantMigration;
import com.example.feature.base.tenant.Schema;
import com.example.feature.base.tenant.Tenant;
import com.example.feature.base.tenant.discovery.SchemaDiscoverer;
import com.example.feature.user.entity.UserEntity;
import com.example.feature.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nonnull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TenantContextHolderTest {

    private static final Logger LOG = LoggerFactory.getLogger(TenantContextHolderTest.class);

    @Mock
    private RequestAttributes attributes;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MultiTenantMigration tenantMigration;

    @Autowired
    private SchemaDiscoverer schemaDiscoverer;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    private static void setTenant(@Nonnull final Tenant tenant) {
        LOG.debug("Overriding attributes");

        final MockHttpServletRequest request = new MockHttpServletRequest();
        final RequestAttributes attrs = new ServletRequestAttributes(request);
        attrs.setAttribute(TenantFilter.TENANT_ATTRIBUTE, tenant, RequestAttributes.SCOPE_REQUEST);
        RequestContextHolder.setRequestAttributes(attrs);
    }

    @Test
    public void test_creating_a_tenant() {
        final Tenant tenant = Tenant.of("new_tenant");
        final Schema schema = Schema.of(tenant);


        assertThat(schemaDiscoverer.getSchemas(), not(hasItem(schema)));

        tenantMigration.createAndMigrate(tenant);

        assertThat(schemaDiscoverer.getSchemas(), hasItem(schema));
    }

    @Test
    public void test_foobar() {
        setTenant(Tenant.of("default"));

        userRepository.save(UserEntity.create("user a"));

        assertThat(userRepository.count(), is(1L));
    }
}
