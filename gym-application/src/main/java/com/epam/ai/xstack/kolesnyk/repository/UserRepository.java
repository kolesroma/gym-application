package com.epam.ai.xstack.kolesnyk.repository;

import com.epam.ai.xstack.kolesnyk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);
}