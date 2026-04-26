package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Providers list response model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvidersResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("providers")
    private List<Provider> providers;

    @JsonProperty("total_providers")
    private int totalProviders;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("currency")
    private String currency;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public List<Provider> getProviders() { return providers; }
    public void setProviders(List<Provider> providers) { this.providers = providers; }

    public int getTotalProviders() { return totalProviders; }
    public void setTotalProviders(int totalProviders) { this.totalProviders = totalProviders; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
