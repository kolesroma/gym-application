package com.ai.xstack.kolesnyk.service;

import com.ai.xstack.kolesnyk.dto.TrainerDto;

import java.util.List;
import java.util.Map;

public interface TrainerService {
    TrainerDto getByUsername(String username);

    Map<String, String> create(TrainerDto trainerDto);

    TrainerDto update(TrainerDto trainerDto, String username);

    List<TrainerDto> getAllFreeTrainers(String usernameTrainee);
}
