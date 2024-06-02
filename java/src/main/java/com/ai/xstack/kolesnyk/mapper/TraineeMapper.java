package com.ai.xstack.kolesnyk.mapper;

import com.ai.xstack.kolesnyk.dto.TraineeDto;
import com.ai.xstack.kolesnyk.entity.TraineeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "user.isActive", target = "isActive")
    @Mapping(target = "trainers", ignore = true)
    @Mapping(source = "user.email", target = "email")
    TraineeDto toDto(TraineeEntity traineeEntity);

    @Mapping(source = "username", target = "user.username")
    @Mapping(source = "firstName", target = "user.firstName")
    @Mapping(source = "lastName", target = "user.lastName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "email", target = "user.email")
    TraineeEntity toEntity(TraineeDto traineeDto);
}
