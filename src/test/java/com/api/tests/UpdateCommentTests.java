package com.api.tests;

import com.api.models.request.*;
import com.api.models.response.CreateCommentResponse;
import com.api.services.CommentService;
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

import static com.api.constants.APIConstants.Tokens;
import static com.api.constants.APIConstants.StatusCodes;
import static com.api.constants.APIConstants.Schemas;

/**
 * 📘 UpdateCommentTests
 *
 * This test class verifies the behavior of the PUT /comments/{id} endpoint.
 * It covers positive update with valid data, and validates both schema and content.
 */
@Epic("💬 Comment Module")
@Feature("✏️ Update Comment API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class UpdateCommentTests extends BaseTest {

    private CommentService commentService;
    private PostService postService;
    private UserService userService;
    private Faker faker;
    private int commentId;

    @BeforeClass
    @Step("🔧 Setup: Create user, post, and comment for update tests")
    public void setup() {
        faker = new Faker();
        commentService = new CommentService();
        postService = new PostService();
        userService = new UserService();

        String token = System.getProperty("api.token", Tokens.ACCESS_TOKEN);
        commentService.setAuthToken(token);
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
        Assert.assertEquals(userResponse.statusCode(), StatusCodes.CREATED, "❌ User creation failed");

        int userId = userResponse.jsonPath().getInt("id");
        Allure.step("📌 Created user ID: " + userId);

        // 📝 Create Post
        CreatePostRequest postPayload = new CreatePostRequest("Post for Comment", "Body of post");
        AllureLogger.attachJson("Create Post", postPayload);
        Response postResponse = postService.createPost(userId, postPayload);
        AllureLogger.attachResponse("Create Post Response", postResponse);
        Assert.assertEquals(postResponse.statusCode(), StatusCodes.CREATED, "❌ Post creation failed");

        int postId = postResponse.jsonPath().getInt("id");
        Allure.step("📌 Created post ID: " + postId);

        // 💬 Create Comment
        CreateCommentRequest commentPayload = new CreateCommentRequest(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                "Initial comment content"
        );

        AllureLogger.attachJson("Create Comment", commentPayload);
        Response commentResponse = commentService.createComment(postId, commentPayload);
        AllureLogger.attachResponse("Create Comment Response", commentResponse);
        Assert.assertEquals(commentResponse.statusCode(), StatusCodes.CREATED, "❌ Comment creation failed");

        commentId = commentResponse.jsonPath().getInt("id");
        Allure.step("📌 Created comment ID: " + commentId);
    }

    @Test(description = "🟢 Update a comment successfully", groups = "positive")
    @Story("✅ Successful Comment Update")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a comment can be updated with valid data and schema validation passes")
    public void testUpdateComment_ShouldSucceed() {
        UpdateCommentRequest updatePayload = buildUpdateCommentPayload();

        Allure.step("📤 Sending update request for comment ID: " + commentId);
        AllureLogger.attachJson("Update Comment Payload", updatePayload);

        Response updateResponse = commentService.updateComment(commentId, updatePayload);
        AllureLogger.attachResponse("Update Comment Response", updateResponse);

        Assert.assertEquals(updateResponse.statusCode(), StatusCodes.OK, "❌ Expected 200 OK for comment update");

        Allure.step("📐 Validating update comment response schema...");
        JsonSchemaValidatorUtil.validateJsonSchema(updateResponse, Schemas.Comment.UPDATE);

        CreateCommentResponse updatedComment = updateResponse.as(CreateCommentResponse.class);
        Assert.assertEquals(updatedComment.getBody(), updatePayload.getBody(), "❌ Mismatch in comment body");
        Assert.assertEquals(updatedComment.getName(), updatePayload.getName(), "❌ Mismatch in commenter name");
        Assert.assertEquals(updatedComment.getEmail(), updatePayload.getEmail(), "❌ Mismatch in commenter email");
    }

    @Step("🛠 Generate update payload for comment")
    private UpdateCommentRequest buildUpdateCommentPayload() {
        return UpdateCommentRequest.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .body("✅ Updated comment body")
                .build();
    }
}
