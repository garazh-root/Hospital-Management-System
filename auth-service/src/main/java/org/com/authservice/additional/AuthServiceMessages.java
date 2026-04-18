package org.com.authservice.additional;

public enum AuthServiceMessages {

    USER_NOT_FOUND("User not found"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    PASSWORD_NOT_MATCH("Password does not match"),
    BAD_CREDENTIALS("Bad credentials");

    private final String message;

    AuthServiceMessages(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
