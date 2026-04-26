package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Payment provider model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Provider {

    @JsonProperty("provider_code")
    private String providerCode;

    @JsonProperty("name")
    private String name;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("cashin_service_code")
    private String cashinServiceCode;

    @JsonProperty("cashout_service_code")
    private String cashoutServiceCode;

    @JsonProperty("supports_cashin")
    private boolean supportsCashin;

    @JsonProperty("supports_cashout")
    private boolean supportsCashout;

    @JsonProperty("status")
    private String status;

    @JsonProperty("logo_url")
    private String logoUrl;

    public String getProviderCode() { return providerCode; }
    public void setProviderCode(String providerCode) { this.providerCode = providerCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCashinServiceCode() { return cashinServiceCode; }
    public void setCashinServiceCode(String cashinServiceCode) { this.cashinServiceCode = cashinServiceCode; }

    public String getCashoutServiceCode() { return cashoutServiceCode; }
    public void setCashoutServiceCode(String cashoutServiceCode) { this.cashoutServiceCode = cashoutServiceCode; }

    public boolean isSupportsCashin() { return supportsCashin; }
    public void setSupportsCashin(boolean supportsCashin) { this.supportsCashin = supportsCashin; }

    public boolean isSupportsCashout() { return supportsCashout; }
    public void setSupportsCashout(boolean supportsCashout) { this.supportsCashout = supportsCashout; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
