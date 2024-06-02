package com.ai.xstack.kolesnyk.mapper;

import com.ai.xstack.kolesnyk.dto.TrainingDto;
import com.ai.xstack.kolesnyk.entity.TrainingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(source = "trainee.user.username", target = "traineeName")
    @Mapping(source = "trainer.user.username", target = "trainerName")
    @Mapping(source = "trainingName", target = "trainingName")
    @Mapping(source = "trainingDuration", target = "trainingDuration")
    @Mapping(source = "trainingType.trainingTypeName", target = "trainingType")
    TrainingDto toDto(TrainingEntity trainingEntity);

    @Mapping(source = "traineeName", target = "trainee.user.username")
    @Mapping(source = "trainerName", target = "trainer.user.username")
    @Mapping(source = "trainingName", target = "trainingName")
    @Mapping(source = "trainingDuration", target = "trainingDuration")
    @Mapping(source = "trainingType", target = "trainingType.trainingTypeName")
    @Mapping(source = "trainingDate", target = "trainingDate")
    TrainingEntity toEntity(TrainingDto trainingDto);
}
