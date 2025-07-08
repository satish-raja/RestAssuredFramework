package com.api.tests;

import com.api.models.request.*;
import com.api.models.response.CreatePostResponse;
import com.api.services.PostService;
import com.api.services.UserService;
import com.api.tests.base.BaseTest;
import com.api.utils.AllureLogger;
import com.api.utils.JsonSchemaValidatorUtil;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static com.api.constants.APIConstants.*;

/**
 * 📘 UpdatePostTests
 *
 * 📌 This class validates the functionality of updating posts via PUT /posts/{id}.
 * Covered scenarios:
 * ✅ Valid update
 * ❌ Invalid ID
 * ❌ Empty payload
 * ❌ Missing Auth token
 */
@Epic("📝 Post Module")
@Feature("🛠 Update Post API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class UpdatePostTests extends BaseTest {

    private PostService postService;
    private UserService userService;
    private int userId;
    private int postId;
    private Faker faker;

    @BeforeClass
    @Step("🔧 Set up: Create User and Post for update tests")
    public void setup() {
        postService = new PostService();
        userService = new UserService();
        faker = new Faker();

        String token = System.getProperty("api.token", ACCESS_TOKEN);
        postService.setAuthToken(token);
        userService.setAuthToken(token);

        // 👤 Create User
        CreateUserRequest userPayload = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("active")
                .build();

        AllureLogger.attachJson("Create User", userPayload);
        Response userResponse = userService.createUser(userPayload);
        AllureLogger.attachResponse("Create User Response", userResponse);
        Assert.assertEquals(userResponse.statusCode(), STATUS_CODE_CREATED, "❌ User creation failed");
        userId = userResponse.jsonPath().getInt("id");
        Allure.step("📌 Created user ID: " + userId);

        // 📝 Create Post
        CreatePostRequest postPayload = new CreatePostRequest("Initial Title", "Initial Body");
        AllureLogger.attachJson("Create Post", postPayload);
        Response postResponse = postService.createPost(userId, postPayload);
        AllureLogger.attachResponse("Create Post Response", postResponse);
        Assert.assertEquals(postResponse.statusCode(), STATUS_CODE_CREATED, "❌ Post creation failed");
        postId = postResponse.jsonPath().getInt("id");
        Allure.step("📌 Created post ID: " + postId);
    }

    @Test(description = "🟢 Update post title and body", groups = "positive")
    @Story("✅ Successful Post Update")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure a post can be updated successfully using a valid payload and post ID.")
    public void testUpdatePost_ShouldSucceed() {
        UpdatePostRequest updatePayload = new UpdatePostRequest("Updated Title", "Updated body content");

        Allure.step("🔁 Updating post with ID: " + postId);
        AllureLogger.attachJson("Update Post Request", updatePayload);

        Response response = postService.updatePost(postId, updatePayload);
        AllureLogger.attachResponse("Update Post Response", response);

        Assert.assertEquals(response.statusCode(), STATUS_CODE_OK, "❌ Expected 200 OK for post update");

        Allure.step("📐 Validating Update Post JSON schema");
        JsonSchemaValidatorUtil.validateJsonSchema(response, "schemas/post/update_post_response_schema.json");

        CreatePostResponse updatedPost = response.as(CreatePostResponse.class);
        Assert.assertEquals(updatedPost.getTitle(), updatePayload.getTitle(), "❌ Mismatch in updated title");
        Assert.assertEquals(updatedPost.getBody(), updatePayload.getBody(), "❌ Mismatch in updated body");
    }

    @Test(description = "🔴 Update with invalid post ID should return 404", groups = "negative")
    @Story("❌ Invalid Post Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that updating a post with an invalid ID results in a 404 Not Found error.")
    public void testUpdatePostWithInvalidId_ShouldReturn404() {
        int invalidPostId = 999999;
        UpdatePostRequest payload = new UpdatePostRequest("Invalid Update", "Trying to update with wrong ID");

        Allure.step("🚫 Attempting update with invalid post ID: " + invalidPostId);
        AllureLogger.attachJson("Update Request (Invalid ID)", payload);

        Response response = postService.updatePost(invalidPostId, payload);
        AllureLogger.attachResponse("Update Response (Invalid ID)", response);

        Assert.assertEquals(response.statusCode(), STATUS_CODE_NOT_FOUND,
                "❌ Expected 404 Not Found for invalid post ID but got: " + response.statusCode());
    }

    @Test(description = "🔴 Update post with empty payload should return 422", groups = "negative")
    @Story("❌ Invalid Payload on Post Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure 422 Unprocessable Entity is returned when updating a post with empty title/body.")
    public void testUpdatePostWithEmptyPayload_ShouldReturn422() {
        UpdatePostRequest emptyPayload = new UpdatePostRequest("", "");

        Allure.step("🚫 Attempting update with empty title and body");
        AllureLogger.attachJson("Empty Update Payload", emptyPayload);

        Response response = postService.updatePost(postId, emptyPayload);
        AllureLogger.attachResponse("Update Response (Empty Payload)", response);

        Assert.assertEquals(response.statusCode(), STATUS_CODE_UNPROCESSABLE_ENTITY,
                "❌ Expected 422 Unprocessable Entity for empty payload but got: " + response.statusCode());
    }

    @Test(description = "🔴 Update post without auth token should return 401 or 404", groups = "negative")
    @Story("❌ Unauthorized Post Update")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that updating a post without an authorization token returns 401 or 404.")
    public void testUpdatePostWithoutAuthToken_ShouldReturn401or404() {
        UpdatePostRequest payload = new UpdatePostRequest("Unauthorized", "No token provided");

        Allure.step("🔐 Attempting unauthorized update for post ID: " + postId);
        AllureLogger.attachJson("Unauthorized Update Payload", payload);

        Response response = given()
                .baseUri(BASE_URL)
                .basePath("/posts/" + postId)
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .when()
                .put()
                .then()
                .extract()
                .response();

        AllureLogger.attachResponse("Unauthorized Update Response", response);

        Assert.assertTrue(
                response.statusCode() == STATUS_CODE_UNAUTHORIZED || response.statusCode() == STATUS_CODE_NOT_FOUND,
                "❌ Expected 401 or 404 for unauthorized request but got: " + response.statusCode()
        );
    }
}
