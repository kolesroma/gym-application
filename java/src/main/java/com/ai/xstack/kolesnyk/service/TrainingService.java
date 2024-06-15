package com.ai.xstack.kolesnyk.service;

import com.ai.xstack.kolesnyk.dto.TrainingDto;

import java.util.List;

public interface TrainingService {
    List<TrainingDto> getTrainingListForTrainer(String trainerUsername);

    List<TrainingDto> getTrainingListForTrainee(String traineeUsername);

    TrainingDto create(TrainingDto trainingDto);

    void toggleVisitedStatus(Long trainingId);
}
