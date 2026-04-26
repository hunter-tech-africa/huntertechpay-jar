package com.huntertechpay;

import com.huntertechpay.client.HunterTechPayClient;
import com.huntertechpay.exceptions.HunterTechPayException;
import com.huntertechpay.models.BalanceResponse;
import com.huntertechpay.models.PaymentRequest;
import com.huntertechpay.models.PaymentResponse;
import com.huntertechpay.models.TransactionListResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for HunterTechPay Java SDK.
 *
 * Run with: mvn test -Dtest=IntegrationTest
 */
public class IntegrationTest {

    private static HunterTechPayClient client;

    // Credentials
    private static final String API_KEY = "htp_live_6f09";
    private static final String SECRET_KEY = "sk_live_0825";

    // Test data
    private static final String MTN_PHONE = "670XXXXXX";  // Sans préfixe
    private static final String ORANGE_PHONE = "697040726";  // Sans préfixe

    // Service codes
    private static final String MTN_CASHIN = "HT_CASHIN_MTN_CM";
    private static final String ORANGE_CASHIN = "HT_CASHIN_ORANGE_CM";
    private static final String MTN_PAYMENT = "HT_PAIEMENTMARCHAND_MTN_CM";
    private static final String ORANGE_PAYMENT = "HT_PAIEMENTMARCHAND_ORANGE_CM";

    @BeforeAll
    public static void setUp() {
        // Create custom OkHttpClient with SSL verification disabled for local testing
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{
            new javax.net.ssl.X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
        };

        try {
            javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            okhttp3.OkHttpClient customClient = new okhttp3.OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (javax.net.ssl.X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

            client = new HunterTechPayClient.Builder()
                    .apiKey(API_KEY)
                    .secretKey(SECRET_KEY)
                    .baseUrl("https://localhost:8008")
                    .httpClient(customClient)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HTTP client", e);
        }

        System.out.println("=".repeat(80));
        System.out.println("🧪 HunterTechPay Java SDK - Integration Tests");
        System.out.println("=".repeat(80));
    }

    @Test
    public void test01_InitiatePaymentMTN() {
        System.out.println("\n📱 Test 1: Initiate MTN Payment (Cash In)");
        System.out.println("-".repeat(80));

        try {
            String partnerId = "java-test-mtn-" + System.currentTimeMillis();

            PaymentRequest request = PaymentRequest.builder()
                    .phoneNumber(MTN_PHONE)
                    .amount(100.0)  // 100 FCFA
                    .currency("XAF")
                    .country("CM")
                    .serviceCode(MTN_CASHIN)
                    .partnerId(partnerId)
                    .description("Test payment MTN from Java SDK")
                    .customerName("Test User")
                    .addMetadata("test_framework", "JUnit")
                    .addMetadata("sdk", "Java")
                    .build();

            PaymentResponse response = client.deposit(request);

            System.out.println("✅ Payment initiated successfully!");
            System.out.println("Transaction ID: " + response.getTransactionId());
            System.out.println("Partner ID: " + response.getPartnerId());
            System.out.println("Status: " + response.getStatus());
            System.out.println("Amount: " + response.getAmount() + " " + response.getCurrency());

            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertNotNull(response.getTransactionId());
            assertEquals(partnerId, response.getPartnerId());

        } catch (HunterTechPayException e) {
            System.err.println("❌ Error: " + e.getMessage());
            fail("Payment initiation failed: " + e.getMessage());
        }
    }

    @Test
    public void test02_InitiatePaymentOrange() {
        System.out.println("\n🍊 Test 2: Initiate Orange Payment (Cash In)");
        System.out.println("-".repeat(80));

        try {
            String partnerId = "java-test-orange-" + System.currentTimeMillis();

            PaymentRequest request = PaymentRequest.builder()
                    .phoneNumber(ORANGE_PHONE)
                    .amount(100.0)
                    .currency("XAF")
                    .country("CM")
                    .serviceCode(ORANGE_CASHIN)
                    .partnerId(partnerId)
                    .description("Test payment Orange from Java SDK")
                    .customerName("Test User")
                    .build();

            PaymentResponse response = client.deposit(request);

            System.out.println("✅ Payment initiated successfully!");
            System.out.println("Transaction ID: " + response.getTransactionId());
            System.out.println("Partner ID: " + response.getPartnerId());
            System.out.println("Status: " + response.getStatus());
            System.out.println("Amount: " + response.getAmount() + " " + response.getCurrency());

            assertNotNull(response);
            assertTrue(response.isSuccess());

        } catch (HunterTechPayException e) {
            System.err.println("❌ Error: " + e.getMessage());
            fail("Payment initiation failed: " + e.getMessage());
        }
    }

    @Test
    public void test03_CheckPaymentStatus() {
        System.out.println("\n🔍 Test 3: Check Payment Status");
        System.out.println("-".repeat(80));

        try {
            // First create a payment
            String partnerId = "java-test-status-" + System.currentTimeMillis();

            PaymentRequest request = PaymentRequest.builder()
                    .phoneNumber(MTN_PHONE)
                    .amount(100.0)
                    .currency("XAF")
                    .country("CM")
                    .serviceCode(MTN_CASHIN)
                    .partnerId(partnerId)
                    .description("Test for status check")
                    .build();

            PaymentResponse initResponse = client.deposit(request);
            System.out.println("Payment created: " + initResponse.getTransactionId());

            // Wait a moment
            Thread.sleep(2000);

            // Check status
            PaymentResponse statusResponse = client.checkStatus(partnerId);

            System.out.println("✅ Status retrieved successfully!");
            System.out.println("Transaction ID: " + statusResponse.getTransactionId());
            System.out.println("Status: " + statusResponse.getStatus());
            System.out.println("Amount: " + statusResponse.getAmount() + " " + statusResponse.getCurrency());

            assertNotNull(statusResponse);
            assertEquals(partnerId, statusResponse.getPartnerId());

        } catch (HunterTechPayException | InterruptedException e) {
            System.err.println("❌ Error: " + e.getMessage());
            fail("Status check failed: " + e.getMessage());
        }
    }

    @Test
    public void test04_GetTransactionList() {
        System.out.println("\n📋 Test 4: Get Transaction List");
        System.out.println("-".repeat(80));

        try {
            TransactionListResponse response = client.getTransactions(1, 10);

            System.out.println("✅ Transactions retrieved successfully!");
            System.out.println("Total: " + response.getTotal());
            System.out.println("Page: " + response.getPage());
            System.out.println("Limit: " + response.getLimit());
            System.out.println("Transactions count: " + response.getTransactions().size());

            if (!response.getTransactions().isEmpty()) {
                System.out.println("\nFirst transaction:");
                var tx = response.getTransactions().get(0);
                System.out.println("  ID: " + tx.getId());
                System.out.println("  Status: " + tx.getStatus());
                System.out.println("  Amount: " + tx.getAmount() + " " + tx.getCurrency());
            }

            assertNotNull(response);
            assertTrue(response.isSuccess());

        } catch (HunterTechPayException e) {
            System.err.println("❌ Error: " + e.getMessage());
            fail("Get transactions failed: " + e.getMessage());
        }
    }

    @Test
    public void test05_GetBalance() {
        System.out.println("\n💰 Test 5: Get Account Balance");
        System.out.println("-".repeat(80));

        try {
            BalanceResponse response = client.getBalance("XAF");

            System.out.println("✅ Balance retrieved successfully!");
            System.out.println("Currency: " + response.getCurrency());
            System.out.println("Balance: " + response.getBalance());
            System.out.println("Available: " + response.getAvailableBalance());
            System.out.println("Pending: " + response.getPendingBalance());

            assertNotNull(response);
            assertTrue(response.isSuccess());

        } catch (HunterTechPayException e) {
            System.err.println("❌ Error: " + e.getMessage());
            fail("Get balance failed: " + e.getMessage());
        }
    }

    @Test
    public void test06_VerifyWebhookSignature() {
        System.out.println("\n🔐 Test 6: Verify Webhook Signature");
        System.out.println("-".repeat(80));

        // Simulate webhook payload
        String payload = "{\"event_type\":\"payment.status_changed\",\"transaction_id\":\"txn_123\",\"status\":\"success\"}";
        long timestamp = System.currentTimeMillis() / 1000;

        // Generate signature (simulate what backend would send)
        String signature = com.huntertechpay.security.HMACSignature.generateSignature(
                SECRET_KEY, timestamp, payload
        );

        System.out.println("Payload: " + payload);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Signature: " + signature.substring(0, 20) + "...");

        // Verify signature
        boolean isValid = client.verifyWebhookSignature(payload, timestamp, signature);

        System.out.println("✅ Signature verification: " + (isValid ? "VALID" : "INVALID"));

        assertTrue(isValid, "Webhook signature should be valid");
    }

    @Test
    public void test07_MerchantPaymentMTN() {
        System.out.println("\n🛒 Test 7: Merchant Payment MTN");
        System.out.println("-".repeat(80));

        try {
            String partnerId = "java-test-merchant-mtn-" + System.currentTimeMillis();

            PaymentRequest request = PaymentRequest.builder()
                    .phoneNumber(MTN_PHONE)
                    .amount(500.0)  // 500 FCFA
                    .currency("XAF")
                    .country("CM")
                    .serviceCode(MTN_PAYMENT)
                    .partnerId(partnerId)
                    .description("Test merchant payment MTN")
                    .customerName("Test Merchant Customer")
                    .build();

            PaymentResponse response = client.withdraw(request);

            System.out.println("✅ Merchant payment initiated successfully!");
            System.out.println("Transaction ID: " + response.getTransactionId());
            System.out.println("Status: " + response.getStatus());

            assertNotNull(response);
            assertTrue(response.isSuccess());

        } catch (HunterTechPayException e) {
            System.err.println("❌ Error: " + e.getMessage());
            fail("Merchant payment failed: " + e.getMessage());
        }
    }
}
