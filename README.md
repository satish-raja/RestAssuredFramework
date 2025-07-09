[![Allure Report](https://img.shields.io/badge/Allure--Report-View-green?logo=allure)](https://satish-raja.github.io/RestAssuredFramework/)
[![CI](https://github.com/satish-raja/RestAssuredFramework/actions/workflows/run-api-tests.yml/badge.svg)](https://github.com/satish-raja/RestAssuredFramework/actions)
[![Java](https://img.shields.io/badge/Java-17-blue?logo=java)](https://www.oracle.com/java/)
[![TestNG](https://img.shields.io/badge/TestNG-7.11.0-orange?logo=testng)](https://testng.org/)
[![Allure](https://img.shields.io/badge/Allure-2.29.1-ff69b4?logo=allure)](https://docs.qameta.io/allure/)

# 🔬 RestAssured API Automation Framework

A scalable, modular, and extensible API automation framework for testing RESTful APIs using **RestAssured**, **TestNG**, and **Allure**. This framework is tailored for testing **GoRest API** and supports full CRUD operations with modern test design patterns and CI integration.

---

## 🚀 Key Features

- ✅ **CRUD API Test Coverage**: User, Post, Comment modules
- 🔁 **Retry Mechanism** with configurable logic
- 🔐 **Bearer Token Management** via dynamic Auth headers
- 🏗️ **Builder Pattern** for complex JSON request payloads
- 🧪 **Multiple TestNG Suites**: Smoke, Regression, UAT, Positive, Negative, Auth, etc.
- 📊 **Allure Reporting** with custom filters and step annotations
- 📄 **JSON Schema Validation**
- 📋 **Excel-based Data-Driven Testing**
- 📦 **Page-Object-Like Modular Service Layers**
- 🛠️ **Logging with Log4j2** (Console + File logging)
- ✅ **CI/CD Ready**: Integrated with GitHub Actions

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

## 📂 Folder Structure

```bash
RestAssuredFramework/
├── src/
│   ├── main/java/com/api/
│   │   ├── constants/            # API constants and endpoints
│   │   ├── models/               # Request/Response POJOs
│   │   ├── services/             # Service layer per resource
│   │   └── utils/                # Utility classes (config, excel, logger)
│   └── test/java/com/api/tests/ # Test classes (CRUD, security)
├── suites/                       # TestNG XML suite files
├── resources/
│   └── schemas/                  # JSON schemas for response validation
├── logs/                         # Log4j2 log files
├── pom.xml                       # Project config and dependency management
├── .github/workflows/           # CI pipeline YAML
└── README.md
✅ Usage
🧪 Run Tests Locally
bash
Copy
Edit
# Full API suite
mvn clean test -Papi-tests -Denv=qa

# Smoke suite
mvn clean test -Psmoke-tests -Denv=qa
📈 Generate Allure Report Locally
bash
Copy
Edit
allure serve target/allure-results
🧪 Test Suites (via Maven Profiles)
Profile ID	Test Suite XML
api-tests	suites/restAssuredTestsSuite.xml
smoke-tests	testng-suites/smokeTestsSuite.xml
regression-tests	testng-suites/regressionTestsSuite.xml
uat-tests	testng-suites/uatTestsSuite.xml
positive-tests	testng-suites/positiveTestsSuite.xml
negative-tests	testng-suites/negativeTestsSuite.xml
auth-tests	testng-suites/authTestsSuite.xml
post-service-tests	testng-suites/PostServiceTestsSuite.xml

⚙️ CI/CD Pipeline
This project uses GitHub Actions with a scheduled, manual, and push-based workflow:

✅ Triggered on every push to main

🕒 Scheduled run daily at 3:00 AM IST

🎯 Supports manual trigger with environment + suite input

📤 Uploads Allure results as artifacts

🖥️ Builds on ubuntu-latest with Java 17

Example workflow:

yaml
Copy
Edit
mvn clean test -Papi-tests -Denv=qa
View GitHub Action Logs & Artifacts »

📘 Sample Allure Report
Click the badge at the top or View Report for latest results.

👤 Author
Satish Raja — Freelance API Automation Engineer
🔗 Upwork Profile
📧 satishraja4u@gmail.com

📜 License
Licensed under the MIT License.