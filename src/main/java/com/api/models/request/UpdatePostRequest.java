package com.api.models.request;

/**
 * ✏️ UpdatePostRequest – Used for updating a post
 */
public class UpdatePostRequest {

    private String title;
    private String body;

    public UpdatePostRequest(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
