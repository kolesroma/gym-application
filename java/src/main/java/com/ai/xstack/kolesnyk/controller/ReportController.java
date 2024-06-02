package com.ai.xstack.kolesnyk.controller;

import com.ai.xstack.kolesnyk.dto.TrainingVisitedReportDto;
import com.ai.xstack.kolesnyk.dto.TrainingVelocityReportDto;
import com.ai.xstack.kolesnyk.entity.UserEntity;
import com.ai.xstack.kolesnyk.service.ReportService;
import com.ai.xstack.kolesnyk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    @GetMapping("/visited")
    public List<TrainingVisitedReportDto> generateTrainingVisitedReport() {
        UserEntity user = userService.getUserFromCurrentSecurityContext();
        return reportService.generateTrainingVisitedReport(user.getUsername());
    }

    @GetMapping("/velocity")
    public List<TrainingVelocityReportDto> generateTrainingVelocityReport() {
        UserEntity user = userService.getUserFromCurrentSecurityContext();
        return reportService.generateTrainingVelocityReport(user.getUsername());
    }

}
