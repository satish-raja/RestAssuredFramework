package com.api.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCommentResponse {

    private int id;

    @JsonProperty("post_id")
    private int postId;

    private String name;
    private String email;
    private String body;

    // ✅ Getters
    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }
}
