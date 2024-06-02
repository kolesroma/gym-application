package com.ai.xstack.kolesnyk.controller;

import com.ai.xstack.kolesnyk.controller.utils.AccessUtil;
import com.ai.xstack.kolesnyk.dto.TrainingDto;
import com.ai.xstack.kolesnyk.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainings")
public class TrainingController {
    private final TrainingService trainingService;

    @GetMapping
    public List<TrainingDto> getAllTrainingsForUser(Authentication authentication) {
        if (AccessUtil.hasRole(authentication, "ROLE_TRAINEE")) {
         return trainingService.getTrainingListForTrainee(authentication.getName());
        }
        if (AccessUtil.hasRole(authentication, "ROLE_TRAINER")) {
            return trainingService.getTrainingListForTrainer(authentication.getName());
        }
        throw new AccessDeniedException("provided authentication neither has ROLE_TRAINEE nor ROLE_TRAINER");
    }

    @PostMapping
    public TrainingDto createTraining(@RequestBody TrainingDto trainingDto, Authentication authentication) {
        if (AccessUtil.hasRole(authentication, "ROLE_TRAINEE")) {
            trainingDto.setTraineeName(authentication.getName());
        } else if (AccessUtil.hasRole(authentication, "ROLE_TRAINER")) {
            trainingDto.setTrainerName(authentication.getName());
        }
        return trainingService.create(trainingDto);
    }

}
