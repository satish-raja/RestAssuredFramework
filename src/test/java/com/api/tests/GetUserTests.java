package com.api.tests;

import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
import com.api.models.response.CreateUserResponse;
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

/**
 * 📘 GetUserTests
 *
 * This class contains positive and negative test cases for the GET /users and GET /users/{id} endpoints.
 */
@Epic("👤 User Module")
@Feature("🔍 Get User API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class GetUserTests extends BaseTest {

    private UserService userService;
    private Faker faker;
    private int validUserId;

    @BeforeClass
    @Step("🔧 Setup: Create a user for GET tests")
    public void setup() {
        faker = new Faker();
        userService = new UserService();

        String token = System.getProperty("api.token", Tokens.ACCESS_TOKEN);
        userService.setAuthToken(token);

        validUserId = createUserAndReturnId();
        Allure.step("📌 Stored valid user ID for test: " + validUserId);
    }

    @Test(description = "🟢 Fetch all users - Should return 200 and at least one user", groups = "positive")
    @Story("✅ Get All Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that GET /users returns a list of users with 200 status")
    public void testGetAllUsers_ShouldReturnUserList() {
        Allure.step("📥 Sending GET request to /users endpoint");

        Response response = userService.getAllUsers();
        AllureLogger.attachResponse("📎 Get All Users Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.OK, "❌ Expected status code 200 for /users");

        var userList = response.jsonPath().getList("$");
        Assert.assertNotNull(userList, "❌ Expected user list but got null");
        Assert.assertTrue(userList.size() > 0, "❌ Expected at least one user in response");
    }

    @Test(description = "🟢 Fetch single user by valid ID - Should return 200", groups = "positive")
    @Story("✅ Get User by Valid ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that GET /users/{id} returns correct user details")
    public void testGetSingleUser_ValidId_ShouldReturnUser() {
        Allure.step("📥 Fetching user with valid ID: " + validUserId);

        Response response = userService.getUserById(validUserId);
        AllureLogger.attachResponse("📎 Get User By Valid ID Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.OK, "❌ Expected 200 OK for valid user ID");

        int returnedId = response.jsonPath().getInt("id");
        Assert.assertEquals(returnedId, validUserId, "❌ Returned user ID does not match expected");
    }

    @Test(description = "🔴 Fetch single user by invalid ID - Should return 404", groups = "negative")
    @Story("❌ Get User by Invalid ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure that GET /users/{invalid-id} returns 404 Not Found")
    public void testGetSingleUser_InvalidId_ShouldReturn404() {
        int invalidUserId = 99999999;
        Allure.step("🚫 Attempting to fetch user with invalid ID: " + invalidUserId);

        Response response = userService.getUserById(invalidUserId);
        AllureLogger.attachResponse("📎 Get User By Invalid ID Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.NOT_FOUND, "❌ Expected 404 for invalid user ID");
    }

    /**
     * 🧠 Utility method to create a user and return ID
     */
    @Step("🛠 Create user and return ID")
    private int createUserAndReturnId() {
        CreateUserRequest request = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("active")
                .build();

        AllureLogger.attachJson("📎 Create User Payload", request);

        Response response = userService.createUserAndReturnResponse(request);
        AllureLogger.attachResponse("📎 Create User Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.CREATED, "❌ User creation failed");

        CreateUserResponse user = response.as(CreateUserResponse.class);
        return user.getId();
    }
}
