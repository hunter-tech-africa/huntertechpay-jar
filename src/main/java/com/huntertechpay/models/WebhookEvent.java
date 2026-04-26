package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;

/**
 * Webhook event model for receiving payment status updates.
 */
public class WebhookEvent {

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("previous_status")
    private String previousStatus;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("timestamp")
    private Instant timestamp;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

    // Getters and Setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(String previousStatus) { this.previousStatus = previousStatus; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
