package com.huntertechpay.exceptions;

/**
 * Base exception class for all HunterTechPay SDK exceptions.
 */
public class HunterTechPayException extends Exception {

    private final int statusCode;
    private final String errorCode;

    public HunterTechPayException(String message) {
        super(message);
        this.statusCode = 0;
        this.errorCode = null;
    }

    public HunterTechPayException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.errorCode = null;
    }

    public HunterTechPayException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
