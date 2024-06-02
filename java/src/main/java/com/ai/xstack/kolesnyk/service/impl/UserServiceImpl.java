package com.ai.xstack.kolesnyk.service.impl;

import com.ai.xstack.kolesnyk.dto.ChangePasswordDto;
import com.ai.xstack.kolesnyk.entity.UserEntity;
import com.ai.xstack.kolesnyk.exception.BruteForceTryingException;
import com.ai.xstack.kolesnyk.exception.EntityNotFound;
import com.ai.xstack.kolesnyk.repository.UserRepository;
import com.ai.xstack.kolesnyk.service.BruteForceMonitorService;
import com.ai.xstack.kolesnyk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BruteForceMonitorService bruteForceMonitorService;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    @Override
    public UserEntity getByUsernameAndPassword(String username, String password, String ip) {
        if (bruteForceMonitorService.isSuspiciousIp(ip)) {
            throw new AccessDeniedException("Your IP is suspicious and was banned because of many unsuccessful attempts to log in");
        }
        UserEntity userDb;
        try {
            userDb = getByUsername(username);
            if (!isPasswordMatches(userDb, password)) {
                throw new BruteForceTryingException();
            }
        } catch (BruteForceTryingException e) {
            log.debug(String.format("Brute force for username %s", username));
            bruteForceMonitorService.trackFailedAttempt(ip);
            throw new IllegalArgumentException(e);
        }
        return userDb;
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(BruteForceTryingException::new);
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();
        if (!isPasswordMatches(getUserFromCurrentSecurityContext(), oldPassword)) {
            throw new IllegalArgumentException("Provided old password differs from password saved in database");
        }
        if (!newPassword.equals(changePasswordDto.getNewPasswordRepeat())) {
            throw new IllegalArgumentException("New password and password repeat are not equal");
        }
        jdbcUserDetailsManager.changePassword(
                passwordEncoder.encode(oldPassword),
                passwordEncoder.encode(newPassword)
        );
    }

    @Override
    public UserEntity getUserFromCurrentSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFound("Authentication not provided"));
    }

    @Override
    public void disableAccount() {
        UserEntity user = getUserFromCurrentSecurityContext();
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public boolean isPasswordMatches(UserEntity userDb, String rawPassword) {
        return passwordEncoder.matches(rawPassword, userDb.getPassword());
    }
}
