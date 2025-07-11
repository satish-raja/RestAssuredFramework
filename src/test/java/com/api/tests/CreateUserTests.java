package com.api.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.api.constants.APIConstants.Schemas;
import com.api.constants.APIConstants.StatusCodes;
import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
import com.api.models.response.CreateUserResponse;
import com.api.services.UserService;
import com.api.tests.base.BaseTest;
import com.api.utils.AllureLogger;
import com.api.utils.JsonSchemaValidatorUtil;
import com.github.javafaker.Faker;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;

/**
 * ✅ This test class validates Create User API functionality with both
 * valid and invalid payloads, schema validation, and performance checks.
 */
@Epic("👤 User Management")
@Feature("➕ Create User API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class CreateUserTests extends BaseTest {

    private UserService userService;
    private Faker faker;

    @BeforeClass
    @Step("🧰 Initialize Faker and UserService")
    public void setup() {
        userService = new UserService();
        faker = new Faker();
    }

    @Test(groups = "positive", description = "🟢 Create user with valid data")
    @Story("✅ Valid User Creation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a user is successfully created when valid data is submitted")
    public void testCreateUser_ValidData_ShouldSucceed() {
        CreateUserRequest request = buildValidUserRequest();

        AllureLogger.attachJson("Create User Request", request);
        Response response = userService.createUser(request);
        AllureLogger.attachResponse("Create User Response", response);

        Allure.step("✅ Assert status code is 201 Created");
        Assert.assertEquals(response.statusCode(), StatusCodes.CREATED, "❌ Status code mismatch. Expected 201 Created.");

        assertResponseTimeWithinLimit(response);

        validateCreateUserSchema(response);

        Allure.step("📋 Validate response body matches request payload");
        CreateUserResponse user = response.as(CreateUserResponse.class);
        Assert.assertEquals(user.getName(), request.getName(), "❌ Name mismatch");
        Assert.assertEquals(user.getEmail(), request.getEmail(), "❌ Email mismatch");
        Assert.assertEquals(user.getGender(), request.getGender(), "❌ Gender mismatch");
        Assert.assertEquals(user.getStatus(), request.getStatus(), "❌ Status mismatch");
    }

    @Step("🧱 Build valid CreateUserRequest object")
    private CreateUserRequest buildValidUserRequest() {
        return CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("active")
                .build();
    }

    @Step("📐 Validate Create User response schema")
    private void validateCreateUserSchema(Response response) {
        JsonSchemaValidatorUtil.validateJsonSchema(response, Schemas.User.CREATE);
    }

    @Step("⏱ Validate response time is under 2 seconds")
    private void assertResponseTimeWithinLimit(Response response) {
        Assert.assertTrue(response.time() <= 2000, "❌ Response time exceeded 2 seconds");
    }

    @DataProvider(name = "invalidUserData")
    public Object[][] invalidUserData() {
        return new Object[][]{
                {"", faker.internet().emailAddress("missing-name"), "male", "active", "Missing name"},
                {"Test User", "", "female", "active", "Missing email"},
                {"Test User", "not-an-email", "female", "active", "Invalid email format"},
                {"Test User", faker.internet().emailAddress("bad-gender"), "unknown", "active", "Invalid gender"},
                {"Test User", faker.internet().emailAddress("bad-status"), "male", "running", "Invalid status"}
        };
    }

    @Test(dataProvider = "invalidUserData", groups = "negative")
    @Story("❌ Invalid User Creation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures user creation fails for invalid request payloads")
    @Link(name = "GoRest Docs", url = "https://gorest.co.in/")
    public void testCreateUser_InvalidData_ShouldFail(
            String name,
            String email,
            String gender,
            String status,
            String scenario) {

        Allure.parameter("Name", name);
        Allure.parameter("Email", email);
        Allure.parameter("Gender", gender);
        Allure.parameter("Status", status);
        Allure.parameter("Scenario", scenario);

        Allure.step("🧪 Scenario: " + scenario);

        CreateUserRequest request = CreateUserRequestBuilder.builder()
                .name(name)
                .email(email)
                .gender(gender)
                .status(status)
                .build();

        AllureLogger.attachJson("Invalid User Request - " + scenario, request);
        Response response = userService.createUser(request);
        AllureLogger.attachResponse("Response for Scenario - " + scenario, response);

        Allure.step("❌ Expecting HTTP 422 for: " + scenario);
        Assert.assertEquals(response.statusCode(), StatusCodes.UNPROCESSABLE_ENTITY, "❌ Expected 422 for scenario: " + scenario);
    }
}
