package com.example.feature.base.repository;

import com.example.feature.base.dto.BaseUnmodifiableDto;
import com.example.feature.base.entity.BaseEntity;
import com.example.feature.user.dto.UserDto;
import com.example.feature.user.dto.UserDtoFactory;
import com.example.feature.user.entity.QUserEntity;
import com.example.feature.user.entity.UserEntity;
import com.example.feature.user.repository.UserRepository;
import com.google.common.collect.Ordering;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class QueryTest {

    // Use the UserEntity-table as the test data
    private static final QUserEntity USER = QUserEntity.userEntity;
    private static final UserDtoFactory USER_FACTORY = new UserDtoFactory();

    // Total number of elements in DB
    private static final Long COUNT = 7L;

    // Page request
    private static final int PAGE_SIZE = 3;
    private static final Pageable PAGE =
            new PageRequest(0, PAGE_SIZE);

    // Custom limit
    private static final Long LIMIT = 2L;

    // Custom ordering
    private static final OrderSpecifier<String> ORDER =
            USER.username.asc();

    // Custom where condition
    private static final Predicate PRED =
            Expressions.anyOf(
                    USER.username.likeIgnoreCase("B"),
                    USER.username.likeIgnoreCase("D"),
                    USER.username.likeIgnoreCase("E")
            );
    private static final Long MATCH_TO_PRED = 3L;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        userRepository.save(UserEntity.create("g"));
        userRepository.save(UserEntity.create("f"));
        userRepository.save(UserEntity.create("e"));
        userRepository.save(UserEntity.create("d"));
        userRepository.save(UserEntity.create("c"));
        userRepository.save(UserEntity.create("b"));
        userRepository.save(UserEntity.create("a"));
    }

    private static boolean areEntitiesOrderedById(final Iterable<UserEntity> users) {
        final List<Long> ids = StreamSupport
                .stream(users.spliterator(), false)
                .map(BaseEntity::getId)
                .collect(toList());
        return Ordering.natural().isOrdered(ids);
    }

    private static boolean areEntitiesOrderedByName(final Iterable<UserEntity> users) {
        final List<String> names = StreamSupport
                .stream(users.spliterator(), false)
                .map(UserEntity::getUsername)
                .collect(toList());
        return Ordering.natural().isOrdered(names);
    }

    private static boolean areDtosOrderedById(final Iterable<UserDto> users) {
        final List<Long> ids = StreamSupport
                .stream(users.spliterator(), false)
                .map(BaseUnmodifiableDto::getId)
                .collect(toList());
        return Ordering.natural().isOrdered(ids);
    }

    private static boolean areDtosOrderedByName(final Iterable<UserDto> users) {
        final List<String> names = StreamSupport
                .stream(users.spliterator(), false)
                .map(UserDto::getUsername)
                .collect(toList());
        return Ordering.natural().isOrdered(names);
    }

    @Test
    public void t1_no_filter_by_id_no_limit_as_entity_list() {
        final List<UserEntity> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderById()
                        .noLimit()
                        .entity()
                        .list();

        assertThat(users, hasSize(COUNT.intValue()));
        assertThat(areEntitiesOrderedById(users), is(true));
    }

    @Test
    public void t2_no_filter_by_id_no_limit_as_entity_page() {
        final Page<UserEntity> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderById()
                        .noLimit()
                        .entity()
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(COUNT));
        assertThat(users.getContent(), hasSize(PAGE_SIZE));
        assertThat(areEntitiesOrderedById(users), is(true));
    }

    @Test
    public void t3_no_filter_by_id_no_limit_as_dto_list() {
        final List<UserDto> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderById()
                        .noLimit()
                        .dto(USER_FACTORY)
                        .list();

        assertThat(users, hasSize(COUNT.intValue()));
        assertThat(areDtosOrderedById(users), is(true));
    }

    @Test
    public void t4_no_filter_by_id_no_limit_as_dto_page() {
        final Page<UserDto> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderById()
                        .noLimit()
                        .dto(USER_FACTORY)
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(COUNT));
        assertThat(users.getContent(), hasSize(PAGE_SIZE));
        assertThat(areDtosOrderedById(users), is(true));
    }

    @Test
    public void t5_no_filter_by_id_limit_as_entity_list() {
        final List<UserEntity> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderById()
                        .limit(LIMIT)
                        .entity()
                        .list();

        assertThat(users, hasSize(LIMIT.intValue()));
        assertThat(areEntitiesOrderedById(users), is(true));
    }

    @Test
    public void t6_no_filter_by_id_limit_as_entity_page() {
        final Page<UserEntity> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderById()
                        .limit(LIMIT)
                        .entity()
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(COUNT));
        assertThat(users.getContent(), hasSize(LIMIT.intValue()));
        assertThat(areEntitiesOrderedById(users), is(true));
    }

    @Test
    public void t7_no_filter_by_id_limit_as_dto_list() {
        final List<UserDto> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderById()
                        .limit(LIMIT)
                        .dto(USER_FACTORY)
                        .list();

        assertThat(users, hasSize(LIMIT.intValue()));
        assertThat(areDtosOrderedById(users), is(true));
    }

    @Test
    public void t8_no_filter_by_id_limit_as_dto_page() {
        final Page<UserDto> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderById()
                        .limit(LIMIT)
                        .dto(USER_FACTORY)
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(COUNT));
        assertThat(users.getContent(), hasSize(LIMIT.intValue()));
        assertThat(areDtosOrderedById(users), is(true));
    }

    @Test
    public void t9_no_filter_by_username_no_limit_as_entity_list() {
        final List<UserEntity> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderBy(ORDER)
                        .noLimit()
                        .entity()
                        .list();

        assertThat(users, hasSize(COUNT.intValue()));
        assertThat(areEntitiesOrderedByName(users), is(true));
    }

    @Test
    public void t10_no_filter_by_username_no_limit_as_entity_page() {
        final Page<UserEntity> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderBy(ORDER)
                        .noLimit()
                        .entity()
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(COUNT));
        assertThat(users.getContent(), hasSize(PAGE_SIZE));
        assertThat(areEntitiesOrderedByName(users), is(true));
    }

    @Test
    public void t11_no_filter_by_username_no_limit_as_dto_list() {
        final List<UserDto> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderBy(ORDER)
                        .noLimit()
                        .dto(USER_FACTORY)
                        .list();

        assertThat(users, hasSize(COUNT.intValue()));
        assertThat(areDtosOrderedByName(users), is(true));
    }

    @Test
    public void t12_no_filter_by_username_no_limit_as_dto_page() {
        final Page<UserDto> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderBy(ORDER)
                        .noLimit()
                        .dto(USER_FACTORY)
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(COUNT));
        assertThat(areDtosOrderedByName(users), is(true));
    }

    @Test
    public void t13_no_filter_by_username_limit_as_entity_list() {
        final List<UserEntity> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderBy(ORDER)
                        .limit(LIMIT)
                        .entity()
                        .list();

        assertThat(users, hasSize(LIMIT.intValue()));
        assertThat(areEntitiesOrderedByName(users), is(true));
    }

    @Test
    public void t14_no_filter_by_username_limit_as_entity_page() {
        final Page<UserEntity> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderBy(ORDER)
                        .limit(LIMIT)
                        .entity()
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(COUNT));
        assertThat(areEntitiesOrderedByName(users), is(true));
    }

    @Test
    public void t15_no_filter_by_username_limit_as_dto_list() {
        final List<UserDto> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderBy(ORDER)
                        .limit(LIMIT)
                        .dto(USER_FACTORY)
                        .list();

        assertThat(users, hasSize(LIMIT.intValue()));
        assertThat(areDtosOrderedByName(users), is(true));
    }

    @Test
    public void t16_no_filter_by_username_limit_as_dto_page() {
        final Page<UserDto> users =
                Query.of(userRepository)
                        .noFilter()
                        .orderBy(ORDER)
                        .limit(LIMIT)
                        .dto(USER_FACTORY)
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(COUNT));
        assertThat(users.getContent(), hasSize(LIMIT.intValue()));
        assertThat(areDtosOrderedByName(users), is(true));
    }

    @Test
    public void t17_username_filter_by_id_no_limit_as_entity_list() {
        final List<UserEntity> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderById()
                        .noLimit()
                        .entity()
                        .list();

        assertThat(users, hasSize(MATCH_TO_PRED.intValue()));
        assertThat(areEntitiesOrderedById(users), is(true));
    }

    @Test
    public void t18_username_filter_by_id_no_limit_as_entity_page() {
        final Page<UserEntity> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderById()
                        .noLimit()
                        .entity()
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(MATCH_TO_PRED));
        assertThat(users.getContent(), hasSize(MATCH_TO_PRED.intValue()));
        assertThat(areEntitiesOrderedById(users), is(true));
    }

    @Test
    public void t19_username_filter_by_id_no_limit_as_dto_list() {
        final List<UserDto> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderById()
                        .noLimit()
                        .dto(USER_FACTORY)
                        .list();

        assertThat(users, hasSize(MATCH_TO_PRED.intValue()));
        assertThat(areDtosOrderedById(users), is(true));
    }

    @Test
    public void t20_username_filter_by_id_no_limit_as_dto_page() {
        final Page<UserDto> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderById()
                        .noLimit()
                        .dto(USER_FACTORY)
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(MATCH_TO_PRED));
        assertThat(users.getContent(), hasSize(MATCH_TO_PRED.intValue()));
        assertThat(areDtosOrderedById(users), is(true));
    }

    @Test
    public void t21_username_filter_by_id_limit_as_entity_list() {
        final List<UserEntity> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderById()
                        .limit(LIMIT)
                        .entity()
                        .list();

        assertThat(users, hasSize(LIMIT.intValue()));
        assertThat(areEntitiesOrderedById(users), is(true));
    }

    @Test
    public void t22_username_filter_by_id_limit_as_entity_page() {
        final Page<UserEntity> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderById()
                        .limit(LIMIT)
                        .entity()
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(MATCH_TO_PRED));
        assertThat(users.getContent(), hasSize(LIMIT.intValue()));
        assertThat(areEntitiesOrderedById(users), is(true));
    }

    @Test
    public void t23_username_filter_by_id_limit_as_dto_list() {
        final List<UserDto> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderById()
                        .limit(LIMIT)
                        .dto(USER_FACTORY)
                        .list();

        assertThat(users, hasSize(LIMIT.intValue()));
        assertThat(areDtosOrderedById(users), is(true));
    }

    @Test
    public void t24_username_filter_by_id_limit_as_dto_page() {
        final Page<UserDto> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderById()
                        .limit(LIMIT)
                        .dto(USER_FACTORY)
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(MATCH_TO_PRED));
        assertThat(users.getContent(), hasSize(LIMIT.intValue()));
        assertThat(areDtosOrderedById(users), is(true));
    }

    @Test
    public void t25_username_filter_by_username_no_limit_as_entity_list() {
        final List<UserEntity> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderBy(ORDER)
                        .noLimit()
                        .entity()
                        .list();

        assertThat(users, hasSize(MATCH_TO_PRED.intValue()));
        assertThat(areEntitiesOrderedByName(users), is(true));
    }

    @Test
    public void t26_username_filter_by_username_no_limit_as_entity_page() {
        final Page<UserEntity> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderBy(ORDER)
                        .noLimit()
                        .entity()
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(MATCH_TO_PRED));
        assertThat(users.getContent(), hasSize(MATCH_TO_PRED.intValue()));
        assertThat(areEntitiesOrderedByName(users), is(true));
    }

    @Test
    public void t27_username_filter_by_username_no_limit_as_dto_list() {
        final List<UserDto> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderBy(ORDER)
                        .noLimit()
                        .dto(USER_FACTORY)
                        .list();

        assertThat(users, hasSize(MATCH_TO_PRED.intValue()));
        assertThat(areDtosOrderedByName(users), is(true));
    }

    @Test
    public void t28_username_filter_by_username_no_limit_as_dto_page() {
        final Page<UserDto> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderBy(ORDER)
                        .noLimit()
                        .dto(USER_FACTORY)
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(MATCH_TO_PRED));
        assertThat(users.getContent(), hasSize(MATCH_TO_PRED.intValue()));
        assertThat(areDtosOrderedByName(users), is(true));
    }

    @Test
    public void t29_username_filter_by_username_limit_as_entity_list() {
        final List<UserEntity> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderBy(ORDER)
                        .limit(LIMIT)
                        .entity()
                        .list();

        assertThat(users, hasSize(LIMIT.intValue()));
        assertThat(areEntitiesOrderedByName(users), is(true));
    }

    @Test
    public void t30_username_filter_by_username_limit_as_entity_page() {
        final Page<UserEntity> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderBy(ORDER)
                        .limit(LIMIT)
                        .entity()
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(MATCH_TO_PRED));
        assertThat(users.getContent(), hasSize(LIMIT.intValue()));
        assertThat(areEntitiesOrderedByName(users), is(true));
    }

    @Test
    public void t31_username_filter_by_username_limit_as_dto_list() {
        final List<UserDto> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderBy(ORDER)
                        .limit(LIMIT)
                        .dto(USER_FACTORY)
                        .list();

        assertThat(users, hasSize(LIMIT.intValue()));
        assertThat(areDtosOrderedByName(users), is(true));
    }

    @Test
    public void t32_username_filter_by_username_limit_as_dto_page() {
        final Page<UserDto> users =
                Query.of(userRepository)
                        .filter(PRED)
                        .orderBy(ORDER)
                        .limit(LIMIT)
                        .dto(USER_FACTORY)
                        .page(PAGE);

        assertThat(users.getTotalElements(), is(MATCH_TO_PRED));
        assertThat(users.getContent(), hasSize(LIMIT.intValue()));
        assertThat(areDtosOrderedByName(users), is(true));
    }

}
