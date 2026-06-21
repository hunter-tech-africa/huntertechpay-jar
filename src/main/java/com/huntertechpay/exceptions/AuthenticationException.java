package com.huntertechpay.exceptions;

import java.util.Map;

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

    public AuthenticationException(
        String message,
        int statusCode,
        String errorCode,
        Map<String, Object> data,
        String apiMessage,
        String requestId
    ) {
        super(message, statusCode, errorCode, data, apiMessage, requestId);
    }
}
