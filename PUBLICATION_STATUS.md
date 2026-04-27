# HunterTechPay SDK Publication Status

Status of all HunterTechPay SDKs across package registries.

---

## ✅ Published SDKs

### 1. JavaScript SDK - npm

**Package Name**: `huntertechpay-sdk`

**Registry**: https://www.npmjs.com/package/huntertechpay-sdk

**Status**: ✅ **Published - v1.0.1**

**Installation**:
```bash
npm install huntertechpay-sdk
```

**Repository**: https://github.com/hunter-tech-africa/huntertechpay-js

**Documentation**: `/Users/huntertech/Desktop/workspace/hunter-tech/huntertechpay-js/README.md`

---

### 2. Python SDK - PyPI

**Package Name**: `huntertechpay`

**Registry (Test)**: https://test.pypi.org/project/huntertechpay/

**Status**: ✅ **Published on TestPyPI - v1.0.0**

**Status (Production)**: ⏳ **Pending** - Not yet published to production PyPI

**Installation (TestPyPI)**:
```bash
pip install --index-url https://test.pypi.org/simple/ --extra-index-url https://pypi.org/simple/ huntertechpay
```

**Installation (Production - After publishing)**:
```bash
pip install huntertechpay
```

**Repository**: https://github.com/hunter-tech-africa/huntertechpay-python

**Documentation**: `/Users/huntertech/Desktop/workspace/hunter-tech/HunterTechPay/sdks/python/README.md`

**Publishing Guide**: `/Users/huntertech/Desktop/workspace/hunter-tech/huntertechpay-js/PYPI_SETUP.md`

**Next Steps**:
1. Test thoroughly on TestPyPI
2. Run: `python -m twine upload dist/*` to publish to production PyPI

---

### 3. PHP SDK - Packagist

**Package Name**: `huntertechpay/sdk`

**Registry**: https://packagist.org/packages/huntertechpay/sdk

**Status**: ✅ **Published - v1.0.1**

**Installation**:
```bash
composer require huntertechpay/sdk
```

**Repository**: https://github.com/hunter-tech-africa/huntertechpay-php

**Documentation**: `/Users/huntertech/Desktop/workspace/hunter-tech/HunterTechPay/sdks/php/README.md`

**Publishing Guide**: `/Users/huntertech/Desktop/workspace/hunter-tech/huntertechpay-php/PACKAGIST_SETUP.md`

**Notes**:
- Initial v1.0.0 tag was not indexed by Packagist
- Created v1.0.1 tag which successfully triggered Packagist indexing
- Webhook should be configured for automatic future updates

---

### 4. Java SDK - Maven Central

**Package Name**: `com.huntertechpay:huntertechpay-java-sdk`

**Registry**: Maven Central (pending publication)

**Status**: ⏳ **Ready to Publish - v1.0.0**

**Installation (After publishing)**:
```xml
<dependency>
    <groupId>com.huntertechpay</groupId>
    <artifactId>huntertechpay-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Repository**: https://github.com/hunter-tech-africa/huntertechpay-jar

**Documentation**: `/Users/huntertech/Desktop/workspace/hunter-tech/huntertechpay-jar/README.md`

**Publishing Guide**: `/Users/huntertech/Desktop/workspace/hunter-tech/huntertechpay-jar/MAVEN_CENTRAL_SETUP.md`

**POM Configuration**: ✅ **Updated** with:
- SCM information
- Distribution management (Sonatype OSSRH)
- GPG signing plugin
- Nexus staging plugin
- Source and Javadoc plugins

**Git Tag**: ✅ **Created and pushed** - v1.0.0

**Build Status**: ✅ **Successful** - `mvn clean package` completed without errors

**Next Steps**:
1. Create Sonatype JIRA account
2. Submit ticket for `com.huntertechpay` group ID claim
3. Verify domain ownership
4. Generate GPG key and publish to keyserver
5. Configure `~/.m2/settings.xml` with credentials
6. Run `mvn clean deploy` to publish

---

## 📊 Publication Summary

| SDK        | Package Registry | Status              | Version | Repository |
|------------|------------------|---------------------|---------|------------|
| JavaScript | npm              | ✅ Published        | 1.0.1   | huntertechpay-js |
| Python     | TestPyPI         | ✅ Published        | 1.0.0   | huntertechpay-python |
| Python     | PyPI (prod)      | ⏳ Pending          | -       | huntertechpay-python |
| PHP        | Packagist        | ✅ Published        | 1.0.1   | huntertechpay-php |
| Java       | Maven Central    | ⏳ Ready to Publish | 1.0.0   | huntertechpay-jar |

---

## 🔗 All Repositories

1. **JavaScript**: https://github.com/hunter-tech-africa/huntertechpay-js
2. **Python**: https://github.com/hunter-tech-africa/huntertechpay-python
3. **PHP**: https://github.com/hunter-tech-africa/huntertechpay-php
4. **Java**: https://github.com/hunter-tech-africa/huntertechpay-jar

---

## 📝 Documentation Updates

All SDK README files have been translated to English for international developers:

- ✅ JavaScript: `/Users/huntertech/Desktop/workspace/hunter-tech/HunterTechPay/sdks/javascript/README.md`
- ✅ Python: `/Users/huntertech/Desktop/workspace/hunter-tech/HunterTechPay/sdks/python/README.md`
- ✅ PHP: `/Users/huntertech/Desktop/workspace/hunter-tech/HunterTechPay/sdks/php/README.md`
- ✅ Java: `/Users/huntertech/Desktop/workspace/hunter-tech/HunterTechPay/sdks/java/README.md`
- ✅ Main: `/Users/huntertech/Desktop/workspace/hunter-tech/HunterTechPay/sdks/README.md`

---

## 🎯 Remaining Tasks

### Python SDK (Production PyPI)

```bash
cd /path/to/huntertechpay-python-sdk

# Clean previous builds
rm -rf dist/ build/ *.egg-info

# Build
python -m build

# Upload to production PyPI
python -m twine upload dist/*
```

### Java SDK (Maven Central)

Follow the complete guide: `MAVEN_CENTRAL_SETUP.md`

**Quick Steps**:
1. Register Sonatype account: https://issues.sonatype.org/secure/Signup!default.jspa
2. Create JIRA ticket for group ID claim
3. Verify domain ownership (DNS TXT or website redirect)
4. Generate GPG key: `gpg --gen-key`
5. Publish GPG key: `gpg --keyserver keyserver.ubuntu.com --send-keys KEY_ID`
6. Configure `~/.m2/settings.xml` with credentials
7. Deploy: `mvn clean deploy`

---

## 🔐 Security Notes

### npm
- Using access token with "bypass 2FA" enabled
- Token stored in `.npmrc` (not committed to Git)
- Package name changed from scoped `@huntertechpay/sdk` to `huntertechpay-sdk`

### PyPI
- Using API tokens for both TestPyPI and production
- Tokens stored in `~/.pypirc` with `chmod 600` permissions
- Never commit `.pypirc` to Git

### Packagist
- Automatic updates via GitHub webhook (recommended to configure)
- Manual updates via package dashboard
- No API tokens required for initial setup

### Maven Central
- GPG signing required for all artifacts
- Sonatype credentials in `~/.m2/settings.xml`
- GPG keys published to public keyservers
- Manual or automatic release via Nexus staging plugin

---

## 🚀 Installation Examples

### JavaScript (Node.js)

```javascript
const HunterTechPay = require('huntertechpay-sdk');

const hunter = new HunterTechPay('api_key', 'secret_key');
```

### Python

```python
from huntertechpay import HunterTechPay

hunter = HunterTechPay('api_key', 'secret_key')
```

### PHP

```php
<?php
require 'vendor/autoload.php';

$hunter = new HunterTechPay('api_key', 'secret_key');
```

### Java

```java
import com.huntertechpay.HunterTechPay;

public class Main {
    public static void main(String[] args) {
        HunterTechPay client = new HunterTechPay("api_key", "secret_key");
    }
}
```

---

## 📞 Support

For issues with SDKs or publication process:
- Email: support@huntertechpay.com
- GitHub Issues: Open issue in respective SDK repository

---

**Last Updated**: April 26, 2026
