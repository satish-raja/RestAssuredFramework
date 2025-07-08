package com.api.services;

import com.api.models.request.CreatePostRequest;
import com.api.models.request.UpdatePostRequest;
import com.api.utils.RetryUtil;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.api.constants.Endpoints.*;

public class PostService extends BaseService {

    @Step("📝 Creating post for userId: {0} with title: {1.title}")
    public Response createPost(int userId, CreatePostRequest payload) {
        return RetryUtil.retry(
            () -> post(postsByUserId(userId), payload),
            3,
            1000,
            "Create Post for userId: " + userId + " (title: " + payload.getTitle() + ")"
        );
    }

    @Step("🔁 Updating post (CreatePostRequest) with postId: {0}, title: {1.title}")
    public Response updatePost(int postId, CreatePostRequest payload) {
        return put(postById(postId), payload);
    }

    @Step("🔁 Updating post (UpdatePostRequest) with postId: {0}, title: {1.title}")
    public Response updatePost(int postId, UpdatePostRequest payload) {
        return put(postById(postId), payload);
    }

    @Step("📋 Getting all posts")
    public Response getAllPosts() {
        return get(POSTS);
    }

    @Step("📋 Getting all posts for userId: {0}")
    public Response getAllPostsForUser(int userId) {
        return get(postsByUserId(userId));
    }

    @Step("🔍 Getting post by postId: {0}")
    public Response getPostById(int postId) {
        return get(postById(postId));
    }

    @Step("🗑 Deleting post with postId: {0}")
    public Response deletePost(int postId) {
        return delete(postById(postId));
    }
}
