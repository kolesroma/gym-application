package com.ai.xstack.kolesnyk.service;

import com.ai.xstack.kolesnyk.dto.TrainingVelocityReportDto;
import com.ai.xstack.kolesnyk.dto.TrainingVisitedReportDto;

import java.util.ArrayList;

public interface ReportService {

    ArrayList<TrainingVisitedReportDto> generateTrainingVisitedReport(String trainerUsername);

    ArrayList<TrainingVelocityReportDto> generateTrainingVelocityReport(String trainerUsername);

}
