package com.epam.ai.xstack.kolesnyk.service;

import com.epam.ai.xstack.kolesnyk.dto.TrainingDto;

import java.util.List;

public interface TrainingService {
    List<TrainingDto> getTrainingListForTrainer(String trainerUsername);

    List<TrainingDto> getTrainingListForTrainee(String traineeUsername);

    TrainingDto create(TrainingDto trainingDto);
}
