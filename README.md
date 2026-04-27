# HunterTechPay Java SDK

Official Java SDK for HunterTechPay Mobile Money API - Accept mobile money payments in Africa (CEMAC & UEMOA).

[![Maven Central](https://img.shields.io/maven-central/v/com.huntertechpay/huntertechpay-java-sdk)](https://central.sonatype.com/artifact/com.huntertechpay/huntertechpay-java-sdk)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/java-11%2B-blue)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

## Features

- ✅ **HMAC-SHA512 Signature** - Secure API authentication
- 💰 **Mobile Money Payments** - Orange Money, MTN MoMo, Wave, Free Money
- 🌍 **10 African Countries** - Cameroon, Senegal, Ivory Coast, Burkina Faso, etc.
- 🔔 **Webhook Support** - Real-time payment notifications
- 🛡️ **Type-Safe** - Full Java type safety with models
- 📦 **Zero Configuration** - Works out of the box

## Supported Countries & Providers

| Country | Currency | Providers |
|---------|----------|-----------|
| 🇨🇲 Cameroon | XAF | Orange Money, MTN MoMo |
| 🇸🇳 Senegal | XOF | Orange Money, Wave, Free Money |
| 🇨🇮 Ivory Coast | XOF | Orange Money, MTN, Moov |
| 🇧🇫 Burkina Faso | XOF | Orange Money, Moov |
| 🇹🇬 Togo | XOF | Orange Money, Moov |
| 🇧🇯 Benin | XOF | MTN, Moov |
| 🇲🇱 Mali | XOF | Orange Money |
| 🇳🇪 Niger | XOF | Orange Money |

## Installation

### Maven

```xml
<dependency>
    <groupId>com.huntertechpay</groupId>
    <artifactId>huntertechpay-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.huntertechpay:huntertechpay-java-sdk:1.0.0'
```

### Manual Installation

```bash
git clone https://github.com/hunter-tech-africa/huntertechpay-jar.git
cd huntertechpay-jar
mvn clean install
```

## Quick Start

### 1. Get Your API Credentials

Visit [HunterTechPay Dashboard](https://huntertechpay.com/dashboard) to get:
- `API Key` - Your public identifier
- `Secret Key` - Your private key for signing requests

### 2. Initialize Client

```java
import com.huntertechpay.client.HunterTechPayClient;

HunterTechPayClient client = new HunterTechPayClient.Builder()
    .apiKey("your_api_key")
    .secretKey("your_secret_key")
    .build();
```

### 3. Initiate Payment

```java
import com.huntertechpay.models.PaymentRequest;
import com.huntertechpay.models.PaymentResponse;
import com.huntertechpay.exceptions.HunterTechPayException;

try {
    PaymentRequest request = PaymentRequest.builder()
        .phoneNumber("+237690000000")
        .amount(5000.0)
        .currency("XAF")
        .country("CM")
        .serviceCode("CM_OMCMR_CASHOUT")
        .partnerId("order-123")
        .description("Payment for order #123")
        .webhookUrl("https://yoursite.com/webhooks/payment")
        .build();

    PaymentResponse response = client.initiatePayment(request);

    if (response.isSuccess()) {
        System.out.println("Transaction ID: " + response.getTransactionId());
        System.out.println("Status: " + response.getStatus());
    }
} catch (HunterTechPayException e) {
    System.err.println("Error: " + e.getMessage());
}
```

## Service Codes

Service codes identify the mobile money provider and operation type.

### Format

```
{COUNTRY}_{PROVIDER}_{OPERATION}
```


## API Reference

### Initiate Payment

```java
PaymentRequest request = PaymentRequest.builder()
    .phoneNumber("+237690000000")        // Required: Customer phone
    .amount(5000.0)                      // Required: Amount
    .currency("XAF")                     // Required: XAF or XOF
    .country("CM")                       // Required: ISO country code
    .serviceCode("CM_OMCMR_CASHOUT")     // Required: Service code
    .partnerId("unique-id-123")          // Required: Your unique reference
    .description("Order #123")           // Optional: Description
    .customerName("John Doe")            // Optional: Customer name
    .customerEmail("john@example.com")   // Optional: Customer email
    .webhookUrl("https://...")           // Optional: Webhook URL
    .addMetadata("key", "value")         // Optional: Custom data
    .build();

PaymentResponse response = client.initiatePayment(request);
```

### Check Payment Status

```java
PaymentResponse status = client.checkStatus("your-partner-id");

System.out.println("Status: " + status.getStatus());
// Possible values: pending, processing, success, failed, cancelled
```

### Get Transaction List

```java
TransactionListResponse transactions = client.getTransactions(1, 50);

for (Transaction tx : transactions.getTransactions()) {
    System.out.println(tx.getId() + ": " + tx.getStatus());
}
```

### Get Account Balance

```java
BalanceResponse balance = client.getBalance("XAF");

System.out.println("Balance: " + balance.getBalance());
System.out.println("Available: " + balance.getAvailableBalance());
```

## Webhooks

Receive real-time notifications when payment status changes.

### 1. Verify Webhook Signature

```java
public void handleWebhook(String payload, String signature, String timestamp) {
    long timestampLong = Long.parseLong(timestamp);

    if (!client.verifyWebhookSignature(payload, timestampLong, signature)) {
        // Invalid signature - reject request
        return;
    }

    // Signature valid - process webhook
    WebhookEvent event = client.parseWebhookEvent(payload);
    processPaymentUpdate(event);
}
```

### 2. Spring Boot Webhook Controller

```java
@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    @Autowired
    private HunterTechPayClient client;

    @PostMapping("/payment")
    public ResponseEntity<String> handlePayment(
            @RequestBody String payload,
            @RequestHeader("X-Hunter-Signature") String signature,
            @RequestHeader("X-Hunter-Timestamp") String timestamp
    ) {
        try {
            // Verify signature
            long ts = Long.parseLong(timestamp);
            if (!client.verifyWebhookSignature(payload, ts, signature)) {
                return ResponseEntity.status(401).body("Invalid signature");
            }

            // Parse and process event
            WebhookEvent event = client.parseWebhookEvent(payload);

            switch (event.getStatus()) {
                case "success":
                    fulfillOrder(event.getPartnerId());
                    break;
                case "failed":
                    handleFailure(event);
                    break;
            }

            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
```

### Webhook Event Structure

```json
{
  "event_type": "payment.status_changed",
  "transaction_id": "txn_123...",
  "partner_id": "your-reference-id",
  "status": "success",
  "previous_status": "pending",
  "amount": 5000.0,
  "currency": "XAF",
  "phone_number": "+237690000000",
  "service_code": "CM_OMCMR_CASHOUT",
  "timestamp": "2024-01-15T10:30:00Z",
  "metadata": {
    "order_id": "123"
  }
}
```

## Error Handling

```java
try {
    PaymentResponse response = client.initiatePayment(request);

} catch (AuthenticationException e) {
    // Invalid API key or signature
    System.err.println("Auth error: " + e.getMessage());

} catch (ValidationException e) {
    // Invalid request parameters
    System.err.println("Validation error: " + e.getMessage());
    System.err.println("Error code: " + e.getErrorCode());

} catch (HunterTechPayException e) {
    // Other API errors
    System.err.println("API error: " + e.getMessage());
    System.err.println("Status code: " + e.getStatusCode());
}
```

## Advanced Configuration

### Custom HTTP Client

```java
OkHttpClient customClient = new OkHttpClient.Builder()
    .connectTimeout(Duration.ofSeconds(60))
    .readTimeout(Duration.ofSeconds(60))
    .addInterceptor(new LoggingInterceptor())
    .build();

HunterTechPayClient client = new HunterTechPayClient.Builder()
    .apiKey("your_api_key")
    .secretKey("your_secret_key")
    .httpClient(customClient)
    .build();
```

### Custom Base URL (Testing)

```java
HunterTechPayClient client = new HunterTechPayClient.Builder()
    .apiKey("test_key")
    .secretKey("test_secret")
    .baseUrl("https://api.huntertechpay.com")
    .build();
```

## Testing

Run unit tests:

```bash
mvn test
```

Run integration tests:

```bash
mvn verify -Pintegration-tests
```

## Examples

See the [examples](examples/) directory for complete working examples:

- [BasicPaymentExample.java](examples/BasicPaymentExample.java) - Basic payment flow
- [CheckStatusExample.java](examples/CheckStatusExample.java) - Check payment status
- [WebhookExample.java](examples/WebhookExample.java) - Handle webhooks

## Security Best Practices

1. **Never commit secrets** - Keep `secretKey` in environment variables
2. **Verify webhooks** - Always verify signature before processing
3. **Use HTTPS** - Only accept webhooks over HTTPS
4. **Validate amounts** - Always validate payment amounts match expected values
5. **Idempotency** - Use unique `partnerId` for each payment

## Environment Variables

```bash
export HUNTERTECHPAY_API_KEY="your_api_key"
export HUNTERTECHPAY_SECRET_KEY="your_secret_key"
```

```java
String apiKey = System.getenv("HUNTERTECHPAY_API_KEY");
String secretKey = System.getenv("HUNTERTECHPAY_SECRET_KEY");

HunterTechPayClient client = new HunterTechPayClient.Builder()
    .apiKey(apiKey)
    .secretKey(secretKey)
    .build();
```

## Support

- 📧 Email: support@huntertechpay.com
- 📚 Documentation: https://huntertechpay.com/merchant-api/documentation
- 🐛 Issues: https://github.com/hunter-tech-africa/huntertechpay-jar/issues

## License

MIT License - see [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please read our [Contributing Guide](CONTRIBUTING.md) first.

---

Made with ❤️ by [HunterTech](https://huntertechpay.com)
