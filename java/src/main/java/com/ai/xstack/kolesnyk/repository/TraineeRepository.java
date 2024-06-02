package com.ai.xstack.kolesnyk.repository;

import com.ai.xstack.kolesnyk.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<TraineeEntity, Long> {
    Optional<TraineeEntity> findByUserUsername(String username);
}