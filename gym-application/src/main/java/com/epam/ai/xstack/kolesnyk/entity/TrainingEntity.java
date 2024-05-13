package com.epam.ai.xstack.kolesnyk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainings")
public class TrainingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private TraineeEntity trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainer;

    private String trainingName;

    private LocalDate trainingDate;

    private Integer trainingDuration;

    @ManyToOne(cascade={CascadeType.PERSIST})
    @JoinColumn(name = "training_type_id")
    private TrainingTypeEntity trainingType;
}
