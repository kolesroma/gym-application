package com.epam.ai.xstack.kolesnyk.service.impl;

import com.epam.ai.xstack.kolesnyk.dto.TrainingDto;
import com.epam.ai.xstack.kolesnyk.entity.TraineeEntity;
import com.epam.ai.xstack.kolesnyk.entity.TrainerEntity;
import com.epam.ai.xstack.kolesnyk.entity.TrainingEntity;
import com.epam.ai.xstack.kolesnyk.exception.EntityNotFound;
import com.epam.ai.xstack.kolesnyk.mapper.TrainingMapper;
import com.epam.ai.xstack.kolesnyk.repository.TraineeRepository;
import com.epam.ai.xstack.kolesnyk.repository.TrainerRepository;
import com.epam.ai.xstack.kolesnyk.repository.TrainingRepository;
import com.epam.ai.xstack.kolesnyk.service.NotificationService;
import com.epam.ai.xstack.kolesnyk.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final NotificationService notificationService;

    @Override
    public List<TrainingDto> getTrainingListForTrainer(String trainerUsername) {
        return trainingRepository.findByTrainerUserUsername(trainerUsername)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @Override
    public List<TrainingDto> getTrainingListForTrainee(String traineeUsername) {
        return trainingRepository.findByTraineeUserUsername(traineeUsername)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @Override
    public TrainingDto create(TrainingDto trainingDto) {
        TrainerEntity trainer = trainerRepository.findByUserUsername(trainingDto.getTrainerName())
                .orElseThrow(() -> new EntityNotFound("no trainer with such username"));
        TraineeEntity trainee = traineeRepository.findByUserUsername(trainingDto.getTraineeName())
                .orElseThrow(() -> new EntityNotFound("no trainee with such username"));
        TrainingEntity trainingEntity = trainingMapper.toEntity(trainingDto);
        trainingEntity.setTrainer(trainer);
        trainingEntity.setTrainee(trainee);
        trainingRepository.save(trainingEntity);
        TrainingDto returnedDto = trainingMapper.toDto(trainingEntity);
        sendCreatedTrainingEmail(trainingDto, trainee);
        return returnedDto;
    }

    private void sendCreatedTrainingEmail(TrainingDto trainingDto, TraineeEntity trainee) {
        notificationService.sendMessage(trainee.getUser().getEmail(),
                "Created Trainings",
                "You created new training for date: " + trainingDto.getTrainingDate() +
                        ".\nWith name: " + trainingDto.getTrainingName() +
                        ".\nWith trainer: " + trainingDto.getTrainerName() +
                        ".\nWith type: " + trainingDto.getTrainingType() + "."
        );
    }

}
