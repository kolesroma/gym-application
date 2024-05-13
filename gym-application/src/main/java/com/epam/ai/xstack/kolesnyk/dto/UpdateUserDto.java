package com.epam.ai.xstack.kolesnyk.dto;

import lombok.Data;

@Data
public class UpdateUserDto {
    private TrainerDto trainer;

    private TraineeDto trainee;
}
