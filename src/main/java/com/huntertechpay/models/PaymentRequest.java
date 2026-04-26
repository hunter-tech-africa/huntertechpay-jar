package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Request model for initiating a payment.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequest {

    @JsonProperty("phone")
    private String phoneNumber;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("country")
    private String country;

    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("webhook_url")
    private String webhookUrl;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    // Builder pattern
    public static class Builder {
        private final PaymentRequest request = new PaymentRequest();

        public Builder phoneNumber(String phoneNumber) {
            request.phoneNumber = phoneNumber;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            request.amount = amount;
            return this;
        }

        public Builder amount(double amount) {
            request.amount = BigDecimal.valueOf(amount);
            return this;
        }

        public Builder currency(String currency) {
            request.currency = currency;
            return this;
        }

        public Builder country(String country) {
            request.country = country;
            return this;
        }

        public Builder serviceCode(String serviceCode) {
            request.serviceCode = serviceCode;
            return this;
        }

        public Builder partnerId(String partnerId) {
            request.partnerId = partnerId;
            return this;
        }

        public Builder description(String description) {
            request.description = description;
            return this;
        }

        public Builder customerName(String customerName) {
            request.customerName = customerName;
            return this;
        }

        public Builder customerEmail(String customerEmail) {
            request.customerEmail = customerEmail;
            return this;
        }

        public Builder webhookUrl(String webhookUrl) {
            request.webhookUrl = webhookUrl;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            request.metadata = metadata;
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            if (request.metadata == null) {
                request.metadata = new HashMap<>();
            }
            request.metadata.put(key, value);
            return this;
        }

        public PaymentRequest build() {
            // Validation
            if (request.phoneNumber == null || request.phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("phoneNumber is required");
            }
            if (request.amount == null || request.amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("amount must be greater than 0");
            }
            if (request.currency == null || request.currency.isEmpty()) {
                throw new IllegalArgumentException("currency is required");
            }
            if (request.country == null || request.country.isEmpty()) {
                throw new IllegalArgumentException("country is required");
            }
            if (request.serviceCode == null || request.serviceCode.isEmpty()) {
                throw new IllegalArgumentException("serviceCode is required");
            }
            if (request.partnerId == null || request.partnerId.isEmpty()) {
                throw new IllegalArgumentException("partnerId is required");
            }

            return request;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getPhoneNumber() { return phoneNumber; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCountry() { return country; }
    public String getServiceCode() { return serviceCode; }
    public String getPartnerId() { return partnerId; }
    public String getDescription() { return description; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getWebhookUrl() { return webhookUrl; }
    public Map<String, Object> getMetadata() { return metadata; }
}
