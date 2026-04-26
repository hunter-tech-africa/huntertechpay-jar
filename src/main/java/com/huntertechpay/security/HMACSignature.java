package com.huntertechpay.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for generating HMAC-SHA512 signatures for API requests.
 *
 * <p>HunterTechPay requires HMAC-SHA512 signatures for all non-GET requests
 * to ensure request authenticity and integrity.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * String signature = HMACSignature.generateSignature(
 *     secretKey,
 *     timestamp,
 *     jsonPayload
 * );
 * }</pre>
 */
public class HMACSignature {

    private static final String HMAC_SHA512 = "HmacSHA512";

    /**
     * Generate HMAC-SHA512 signature for API request.
     *
     * @param secretKey The merchant secret key
     * @param timestamp Unix timestamp in seconds
     * @param payload JSON payload as string
     * @return Hexadecimal signature string
     * @throws SecurityException if signature generation fails
     */
    public static String generateSignature(String secretKey, long timestamp, String payload) {
        try {
            // Create message: {timestamp}.{payload}
            String message = timestamp + "." + payload;

            // Initialize HMAC-SHA512
            Mac mac = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                HMAC_SHA512
            );
            mac.init(secretKeySpec);

            // Generate signature
            byte[] signatureBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

            // Convert to hex
            return bytesToHex(signatureBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SecurityException("Failed to generate HMAC signature", e);
        }
    }

    /**
     * Verify HMAC signature for webhook validation.
     *
     * @param secretKey The merchant secret key
     * @param timestamp Unix timestamp from webhook header
     * @param payload Webhook payload
     * @param expectedSignature Signature from X-Hunter-Signature header
     * @return true if signature is valid
     */
    public static boolean verifySignature(
            String secretKey,
            long timestamp,
            String payload,
            String expectedSignature
    ) {
        String calculatedSignature = generateSignature(secretKey, timestamp, payload);
        return calculatedSignature.equalsIgnoreCase(expectedSignature);
    }

    /**
     * Convert byte array to hexadecimal string.
     *
     * @param bytes Byte array
     * @return Hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Get current Unix timestamp in seconds.
     *
     * @return Current timestamp
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
