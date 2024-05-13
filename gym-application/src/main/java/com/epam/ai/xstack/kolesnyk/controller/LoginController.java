package com.epam.ai.xstack.kolesnyk.controller;

import com.epam.ai.xstack.kolesnyk.dto.ChangePasswordDto;
import com.epam.ai.xstack.kolesnyk.entity.UserEntity;
import com.epam.ai.xstack.kolesnyk.service.JwtAuthorizationService;
import com.epam.ai.xstack.kolesnyk.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final JwtAuthorizationService jwtAuthorizationService;
    private final UserService userService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserEntity auth,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        UserEntity userDb =
                userService.getByUsernameAndPassword(auth.getUsername(), auth.getPassword(), request.getRemoteAddr());

        if (!userDb.getEnabled()) {
            throw new AccessDeniedException("Your account is disabled");
        }

        String token = jwtAuthorizationService.createToken(userDb);
        response.addHeader("Authorization", String.format("Bearer %s", token));
        return Map.of("token", token);
    }

    @PostMapping("/change-password")
    public Map<String, String> changePassword(@RequestBody @Validated ChangePasswordDto changePasswordDto,
                                              Principal principal) {
        userService.changePassword(changePasswordDto);
        return Map.of("message", "Password was changed successfully", "username", principal.getName());
    }

    @PostMapping("/disable-account")
    public Map<String, String> disableAccount(Principal principal) {
        userService.disableAccount();
        return Map.of("message", "Account was disabled", "username", principal.getName());
    }

    @Deprecated
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        response.addHeader("Authorization", "");
        return "logged out";
    }

}
