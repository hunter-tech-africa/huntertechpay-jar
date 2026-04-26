import com.huntertechpay.client.HunterTechPayClient;
import com.huntertechpay.models.KYCVerificationRequest;
import com.huntertechpay.models.KYCVerificationResponse;
import com.huntertechpay.exceptions.HunterTechPayException;

/**
 * Example of KYC verification using HunterTechPay Java SDK
 */
public class KYCExample {

    public static void main(String[] args) {
        // Configuration
        String apiKey = "htp_live_6f09";
        String secretKey = "sk_live_0825";
        String baseUrl = "https://localhost:8008";

        // Test data
        String testPhone = "670XXXXXX";
        String testCountry = "CM";
        String testProvider = "mtn_cm";

        System.out.println();
        System.out.println("╔" + "=".repeat(78) + "╗");
        System.out.println("║" + " ".repeat(24) + "TEST KYC - SDK JAVA" + " ".repeat(35) + "║");
        System.out.println("╚" + "=".repeat(78) + "╝");
        System.out.println();

        try {
            // Initialize client (with SSL disabled for local testing)
            okhttp3.OkHttpClient httpClient = new okhttp3.OkHttpClient.Builder()
                .hostnameVerifier((hostname, session) -> true)
                .sslSocketFactory(
                    createTrustAllSslContext().getSocketFactory(),
                    new javax.net.ssl.X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
                    }
                )
                .build();

            HunterTechPayClient client = new HunterTechPayClient.Builder()
                .apiKey(apiKey)
                .secretKey(secretKey)
                .baseUrl(baseUrl)
                .httpClient(httpClient)
                .build();

            System.out.println("=".repeat(80));
            System.out.println("  TEST KYC - SDK Java");
            System.out.println("=".repeat(80));
            System.out.println();

            System.out.println("  📋 Test: Vérification KYC pour " + testPhone);
            System.out.println("  Provider: " + testProvider);
            System.out.println("  Country: " + testCountry);
            System.out.println();

            // Build KYC request
            KYCVerificationRequest request = KYCVerificationRequest.builder()
                .phoneNumber(testPhone)
                .country(testCountry)
                .providerCode(testProvider)
                .partnerId("test_kyc_java_" + System.currentTimeMillis())
                .build();

            // Call KYC API
            KYCVerificationResponse kycResult = client.kyc(request);

            // Display results
            System.out.println("  ✅ Verification ID: " + kycResult.getVerificationId());
            System.out.println("  ✅ Status: " + kycResult.getStatus());
            System.out.println("  ✅ Phone: " + kycResult.getPhoneNumber());
            System.out.println("  ✅ Country: " + kycResult.getCountryCode());
            System.out.println("  ✅ Provider: " + kycResult.getProviderCode());

            if (kycResult.getKycData() != null && !kycResult.getKycData().isEmpty()) {
                System.out.println();
                System.out.println("  📄 KYC Data:");
                kycResult.getKycData().forEach((key, value) ->
                    System.out.println("     - " + key + ": " + value)
                );
            }

            if (kycResult.getVerifiedAt() != null) {
                System.out.println();
                System.out.println("  ⏰ Verified at: " + kycResult.getVerifiedAt());
            }

            System.out.println();
            System.out.println("  🎉 TEST KYC RÉUSSI!");
            System.out.println();
            System.out.println("=".repeat(80));
            System.out.println("  ✅ Tous les tests ont réussi!");
            System.out.println("=".repeat(80));
            System.out.println();

        } catch (HunterTechPayException e) {
            System.err.println("  ❌ ERREUR: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
            System.out.println("=".repeat(80));
            System.out.println("  ❌ Des erreurs sont survenues");
            System.out.println("=".repeat(80));
            System.out.println();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("  ❌ ERREUR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static javax.net.ssl.SSLContext createTrustAllSslContext() throws Exception {
        javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
        sslContext.init(null, new javax.net.ssl.TrustManager[] {
            new javax.net.ssl.X509TrustManager() {
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[]{}; }
            }
        }, new java.security.SecureRandom());
        return sslContext;
    }
}
