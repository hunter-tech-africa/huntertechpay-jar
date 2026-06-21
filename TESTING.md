# Testing HunterTechPay Java SDK

Guide for running tests with the SDK.

---

## Test Types

### 1. Integration Tests (Excluded by Default)

**File**: `src/test/java/com/huntertechpay/IntegrationTest.java`

**Status**: ⚠️ Excluded by default (requires running API server)

**Tests included**:
- ✅ Initiate MTN Payment
- ✅ Initiate Orange Payment
- ✅ Check Payment Status
- ✅ Get Transaction List
- ✅ Get Account Balance
- ✅ Verify Webhook Signature
- ✅ Merchant Payment MTN

**Why excluded**: These tests require a running HunterTechPay API server on `localhost:8008`.

---

## Running Tests

### Build Without Tests (Default)

```bash
mvn clean verify
```

Integration tests are **automatically excluded** during normal builds.

### Build With Tests Skipped Explicitly

```bash
mvn clean verify -DskipTests
```

### Run Only Unit Tests (if any)

```bash
mvn test
```

Currently no unit tests, only integration tests which are excluded.

### Run Integration Tests (Requires Server)

**Prerequisites**:
1. HunterTechPay API server running on `localhost:8008`
2. Valid API credentials in test configuration

**Run**:
```bash
# Temporarily include integration tests
mvn test -Dsurefire.excludes=""
```

Or modify `pom.xml` to remove the `<excludes>` section in `maven-surefire-plugin`.

---

## Test Configuration

Integration tests use:
- **Base URL**: `http://localhost:8008`
- **API Key**: Test key (configured in test)
- **Secret Key**: Test secret (configured in test)

### Running Against Real API

To test against production or staging:

1. Edit `src/test/java/com/huntertechpay/IntegrationTest.java`
2. Change base URL:
   ```java
   private static final String BASE_URL = "https://api.huntertechpay.com";
   ```
3. Use real credentials:
   ```java
   private static final String API_KEY = "your_real_api_key";
   private static final String SECRET_KEY = "your_real_secret_key";
   ```
4. Run tests:
   ```bash
   mvn test -Dsurefire.excludes=""
   ```

**⚠️ Warning**: Real tests will create actual transactions!

---

## Publishing to Maven Central

For Maven Central publication, tests are **automatically excluded** because:
- Integration tests require external dependencies (API server)
- Maven Central build process happens in isolated environment
- Only compilation verification is needed

The build will succeed with:
```bash
mvn clean verify
# or
mvn clean deploy
```

---

## Adding Unit Tests (Future)

To add unit tests that don't require external dependencies:

1. Create test class without `IntegrationTest` suffix:
   ```java
   src/test/java/com/huntertechpay/HMACSignatureTest.java
   ```

2. Write tests using mocking:
   ```java
   @Test
   public void testSignatureGeneration() {
       // Test HMAC signature logic without network calls
   }
   ```

3. These will run by default:
   ```bash
   mvn test
   ```

---

## Test Coverage

Currently covered:
- ✅ Payment initiation (MTN, Orange)
- ✅ Transaction status checks
- ✅ Transaction listing
- ✅ Balance retrieval
- ✅ Webhook signature verification
- ✅ Merchant payments

Not yet covered (future):
- ❌ Error handling edge cases
- ❌ Network retry logic
- ❌ HMAC signature generation (unit test needed)
- ❌ Request/response serialization

---

## CI/CD Integration

### GitHub Actions Example

```yaml
name: Test Java SDK

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'

    - name: Build and verify
      run: mvn clean verify

    # Integration tests require API server
    # - name: Run integration tests
    #   run: mvn test -Dsurefire.excludes=""
    #   env:
    #     API_KEY: ${{ secrets.HUNTERTECHPAY_API_KEY }}
    #     SECRET_KEY: ${{ secrets.HUNTERTECHPAY_SECRET_KEY }}
```

---

## Quick Reference

```bash
# Standard build (integration tests excluded)
mvn clean verify

# Package without tests
mvn clean package -DskipTests

# Run integration tests (requires server)
mvn test -Dsurefire.excludes=""

# Deploy to Maven Central (tests excluded automatically)
mvn clean deploy
```

---

**Last Updated**: April 27, 2026
