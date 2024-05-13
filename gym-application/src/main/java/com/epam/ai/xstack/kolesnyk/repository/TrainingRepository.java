package com.epam.ai.xstack.kolesnyk.repository;

import com.epam.ai.xstack.kolesnyk.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<TrainingEntity, Long> {
    List<TrainingEntity> findByTraineeUserUsername(String traineeUsername);

    List<TrainingEntity> findByTrainerUserUsername(String trainerUsername);
}
