# 🔬 RestAssured API Automation Framework

A robust, modular, and extensible API automation framework built with **RestAssured**, **TestNG**, **Allure**, and **Log4j2**, designed to test CRUD operations for GoRest APIs.

----

## 🚀 Features

- ✅ Full CRUD Test Coverage (User, Post, Comment)
- 💡 Builder Pattern for Request Payloads
- 🧪 TestNG Suite Support (Smoke, Regression, UAT)
- 📦 Modular service layers with `UserService`, `PostService`, and `CommentService`
- 🔄 Retry logic with `RetryUtil`
- 📊 Allure Reporting with Allure Filters and Custom Steps
- 📂 JSON Schema Validation
- 📋 Excel Data Reader for future data-driven testing
- 🔐 Authorization Token Handling
- 🧼 Clean, extensible project structure

----

## 📁 Project Structure

```bash
.
├── src
│   ├── main
│   │   ├── java/com/api/constants
│   │   ├── java/com/api/models/request
│   │   ├── java/com/api/models/response
│   │   ├── java/com/api/services
│   │   ├── java/com/api/utils
│   │   └── resources
│   └── test
│       ├── java/com/api/tests
│       ├── java/com/api/tests/security
│       ├── java/com/api/tests/base
│       └── resources/schemas
├── testng-suites
├── pom.xml
├── README.md
└── restAssuredTestsSuite.xml
