# Publishing Java SDK to Maven Central

Guide for publishing `com.huntertechpay:huntertechpay-java-sdk` to Maven Central via **Sonatype Central Portal** (new system as of 2024).

---

## Prerequisites

1. **Maven installed**: Check with `mvn --version`
2. **GPG installed**: For signing artifacts
3. **GitHub repository**: Code must be on GitHub with tags
4. **Access to huntertechpay.com DNS**: For domain verification
5. **GPG key pair**: For signing releases

---

## ⚠️ Important: New Publishing System

**The old JIRA ticketing system (issues.sonatype.org) has been decommissioned.**

Now use: **Central Portal** at https://central.sonatype.com

---

## Step 1: Create Central Portal Account

### 1.1 Register Account

Visit: https://central.sonatype.com

Click **"Sign In"** and choose:
- **Social login**: Google or GitHub (recommended)
- **Custom credentials**: Username and password

**⚠️ Important**:
- Provide a valid email address (you'll need it for verification)
- You **cannot change your username** after creation
- Choose your username carefully!

### 1.2 Verify Email

Check your inbox and verify your email address.

---

## Step 2: Register Namespace (Group ID)

### 2.1 Access Namespace Management

1. Log into https://central.sonatype.com
2. Click your **username/email** in the top right corner
3. Select **"View Namespaces"**
4. Click **"Add Namespace"** button

### 2.2 Request Namespace

1. Enter your namespace: `com.huntertechpay`
2. Click **"Add"**
3. Status will be: **"Unverified"**

### 2.3 Initiate Verification

1. Click **"Verify Namespace"** button
2. Status changes to: **"Verification Pending"**
3. You'll receive a **Verification Key** (unique code)

### 2.4 Verify Domain Ownership via DNS

**Required**: Add a DNS TXT record to `huntertechpay.com`

**Steps**:
1. Go to your DNS provider (where huntertechpay.com is hosted)
2. Add a new **TXT record**:
   - **Host/Name**: `@` or `huntertechpay.com` (root domain)
   - **Value**: `[Your Verification Key from Central Portal]`
   - **TTL**: 3600 (or default)

**Example**:
```
Name:  huntertechpay.com
Type:  TXT
Value: abcd1234-efgh-5678-ijkl-9012mnop3456
```

**⚠️ Critical**: The automated system checks the **exact domain** (`huntertechpay.com`), not subdomains like `maven-central.huntertechpay.com`.

### 2.5 Wait for Verification

1. Save the DNS record
2. Wait for DNS propagation (5-30 minutes)
3. Central Portal automatically checks your DNS
4. Verification typically completes in **a few minutes**

If it takes longer than 1 hour, contact Central Support: central-support@sonatype.com

### 2.6 Confirmation

Once verified, namespace status becomes: **"Verified"**

You can now publish artifacts under `com.huntertechpay.*`

---

## Step 3: Generate User Token

### 3.1 Access Token Generation

1. Log into https://central.sonatype.com
2. Click your **username/email** in the top right
3. Select **"View Account"**
4. Click **"Generate User Token"** button

### 3.2 Save Token Credentials

You'll receive:
- **Username**: (token username - looks like a hash)
- **Password**: (token password - looks like a hash)

**⚠️ IMPORTANT**: Copy these immediately! You won't see them again.

---

## Step 4: Generate GPG Key

Maven Central requires all artifacts to be signed with GPG.

### 4.1 Install GPG

**macOS**:
```bash
brew install gnupg
```

**Linux**:
```bash
sudo apt-get install gnupg
# or
sudo yum install gnupg
```

### 4.2 Generate Key Pair

```bash
gpg --gen-key
```

Fill in:
- **Real name**: `HunterTechPay`
- **Email**: `support@huntertechpay.com`
- **Passphrase**: Choose a strong passphrase (you'll need this later)

### 4.3 List Keys

```bash
gpg --list-keys
```

Output:
```
pub   rsa3072 2026-04-26 [SC] [expires: 2028-04-26]
      ABCD1234EFGH5678IJKL9012MNOP3456QRST7890
uid           [ultimate] HunterTechPay <support@huntertechpay.com>
sub   rsa3072 2026-04-26 [E] [expires: 2028-04-26]
```

Copy the key ID (e.g., `ABCD1234EFGH5678IJKL9012MNOP3456QRST7890`)

### 4.4 Publish Public Key to Keyserver

```bash
# Replace with your actual key ID
gpg --keyserver keyserver.ubuntu.com --send-keys ABCD1234EFGH5678IJKL9012MNOP3456QRST7890

# Also send to other keyservers
gpg --keyserver keys.openpgp.org --send-keys ABCD1234EFGH5678IJKL9012MNOP3456QRST7890
gpg --keyserver pgp.mit.edu --send-keys ABCD1234EFGH5678IJKL9012MNOP3456QRST7890
```

**Important**: It may take a few hours for keys to propagate across keyservers.

---

## Step 5: Update pom.xml for Central Portal

### 5.1 Add Central Publishing Plugin

The pom.xml has already been updated with the necessary plugins. Verify it contains:

```xml
<plugin>
  <groupId>org.sonatype.central</groupId>
  <artifactId>central-publishing-maven-plugin</artifactId>
  <version>0.6.0</version>
  <extensions>true</extensions>
  <configuration>
    <publishingServerId>central</publishingServerId>
    <autoPublish>true</autoPublish>
  </configuration>
</plugin>
```

**Configuration options**:
- `autoPublish=true`: Automatically publish after upload (recommended for CI/CD)
- `autoPublish=false`: Manual publishing via Central Portal web interface

---

## Step 6: Configure Maven Settings

### 6.1 Create/Edit ~/.m2/settings.xml

```bash
mkdir -p ~/.m2
nano ~/.m2/settings.xml
```

Add:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <!-- Central Portal Credentials (from Step 3) -->
    <server>
      <id>central</id>
      <username>36IIYZ</username>
      <password>Ivl8mmZg4ov3cH2igTYfMecEaN6VhrN0v</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>central-publishing</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <!-- GPG Key ID (last 8 characters) -->
        <gpg.keyname>1D5D18AA</gpg.keyname>
        <!-- GPG Passphrase -->
        <gpg.passphrase>hunter@</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

**⚠️ IMPORTANT**:
- Replace `YOUR_TOKEN_USERNAME` with your token username from Step 3
- Replace `YOUR_TOKEN_PASSWORD` with your token password from Step 3
- Replace `QRST7890` with the last 8 chars of your GPG key
- Replace `YOUR_GPG_PASSPHRASE` with your actual GPG passphrase
- Never commit this file to Git!

### 6.2 Secure the File

```bash
chmod 600 ~/.m2/settings.xml
```

---

## Step 7: Build and Deploy

### 4.1 Clean Build

```bash
cd /Users/huntertech/Desktop/workspace/hunter-tech/huntertechpay-jar
mvn clean verify
```

This will:
- Compile the code
- Run tests
- Generate javadoc
- Generate sources JAR
- Sign all artifacts with GPG

### 4.2 Deploy to Sonatype Staging

```bash
mvn clean deploy
```

If successful, you'll see:
```
[INFO] Performing deferred deploys (gathering into "...)
[INFO]  * com.huntertechpay:huntertechpay-java-sdk:1.0.0
[INFO] Remote deploy finished with success
[INFO] BUILD SUCCESS
```

### 4.3 Release to Maven Central

The `nexus-staging-maven-plugin` is configured with `autoReleaseAfterClose=true`, which means artifacts will automatically be released to Maven Central after staging validation passes.

If you want manual control, set `autoReleaseAfterClose=false` in pom.xml and run:

```bash
mvn nexus-staging:release
```

---

## Step 5: Verify Publication

### 5.1 Check on Maven Central

It takes **10-30 minutes** for artifacts to appear on Maven Central search.

Visit: https://search.maven.org/search?q=g:com.huntertechpay

Or directly: https://search.maven.org/artifact/com.huntertechpay/huntertechpay-java-sdk/1.0.0/jar

### 5.2 Test Installation

Create a test project:

```bash
mkdir -p /tmp/test-huntertechpay-java
cd /tmp/test-huntertechpay-java
```

Create `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>test-app</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.huntertechpay</groupId>
            <artifactId>huntertechpay-java-sdk</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
</project>
```

Install:

```bash
mvn clean install
```

Create test file `src/main/java/TestSDK.java`:

```java
import com.huntertechpay.HunterTechPay;

public class TestSDK {
    public static void main(String[] args) {
        HunterTechPay client = new HunterTechPay("api_key", "secret_key");
        System.out.println("✅ HunterTechPay SDK loaded successfully!");
    }
}
```

Compile and run:

```bash
mvn compile exec:java -Dexec.mainClass="TestSDK"
```

---

## Version Updates

### 1. Update version in pom.xml

```xml
<version>1.0.1</version>
```

### 2. Commit changes

```bash
git add pom.xml
git commit -m "Bump version to 1.0.1"
git push
```

### 3. Create Git tag

```bash
git tag -a v1.0.1 -m "Release version 1.0.1"
git push origin v1.0.1
```

### 4. Deploy new version

```bash
mvn clean deploy
```

---

## Troubleshooting

### Error: GPG signing failed

**Cause**: GPG key not found or passphrase incorrect

**Solution**:
```bash
# List keys
gpg --list-keys

# Verify passphrase
gpg --export-secret-keys > /dev/null

# Update ~/.m2/settings.xml with correct keyname and passphrase
```

### Error: 401 Unauthorized

**Cause**: Incorrect Sonatype credentials

**Solution**: Update `~/.m2/settings.xml` with correct username/password from JIRA

### Error: 403 Forbidden - No permission to deploy

**Cause**: Your JIRA ticket not approved yet, or wrong group ID

**Solution**:
- Wait for Sonatype approval (check JIRA ticket status)
- Ensure Group ID in pom.xml matches approved group ID

### Error: Public key not found on keyserver

**Cause**: GPG key not published or not propagated yet

**Solution**:
```bash
# Republish key
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID

# Wait 1-2 hours for propagation
```

### Error: Nexus staging failed validation

**Cause**: Missing required artifacts (sources, javadoc) or POM issues

**Solution**:
- Ensure `maven-source-plugin` and `maven-javadoc-plugin` are configured
- Run `mvn clean verify` locally first
- Check Nexus staging repository for detailed error messages

---

## Manual Release Process (Alternative)

If `autoReleaseAfterClose=false`:

### 1. Deploy to Staging

```bash
mvn clean deploy
```

### 2. Login to Nexus Repository Manager

Visit: https://s01.oss.sonatype.org/

Login with your Sonatype credentials.

### 3. Find Staging Repository

1. Click **"Staging Repositories"** (left sidebar)
2. Search for `comhuntertechpay-XXXX`
3. Select your repository

### 4. Close Repository

1. Click **"Close"** button
2. Wait for validation (5-10 minutes)
3. If validation fails, read error messages and fix

### 5. Release Repository

1. Select the closed repository
2. Click **"Release"** button
3. Artifacts will sync to Maven Central in 10-30 minutes

---

## Security Best Practices

1. **Never commit credentials to Git**
   ```bash
   # .gitignore
   .m2/settings.xml
   ```

2. **Use encrypted passwords** in settings.xml:
   ```bash
   mvn --encrypt-master-password YOUR_MASTER_PASSWORD
   mvn --encrypt-password YOUR_SONATYPE_PASSWORD
   ```

3. **Rotate GPG keys** every 2-3 years

4. **Keep GPG passphrase secure** - Store in password manager

5. **Use different credentials** for CI/CD (create dedicated token)

---

## Quick Reference

```bash
# Generate GPG key
gpg --gen-key

# List GPG keys
gpg --list-keys

# Publish GPG key
gpg --keyserver keyserver.ubuntu.com --send-keys KEY_ID

# Build and verify
mvn clean verify

# Deploy to Maven Central
mvn clean deploy

# Manual release (if autoRelease disabled)
mvn nexus-staging:release

# Check build
mvn dependency:tree
```

---

## CI/CD Integration (GitHub Actions)

Create `.github/workflows/maven-publish.yml`:

```yaml
name: Publish to Maven Central

on:
  release:
    types: [created]

jobs:
  publish:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
        server-id: ossrh
        server-username: MAVEN_USERNAME
        server-password: MAVEN_PASSWORD
        gpg-private-key: ${{ secrets.GPG_PRIVATE_KEY }}
        gpg-passphrase: MAVEN_GPG_PASSPHRASE

    - name: Build and publish
      run: mvn -B clean deploy
      env:
        MAVEN_USERNAME: ${{ secrets.OSSRH_USERNAME }}
        MAVEN_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}
        MAVEN_GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
```

### Required GitHub Secrets

1. `OSSRH_USERNAME` - Your Sonatype username
2. `OSSRH_PASSWORD` - Your Sonatype password
3. `GPG_PRIVATE_KEY` - Export with: `gpg --export-secret-keys --armor KEY_ID`
4. `GPG_PASSPHRASE` - Your GPG passphrase

---

## Support

- **Sonatype OSSRH Guide**: https://central.sonatype.org/publish/publish-guide/
- **Maven Central Portal**: https://central.sonatype.com/
- **Report Issues**: support@huntertechpay.com

---

**Last Updated**: April 26, 2026
