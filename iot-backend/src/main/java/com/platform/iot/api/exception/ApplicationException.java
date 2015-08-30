package com.platform.iot.api.exception;


public class ApplicationException extends Exception {

    private ExceptionType type;
    private String message;

    public static ApplicationException create(ExceptionType type) {
        return new ApplicationException(type);
    }

    public static ApplicationException create(ExceptionType type, Throwable e) {
        return new ApplicationException(type, e);
    }

    public static ApplicationException create(ExceptionType type, String message) {
        ApplicationException exception = new ApplicationException(type);
        exception.setMessage(message);
        return exception;
    }

    public ApplicationException(ExceptionType type) {
        super(type.getDefaultMessage());
        this.type = type;
        this.message = type.getDefaultMessage();
    }

    public ApplicationException(ExceptionType type, Throwable e) {
        super(type.getDefaultMessage(), e);
    }

    public ExceptionType getType() {
        return type;
    }

    public void setType(ExceptionType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
