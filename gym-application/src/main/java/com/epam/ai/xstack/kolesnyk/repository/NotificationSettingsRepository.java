package com.epam.ai.xstack.kolesnyk.repository;

import com.epam.ai.xstack.kolesnyk.entity.NotificationSettingsEntity;
import com.epam.ai.xstack.kolesnyk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettingsEntity, Long> {

    Optional<NotificationSettingsEntity> findByUserEmail(String email);

}