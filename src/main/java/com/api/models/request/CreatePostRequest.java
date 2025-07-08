package com.api.models.request;

/**
 * 📦 This class represents the request body structure for the "Create Post" API.
 *
 * 📌 Fields:
 * - title: The title of the post (required)
 * - body: The content/body of the post (required)
 *
 * ⚙️ This class is a simple POJO used for request serialization by RestAssured.
 */
public class CreatePostRequest {

    private String title;
    private String body;

    // Required: No-arg constructor for serialization/deserialization
    public CreatePostRequest() {}

    // All-args constructor used internally or by the builder
    public CreatePostRequest(String title, String body) {
        this.title = title;
        this.body = body;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    // Setters (optional, needed if not using builder pattern)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // Optional: toString() for logging/debugging
    @Override
    public String toString() {
        return "CreatePostRequest{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
