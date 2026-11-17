package com.auca.interfaces;

import com.auca.exceptions.ValidationException;
import java.time.LocalDate;

public interface Registrable {
    int MIN_NAME_LENGTH = 2;
    int MAX_NAME_LENGTH = 60;
    int MAX_EMAIL_LENGTH = 100;
    int MIN_PHONE_LENGTH = 10;
    int MAX_PHONE_LENGTH = 15;
    String DATE_PATTERN = "yyyy-MM-dd";

    void validate() throws ValidationException;

    void printDetails();

    String getRegistrationSummary();

    static void requireText(String value, String label, int min, int max) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(label + " must not be blank.");
        }
        int length = value.trim().length();
        if (length < min || length > max) {
            throw new ValidationException(label + " must be " + min + "-" + max + " characters long.");
        }
    }

    static void requireEmail(String value, String label) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(label + " must not be blank.");
        }
        String email = value.trim();
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new ValidationException(label + " must not exceed " + MAX_EMAIL_LENGTH + " characters.");
        }
        if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}")) {
            throw new ValidationException(label + " is not a valid email address (example: name@example.com).");
        }
    }

    static void requirePhone(String value, String label) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(label + " must not be blank.");
        }
        String phone = value.trim();
        String digits = phone.startsWith("+") ? phone.substring(1) : phone;
        if (!digits.matches("[0-9]+") || digits.length() < MIN_PHONE_LENGTH || digits.length() > MAX_PHONE_LENGTH) {
            throw new ValidationException(label + " must contain " + MIN_PHONE_LENGTH + "-" + MAX_PHONE_LENGTH
                    + " digits with an optional leading +.");
        }
    }

    static void requireNotNull(Object value, String label) throws ValidationException {
        if (value == null) {
            throw new ValidationException(label + " is required.");
        }
    }

    static void requireChoice(String value, String label, String[] allowed) throws ValidationException {
        if (value != null) {
            for (String option : allowed) {
                if (option.equals(value)) {
                    return;
                }
            }
        }
        throw new ValidationException(label + " must be one of: " + String.join(", ", allowed) + ".");
    }

    static void requirePastDate(LocalDate date, String label) throws ValidationException {
        if (date == null) {
            throw new ValidationException(label + " is required.");
        }
        if (!date.isBefore(LocalDate.now())) {
            throw new ValidationException(label + " must be in the past.");
        }
    }

    static void requireNotFutureDate(LocalDate date, String label) throws ValidationException {
        if (date == null) {
            throw new ValidationException(label + " is required.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(label + " must not be in the future.");
        }
    }
}
