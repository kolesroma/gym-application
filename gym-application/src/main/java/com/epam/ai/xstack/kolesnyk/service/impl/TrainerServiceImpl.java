package com.epam.ai.xstack.kolesnyk.service.impl;

import com.epam.ai.xstack.kolesnyk.dto.TrainerDto;
import com.epam.ai.xstack.kolesnyk.entity.AuthorityEntity;
import com.epam.ai.xstack.kolesnyk.entity.TrainerEntity;
import com.epam.ai.xstack.kolesnyk.entity.UserEntity;
import com.epam.ai.xstack.kolesnyk.entity.enums.AuthorityRole;
import com.epam.ai.xstack.kolesnyk.exception.EntityNotFound;
import com.epam.ai.xstack.kolesnyk.mapper.TrainerMapper;
import com.epam.ai.xstack.kolesnyk.repository.AuthorityRepository;
import com.epam.ai.xstack.kolesnyk.repository.TrainerRepository;
import com.epam.ai.xstack.kolesnyk.repository.UserRepository;
import com.epam.ai.xstack.kolesnyk.service.LoginPasswordGenerator;
import com.epam.ai.xstack.kolesnyk.service.TrainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class TrainerServiceImpl implements TrainerService {
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final LoginPasswordGenerator loginPasswordGenerator;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    @Override
    public TrainerDto getByUsername(String username) {
        TrainerEntity trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new EntityNotFound("no trainer with such username"));
        return trainerMapper.toDto(trainer);
    }

    @Override
    @Transactional
    public Map<String, String> create(TrainerDto trainer) {
        String username = loginPasswordGenerator.generateLogin(trainer.getFirstName(), trainer.getLastName());
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(String.format("User with username %s already registered", username));
        }
        String passwordRaw = loginPasswordGenerator.generatePassword();

        trainerRepository.save(getTrainerEntity(trainer, username, passwordRaw));
        authorityRepository.save(getTrainerAuthority(username));

        log.info("Registered new trainer: {}", trainer);
        return Map.of("username", username, "password", passwordRaw);
    }

    @Override
    public TrainerDto update(TrainerDto trainerDto, String username) {
        if (trainerDto == null) {
            throw new IllegalArgumentException("provided trainer is null");
        }
        TrainerEntity trainer = trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new EntityNotFound("no trainer with such username"));
        prepareTrainer(trainer, trainerDto);
        trainerRepository.save(trainer);
        log.info("Updated trainer profile: {}", trainer);
        return trainerMapper.toDto(trainer);
    }

    @Override
    public List<TrainerDto> getAllFreeTrainers(String usernameTrainee) {
        return trainerRepository.findAll()
                .stream()
                .filter(trainerEntity -> notInTraineeList(trainerEntity, usernameTrainee))
                .map(trainerMapper::toDto)
                .toList();
    }

    private boolean notInTraineeList(TrainerEntity trainer, String usernameTrainee) {
        return true;
    }

    private static void prepareTrainer(TrainerEntity entity, TrainerDto dto) {
        entity.setSpecialization(dto.getSpecialization());
        UserEntity user = entity.getUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
    }

    private static AuthorityEntity getTrainerAuthority(String login) {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setUsername(login);
        authorityEntity.setAuthority(AuthorityRole.ROLE_TRAINER);
        return authorityEntity;
    }

    private TrainerEntity getTrainerEntity(TrainerDto trainerDto, String login, String passwordRaw) {
        TrainerEntity trainerEntity = trainerMapper.toEntity(trainerDto);
        UserEntity userEntity = trainerEntity.getUser();
        userEntity.setUsername(login);
        userEntity.setPassword(passwordEncoder.encode(passwordRaw));
        userEntity.setEnabled(true);
        userEntity.setIsActive(true);
        return trainerEntity;
    }

}
