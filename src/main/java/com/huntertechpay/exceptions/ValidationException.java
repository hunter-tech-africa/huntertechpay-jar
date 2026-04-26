package com.huntertechpay.exceptions;

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
}
