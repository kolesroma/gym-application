package com.ai.xstack.kolesnyk.service;

import com.ai.xstack.kolesnyk.dto.TraineeDto;

import java.util.List;
import java.util.Map;

public interface TraineeService {
    List<TraineeDto> getAll();

    TraineeDto getByUsername(String username);

    Map<String, String> create(TraineeDto traineeDto);

    TraineeDto update(TraineeDto traineeDto, String username);

    void createEmptyTrainings(List<String> trainerUsernameList);
}

