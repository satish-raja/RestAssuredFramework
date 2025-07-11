package com.api.tests;

import com.api.models.request.CreatePostRequest;
import com.api.models.request.CreateUserRequest;
import com.api.models.request.CreateUserRequestBuilder;
import com.api.models.response.CreatePostResponse;
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

import static com.api.constants.APIConstants.Tokens;
import static com.api.constants.APIConstants.StatusCodes;

/**
 * 📘 GetPostTests
 *
 * Contains tests to validate the GET /posts and GET /posts/{id} endpoints.
 */
@Epic("📝 Post Module")
@Feature("🔍 Get Post API")
@Owner("Satish Raja")
@Link(name = "API Docs", url = "https://gorest.co.in/")
@Severity(SeverityLevel.BLOCKER)
public class GetPostTests extends BaseTest {

    private PostService postService;
    private UserService userService;
    private int userId;
    private int postId;
    private Faker faker;

    @BeforeClass
    @Step("🔧 Setup test data: Create user and post")
    public void setup() {
        faker = new Faker();
        postService = new PostService();
        userService = new UserService();

        String token = System.getProperty("api.token", Tokens.ACCESS_TOKEN);
        postService.setAuthToken(token);
        userService.setAuthToken(token);

        createUserAndPost();
    }

    @Test(description = "🟢 Get post by ID", groups = "positive")
    @Story("✅ Valid GET by Post ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that a post can be retrieved using a valid post ID")
    public void testGetPostById_ShouldReturnCorrectPost() {
        Allure.step("📥 Fetching post by ID: " + postId);

        Response response = postService.getPostById(postId);
        AllureLogger.attachResponse("📎 Get Post By ID Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.OK, "❌ Expected 200 OK for valid post ID");

        CreatePostResponse post = response.as(CreatePostResponse.class);
        Assert.assertEquals(post.getId(), postId, "❌ Post ID mismatch");
        Assert.assertEquals(post.getTitle(), "Get Post Title", "❌ Title mismatch");
        Assert.assertEquals(post.getBody(), "Get post body text", "❌ Body content mismatch");
    }

    @Test(description = "🟢 Get all posts", groups = "positive")
    @Story("✅ Retrieve All Posts")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that all posts can be retrieved and list is not empty")
    public void testGetAllPosts_ShouldReturnList() {
        Allure.step("📥 Fetching all posts");

        Response response = postService.getAllPosts();
        AllureLogger.attachResponse("📎 Get All Posts Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.OK, "❌ Expected status 200 OK");

        var postList = response.jsonPath().getList("id");
        Assert.assertNotNull(postList, "❌ Expected post list but got null");
        Assert.assertTrue(postList.size() > 0, "❌ Expected non-empty post list");
    }

    @Test(description = "🔴 Get non-existent post should return 404", groups = "negative")
    @Story("❌ Invalid GET by Post ID")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures 404 is returned when post ID does not exist")
    public void testGetPostByInvalidId_ShouldReturn404() {
        int invalidPostId = 99999999;
        Allure.step("🚫 Attempting to fetch non-existent post with ID: " + invalidPostId);

        Response response = postService.getPostById(invalidPostId);
        AllureLogger.attachResponse("📎 Get Invalid Post Response", response);

        Assert.assertEquals(response.statusCode(), StatusCodes.NOT_FOUND, "❌ Expected 404 Not Found");
    }

    /**
     * 🛠 Helper method to create user and post
     */
    @Step("🛠 Create user and post, and capture IDs")
    private void createUserAndPost() {
        CreateUserRequest userPayload = CreateUserRequestBuilder.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("active")
                .build();

        AllureLogger.attachJson("📎 Create User Request", userPayload);
        Response userResponse = userService.createUser(userPayload);
        AllureLogger.attachResponse("📎 Create User Response", userResponse);

        Assert.assertEquals(userResponse.statusCode(), StatusCodes.CREATED, "❌ User creation failed");
        userId = userResponse.jsonPath().getInt("id");
        Allure.step("📌 Created User ID: " + userId);

        CreatePostRequest postPayload = new CreatePostRequest("Get Post Title", "Get post body text");
        AllureLogger.attachJson("📎 Create Post Request", postPayload);
        Response postResponse = postService.createPost(userId, postPayload);
        AllureLogger.attachResponse("📎 Create Post Response", postResponse);

        Assert.assertEquals(postResponse.statusCode(), StatusCodes.CREATED, "❌ Post creation failed");
        postId = postResponse.jsonPath().getInt("id");
        Allure.step("📌 Created Post ID: " + postId);
    }
}
