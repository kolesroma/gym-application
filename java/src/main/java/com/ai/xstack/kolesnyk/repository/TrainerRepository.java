package com.ai.xstack.kolesnyk.repository;

import com.ai.xstack.kolesnyk.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<TrainerEntity, Long> {
    Optional<TrainerEntity> findByUserUsername(String username);
}