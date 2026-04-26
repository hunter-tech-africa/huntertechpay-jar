package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * KYC verification response model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KYCVerificationResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("verification_id")
    private String verificationId;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("provider_code")
    private String providerCode;

    @JsonProperty("kyc_data")
    private Map<String, Object> kycData;

    @JsonProperty("verified_at")
    private String verifiedAt;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getVerificationId() { return verificationId; }
    public void setVerificationId(String verificationId) { this.verificationId = verificationId; }

    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getProviderCode() { return providerCode; }
    public void setProviderCode(String providerCode) { this.providerCode = providerCode; }

    public Map<String, Object> getKycData() { return kycData; }
    public void setKycData(Map<String, Object> kycData) { this.kycData = kycData; }

    public String getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(String verifiedAt) { this.verifiedAt = verifiedAt; }
}
