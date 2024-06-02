package com.ai.xstack.kolesnyk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingVelocityReportDto {

    private LocalDate trainingDate;

    private Integer trainingCount;

    private Integer totalDuration;

    private Double velocity;

}
