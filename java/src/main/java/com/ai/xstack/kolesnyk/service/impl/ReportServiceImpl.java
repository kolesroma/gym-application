package com.ai.xstack.kolesnyk.service.impl;

import com.ai.xstack.kolesnyk.dto.TrainingVelocityReportDto;
import com.ai.xstack.kolesnyk.dto.TrainingVisitedReportDto;
import com.ai.xstack.kolesnyk.entity.TrainingEntity;
import com.ai.xstack.kolesnyk.repository.TrainingRepository;
import com.ai.xstack.kolesnyk.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TrainingRepository trainingRepository;

    @Override
    @Transactional
    public ArrayList<TrainingVisitedReportDto> generateTrainingVisitedReport(String trainerUsername) {
        List<TrainingEntity> trainings = trainingRepository.findByTrainerUserUsername(trainerUsername);
        ArrayList<TrainingVisitedReportDto> accumulator = new ArrayList<>();

        trainings.forEach(training -> {
            String traineeUsername = training.getTrainee().getUser().getUsername();
            if (!accumulatorContainsUsername(accumulator, traineeUsername)) {
                Long traineeId = training.getTrainee().getId();
                int totalDuration = trainingRepository.calculateTotalDurationForTraineeWithId(traineeId);
                int visitedCount = trainingRepository.calculateVisitedCount(traineeId);
                int totalCount = trainingRepository.calculateTotalCount(traineeId);
                TrainingVisitedReportDto visitedReportDto = new TrainingVisitedReportDto(traineeUsername, totalDuration, visitedCount, totalCount);
                accumulator.add(visitedReportDto);
            }
        });

        return accumulator;
    }

    @Override
    @Transactional
    public ArrayList<TrainingVelocityReportDto> generateTrainingVelocityReport(String trainerUsername) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        List<TrainingEntity> trainings = trainingRepository.findByTrainerUserUsername(trainerUsername)
                .stream()
                .filter(training -> training.getTrainingDate().isAfter(start))
                .toList();
        ArrayList<TrainingVelocityReportDto> accumulator = new ArrayList<>();

        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            LocalDate dateCopy = date;
            List<TrainingEntity> trainingEntities = trainings.stream().filter(x -> x.getTrainingDate().equals(dateCopy)).toList();
            int totalDuration = trainingEntities.stream().mapToInt(TrainingEntity::getTrainingDuration).sum();
            TrainingVelocityReportDto trainingVelocityReportDto = new TrainingVelocityReportDto(dateCopy, trainingEntities.size(), totalDuration, totalDuration / 480d);
            accumulator.add(trainingVelocityReportDto);
        }

        return accumulator;
    }

    private static boolean accumulatorContainsUsername(ArrayList<TrainingVisitedReportDto> accumulator,
                                                       String traineeUsername) {
        return accumulator.stream()
                .map(TrainingVisitedReportDto::getTraineeUsername)
                .anyMatch(x -> x.equals(traineeUsername));
    }

    @Bean
    CommandLineRunner f(ReportService reportService) {
        return x -> reportService.generateTrainingVelocityReport("cleaner_anatoliy");
    }

}
