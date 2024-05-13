package com.epam.ai.xstack.kolesnyk.repository;

import com.epam.ai.xstack.kolesnyk.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
}
