package com.ai.xstack.kolesnyk.controller;

import com.ai.xstack.kolesnyk.dto.TraineeDto;
import com.ai.xstack.kolesnyk.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trainees")
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping
    public List<TraineeDto> getAllTrainees() {
        return traineeService.getAll();
    }

    @GetMapping("/{username}")
    public TraineeDto getTraineeProfile(@PathVariable String username) {
        return traineeService.getByUsername(username);
    }

    @PostMapping("/trainers")
    public void createEmptyTrainings(@RequestBody List<String> trainerUsernameList) {
        traineeService.createEmptyTrainings(trainerUsernameList);
    }

}
