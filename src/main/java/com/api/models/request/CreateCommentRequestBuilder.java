package com.api.models.request;

/**
 * 🛠️ Builder class for creating dynamic and readable CreateCommentRequest objects.
 *
 * 💡 Why Builder?
 * - Simplifies creation of positive and negative payloads for comment API tests.
 * - Avoids repetitive constructor calls with many parameters.
 * - Enables fluent chaining: .name("John").email("john@example.com").body("...").build()
 */
public class CreateCommentRequestBuilder {
    private String name;
    private String email;
    private String body;

    /**
     * 📦 Static factory method to start building.
     */
    public static CreateCommentRequestBuilder builder() {
        return new CreateCommentRequestBuilder();
    }

    /**
     * 🧑 Set the commenter's name.
     */
    public CreateCommentRequestBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * 📧 Set the commenter's email address.
     */
    public CreateCommentRequestBuilder email(String email) {
        this.email = email;
        return this;
    }

    /**
     * 💬 Set the body/content of the comment.
     */
    public CreateCommentRequestBuilder body(String body) {
        this.body = body;
        return this;
    }

    /**
     * ✅ Build and return the CreateCommentRequest object.
     */
    public CreateCommentRequest build() {
        return new CreateCommentRequest(name, email, body);
    }
}
