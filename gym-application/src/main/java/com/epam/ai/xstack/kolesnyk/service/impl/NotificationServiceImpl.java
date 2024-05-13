package com.epam.ai.xstack.kolesnyk.service.impl;

import com.epam.ai.xstack.kolesnyk.entity.NotificationSettingsEntity;
import com.epam.ai.xstack.kolesnyk.entity.TrainingEntity;
import com.epam.ai.xstack.kolesnyk.entity.UserEntity;
import com.epam.ai.xstack.kolesnyk.repository.NotificationSettingsRepository;
import com.epam.ai.xstack.kolesnyk.repository.TrainingRepository;
import com.epam.ai.xstack.kolesnyk.service.NotificationService;
import com.epam.ai.xstack.kolesnyk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender emailSender;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final UserService userService;
    private final TrainingRepository trainingRepository;

    @Override
    public void sendMessage(String email, String subject, String text) {
        NotificationSettingsEntity settings = getNotificationSettingsByEmail(email);
        if (!settings.isEnabled()) {
            log.info("Email {} disable notification", email);
            return;
        }
        sendSimpleMessage(email, subject, text);
        log.info("Sent letter to {}", email);
    }

    @Override
    public NotificationSettingsEntity getNotificationSettingsByEmail(String email) {
        return notificationSettingsRepository.findByUserEmail(email)
                .orElse(new NotificationSettingsEntity(0L, null, true));
    }

    @Override
    public void setNotificationStatusTo(boolean status) {
        UserEntity user = userService.getUserFromCurrentSecurityContext();
        Long userId = user.getId();
        Optional<NotificationSettingsEntity> settingsOptional = notificationSettingsRepository.findById(userId);
        if (settingsOptional.isEmpty()) {
            var newSettings = new NotificationSettingsEntity(userId, user, status);
            notificationSettingsRepository.save(newSettings);
        } else {
            NotificationSettingsEntity settings = settingsOptional.get();
            settings.setEnabled(status);
            notificationSettingsRepository.save(settings);
        }
    }

    @Override
    @Scheduled(cron = "0 1 0 * * *") // Runs at 00:01 every day
    public void sendNotificationForTomorrowTraining() {
        log.info("Started job to send notifications");
        List<TrainingEntity> filteredTrainings = trainingRepository.findAll()
                .stream()
                .filter(training -> LocalDate.now().equals(training.getTrainingDate()))
                .toList();
        filteredTrainings
                .forEach(training -> sendSimpleMessage(training.getTrainee().getUser().getEmail(), "Reminder", "You have training soon: " + training.getTrainingName() + ".\nWith date: " + training.getTrainingDate()));
        log.info("Ended job to send notifications. Sent to count of users: " + filteredTrainings.size());
    }

    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kolesgym@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

}
