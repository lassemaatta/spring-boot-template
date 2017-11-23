package com.example.configuration;

import com.example.security.TenantFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.servlet.Filter;
import java.util.Objects;

@Configuration
public class FilterConfiguration {

    @Nonnull
    private final AutowireCapableBeanFactory beanFactory;

    @Autowired
    public FilterConfiguration(@Nonnull final AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = Objects.requireNonNull(beanFactory);
    }

    @Bean
    public FilterRegistrationBean registrationBean() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();

        final Filter tenantFilter = new TenantFilter();
        beanFactory.autowireBean(tenantFilter);
        registration.setFilter(tenantFilter);

        registration.addUrlPatterns("/*");
        return registration;
    }

}
