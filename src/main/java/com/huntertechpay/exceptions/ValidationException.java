package com.huntertechpay.exceptions;

import java.util.Map;

/**
 * Exception thrown when request validation fails.
 */
public class ValidationException extends HunterTechPayException {

    public ValidationException(String message) {
        super(message, 400, "VALIDATION_ERROR");
    }

    public ValidationException(String message, String errorCode) {
        super(message, 400, errorCode);
    }

    public ValidationException(
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
