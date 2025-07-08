package com.api.models.request;

/**
 * 📦 This class represents the request body structure for the "Create User" API.
 *
 * 📌 Fields:
 * - name: Full name of the user
 * - gender: "male" or "female"
 * - email: User's email (must be unique)
 * - status: "active" or "inactive"
 *
 * ⚙️ This class is a simple POJO with no logic.
 * It's mainly used by the builder pattern and for serialization by RestAssured.
 */
public class CreateUserRequest {

    private String name;
    private String gender;
    private String email;
    private String status;

    // Required: No-arg constructor for serialization/deserialization
    public CreateUserRequest() {}

    // All-args constructor used internally by the builder
    public CreateUserRequest(String name, String gender, String email, String status) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.status = status;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    // Optional: Add toString() for logging/debugging
    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
