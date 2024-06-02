package com.ai.xstack.kolesnyk.repository;

import com.ai.xstack.kolesnyk.entity.NotificationSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettingsEntity, Long> {

    Optional<NotificationSettingsEntity> findByUserEmail(String email);

}