package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * KYC verification request model.
 */
public class KYCVerificationRequest {

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("country")
    private String country;

    @JsonProperty("provider_code")
    private String providerCode;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    private KYCVerificationRequest(Builder builder) {
        this.phoneNumber = builder.phoneNumber;
        this.country = builder.country;
        this.providerCode = builder.providerCode;
        this.partnerId = builder.partnerId;
        this.metadata = builder.metadata;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public String getCountry() { return country; }
    public String getProviderCode() { return providerCode; }
    public String getPartnerId() { return partnerId; }
    public Map<String, Object> getMetadata() { return metadata; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String phoneNumber;
        private String country;
        private String providerCode;
        private String partnerId;
        private Map<String, Object> metadata;

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder providerCode(String providerCode) {
            this.providerCode = providerCode;
            return this;
        }

        public Builder partnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public KYCVerificationRequest build() {
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("phoneNumber is required");
            }
            if (country == null || country.isEmpty()) {
                throw new IllegalArgumentException("country is required");
            }
            if (providerCode == null || providerCode.isEmpty()) {
                throw new IllegalArgumentException("providerCode is required");
            }
            return new KYCVerificationRequest(this);
        }
    }
}
