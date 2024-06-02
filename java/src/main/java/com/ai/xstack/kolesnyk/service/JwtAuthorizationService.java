package com.ai.xstack.kolesnyk.service;

import com.ai.xstack.kolesnyk.entity.UserEntity;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtAuthorizationService {
    String createToken(UserEntity user);

    String getTokenFromUserRequest(HttpServletRequest request);

    Claims parseTokenToClaims(String token);
}
