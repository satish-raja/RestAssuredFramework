package com.api.tests;

import static com.api.constants.APIConstants.StatusCodes.CREATED;
import static com.api.constants.APIConstants.StatusCodes.NOT_FOUND;
import static com.api.constants.APIConstants.StatusCodes.OK;
import static com.api.constants.APIConstants.StatusCodes.UNPROCESSABLE_ENTITY;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.constants.APIConstants.Schemas;
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
 * 🔁 UpdateUserTests
 *
 * 📌 This class verifies the update functionality for the User API:
 * - Valid update with all fields
 * - Update with non-existent user ID
 * - Update with malformed email
 */
@Epic("👤 User Management")
@Feature("🔁 Update User API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class UpdateUserTests extends BaseTest {

    private UserService userService;
    private Faker faker;

    @BeforeClass
    @Step("🔧 Initialize UserService and Faker")
    public void setup() {
        userService = new UserService();
        faker = new Faker();
    }

    /**
     * Creates a new user and returns its ID.
     * Used as a precondition step for update tests.
     */
    @Step("👤 Create user and return ID (gender: {gender})")
    private int createUserAndReturnId(String gender) {
        CreateUserRequest request = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender(gender)
                .status("active")
                .build();

        AllureLogger.attachJson("Create User Request", request);
        Response response = userService.createUser(request);
        AllureLogger.attachResponse("Create User Response", response);

        Allure.step("✅ Assert status code is 201 Created");
        Assert.assertEquals(response.statusCode(), CREATED, "User creation failed");

        int id = response.jsonPath().getInt("id");
        Allure.step("📌 Extracted User ID: " + id);
        return id;
    }

    @Test(description = "🟢 Update user with valid data", groups = "positive")
    @Story("✅ Successful User Update")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure a user can be updated successfully with valid name, email, gender, and status.")
    public void testUpdateUser_ValidData_ShouldSucceed() {
        int userId = createUserAndReturnId("male");

        CreateUserRequest updatePayload = CreateUserRequestBuilder.builder()
                .name("Updated Name")
                .email(faker.internet().emailAddress())
                .gender("female")
                .status("inactive")
                .build();

        AllureLogger.attachJson("Update User Request", updatePayload);
        Response updateResponse = userService.updateUser(userId, updatePayload);
        AllureLogger.attachResponse("Update User Response", updateResponse);

        Allure.step("✅ Assert status code is 200 OK");
        Assert.assertEquals(updateResponse.statusCode(), OK, "Expected 200 OK for successful update");

        Allure.step("📐 Validating response schema for update");
        JsonSchemaValidatorUtil.validateJsonSchema(updateResponse, Schemas.User.UPDATE);

        CreateUserResponse updatedUser = updateResponse.as(CreateUserResponse.class);
        Allure.step("🔍 Asserting response body matches updated fields");
        Assert.assertEquals(updatedUser.getName(), updatePayload.getName(), "Name mismatch after update");
        Assert.assertEquals(updatedUser.getEmail(), updatePayload.getEmail(), "Email mismatch after update");
        Assert.assertEquals(updatedUser.getGender(), updatePayload.getGender(), "Gender mismatch after update");
        Assert.assertEquals(updatedUser.getStatus(), updatePayload.getStatus(), "Status mismatch after update");
    }

    @Test(description = "🔴 Update user with invalid ID", groups = "negative")
    @Story("❌ Invalid User Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that updating a user with a non-existent ID returns 404 Not Found.")
    public void testUpdateUser_InvalidId_ShouldReturn404() {
        int invalidUserId = 9999999;

        CreateUserRequest updatePayload = CreateUserRequestBuilder.builder()
                .name("Ghost User")
                .email("ghost@example.com")
                .gender("male")
                .status("active")
                .build();

        Allure.step("Attempting to update invalid user ID: " + invalidUserId);
        AllureLogger.attachJson("Update Request (Invalid ID)", updatePayload);

        Response response = userService.updateUser(invalidUserId, updatePayload);
        AllureLogger.attachResponse("Update Response (Invalid ID)", response);

        Allure.step("✅ Assert status code is 404 Not Found");
        Assert.assertEquals(response.statusCode(), NOT_FOUND, "Expected 404 for non-existent user");
    }

    @Test(description = "🔴 Update user with invalid email format", groups = "negative")
    @Story("❌ Invalid Email in User Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure that updating a user with malformed email results in 422 Unprocessable Entity.")
    public void testUpdateUser_InvalidEmail_ShouldReturn422() {
        int userId = createUserAndReturnId("female");

        CreateUserRequest invalidUpdatePayload = CreateUserRequestBuilder.builder()
                .name("Invalid Email Update")
                .email("not-an-email") // malformed
                .gender("male")
                .status("inactive")
                .build();

        Allure.step("Updating user with invalid email: " + invalidUpdatePayload.getEmail());
        AllureLogger.attachJson("Update Payload (Invalid Email)", invalidUpdatePayload);

        Response response = userService.updateUser(userId, invalidUpdatePayload);
        AllureLogger.attachResponse("Update Response (Invalid Email)", response);

        Allure.step("✅ Assert status code is 422 Unprocessable Entity");
        Assert.assertEquals(response.statusCode(), UNPROCESSABLE_ENTITY, "Expected 422 for invalid email format");
    }
}
