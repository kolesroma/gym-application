package com.ai.xstack.kolesnyk.service;

public interface LoginPasswordGenerator {
    String generateLogin(String firstName, String lastName);

    String generatePassword();
}
