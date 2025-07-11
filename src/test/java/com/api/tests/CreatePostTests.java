package com.api.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.constants.APIConstants.Schemas;
import com.api.constants.APIConstants.StatusCodes;
import com.api.models.request.CreatePostRequest;
import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
import com.api.models.response.CreatePostResponse;
import com.api.services.PostService;
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
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;

/**
 * ✅ This class verifies successful post creation for a valid user
 * and validates status, schema, and response content.
 */
@Epic("📝 Post Module")
@Feature("📤 Create Post API")
@Owner("Satish Raja")
@Severity(SeverityLevel.CRITICAL)
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Tag("regression")
@Tag("smoke")
public class CreatePostTests extends BaseTest {

    private PostService postService;
    private UserService userService;
    private int userId;
    private Faker faker;

    @BeforeClass
    @Step("🛠 Setup PostService, UserService, and test data")
    public void setup() {
        postService = new PostService();
        userService = new UserService();
        faker = new Faker();
        initializeTestData();
    }

    @Step("👤 Create a user for post creation")
    private void initializeTestData() {
        CreateUserRequest userPayload = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("active")
                .build();

        AllureLogger.attachJson("Create User Request", userPayload);

        Response response = userService.createUser(userPayload);
        AllureLogger.attachResponse("Create User Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.CREATED, "❌ User creation failed");

        userId = response.jsonPath().getInt("id");
    }

    @Test(description = "🟢 Create post for a valid user")
    @Story("✅ Positive Case - Create Post")
    @Description("Verifies that a post can be successfully created for a valid user")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePost_ShouldSucceed() {
        CreatePostRequest postPayload = buildValidPostPayload();
        AllureLogger.attachJson("Create Post Request", postPayload);

        Response response = postService.createPost(userId, postPayload);
        AllureLogger.attachResponse("Create Post Response", response);

        Allure.step("✅ Verify status code is 201 Created");
        Assert.assertEquals(response.statusCode(), StatusCodes.CREATED, "❌ Expected 201 Created");

        Allure.step("📐 Validate response against JSON schema");
        JsonSchemaValidatorUtil.validateJsonSchema(response, Schemas.Post.CREATE);

        Allure.step("🔍 Validate response body matches request payload");
        CreatePostResponse post = response.as(CreatePostResponse.class);
        Assert.assertEquals(post.getTitle(), postPayload.getTitle(), "❌ Title mismatch");
        Assert.assertEquals(post.getBody(), postPayload.getBody(), "❌ Body mismatch");

        Allure.step("⏱ Assert response time is under 2 seconds");
        Assert.assertTrue(response.time() < 2000, "❌ Response took too long: " + response.time() + "ms");
    }

    @Step("📝 Build valid CreatePostRequest")
    private CreatePostRequest buildValidPostPayload() {
        return new CreatePostRequest(
                "Sample Title",
                "This is the body of the post."
        );
    }
}
