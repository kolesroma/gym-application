package com.ai.xstack.kolesnyk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingVisitedReportDto {

    private String traineeUsername;

    private Integer totalDuration;

    private Integer visitedCount;

    private Integer totalCount;

}
