package com.ai.xstack.kolesnyk.service.impl;

import com.ai.xstack.kolesnyk.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Spy
    private TrainingRepository trainingRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void generateTrainingReport() {
        reportService.generateTrainingVisitedReport("cleaner_anatoliy");
        trainingRepository.findAll();
        trainingRepository.findAll();
    }

}