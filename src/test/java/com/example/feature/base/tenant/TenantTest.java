package com.example.feature.base.tenant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
public class TenantTest {


    @Test(expected = NullPointerException.class)
    public void test_null() {
        Tenant.of(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_must_not_start_with_number() {
        Tenant.of("2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_too_short() {
        Tenant.of("a");
    }

    @Test
    public void test_short() {
        assertThat(Tenant.of("fo"), is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_too_long() {
        Tenant.of("foobarfoobarfoobarfoob");
    }

    @Test
    public void test_long() {
        assertThat(Tenant.of("foobarfoobarfoobarfoo"), is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_illegal_whitespace() {
        Tenant.of("foo bar");
    }
}
