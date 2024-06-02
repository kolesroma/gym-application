package com.ai.xstack.kolesnyk.service;

import com.ai.xstack.kolesnyk.entity.NotificationSettingsEntity;

public interface NotificationService {

    void sendMessage(String email, String subject, String text);

    NotificationSettingsEntity getNotificationSettingsByEmail(String email);

    void setNotificationStatusTo(boolean status);

    void sendNotificationForTomorrowTraining();

}
