package com.ai.xstack.kolesnyk.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TraineeDto {
    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate dateOfBirth;

    private String address;

    private Boolean isActive;

    private List<TrainerDto> trainers;
}
