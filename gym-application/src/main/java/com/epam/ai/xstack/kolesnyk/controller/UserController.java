package com.epam.ai.xstack.kolesnyk.controller;

import com.epam.ai.xstack.kolesnyk.controller.utils.AccessUtil;
import com.epam.ai.xstack.kolesnyk.dto.TraineeDto;
import com.epam.ai.xstack.kolesnyk.dto.TrainerDto;
import com.epam.ai.xstack.kolesnyk.dto.UpdateUserDto;
import com.epam.ai.xstack.kolesnyk.service.TraineeService;
import com.epam.ai.xstack.kolesnyk.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @GetMapping("/me")
    public Object getUser(Authentication authentication) {
        if (AccessUtil.hasRole(authentication, "ROLE_TRAINEE")) {
            return traineeService.getByUsername(authentication.getName());
        }
        if (AccessUtil.hasRole(authentication, "ROLE_TRAINER")) {
            return trainerService.getByUsername(authentication.getName());
        }
        throw new AccessDeniedException("provided authentication neither has ROLE_TRAINEE nor ROLE_TRAINER");
    }

    @GetMapping("/me/role")
    public Object getUserRole(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow();
    }

    @PutMapping
    public Object updateUser(@RequestBody UpdateUserDto updateUserDto,
                             Authentication authentication) {
        if (AccessUtil.hasRole(authentication, "ROLE_TRAINEE")) {
            return traineeService.update(updateUserDto.getTrainee(), authentication.getName());
        }
        if (AccessUtil.hasRole(authentication, "ROLE_TRAINER")) {
            return trainerService.update(updateUserDto.getTrainer(), authentication.getName());
        }
        throw new AccessDeniedException("provided authentication neither has ROLE_TRAINEE nor ROLE_TRAINER");
    }
}
