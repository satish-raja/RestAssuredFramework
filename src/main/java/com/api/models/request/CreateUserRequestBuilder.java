package com.api.models.request;

/**
 * 🛠️ Builder class for creating dynamic and readable CreateUserRequest objects.
 *
 * 💡 Why Builder?
 * - Easily construct valid/invalid payloads for positive and negative test cases.
 * - No need to manually set all fields every time.
 * - Enables fluent chaining: .name("John").email("...").build()
 */
public class CreateUserRequestBuilder {

    private String name;
    private String gender;
    private String email;
    private String status;

    // Required: Private constructor
    private CreateUserRequestBuilder() {}

    // Required: Static method to start the builder chain
    public static CreateUserRequestBuilder builder() {
        return new CreateUserRequestBuilder();
    }

    // Chainable setters
    public CreateUserRequestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CreateUserRequestBuilder gender(String gender) {
        this.gender = gender;
        return this;
    }

    public CreateUserRequestBuilder email(String email) {
        this.email = email;
        return this;
    }

    public CreateUserRequestBuilder status(String status) {
        this.status = status;
        return this;
    }

    // Final build method returns the POJO
    public CreateUserRequest build() {
        return new CreateUserRequest(name, gender, email, status);
    }
}
