package com.example.feature.base.repository;

import com.example.feature.base.dto.BaseUnmodifiableDto;
import com.example.feature.base.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<ID extends Serializable, E extends BaseEntity, DTO extends BaseUnmodifiableDto>
        extends CrudRepository<E, ID>, QueryRepository<ID, E, DTO> {
}
