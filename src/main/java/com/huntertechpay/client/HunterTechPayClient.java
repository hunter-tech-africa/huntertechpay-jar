package com.huntertechpay.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.huntertechpay.exceptions.AuthenticationException;
import com.huntertechpay.exceptions.HunterTechPayException;
import com.huntertechpay.exceptions.ValidationException;
import com.huntertechpay.models.*;
import com.huntertechpay.security.HMACSignature;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Main client for HunterTechPay API.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * HunterTechPayClient client = new HunterTechPayClient.Builder()
 *     .apiKey("your_api_key")
 *     .secretKey("your_secret_key")
 *     .build();
 *
 * PaymentRequest request = PaymentRequest.builder()
 *     .phoneNumber("+237690000000")
 *     .amount(5000.0)
 *     .currency("XAF")
 *     .country("CM")
 *     .serviceCode("CM_OMCMR_CASHOUT")
 *     .partnerId("order-123")
 *     .build();
 *
 * PaymentResponse response = client.initiatePayment(request);
 * }</pre>
 */
public class HunterTechPayClient {

    private static final Logger logger = LoggerFactory.getLogger(HunterTechPayClient.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final String apiKey;
    private final String secretKey;
    private final String baseUrl;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    private HunterTechPayClient(Builder builder) {
        this.apiKey = builder.apiKey;
        this.secretKey = builder.secretKey;
        this.baseUrl = builder.baseUrl;
        this.httpClient = builder.httpClient != null ? builder.httpClient : createDefaultHttpClient(builder);
        this.objectMapper = createObjectMapper();
    }

    private static OkHttpClient createDefaultHttpClient(Builder builder) {
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(builder.connectTimeout))
                .readTimeout(Duration.ofSeconds(builder.readTimeout))
                .writeTimeout(Duration.ofSeconds(builder.writeTimeout))
                .build();
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        return mapper;
    }

    /**
     * Initiate a new payment.
     *
     * @param request Payment request details
     * @return Payment response with transaction ID
     * @throws HunterTechPayException if request fails
     */
    public PaymentResponse initiatePayment(PaymentRequest request) throws HunterTechPayException {
        return post("/api/v1/payments/initiate", request, PaymentResponse.class);
    }

    /**
     * Deposit (CASHIN) - From mobile money to wallet.
     *
     * @param request Payment request details
     * @return Payment response with transaction ID
     * @throws HunterTechPayException if request fails
     */
    public PaymentResponse deposit(PaymentRequest request) throws HunterTechPayException {
        return post("/api/v1/payments/deposit", request, PaymentResponse.class);
    }

    /**
     * Withdraw (CASHOUT) - From wallet to mobile money.
     *
     * @param request Payment request details
     * @return Payment response with transaction ID
     * @throws HunterTechPayException if request fails
     */
    public PaymentResponse withdraw(PaymentRequest request) throws HunterTechPayException {
        return post("/api/v1/payments/withdraw", request, PaymentResponse.class);
    }

    /**
     * Get available payment providers.
     *
     * @return List of all available providers
     * @throws HunterTechPayException if request fails
     */
    public ProvidersResponse getProviders() throws HunterTechPayException {
        return get("/api/v1/payments/providers", ProvidersResponse.class);
    }

    /**
     * Get available payment providers for a specific country.
     *
     * @param countryCode Country code (CM, SN, CI, etc.)
     * @return List of providers for the specified country
     * @throws HunterTechPayException if request fails
     */
    public ProvidersResponse getProviders(String countryCode) throws HunterTechPayException {
        if (countryCode == null || countryCode.isEmpty()) {
            return getProviders();
        }
        return get("/api/v1/payments/providers?country_code=" + countryCode, ProvidersResponse.class);
    }

    /**
     * Check payment status by partner ID.
     *
     * @param partnerId Your unique reference ID
     * @return Payment status
     * @throws HunterTechPayException if request fails
     */
    public PaymentResponse checkStatus(String partnerId) throws HunterTechPayException {
        return get("/api/v1/payments/status/" + partnerId, PaymentResponse.class);
    }

    /**
     * Verify KYC (Know Your Customer) information for a phone number.
     *
     * @param request KYC verification request details
     * @return KYC verification response with customer information
     * @throws HunterTechPayException if request fails
     */
    public KYCVerificationResponse kyc(KYCVerificationRequest request) throws HunterTechPayException {
        return post("/api/v1/payments/kyc", request, KYCVerificationResponse.class);
    }

    /**
     * Get list of transactions.
     *
     * @param page Page number (1-based)
     * @param limit Number of transactions per page
     * @return List of transactions
     * @throws HunterTechPayException if request fails
     */
    public TransactionListResponse getTransactions(int page, int limit) throws HunterTechPayException {
        String url = String.format("/api/v1/payments/transactions?page=%d&limit=%d", page, limit);
        return get(url, TransactionListResponse.class);
    }

    /**
     * Get account balance.
     *
     * @param currency Currency code (XAF or XOF)
     * @return Balance information
     * @throws HunterTechPayException if request fails
     */
    public BalanceResponse getBalance(String currency) throws HunterTechPayException {
        String url = "/api/v1/payments/balance?currency=" + currency;
        return get(url, BalanceResponse.class);
    }

    /**
     * Verify webhook signature.
     *
     * @param payload Webhook payload as JSON string
     * @param timestamp Timestamp from X-Hunter-Timestamp header
     * @param signature Signature from X-Hunter-Signature header
     * @return true if signature is valid
     */
    public boolean verifyWebhookSignature(String payload, long timestamp, String signature) {
        return HMACSignature.verifySignature(secretKey, timestamp, payload, signature);
    }

    /**
     * Parse webhook event.
     *
     * @param payload Webhook payload as JSON string
     * @return Parsed webhook event
     * @throws HunterTechPayException if parsing fails
     */
    public WebhookEvent parseWebhookEvent(String payload) throws HunterTechPayException {
        try {
            return objectMapper.readValue(payload, WebhookEvent.class);
        } catch (IOException e) {
            throw new HunterTechPayException("Failed to parse webhook payload", e);
        }
    }

    // HTTP methods

    private <T> T get(String path, Class<T> responseClass) throws HunterTechPayException {
        String url = baseUrl + path;

        // For GET requests, still add signature for security
        long timestamp = HMACSignature.getCurrentTimestamp();
        String signature = HMACSignature.generateSignature(secretKey, timestamp, "");

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Api-Key", apiKey)
                .addHeader("X-Hunter-Signature", signature)
                .addHeader("X-Hunter-Timestamp", String.valueOf(timestamp))
                .get()
                .build();

        return executeRequest(request, responseClass);
    }

    private <T> T post(String path, Object requestBody, Class<T> responseClass) throws HunterTechPayException {
        try {
            String url = baseUrl + path;
            String jsonPayload = objectMapper.writeValueAsString(requestBody);

            long timestamp = HMACSignature.getCurrentTimestamp();
            String signature = HMACSignature.generateSignature(secretKey, timestamp, jsonPayload);

            RequestBody body = RequestBody.create(jsonPayload, JSON);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-Api-Key", apiKey)
                    .addHeader("X-Hunter-Signature", signature)
                    .addHeader("X-Hunter-Timestamp", String.valueOf(timestamp))
                    .post(body)
                    .build();

            return executeRequest(request, responseClass);

        } catch (IOException e) {
            throw new HunterTechPayException("Failed to serialize request", e);
        }
    }

    private <T> T executeRequest(Request request, Class<T> responseClass) throws HunterTechPayException {
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                handleErrorResponse(response.code(), responseBody);
            }

            return objectMapper.readValue(responseBody, responseClass);

        } catch (IOException e) {
            throw new HunterTechPayException("Network error: " + e.getMessage(), e);
        }
    }

    private void handleErrorResponse(int statusCode, String responseBody) throws HunterTechPayException {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorData = objectMapper.readValue(responseBody, Map.class);

            // Extract original API message (unmodified)
            String apiMessage = (String) errorData.getOrDefault("detail",
                errorData.getOrDefault("message",
                    errorData.getOrDefault("error",
                        errorData.getOrDefault("error_message", ""))));

            String message = apiMessage.isEmpty() ? "Unknown error" : apiMessage;

            // Extract error code from API
            String errorCode = (String) errorData.getOrDefault("error_code",
                errorData.get("code"));

            // Extract request ID
            String requestId = (String) errorData.get("request_id");

            if (statusCode == 401 || statusCode == 403) {
                throw new AuthenticationException(message, statusCode, errorCode, errorData, apiMessage, requestId);
            } else if (statusCode == 400) {
                throw new ValidationException(message, statusCode, errorCode, errorData, apiMessage, requestId);
            } else {
                throw new HunterTechPayException(message, statusCode, errorCode, errorData, apiMessage, requestId);
            }
        } catch (IOException e) {
            // If JSON parsing fails, store raw response
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("raw_response", responseBody.substring(0, Math.min(1000, responseBody.length())));
            String message = "HTTP " + statusCode + ": " + responseBody;

            throw new HunterTechPayException(message, statusCode, null, errorData, responseBody, null);
        }
    }

    // Builder pattern

    public static class Builder {
        private String apiKey;
        private String secretKey;
        private String baseUrl = "https://huntertechpay.com:8000";
        private OkHttpClient httpClient;
        private long connectTimeout = 30;
        private long readTimeout = 30;
        private long writeTimeout = 30;

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder httpClient(OkHttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder connectTimeout(long seconds) {
            this.connectTimeout = seconds;
            return this;
        }

        public Builder readTimeout(long seconds) {
            this.readTimeout = seconds;
            return this;
        }

        public Builder writeTimeout(long seconds) {
            this.writeTimeout = seconds;
            return this;
        }

        public HunterTechPayClient build() {
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalArgumentException("apiKey is required");
            }
            if (secretKey == null || secretKey.isEmpty()) {
                throw new IllegalArgumentException("secretKey is required");
            }
            return new HunterTechPayClient(this);
        }
    }
}
