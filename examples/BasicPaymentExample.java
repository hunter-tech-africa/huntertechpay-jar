import com.huntertechpay.client.HunterTechPayClient;
import com.huntertechpay.exceptions.HunterTechPayException;
import com.huntertechpay.models.PaymentRequest;
import com.huntertechpay.models.PaymentResponse;

/**
 * Basic example of initiating a payment with HunterTechPay.
 */
public class BasicPaymentExample {

    public static void main(String[] args) {
        // Initialize client
        HunterTechPayClient client = new HunterTechPayClient.Builder()
                .apiKey("your_api_key")
                .secretKey("your_secret_key")
                .build();

        try {
            // Create payment request
            PaymentRequest request = PaymentRequest.builder()
                    .phoneNumber("+237690000000")
                    .amount(5000.0)  // 5000 FCFA
                    .currency("XAF")
                    .country("CM")
                    .serviceCode("CM_OMCMR_CASHOUT")  // Orange Money Cameroon
                    .partnerId("order-" + System.currentTimeMillis())
                    .description("Payment for order #123")
                    .customerName("John Doe")
                    .customerEmail("john@example.com")
                    .webhookUrl("https://yoursite.com/webhooks/payment")
                    .addMetadata("order_id", "123")
                    .addMetadata("product_name", "Premium Subscription")
                    .build();

            // Initiate payment
            System.out.println("Initiating payment...");
            PaymentResponse response = client.initiatePayment(request);

            // Check response
            if (response.isSuccess()) {
                System.out.println("✓ Payment initiated successfully!");
                System.out.println("Transaction ID: " + response.getTransactionId());
                System.out.println("Status: " + response.getStatus());
                System.out.println("Amount: " + response.getAmount() + " " + response.getCurrency());

                // The customer will receive a prompt on their phone to confirm payment
                System.out.println("\nWaiting for customer to confirm payment on their phone...");

                // In production, you would receive webhook notifications about status changes

            } else {
                System.err.println("✗ Payment failed: " + response.getMessage());
                if (response.getErrorCode() != null) {
                    System.err.println("Error code: " + response.getErrorCode());
                }
            }

        } catch (HunterTechPayException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
