package com.api.models.request;

public class CreateCommentRequest {
    private String name;
    private String email;
    private String body;

    public CreateCommentRequest() {}

    public CreateCommentRequest(String name, String email, String body) {
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getBody() { return body; }
}
