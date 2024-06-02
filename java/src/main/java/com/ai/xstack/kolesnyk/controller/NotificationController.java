package com.ai.xstack.kolesnyk.controller;

import com.ai.xstack.kolesnyk.entity.UserEntity;
import com.ai.xstack.kolesnyk.service.UserService;
import com.ai.xstack.kolesnyk.entity.NotificationSettingsEntity;
import com.ai.xstack.kolesnyk.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/status/me")
    public NotificationSettingsEntity getStatus() {
        UserEntity user = userService.getUserFromCurrentSecurityContext();
        return notificationService.getNotificationSettingsByEmail(user.getEmail());
    }

    @PostMapping("/status")
    public void setNotificationStatus(@RequestParam boolean status) {
        notificationService.setNotificationStatusTo(status);
    }

    @GetMapping("/trigger-sending")
    public void triggerSending() {
        notificationService.sendNotificationForTomorrowTraining();
    }

}
