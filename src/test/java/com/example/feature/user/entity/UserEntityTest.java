package com.example.feature.user.entity;

import com.example.feature.user.repository.UserRepository;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserEntityTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void initial_db_is_empty() {
        assertThat(userRepository.count(), is(0L));
    }

    @Test
    public void storing_valid_users() {
        userRepository.save(
                ImmutableList.of(
                        UserEntity.create("user 1"),
                        UserEntity.create("user 2")
                )
        );
        assertThat(userRepository.count(), is(2L));
    }

    @Test(expected = NullPointerException.class)
    public void null_username_throws_exception() {
        UserEntity.create(null);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void duplicate_username_throws_exception() {
        userRepository.save(
                ImmutableList.of(
                        UserEntity.create("user 1"),
                        UserEntity.create("user 1")
                )
        );
    }
}
