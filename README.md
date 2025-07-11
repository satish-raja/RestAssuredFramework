  [![Allure Report](https://img.shields.io/badge/Allure--Report-View-green?logo=allure)](https://satish-raja.github.io/RestAssuredFramework/)
  [![CI](https://github.com/satish-raja/RestAssuredFramework/actions/workflows/run-api-tests.yml/badge.svg)](https://github.com/satish-raja/RestAssuredFramework/actions)
  [![Java](https://img.shields.io/badge/Java-17-blue?logo=java)](https://www.oracle.com/java/)
  [![TestNG](https://img.shields.io/badge/TestNG-7.11.0-orange?logo=testng)](https://testng.org/)
  [![Allure](https://img.shields.io/badge/Allure-2.29.1-ff69b4?logo=allure)](https://docs.qameta.io/allure/)

# 🔬 RestAssured API Automation Framework

A modern, professional-grade API automation framework built with RestAssured, TestNG, Allure, and Log4j2.

Unlike traditional given().when().then()-based approaches, this framework follows a clean Service Object Model (SOM) that separates HTTP logic from tests—ensuring better maintainability, reusability, and readability.

It supports full CRUD testing for the GoRest API, comes with reusable utilities, JSON schema validation, rich Allure reporting, and is fully integrated with CI/CD via GitHub Actions.

---

## 🚀 Key Features

- ✅ Full CRUD API Test Coverage — User, Post, Comment modules  
- 🧱 **Service Object Model (SOM)** — clean separation of concerns between test logic and HTTP execution  
- 📦 Modular Service Layers (`UserService`, `PostService`, etc.) following Page Object principles  
- 🔁 Retry Mechanism with custom logic  
- 🔐 Bearer Token Authentication (via headers)  
- 🏗️ Builder Pattern for flexible JSON payloads  
- 🧪 Multiple TestNG Suites — Smoke, Regression, UAT, etc.  
- 📊 Beautiful Allure Reports (custom filters + step logging)  
- 📄 JSON Schema Validation  
- 📋 Excel-Based Data-Driven Testing  
- 🧼 Log4j2 Logging — Console + File  
- ⚙️ CI/CD Integration via GitHub Actions  

---

## 🧾 Tech Stack

  | Component         | Tool/Library           |
  |------------------|------------------------|
  | API Client       | RestAssured 5.5.5      |
  | Test Framework   | TestNG 7.11.0          |
  | Reporting        | Allure 2.29.1          |
  | Logging          | Log4j2 2.25.0          |
  | Language         | Java 17                |
  | Build Tool       | Maven                  |
  | CI/CD            | GitHub Actions         |
  | JSON Handling    | Jackson / GSON         |
  | Data-Driven      | Apache POI (Excel)     |

---

## 📁 Folder Structure
  ```bash
  RestAssuredFramework/
  ├── src/
  │   ├── main/java/com/api/
  │   │   ├── constants/              # APIConstants, Endpoints
  │   │   ├── filters/                # AllureLoggingFilter
  │   │   ├── listeners/              # RetryAnalyzer, AllureTestListener
  │   │   ├── models/
  │   │   │   ├── request/            # Create/Update payloads
  │   │   │   └── response/           # Response DTOs
  │   │   ├── services/               # BaseService, UserService, etc.
  │   │   └── utils/                  # Logger, Config, Excel, Retry, JSON Schema, etc.
  │   └── resources/
  ├── test/
  │   ├── java/com/api/
  │   │   ├── tests/                  # All CRUD test classes
  │   │   ├── tests/base/             # BaseTest
  │   │   └── tests/security/         # AuthTokenNegativeTests
  │   └── resources/
  │       ├── config/                 # qa.properties, staging.properties, uat.properties
  │       ├── schemas/
  │       │   ├── comment/
  │       │   ├── post/
  │       │   └── user/
  │       ├── allure.properties
  │       └── log4j2.xml
  ├── testng-suites/
  │   ├── restAssuredTestsSuite.xml
  │   ├── smokeTestsSuite.xml
  │   ├── regressionTestsSuite.xml
  │   ├── uatTestsSuite.xml
  │   ├── positiveTestsSuite.xml
  │   ├── negativeTestsSuite.xml
  │   ├── authTestsSuite.xml
  │   └── PostServiceTestsSuite.xml
  ├── .github/workflows/
  │   └── run-api-tests.yml
  ├── allure-results/                # Generated Allure results
  ├── test-output/                   # TestNG output
  ├── pom.xml
  └── README.md
```

---

## 🔐 Configuration & Secrets

> Do **not** commit sensitive data like API tokens or `qa.properties` to Git.

- Copy the template:
  ```bash
  cp src/test/resources/config/qa.properties.template src/test/resources/config/qa.properties
  ```
- Add your actual token to `qa.properties`
- The `qa.properties.template` is version-controlled to provide structure without secrets

✅ Tokens can also be passed via command line: `-Dapi.token=abc123xyz`

---


## 🧪 Running Tests

### 🔁 Run Locally

```bash
# Full Suite
mvn clean test -Papi-tests -Denv=qa

# Smoke Suite
mvn clean test -Psmoke-tests -Denv=qa

📊 Generate Allure Report Locally
allure serve allure-results

```

### 🧪 Maven Profiles (TestNG Suites)
  | Profile ID         | Test Suite Path                           |
  | ------------------ | ----------------------------------------- |
  | api-tests          | `testng-suites/restAssuredTestsSuite.xml` |
  | smoke-tests        | `testng-suites/smokeTestsSuite.xml`       |
  | regression-tests   | `testng-suites/regressionTestsSuite.xml`  |
  | uat-tests          | `testng-suites/uatTestsSuite.xml`         |
  | positive-tests     | `testng-suites/positiveTestsSuite.xml`    |
  | negative-tests     | `testng-suites/negativeTestsSuite.xml`    |
  | auth-tests         | `testng-suites/authTestsSuite.xml`        |
  | post-service-tests | `testng-suites/PostServiceTestsSuite.xml` |

---

## ⚙️ GitHub Actions CI/CD

This framework supports three types of CI triggers:
- ✅ On every push to `main`
- 🕒 Scheduled at 3:00 AM IST daily
- 👤 Manual trigger with inputs:
  - `env` (e.g., `qa`, `staging`)
  - `suite` (e.g., `smoke-tests`)

```bash
# CI build example
mvn clean test -Papi-tests -Denv=qa
```

🔍 [View Action Logs & Artifacts »](https://github.com/satish-raja/RestAssuredFramework/actions)

📘 Allure Report (Live)
👉 [Click to View Allure Report](https://satish-raja.github.io/RestAssuredFramework/)

---

👨‍💻 Author
    Satish Raja
    Freelance API Automation Engineer
    📧 satishraja4u@gmail.com
    🔗 Upwork Profile

📄 License
  This project is licensed under the MIT License.
    ---
