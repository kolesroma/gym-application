package com.epam.ai.xstack.kolesnyk.service;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EntityService<Dto> {

    Iterable<Dto> findAll(Pageable pageable);

    Optional<Dto> findById(Long entityId);

    Dto create(Dto dto);

    Dto update(Dto dto);

    void deleteById(Long entityId);

}
