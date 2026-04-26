package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Response model for payment operations.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("country")
    private String country;

    @JsonProperty("fee_amount")
    private BigDecimal feeAmount;

    @JsonProperty("commission_amount")
    private BigDecimal commissionAmount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getTransactionId() { return transactionId; }
    public String getPartnerId() { return partnerId; }
    public String getStatus() { return status; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getServiceCode() { return serviceCode; }
    public String getCountry() { return country; }
    public BigDecimal getFeeAmount() { return feeAmount; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Map<String, Object> getMetadata() { return metadata; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }

    // Setters (for Jackson)
    public void setSuccess(boolean success) { this.success = success; }
    public void setMessage(String message) { this.message = message; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    public void setStatus(String status) { this.status = status; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }
    public void setCountry(String country) { this.country = country; }
    public void setFeeAmount(BigDecimal feeAmount) { this.feeAmount = feeAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
