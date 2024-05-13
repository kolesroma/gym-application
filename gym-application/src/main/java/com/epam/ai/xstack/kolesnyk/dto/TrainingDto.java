package com.epam.ai.xstack.kolesnyk.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingDto {
    private String traineeName;

    private String trainerName;

    private String trainingName;

    private LocalDate trainingDate;

    private Integer trainingDuration;

    private String trainingType;
}
