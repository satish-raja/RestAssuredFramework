package com.api.tests;

import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
import com.api.services.UserService;
import com.api.tests.base.BaseTest;
import com.api.utils.AllureLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.api.constants.APIConstants.Tokens;
import static com.api.constants.APIConstants.StatusCodes;

@Epic("👤 User Management")
@Feature("🗑 Delete User API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class DeleteUserTests extends BaseTest {

    private UserService userService;
    private Faker faker;

    @BeforeClass
    @Step("🔧 Setup: Initialize UserService and Faker")
    public void setup() {
        userService = new UserService();
        faker = new Faker();

        String token = System.getProperty("api.token", Tokens.ACCESS_TOKEN);
        userService.setAuthToken(token);
    }

    @Step("🛠 Create test user with gender={gender} and status={status}")
    private int createTestUser(String name, String gender, String status) {
        CreateUserRequest request = CreateUserRequestBuilder.builder()
                .name(name)
                .email(faker.internet().emailAddress())
                .gender(gender)
                .status(status)
                .build();

        AllureLogger.attachJson("Create User Request", request);
        Response response = userService.createUser(request);
        AllureLogger.attachResponse("Create User Response", response);
        Assert.assertEquals(response.statusCode(), StatusCodes.CREATED, "User creation failed");

        return response.jsonPath().getInt("id");
    }

    @Test(description = "🟢 Delete existing user - Should return 204", groups = "positive")
    @Story("✅ Valid User Deletion")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a user can be deleted successfully using a valid ID")
    public void testDeleteUser_ValidId_ShouldSucceed() {
        int userId = createTestUser(faker.name().fullName(), "female", "active");

        Allure.step("🗑 Deleting user with ID: " + userId);
        Response deleteResponse = userService.deleteUser(userId);
        AllureLogger.attachResponse("Delete User Response", deleteResponse);

        Assert.assertEquals(deleteResponse.statusCode(), StatusCodes.NO_CONTENT, "Expected 204 No Content for successful deletion");

        // ✅ Optional schema check: Response should be empty
        Allure.step("✅ Verifying response body is empty for 204");
        Assert.assertTrue(deleteResponse.body().asString().isEmpty(), "Response body should be empty for 204 No Content");
    }

    @Test(description = "🔴 Delete non-existent user - Should return 404", groups = "negative")
    @Story("❌ Invalid User Deletion")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure that deleting a user with an invalid ID returns 404 Not Found")
    public void testDeleteUser_InvalidId_ShouldReturn404() {
        int invalidUserId = 999999;

        Allure.step("🚫 Attempting to delete non-existent user ID: " + invalidUserId);
        Response response = userService.deleteUser(invalidUserId);
        AllureLogger.attachResponse("Delete Invalid User Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.NOT_FOUND, "Expected 404 Not Found for invalid user ID");
    }

    @Test(description = "🔴 Delete same user twice - Second attempt should return 404", groups = "negative")
    @Story("❌ Duplicate Deletion Attempt")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure that deleting a user twice returns 404 on the second attempt")
    public void testDeleteUser_Twice_ShouldReturn404OnSecondAttempt() {
        int userId = createTestUser("Delete Twice User", "male", "inactive");

        // 🗑 First delete attempt
        Allure.step("🗑 First delete attempt for user ID: " + userId);
        Response firstDelete = userService.deleteUser(userId);
        AllureLogger.attachResponse("First Delete Response", firstDelete);
        Assert.assertEquals(firstDelete.statusCode(), StatusCodes.NO_CONTENT, "Expected 204 No Content on first deletion");

        // ✅ Optional schema check for first delete
        Assert.assertTrue(firstDelete.body().asString().isEmpty(), "Response body should be empty for first 204 delete");

        // 🔁 Second delete attempt
        Allure.step("🔁 Second delete attempt for same user ID: " + userId);
        Response secondDelete = userService.deleteUser(userId);
        AllureLogger.attachResponse("Second Delete Response", secondDelete);
        Assert.assertEquals(secondDelete.statusCode(), StatusCodes.NOT_FOUND, "Expected 404 Not Found on second deletion attempt");
    }
}
