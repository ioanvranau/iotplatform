package com.platform.iot.api.exception;

/**
 * Created by Magda Gherasim
 */
public enum ExceptionType {

    INVALID_MESSAGE(0, Severity.WARNING, "Invalid message"),
    TECHNICAL_PROBLEM(1, Severity.ERROR, "Technical problem message"),
    USER_NOT_AUTHENTICATED(3, Severity.ERROR, "User is not authenticates!"),
    USER_ALREADY_EXISTS(6, Severity.WARNING, "User already exists!"),
    WRONG_USERNAME_OR_PASSWORD(7, Severity.WARNING, "Wrong username or password!"),
    CANNOT_UPDATE_USER(8, Severity.WARNING, "Cannot update user!"),
    USER_NOT_FOUND(9, Severity.WARNING, "Could not find in the db the user you're trying to delete"),
    PRODUCER_NOT_FOUND(10, Severity.WARNING, "Could not find in the db the producer you're trying to delete"),
    TOPIC_NOT_FOUND(11, Severity.WARNING , "Could not find in the db the topic you're trying to delete"),
    CANNOT_DISABLE_USER_ACCOUNT(12, Severity.WARNING, "Cannot disable account for user!"),
    DISABLED_USER(13, Severity.WARNING, "Your user is disabled!");

    public enum Severity {ERROR, WARNING, INFO}

    private final int code;
    private final Severity severity;
    private final String defaultMessage;

    private ExceptionType(int code, Severity severity, String defaultMessage) {
        this.code = code;
        this.severity = severity;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}