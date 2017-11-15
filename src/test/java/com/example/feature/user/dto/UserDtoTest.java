package com.example.feature.user.dto;

import com.example.feature.user.entity.UserEntity;
import com.example.feature.user.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserDtoTest {

    @Autowired
    private UserRepository userRepository;

    @Test(expected = NullPointerException.class)
    public void test_null_entity() {
        UserDto.entity(null);
    }

    @Test(expected = NullPointerException.class)
    public void test_entity_without_id() {
        final UserEntity user = UserEntity.create("a user");
        UserDto.entity(user);
    }

    @Test
    public void test_persisted_entity() {
        final UserEntity user = userRepository.save(UserEntity.create("a user"));

        final UserDto dto = UserDto.entity(user);

        assertThat(dto.getUsername(), is("a user"));
        assertThat(dto.getId(), is(notNullValue()));
        assertThat(dto.getCreationTime(), is(notNullValue()));
        assertThat(dto.getModificationTime(), is(notNullValue()));
        assertThat(dto.getDeletionTime(), is(nullValue()));
    }
}
