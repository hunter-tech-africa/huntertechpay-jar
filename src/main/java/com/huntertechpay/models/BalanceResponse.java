package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Balance response model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("available_balance")
    private BigDecimal availableBalance;

    @JsonProperty("pending_balance")
    private BigDecimal pendingBalance;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }

    public BigDecimal getPendingBalance() { return pendingBalance; }
    public void setPendingBalance(BigDecimal pendingBalance) { this.pendingBalance = pendingBalance; }
}
