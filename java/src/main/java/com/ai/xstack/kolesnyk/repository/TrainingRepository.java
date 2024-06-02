package com.ai.xstack.kolesnyk.repository;

import com.ai.xstack.kolesnyk.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainingRepository extends JpaRepository<TrainingEntity, Long> {
    List<TrainingEntity> findByTraineeUserUsername(String traineeUsername);

    List<TrainingEntity> findByTrainerUserUsername(String trainerUsername);

    @Query(value = "select sum(training_duration) from trainings where trainee_id = ?", nativeQuery = true)
    int calculateTotalDurationForTraineeWithId(Long traineeId);

    @Query(value = "select count(visited) from trainings where trainee_id = ?", nativeQuery = true)
    int calculateTotalCount(Long traineeId);

    @Query(value = "select count(visited) from trainings where trainee_id = ? and visited", nativeQuery = true)
    int calculateVisitedCount(Long traineeId);

}
