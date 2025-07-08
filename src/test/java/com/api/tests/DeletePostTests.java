package com.api.tests;

import com.api.models.request.CreatePostRequest;
import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
import com.api.services.PostService;
import com.api.services.UserService;
import com.api.tests.base.BaseTest;
import com.api.utils.AllureLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static com.api.constants.APIConstants.*;

@Epic("📝 Post Module")
@Feature("🗑 Delete Post API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class DeletePostTests extends BaseTest {

    private PostService postService;
    private UserService userService;
    private int userId;
    private int postId;
    private Faker faker;

    @BeforeClass
    @Step("🔧 Setup: Create a user and post to delete")
    public void setup() {
        postService = new PostService();
        userService = new UserService();
        faker = new Faker();

        String token = System.getProperty("api.token", ACCESS_TOKEN);
        postService.setAuthToken(token);
        userService.setAuthToken(token);

        userId = createTestUser();
        postId = createTestPost(userId);
    }

    @Step("👤 Create a test user")
    private int createTestUser() {
        CreateUserRequest userPayload = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("active")
                .build();

        AllureLogger.attachJson("Create User Request", userPayload);
        Response response = userService.createUser(userPayload);
        AllureLogger.attachResponse("Create User Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_CREATED, "User creation failed");

        return response.jsonPath().getInt("id");
    }

    @Step("📝 Create a test post for user ID: {userId}")
    private int createTestPost(int userId) {
        CreatePostRequest postPayload = new CreatePostRequest("Post to Delete", "This post will be deleted.");
        AllureLogger.attachJson("Create Post Request", postPayload);
        Response response = postService.createPost(userId, postPayload);
        AllureLogger.attachResponse("Create Post Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_CREATED, "Post creation failed");

        return response.jsonPath().getInt("id");
    }

    @Test(description = "🟢 Delete a post by ID")
    @Story("✅ Successful Post Deletion")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a post can be successfully deleted using its ID")
    public void testDeletePost_ShouldSucceed() {
        Allure.step("🗑 Deleting post with ID: " + postId);
        Response response = postService.deletePost(postId);
        AllureLogger.attachResponse("Delete Post Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_NO_CONTENT, "Expected 204 No Content for successful deletion");

        // Optionally validate empty body
        // Assert.assertTrue(response.body().asString().isEmpty(), "Expected empty body for 204 response");
    }

    @Test(dependsOnMethods = "testDeletePost_ShouldSucceed", description = "🔴 Get deleted post should return 404")
    @Story("❌ Get Deleted Post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that fetching a deleted post returns 404 Not Found")
    public void testGetDeletedPost_ShouldReturn404() {
        Allure.step("🔎 Trying to fetch deleted post with ID: " + postId);
        Response response = postService.getPostById(postId);
        AllureLogger.attachResponse("Get Deleted Post Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_NOT_FOUND, "Expected 404 Not Found for deleted post");
    }

    @Test(description = "🔴 Delete post with invalid ID should return 404")
    @Story("❌ Delete with Invalid Post ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that deleting a post with a non-existent ID returns 404")
    public void testDeletePostWithInvalidId_ShouldReturn404() {
        int invalidPostId = 999999;

        Allure.step("📛 Attempting to delete post with invalid ID: " + invalidPostId);
        Response response = postService.deletePost(invalidPostId);
        AllureLogger.attachResponse("Delete with Invalid ID Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_NOT_FOUND, "Expected 404 Not Found for invalid post ID");
    }

    @Test(description = "🔴 Delete post without authorization token should return 401 or 404")
    @Story("❌ Unauthorized Post Deletion")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that deleting a post without authorization fails (401 or 404)")
    public void testDeletePostWithoutAuthToken_ShouldReturnUnauthorizedOrNotFound() {
        CreatePostRequest payload = new CreatePostRequest("Unauthorized Delete", "Testing without token.");
        Response postResponse = postService.createPost(userId, payload);
        Assert.assertEquals(postResponse.statusCode(), STATUS_CODE_CREATED, "Post creation failed");

        int tempPostId = postResponse.jsonPath().getInt("id");

        Allure.step("🔒 Attempting unauthorized delete for Post ID: " + tempPostId);
        Response response = given()
                .baseUri(BASE_URL)
                .basePath("/posts/" + tempPostId)
                .accept("application/json")
                .when()
                .delete()
                .then()
                .extract()
                .response();

        AllureLogger.attachResponse("Unauthorized Delete Attempt Response", response);

        int status = response.statusCode();
        Assert.assertTrue(
                status == STATUS_CODE_UNAUTHORIZED || status == STATUS_CODE_NOT_FOUND,
                "Expected 401 or 404, but got: " + status
        );
    }

    @Test(dependsOnMethods = "testDeletePost_ShouldSucceed", description = "🔴 Deleting same post again should return 404")
    @Story("❌ Delete Already Deleted Post")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that deleting the same post twice returns 404 Not Found")
    public void testDeletePostTwice_ShouldReturn404() {
        Allure.step("🔁 Attempting to delete already deleted post with ID: " + postId);
        Response response = postService.deletePost(postId);
        AllureLogger.attachResponse("Second Delete Attempt Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_NOT_FOUND, "Expected 404 Not Found for already deleted post");
    }
}
