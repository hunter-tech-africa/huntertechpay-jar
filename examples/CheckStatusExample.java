import com.huntertechpay.client.HunterTechPayClient;
import com.huntertechpay.exceptions.HunterTechPayException;
import com.huntertechpay.models.PaymentResponse;

/**
 * Example of checking payment status.
 */
public class CheckStatusExample {

    public static void main(String[] args) {
        HunterTechPayClient client = new HunterTechPayClient.Builder()
                .apiKey("your_api_key")
                .secretKey("your_secret_key")
                .build();

        try {
            String partnerId = "order-123";  // Your unique reference

            System.out.println("Checking payment status for: " + partnerId);
            PaymentResponse status = client.checkStatus(partnerId);

            System.out.println("Transaction ID: " + status.getTransactionId());
            System.out.println("Status: " + status.getStatus());
            System.out.println("Amount: " + status.getAmount() + " " + status.getCurrency());

            // Possible statuses:
            // - pending: Customer hasn't confirmed yet
            // - processing: Payment is being processed
            // - success: Payment completed successfully
            // - failed: Payment failed
            // - cancelled: Payment was cancelled

            switch (status.getStatus()) {
                case "success":
                    System.out.println("✓ Payment completed successfully!");
                    break;
                case "pending":
                    System.out.println("⏳ Waiting for customer confirmation...");
                    break;
                case "processing":
                    System.out.println("🔄 Payment is being processed...");
                    break;
                case "failed":
                    System.out.println("✗ Payment failed: " + status.getErrorMessage());
                    break;
                default:
                    System.out.println("Status: " + status.getStatus());
            }

        } catch (HunterTechPayException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
