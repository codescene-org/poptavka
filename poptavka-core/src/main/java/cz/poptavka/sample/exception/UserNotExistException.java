/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.exception;

/**
 * Represents more specific failure of login error.
 * This can be used for application purposes, but end user should never find out if it gives invalid email or password!
 */
public class UserNotExistException extends LoginException {

    private static final String COMMON_MESSAGE = "User with specified email does not exist!";

    public UserNotExistException() {
        super(COMMON_MESSAGE);
    }

    public UserNotExistException(String message) {
        super(COMMON_MESSAGE + message);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(COMMON_MESSAGE + message, cause);
    }
}