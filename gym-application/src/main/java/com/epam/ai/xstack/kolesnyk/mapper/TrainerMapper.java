package com.epam.ai.xstack.kolesnyk.mapper;

import com.epam.ai.xstack.kolesnyk.dto.TrainerDto;
import com.epam.ai.xstack.kolesnyk.entity.TrainerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "specialization", target = "specialization")
    @Mapping(source = "user.email", target = "email")
    TrainerDto toDto(TrainerEntity trainerEntity);

    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "specialization", target = "specialization")
    @Mapping(source = "email", target = "user.email")
    TrainerEntity toEntity(TrainerDto trainerDto);
}
