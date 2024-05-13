package com.epam.ai.xstack.kolesnyk.service.impl;

import com.epam.ai.xstack.kolesnyk.service.LoginPasswordGenerator;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

@Service
public class LoginPasswordGeneratorImpl implements LoginPasswordGenerator {

    @Override
    public String generateLogin(String firstName, String lastName) {
        if (notMatches(firstName) || notMatches(lastName)) {
            throw new IllegalArgumentException("Name must contain only letters");
        }
        return firstName.toLowerCase() + "_" + lastName.toLowerCase();
    }

    @Override
    public String generatePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        lowerCaseRule.setNumberOfCharacters(4);

        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
        upperCaseRule.setNumberOfCharacters(4);

        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            @Override
            public String getErrorCode() {
                return "ERROR";
            }

            @Override
            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule specialCharsRule = new CharacterRule(specialChars);
        specialCharsRule.setNumberOfCharacters(2);

        return passwordGenerator.generatePassword(16, specialCharsRule, lowerCaseRule, upperCaseRule, digitRule);
    }

    private static boolean notMatches(String string) {
        return !string.matches("^[a-zA-Z]+$");
    }
}
