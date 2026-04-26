import com.huntertechpay.client.HunterTechPayClient;
import com.huntertechpay.exceptions.HunterTechPayException;
import com.huntertechpay.models.WebhookEvent;

/**
 * Example of handling webhook notifications.
 *
 * This example shows how to verify and process webhook events
 * in a Spring Boot controller or servlet.
 */
public class WebhookExample {

    private static final HunterTechPayClient client = new HunterTechPayClient.Builder()
            .apiKey("your_api_key")
            .secretKey("your_secret_key")
            .build();

    /**
     * Handle incoming webhook POST request.
     *
     * @param payload Raw JSON payload from request body
     * @param signature X-Hunter-Signature header value
     * @param timestamp X-Hunter-Timestamp header value
     */
    public static void handleWebhook(String payload, String signature, String timestamp) {
        try {
            // 1. Verify webhook signature
            long timestampLong = Long.parseLong(timestamp);
            boolean isValid = client.verifyWebhookSignature(payload, timestampLong, signature);

            if (!isValid) {
                System.err.println("⚠️  Invalid webhook signature! Possible security issue.");
                // Return 401 Unauthorized
                return;
            }

            System.out.println("✓ Webhook signature verified");

            // 2. Parse webhook event
            WebhookEvent event = client.parseWebhookEvent(payload);

            // 3. Process event based on type and status
            System.out.println("Event Type: " + event.getEventType());
            System.out.println("Transaction ID: " + event.getTransactionId());
            System.out.println("Partner ID: " + event.getPartnerId());
            System.out.println("Status: " + event.getStatus());
            System.out.println("Previous Status: " + event.getPreviousStatus());

            // Handle different statuses
            switch (event.getStatus()) {
                case "success":
                    handleSuccessfulPayment(event);
                    break;
                case "failed":
                    handleFailedPayment(event);
                    break;
                case "pending":
                    handlePendingPayment(event);
                    break;
                default:
                    System.out.println("Unhandled status: " + event.getStatus());
            }

            // Return 200 OK to acknowledge receipt

        } catch (HunterTechPayException e) {
            System.err.println("Error processing webhook: " + e.getMessage());
            e.printStackTrace();
            // Return 500 Internal Server Error
        }
    }

    private static void handleSuccessfulPayment(WebhookEvent event) {
        System.out.println("✓ Payment successful!");
        System.out.println("Amount: " + event.getAmount() + " " + event.getCurrency());

        // Update your database
        // Send confirmation email to customer
        // Deliver digital goods
        // etc.

        String orderId = (String) event.getMetadata().get("order_id");
        if (orderId != null) {
            System.out.println("Fulfilling order: " + orderId);
            // fulfillOrder(orderId);
        }
    }

    private static void handleFailedPayment(WebhookEvent event) {
        System.out.println("✗ Payment failed");
        System.out.println("Error: " + event.getErrorMessage());
        System.out.println("Error Code: " + event.getErrorCode());

        // Update your database
        // Notify customer of failure
        // Possibly retry payment
    }

    private static void handlePendingPayment(WebhookEvent event) {
        System.out.println("⏳ Payment still pending");

        // Update status in your database
        // Maybe send reminder to customer
    }

    // Example Spring Boot controller
    /*
    @RestController
    @RequestMapping("/webhooks")
    public class WebhookController {

        @PostMapping("/payment")
        public ResponseEntity<String> handlePaymentWebhook(
                @RequestBody String payload,
                @RequestHeader("X-Hunter-Signature") String signature,
                @RequestHeader("X-Hunter-Timestamp") String timestamp
        ) {
            WebhookExample.handleWebhook(payload, signature, timestamp);
            return ResponseEntity.ok("Webhook received");
        }
    }
    */
}
