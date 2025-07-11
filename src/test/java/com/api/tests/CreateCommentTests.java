package com.api.tests;

import com.api.constants.APIConstants.Schemas;
import com.api.models.request.CreateCommentRequest;
import com.api.models.request.CreateCommentRequestBuilder;
import com.api.models.request.CreatePostRequest;
import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
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

import static com.api.constants.APIConstants.StatusCodes;
import static com.api.constants.APIConstants.Tokens;

/**
 * 🔍 Test class for validating Create Comment API functionality,
 * covering both positive and negative scenarios with schema and response validation.
 */
@Epic("💬 Comment Module")
@Feature("📝 Create Comment API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class CreateCommentTests extends BaseTest {

    private CommentService commentService;
    private PostService postService;
    private UserService userService;
    private Faker faker;
    private int postId;

    @BeforeClass
    @Step("🔧 Setup test dependencies and create a post to comment on")
    public void setup() {
        faker = new Faker();
        commentService = new CommentService();
        postService = new PostService();
        userService = new UserService();

        Allure.step("🔐 Setting up Auth tokens");
        String token = System.getProperty("api.token", Tokens.ACCESS_TOKEN);
        userService.setAuthToken(token);
        postService.setAuthToken(token);
        commentService.setAuthToken(token);

        createUserAndPost();
    }

    @Step("👤 Creating a test user and 📝 test post")
    private void createUserAndPost() {
        CreateUserRequest userPayload = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("active")
                .build();

        AllureLogger.attachJson("Create User Payload", userPayload);
        Response userResponse = userService.createUser(userPayload);
        AllureLogger.attachResponse("Create User Response", userResponse);
        Assert.assertEquals(userResponse.statusCode(), StatusCodes.CREATED, "❌ User creation failed");

        int userId = userResponse.jsonPath().getInt("id");

        CreatePostRequest postPayload = new CreatePostRequest("Test Post", "Post body to comment on");
        AllureLogger.attachJson("Create Post Payload", postPayload);
        Response postResponse = postService.createPost(userId, postPayload);
        AllureLogger.attachResponse("Create Post Response", postResponse);
        Assert.assertEquals(postResponse.statusCode(), StatusCodes.CREATED, "❌ Post creation failed");

        postId = postResponse.jsonPath().getInt("id");
    }

    @Test(description = "🟢 Create comment with valid data", groups = "positive")
    @Story("✅ Valid Comment Creation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure a comment can be successfully created on a valid post")
    @TmsLink("TC-COMMENT-001")
    public void testCreateComment_ValidData_ShouldSucceed() {
        CreateCommentRequest commentPayload = CreateCommentRequestBuilder.builder()
                .name("Test Commenter")
                .email(faker.internet().emailAddress())
                .body("This is a valid test comment.")
                .build();

        Allure.step("📤 Sending create comment request");
        AllureLogger.attachJson("Create Comment Payload", commentPayload);

        Response response = commentService.createComment(postId, commentPayload);
        AllureLogger.attachResponse("Create Comment Response", response);

        Allure.step("✅ Verifying status code is 201 Created");
        Assert.assertEquals(response.statusCode(), StatusCodes.CREATED, "❌ Expected 201 for valid comment creation");

        Allure.step("📐 Validating response JSON schema");
        JsonSchemaValidatorUtil.validateJsonSchema(response, Schemas.Comment.CREATE);

        Allure.step("📋 Asserting response matches the request payload");
        CreateCommentResponse comment = response.as(CreateCommentResponse.class);
        Assert.assertEquals(comment.getName(), commentPayload.getName(), "❌ Name mismatch");
        Assert.assertEquals(comment.getEmail(), commentPayload.getEmail(), "❌ Email mismatch");
        Assert.assertEquals(comment.getBody(), commentPayload.getBody(), "❌ Body mismatch");

        Allure.step("⏱ Verifying response time is under 2 seconds");
        Assert.assertTrue(response.time() < 2000, "❌ Response took too long: " + response.time() + "ms");
    }

    @Test(description = "🔴 Missing name should return 422", groups = "negative")
    @Story("❌ Missing Fields in Comment")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-COMMENT-002")
    public void testCreateComment_MissingName_ShouldFail() {
        CreateCommentRequest payload = CreateCommentRequestBuilder.builder()
                .name(null)
                .email(faker.internet().emailAddress())
                .body("Missing name")
                .build();

        sendInvalidCommentRequest(payload, "Missing Name");
    }

    @Test(description = "🔴 Missing email should return 422", groups = "negative")
    @Story("❌ Missing Fields in Comment")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-COMMENT-003")
    public void testCreateComment_MissingEmail_ShouldFail() {
        CreateCommentRequest payload = CreateCommentRequestBuilder.builder()
                .name("No Email")
                .email(null)
                .body("Missing email")
                .build();

        sendInvalidCommentRequest(payload, "Missing Email");
    }

    @Test(description = "🔴 Missing body should return 422", groups = "negative")
    @Story("❌ Missing Fields in Comment")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-COMMENT-004")
    public void testCreateComment_MissingBody_ShouldFail() {
        CreateCommentRequest payload = CreateCommentRequestBuilder.builder()
                .name("No Body")
                .email(faker.internet().emailAddress())
                .body(null)
                .build();

        sendInvalidCommentRequest(payload, "Missing Body");
    }

    @Test(description = "🔴 Invalid email format should return 422", groups = "negative")
    @Story("❌ Invalid Email Format in Comment")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-COMMENT-005")
    public void testCreateComment_InvalidEmailFormat_ShouldFail() {
        CreateCommentRequest payload = CreateCommentRequestBuilder.builder()
                .name("Bad Email Format")
                .email("invalid-email")
                .body("Bad email test")
                .build();

        sendInvalidCommentRequest(payload, "Invalid Email Format");
    }

    @Test(description = "🔴 Invalid post ID should return 422", groups = "negative")
    @Story("❌ Invalid Post ID in Comment Request")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("TC-COMMENT-006")
    public void testCreateComment_InvalidPostId_ShouldFail() {
        CreateCommentRequest payload = CreateCommentRequestBuilder.builder()
                .name("Valid Name")
                .email(faker.internet().emailAddress())
                .body("Bad postId")
                .build();

        int invalidPostId = 99999999;

        Allure.step("📤 Sending comment to invalid postId = " + invalidPostId);
        AllureLogger.attachJson("Invalid Post ID Request", payload);

        Response response = commentService.createComment(invalidPostId, payload);
        AllureLogger.attachResponse("Invalid Post ID Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.UNPROCESSABLE_ENTITY, "❌ Expected 422 for invalid postId");
    }

    @Step("❌ Sending invalid comment request for negative test case")
    private void sendInvalidCommentRequest(CreateCommentRequest payload, String scenario) {
        Allure.step("🔍 Scenario: " + scenario);
        AllureLogger.attachJson("Invalid Comment Request - " + scenario, payload);

        Response response = commentService.createComment(postId, payload);
        AllureLogger.attachResponse("Response for " + scenario, response);

        Assert.assertEquals(response.statusCode(), StatusCodes.UNPROCESSABLE_ENTITY, "❌ Expected 422 for: " + scenario);
    }
}
