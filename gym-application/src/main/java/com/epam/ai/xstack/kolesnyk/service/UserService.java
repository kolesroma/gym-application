package com.epam.ai.xstack.kolesnyk.service;

import com.epam.ai.xstack.kolesnyk.dto.ChangePasswordDto;
import com.epam.ai.xstack.kolesnyk.entity.UserEntity;

public interface UserService {
    UserEntity getByUsernameAndPassword(String username, String password, String ip);

    UserEntity getByUsername(String username);

    void changePassword(ChangePasswordDto changePasswordDto);

    UserEntity getUserFromCurrentSecurityContext();

    void disableAccount();

    boolean isPasswordMatches(UserEntity userDb, String password);
}

