package com.epam.ai.xstack.kolesnyk.service.impl;

import com.epam.ai.xstack.kolesnyk.dto.TraineeDto;
import com.epam.ai.xstack.kolesnyk.dto.TrainerDto;
import com.epam.ai.xstack.kolesnyk.dto.TrainingDto;
import com.epam.ai.xstack.kolesnyk.entity.AuthorityEntity;
import com.epam.ai.xstack.kolesnyk.entity.TraineeEntity;
import com.epam.ai.xstack.kolesnyk.entity.TrainerEntity;
import com.epam.ai.xstack.kolesnyk.entity.TrainingEntity;
import com.epam.ai.xstack.kolesnyk.entity.UserEntity;
import com.epam.ai.xstack.kolesnyk.entity.enums.AuthorityRole;
import com.epam.ai.xstack.kolesnyk.exception.EntityNotFound;
import com.epam.ai.xstack.kolesnyk.mapper.TraineeMapper;
import com.epam.ai.xstack.kolesnyk.mapper.TrainerMapper;
import com.epam.ai.xstack.kolesnyk.repository.AuthorityRepository;
import com.epam.ai.xstack.kolesnyk.repository.TraineeRepository;
import com.epam.ai.xstack.kolesnyk.repository.TrainerRepository;
import com.epam.ai.xstack.kolesnyk.repository.TrainingRepository;
import com.epam.ai.xstack.kolesnyk.repository.UserRepository;
import com.epam.ai.xstack.kolesnyk.service.LoginPasswordGenerator;
import com.epam.ai.xstack.kolesnyk.service.TraineeService;
import com.epam.ai.xstack.kolesnyk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TraineeServiceImpl implements TraineeService {
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final LoginPasswordGenerator loginPasswordGenerator;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    @Override
    public List<TraineeDto> getAll() {
        return traineeRepository.findAll()
                .stream()
                .map(traineeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TraineeDto getByUsername(String username) {
        TraineeEntity trainee = traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new EntityNotFound("no trainee with such username"));

        List<TrainerDto> trainersOfTrainee = trainingRepository.findByTraineeUserUsername(username)
                .stream()
                .map(TrainingEntity::getTrainer)
                .map(trainerMapper::toDto)
                .toList();

        TraineeDto traineeDto = traineeMapper.toDto(trainee);
        traineeDto.setTrainers(trainersOfTrainee);

        return traineeDto;
    }

    @Override
    @Transactional
    public Map<String, String> create(TraineeDto trainee) {
        String username = loginPasswordGenerator.generateLogin(trainee.getFirstName(), trainee.getLastName());
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(String.format("User with username %s already registered", username));
        }
        String passwordRaw = loginPasswordGenerator.generatePassword();

        traineeRepository.save(getTraineeEntity(trainee, username, passwordRaw));
        authorityRepository.save(getTraineeAuthority(username));

        log.info("Registered new trainee: {}", trainee);
        return Map.of("username", username, "password", passwordRaw);
    }

    @Override
    public TraineeDto update(TraineeDto traineeDto, String username) {
        if (traineeDto == null) {
            throw new IllegalArgumentException("provided trainee is null");
        }
        TraineeEntity trainee = traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new EntityNotFound("no trainee with such username"));
        prepareTrainee(trainee, traineeDto);
        traineeRepository.save(trainee);
        log.info("Updated trainee profile: {}", trainee);
        return traineeMapper.toDto(trainee);
    }

    @Override
    public void createEmptyTrainings(List<String> trainerUsernameList) {
        TraineeEntity trainee = getTraineeFromSecurityContext();
        List<TrainingEntity> trainingEntities = trainerUsernameList.stream()
                .map(trainerUsername -> prepareEmptyTraining(trainerUsername, trainee))
                .toList();
        trainingRepository.saveAll(trainingEntities);
    }

    private TraineeEntity getTraineeFromSecurityContext() {
        String usernameTrainee = userService.getUserFromCurrentSecurityContext().getUsername();
        return traineeRepository.findByUserUsername(usernameTrainee)
                .orElseThrow(() -> new EntityNotFound("no trainee with such username"));
    }

    private TrainingEntity prepareEmptyTraining(String trainerUsername, TraineeEntity trainee) {
        TrainerEntity trainer = trainerRepository.findByUserUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFound("no trainer with such username"));
        TrainingEntity training = new TrainingEntity();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        return training;
    }

    private static void prepareTrainee(TraineeEntity entity, TraineeDto dto) {
        entity.setAddress(dto.getAddress());
        entity.setDateOfBirth(dto.getDateOfBirth());
        UserEntity user = entity.getUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
    }

    private static AuthorityEntity getTraineeAuthority(String login) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setUsername(login);
        authorityEntity.setAuthority(AuthorityRole.ROLE_TRAINEE);
        return authorityEntity;
    }

    private TraineeEntity getTraineeEntity(TraineeDto traineeDto, String login, String passwordRaw) {
        TraineeEntity traineeEntity = traineeMapper.toEntity(traineeDto);
        UserEntity userEntity = traineeEntity.getUser();
        userEntity.setUsername(login);
        userEntity.setPassword(passwordEncoder.encode(passwordRaw));
        userEntity.setEnabled(true);
        userEntity.setIsActive(true);
        return traineeEntity;
    }

}
