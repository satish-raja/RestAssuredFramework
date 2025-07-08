package com.api.tests;

import com.api.models.request.CreateCommentRequest;
import com.api.models.request.CreatePostRequest;
import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
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

@Epic("💬 Comment Module")
@Feature("🗑 Delete Comment API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class DeleteCommentTests extends BaseTest {

    private UserService userService;
    private PostService postService;
    private CommentService commentService;
    private int userId;
    private int postId;
    private int commentId;
    private Faker faker;

    @BeforeClass
    @Step("🔧 Setup: Create user, post and comment for deletion tests")
    public void setup() {
        faker = new Faker();
        userService = new UserService();
        postService = new PostService();
        commentService = new CommentService();

        String token = System.getProperty("api.token", ACCESS_TOKEN);
        userService.setAuthToken(token);
        postService.setAuthToken(token);
        commentService.setAuthToken(token);

        userId = createTestUser();
        postId = createTestPost(userId);
        commentId = createTestComment(postId);
    }

    @Step("👤 Create test user")
    private int createTestUser() {
        CreateUserRequest userPayload = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("female")
                .status("active")
                .build();

        AllureLogger.attachJson("Create User Request", userPayload);
        Response response = userService.createUser(userPayload);
        AllureLogger.attachResponse("Create User Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_CREATED, "❌ User creation failed");

        return response.jsonPath().getInt("id");
    }

    @Step("📝 Create test post for user ID: {userId}")
    private int createTestPost(int userId) {
        CreatePostRequest postPayload = new CreatePostRequest("Post for delete comment", "Post body");
        AllureLogger.attachJson("Create Post Request", postPayload);
        Response response = postService.createPost(userId, postPayload);
        AllureLogger.attachResponse("Create Post Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_CREATED, "❌ Post creation failed");

        return response.jsonPath().getInt("id");
    }

    @Step("💬 Create test comment for post ID: {postId}")
    private int createTestComment(int postId) {
        CreateCommentRequest commentPayload = new CreateCommentRequest(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                "Comment to be deleted"
        );

        AllureLogger.attachJson("Create Comment Request", commentPayload);
        Response response = commentService.createComment(postId, commentPayload);
        AllureLogger.attachResponse("Create Comment Response", response);
        Assert.assertEquals(response.statusCode(), STATUS_CODE_CREATED, "❌ Comment creation failed");

        return response.jsonPath().getInt("id");
    }

    @Test(description = "🟢 Delete comment by ID")
    @Story("✅ Valid Comment Deletion")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a comment can be successfully deleted using a valid comment ID")
    public void testDeleteComment_ShouldReturn204() {
        Allure.step("🗑 Deleting comment with ID: " + commentId);
        Response response = commentService.deleteComment(commentId);
        AllureLogger.attachResponse("Delete Comment Response", response);

        Assert.assertEquals(response.statusCode(), STATUS_CODE_NO_CONTENT, "❌ Expected 204 No Content for valid comment deletion");

        // Optional schema check hint (for later)
        // Allure.step("📐 Validating schema of delete response (should be empty for 204)");
        // Assert.assertTrue(response.body().asString().isEmpty(), "❌ Expected empty body for 204 response");
    }

    @Test(
        description = "🔴 Deleting already deleted comment should return 404",
        dependsOnMethods = "testDeleteComment_ShouldReturn204"
    )
    @Story("❌ Delete Already Removed Comment")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that deleting an already deleted comment returns 404 Not Found")
    public void testDeleteCommentAgain_ShouldReturn404() {
        Allure.step("🔁 Attempting to delete the same comment again: ID = " + commentId);
        Response response = commentService.deleteComment(commentId);
        AllureLogger.attachResponse("Second Delete Attempt Response", response);

        Assert.assertEquals(response.statusCode(), STATUS_CODE_NOT_FOUND, "❌ Expected 404 Not Found when deleting already deleted comment");
    }
}
