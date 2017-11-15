package com.example.feature.user.repository;

import com.example.feature.base.repository.BaseRepository;
import com.example.feature.user.dto.UserDto;
import com.example.feature.user.entity.UserEntity;

public interface UserRepository extends BaseRepository<Long, UserEntity, UserDto> {
}
