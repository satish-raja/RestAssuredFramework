package com.api.services;

import com.api.models.request.CreateCommentRequest;
import com.api.models.request.UpdateCommentRequest;
import com.api.utils.RetryUtil;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.api.constants.Endpoints.*;

public class CommentService extends BaseService {

    @Step("💬 Creating comment on postId: {0} by {1.name}")
    public Response createComment(int postId, CreateCommentRequest payload) {
        return RetryUtil.retry(
            () -> post(commentsByPostId(postId), payload),
            3,
            1000,
            "Create Comment on postId: " + postId + " (by: " + payload.getName() + ")"
        );
    }

    @Step("📋 Getting all comments")
    public Response getAllComments() {
        return get(COMMENTS);
    }

    @Step("🔍 Getting comment by commentId: {0}")
    public Response getCommentById(int commentId) {
        return get(commentById(commentId));
    }

    @Step("🔁 Updating commentId: {0} with new content by {1.name}")
    public Response updateComment(int commentId, UpdateCommentRequest payload) {
        return put(commentById(commentId), payload);
    }

    @Step("🗑 Deleting comment by commentId: {0}")
    public Response deleteComment(int commentId) {
        return delete(commentById(commentId));
    }

    @Step("📋 Getting all comments for postId: {0}")
    public Response getAllCommentsForPost(int postId) {
        return get(commentsByPostId(postId));
    }
}
