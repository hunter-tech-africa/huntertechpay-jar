package com.huntertechpay.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Base exception class for all HunterTechPay SDK exceptions.
 */
public class HunterTechPayException extends Exception {

    private final int statusCode;
    private final String errorCode;
    private final String apiMessage;
    private final Map<String, Object> data;
    private final String requestId;

    public HunterTechPayException(String message) {
        super(message);
        this.statusCode = 0;
        this.errorCode = null;
        this.apiMessage = message;
        this.data = new HashMap<>();
        this.requestId = null;
    }

    public HunterTechPayException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.errorCode = null;
        this.apiMessage = message;
        this.data = new HashMap<>();
        this.requestId = null;
    }

    public HunterTechPayException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.apiMessage = message;
        this.data = new HashMap<>();
        this.requestId = null;
    }

    /**
     * Create a new HunterTechPayException with complete API response details.
     *
     * @param message Error message
     * @param statusCode HTTP status code
     * @param errorCode Error code from API
     * @param data Complete API response data
     * @param apiMessage Original API message (unmodified)
     * @param requestId Request ID for tracing
     */
    public HunterTechPayException(
        String message,
        int statusCode,
        String errorCode,
        Map<String, Object> data,
        String apiMessage,
        String requestId
    ) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.data = data != null ? data : new HashMap<>();
        this.apiMessage = apiMessage != null ? apiMessage : message;
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Get the original error message from API (unmodified).
     *
     * @return Original API message
     */
    public String getApiMessage() {
        return apiMessage;
    }

    /**
     * Get the complete API response data.
     *
     * @return Complete API response data
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * Get the request ID for tracing.
     *
     * @return Request ID
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Get specific detail from error data.
     *
     * @param key The key to retrieve
     * @return The value or null if not found
     */
    public Object getDetail(String key) {
        return data.get(key);
    }

    /**
     * Get specific detail from error data with default value.
     *
     * @param key The key to retrieve
     * @param defaultValue Default value if key not found
     * @return The value or default
     */
    public Object getDetail(String key, Object defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    /**
     * Convert exception to Map for logging.
     *
     * @return Map with complete exception details
     */
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("error_type", this.getClass().getSimpleName());
        result.put("message", getMessage());
        result.put("api_message", apiMessage);
        result.put("status_code", statusCode);
        result.put("error_code", errorCode);
        result.put("request_id", requestId);
        result.put("data", data);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessage());

        if (statusCode > 0) {
            sb.append(" | Status: ").append(statusCode);
        }

        if (errorCode != null && !errorCode.isEmpty()) {
            sb.append(" | Code: ").append(errorCode);
        }

        if (requestId != null && !requestId.isEmpty()) {
            sb.append(" | Request ID: ").append(requestId);
        }

        // Add additional error details from API response
        if (data != null && !data.isEmpty()) {
            Map<String, Object> extraDetails = new HashMap<>(data);
            extraDetails.remove("detail");
            extraDetails.remove("message");
            extraDetails.remove("error");
            extraDetails.remove("error_code");
            extraDetails.remove("error_message");
            extraDetails.remove("code");

            if (!extraDetails.isEmpty()) {
                sb.append(" | Details: ").append(extraDetails);
            }
        }

        return sb.toString();
    }
}
