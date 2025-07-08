package com.api.tests;

import com.api.models.request.CreateCommentRequest;
import com.api.models.request.CreatePostRequest;
import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
import com.api.models.response.CreateCommentResponse;
import com.api.services.CommentService;
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

import static com.api.constants.APIConstants.*;

/**
 * 📘 GetCommentTests
 * 
 * Validates GET endpoints related to comments (by post ID, by comment ID).
 */
@Epic("💬 Comment Module")
@Feature("📥 Get Comment API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class GetCommentTests extends BaseTest {

    private UserService userService;
    private PostService postService;
    private CommentService commentService;
    private int userId;
    private int postId;
    private int commentId;
    private Faker faker;

    @BeforeClass
    @Step("🔧 Setup: Create user, post, and comment for GET tests")
    public void setup() {
        faker = new Faker();
        userService = new UserService();
        postService = new PostService();
        commentService = new CommentService();

        String token = System.getProperty("api.token", ACCESS_TOKEN);
        userService.setAuthToken(token);
        postService.setAuthToken(token);
        commentService.setAuthToken(token);

        createUserPostAndComment();
    }

    @Test(description = "🟢 Get all comments for a post", groups = "positive")
    @Story("✅ Retrieve All Comments")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that the API returns a list of comments for a given post ID")
    public void testGetAllCommentsForPost_ShouldReturnList() {
        Allure.step("📥 Fetching all comments for post ID: " + postId);

        Response response = commentService.getAllCommentsForPost(postId);
        AllureLogger.attachResponse("📎 Get All Comments Response", response);

        Assert.assertEquals(response.statusCode(), STATUS_CODE_OK, "❌ Expected 200 OK for get comments");

        var commentsList = response.jsonPath().getList("$");
        Assert.assertNotNull(commentsList, "❌ Comment list should not be null");
        Assert.assertTrue(commentsList.size() > 0, "❌ Comment list should not be empty");
    }

    @Test(description = "🟢 Get specific comment by ID", groups = "positive")
    @Story("✅ Retrieve Comment By ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that the API returns correct comment when given a valid comment ID")
    public void testGetCommentById_ShouldReturnCorrectComment() {
        Allure.step("🔎 Fetching comment by ID: " + commentId);

        Response response = commentService.getCommentById(commentId);
        AllureLogger.attachResponse("📎 Get Comment By ID Response", response);

        Assert.assertEquals(response.statusCode(), STATUS_CODE_OK, "❌ Expected 200 OK for valid comment ID");

        CreateCommentResponse comment = response.as(CreateCommentResponse.class);
        Assert.assertEquals(comment.getId(), commentId, "❌ Comment ID mismatch");
        Assert.assertEquals(comment.getPostId(), postId, "❌ Post ID mismatch in comment");
    }

    @Test(description = "🔴 Get comment with invalid ID should return 404", groups = "negative")
    @Story("❌ Invalid Comment Lookup")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that fetching a comment with invalid ID returns 404 Not Found")
    public void testGetCommentByInvalidId_ShouldReturn404() {
        int invalidId = 999999;
        Allure.step("🚫 Fetching comment using invalid ID: " + invalidId);

        Response response = commentService.getCommentById(invalidId);
        AllureLogger.attachResponse("📎 Get Comment By Invalid ID Response", response);

        Assert.assertEquals(response.statusCode(), STATUS_CODE_NOT_FOUND, "❌ Expected 404 for invalid comment ID");
    }

    /**
     * 🛠 Helper to create user, post, and comment for test setup
     */
    @Step("🛠 Create user, post, and comment for test context")
    private void createUserPostAndComment() {
        CreateUserRequest userPayload = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("female")
                .status("active")
                .build();

        AllureLogger.attachJson("📎 Create User Request", userPayload);
        Response userResponse = userService.createUser(userPayload);
        AllureLogger.attachResponse("📎 Create User Response", userResponse);
        Assert.assertEquals(userResponse.statusCode(), STATUS_CODE_CREATED, "❌ User creation failed");
        userId = userResponse.jsonPath().getInt("id");
        Allure.step("📌 Created User ID: " + userId);

        CreatePostRequest postPayload = new CreatePostRequest("Test Post", "This is a test post body");
        AllureLogger.attachJson("📎 Create Post Request", postPayload);
        Response postResponse = postService.createPost(userId, postPayload);
        AllureLogger.attachResponse("📎 Create Post Response", postResponse);
        Assert.assertEquals(postResponse.statusCode(), STATUS_CODE_CREATED, "❌ Post creation failed");
        postId = postResponse.jsonPath().getInt("id");
        Allure.step("📌 Created Post ID: " + postId);

        CreateCommentRequest commentPayload = new CreateCommentRequest(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                "Nice post!"
        );
        AllureLogger.attachJson("📎 Create Comment Request", commentPayload);
        Response commentResponse = commentService.createComment(postId, commentPayload);
        AllureLogger.attachResponse("📎 Create Comment Response", commentResponse);
        Assert.assertEquals(commentResponse.statusCode(), STATUS_CODE_CREATED, "❌ Comment creation failed");
        commentId = commentResponse.jsonPath().getInt("id");
        Allure.step("📌 Created Comment ID: " + commentId);
    }
}
