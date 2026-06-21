# Quick Start: Publishing to Maven Central (2024+ Method)

**Updated guide using the new Central Portal system**

---

## ⚠️ Important: New System

The old JIRA ticketing system has been **decommissioned**.

**New system**: Central Portal at https://central.sonatype.com

---

## Quick Steps Overview

1. **Create account** on Central Portal (5 min)
2. **Register namespace** `com.huntertechpay` (5 min)
3. **Verify domain** via DNS TXT record (5-30 min)
4. **Generate GPG key** and publish (10 min)
5. **Generate user token** from portal (2 min)
6. **Configure Maven** settings.xml (5 min)
7. **Deploy** with `mvn deploy` (5 min)

**Total time**: ~30-60 minutes (mostly waiting for DNS propagation)

---

## Step 1: Create Central Portal Account (5 min)

1. Visit: https://central.sonatype.com
2. Click **"Sign In"**
3. Choose login method:
   - **GitHub** (recommended) - instant
   - **Google** - instant
   - **Custom username/password** - verify email required

**⚠️ Note**: You cannot change your username later. Choose wisely!

---

## Step 2: Register Namespace (5 min)

1. Log into https://central.sonatype.com
2. Click your **username/email** (top right)
3. Select **"View Namespaces"**
4. Click **"Add Namespace"**
5. Enter: `com.huntertechpay`
6. Click **"Add"**

Status will show: **"Unverified"**

---

## Step 3: Verify Domain Ownership (5-30 min)

### 3.1 Get Verification Key

1. Click **"Verify Namespace"** button
2. Copy the **Verification Key** (unique code shown in portal)

Example key: `abcd1234-efgh-5678-ijkl-9012mnop3456`

### 3.2 Add DNS TXT Record

Go to your DNS provider (where `huntertechpay.com` is hosted):

**Add TXT record**:
- **Host/Name**: `@` or `huntertechpay.com` (root domain)
- **Type**: TXT
- **Value**: `[paste your verification key]`
- **TTL**: 3600 (or default)

**Example in Cloudflare**:
```
Type: TXT
Name: @
Content: abcd1234-efgh-5678-ijkl-9012mnop3456
TTL: Auto
```

**Example in GoDaddy**:
```
Type: TXT
Host: @
TXT Value: abcd1234-efgh-5678-ijkl-9012mnop3456
TTL: 1 Hour
```

### 3.3 Wait for Verification

1. Save the DNS record
2. Wait 5-30 minutes for DNS propagation
3. Central Portal automatically verifies (checks every few minutes)
4. Status will change to: **"Verified"**

**Check DNS propagation**:
```bash
dig +short TXT huntertechpay.com
# Should show your verification key
```

If not verified after 1 hour, contact: central-support@sonatype.com

---

## Step 4: Generate GPG Key (10 min)

### 4.1 Install GPG

**macOS**:
```bash
brew install gnupg
```

**Linux**:
```bash
sudo apt-get install gnupg
```

### 4.2 Generate Key

```bash
gpg --gen-key
```

Enter:
- **Real name**: `HunterTechPay`
- **Email**: `support@huntertechpay.com`
- **Passphrase**: Choose a strong one (save it!)

### 4.3 Get Key ID

```bash
gpg --list-keys
```

Output:
```
pub   rsa3072 2026-04-26 [SC]
      ABCD1234EFGH5678IJKL9012MNOP3456QRST7890
uid           [ultimate] HunterTechPay <support@huntertechpay.com>
```

Copy the **full key ID**: `ABCD1234EFGH5678IJKL9012MNOP3456QRST7890`

Or just the **last 8 characters**: `QRST7890`

### 4.4 Publish Key to Keyservers

```bash
# Replace with your actual key ID
gpg --keyserver keyserver.ubuntu.com --send-keys ABCD1234EFGH5678IJKL9012MNOP3456QRST7890

# Also send to other keyservers
gpg --keyserver keys.openpgp.org --send-keys ABCD1234EFGH5678IJKL9012MNOP3456QRST7890
```

Wait 1-2 hours for key propagation across keyservers.

---

## Step 5: Generate User Token (2 min)

1. Log into https://central.sonatype.com
2. Click your **username/email** (top right)
3. Select **"View Account"**
4. Click **"Generate User Token"**
5. **Copy the credentials immediately**:
   - Token Username: `aBcD1234...` (looks like a hash)
   - Token Password: `xYz5678...` (looks like a hash)

**⚠️ IMPORTANT**: You won't see these again! Save them securely.

---

## Step 6: Configure Maven (5 min)

### 6.1 Create ~/.m2/settings.xml

```bash
mkdir -p ~/.m2
nano ~/.m2/settings.xml
```

### 6.2 Add Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <!-- Central Portal Token (from Step 5) -->
    <server>
      <id>central</id>
      <username>YOUR_TOKEN_USERNAME</username>
      <password>YOUR_TOKEN_PASSWORD</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>gpg</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <!-- GPG Key ID (last 8 characters from Step 4) -->
        <gpg.keyname>QRST7890</gpg.keyname>
        <!-- GPG Passphrase (from Step 4) -->
        <gpg.passphrase>YOUR_GPG_PASSPHRASE</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

**Replace**:
- `YOUR_TOKEN_USERNAME` → Token username from Step 5
- `YOUR_TOKEN_PASSWORD` → Token password from Step 5
- `QRST7890` → Last 8 chars of your GPG key
- `YOUR_GPG_PASSPHRASE` → Your GPG passphrase

### 6.3 Secure the File

```bash
chmod 600 ~/.m2/settings.xml
```

**⚠️ Never commit this file to Git!**

---

## Step 7: Deploy to Maven Central (5 min)

### 7.1 Build and Verify

```bash
cd /Users/huntertech/Desktop/workspace/hunter-tech/huntertechpay-jar
mvn clean verify
```

This compiles, tests, generates javadoc, sources, and signs everything.

### 7.2 Deploy

```bash
mvn deploy
```

**What happens**:
1. Maven builds all artifacts
2. GPG signs everything
3. `central-publishing-maven-plugin` uploads to Central Portal
4. Portal validates and publishes automatically (`autoPublish=true`)
5. Artifacts sync to Maven Central in 10-30 minutes

**Expected output**:
```
[INFO] Central Portal Publisher: Uploading deployment...
[INFO] Central Portal Publisher: Upload successful!
[INFO] Central Portal Publisher: Publishing deployment...
[INFO] Central Portal Publisher: Deployment published successfully
[INFO] BUILD SUCCESS
```

### 7.3 Verify Publication

**Wait 10-30 minutes**, then check:

https://search.maven.org/search?q=g:com.huntertechpay

Or directly:

https://search.maven.org/artifact/com.huntertechpay/huntertechpay-java-sdk/1.0.0/jar

---

## Step 8: Test Installation

Create a test project:

```bash
mkdir /tmp/test-huntertechpay
cd /tmp/test-huntertechpay
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
    <artifactId>test</artifactId>
    <version>1.0</version>

    <dependencies>
        <dependency>
            <groupId>com.huntertechpay</groupId>
            <artifactId>huntertechpay-java-sdk</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
</project>
```

Test:

```bash
mvn dependency:resolve
# Should download huntertechpay-java-sdk successfully
```

---

## Troubleshooting

### "Namespace not verified"
- Wait longer for DNS propagation (up to 1 hour)
- Check DNS record: `dig +short TXT huntertechpay.com`
- Contact: central-support@sonatype.com

### "GPG signing failed"
- Check key ID: `gpg --list-keys`
- Verify passphrase in `~/.m2/settings.xml`
- Test signing: `gpg --export-secret-keys > /dev/null`

### "401 Unauthorized"
- Regenerate token in Central Portal
- Update `~/.m2/settings.xml` with new credentials
- Verify server `<id>central</id>` matches plugin config

### "Public key not found"
- Wait 1-2 hours for keyserver propagation
- Republish key: `gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID`

---

## Version Updates

### Update to version 1.0.1

1. Edit `pom.xml`:
   ```xml
   <version>1.0.1</version>
   ```

2. Commit and tag:
   ```bash
   git add pom.xml
   git commit -m "Bump version to 1.0.1"
   git tag -a v1.0.1 -m "Release 1.0.1"
   git push && git push --tags
   ```

3. Deploy:
   ```bash
   mvn clean deploy
   ```

---

## Summary

**pom.xml is already configured** with:
- ✅ SCM information
- ✅ `central-publishing-maven-plugin`
- ✅ GPG signing plugin
- ✅ Source and Javadoc plugins

**You just need to**:
1. Register on Central Portal
2. Verify domain via DNS
3. Generate GPG key
4. Get user token
5. Configure `~/.m2/settings.xml`
6. Run `mvn deploy`

**Support**:
- Central Portal: https://central.sonatype.com
- Documentation: https://central.sonatype.org/
- Support: central-support@sonatype.com

---

**Last Updated**: April 26, 2026
