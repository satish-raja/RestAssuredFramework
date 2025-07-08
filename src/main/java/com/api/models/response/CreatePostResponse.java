package com.api.models.response;

public class CreatePostResponse {
    private int id;
    private int user_id;
    private String title;
    private String body;

    public int getId() { return id; }
    public int getUser_id() { return user_id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
}
