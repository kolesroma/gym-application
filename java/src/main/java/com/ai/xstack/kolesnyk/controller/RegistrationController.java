package com.ai.xstack.kolesnyk.controller;

import com.ai.xstack.kolesnyk.dto.TraineeDto;
import com.ai.xstack.kolesnyk.dto.TrainerDto;
import com.ai.xstack.kolesnyk.service.TraineeService;
import com.ai.xstack.kolesnyk.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @PostMapping("/trainee")
    public Map<String, String> createTrainee(@Validated @RequestBody TraineeDto trainee) {
        return traineeService.create(trainee);
    }

    @PostMapping("/trainer")
    public Map<String, String> createTrainer(@Validated @RequestBody TrainerDto trainer) {
        return trainerService.create(trainer);
    }

}
