package com.huntertechpay.exceptions;

/**
 * Exception thrown when API authentication fails.
 */
public class AuthenticationException extends HunterTechPayException {

    public AuthenticationException(String message) {
        super(message, 401, "AUTHENTICATION_FAILED");
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
