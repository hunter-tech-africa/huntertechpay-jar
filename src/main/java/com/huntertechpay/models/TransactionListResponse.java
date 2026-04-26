package com.huntertechpay.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Transaction list response model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionListResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("transactions")
    private List<Transaction> transactions;

    @JsonProperty("total")
    private int total;

    @JsonProperty("page")
    private int page;

    @JsonProperty("limit")
    private int limit;

    @JsonProperty("has_more")
    private boolean hasMore;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public boolean isHasMore() { return hasMore; }
    public void setHasMore(boolean hasMore) { this.hasMore = hasMore; }
}
