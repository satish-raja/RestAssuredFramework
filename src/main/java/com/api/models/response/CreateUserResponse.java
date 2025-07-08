package com.api.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 📄 Purpose:
 * This class represents the JSON response received from the GoRest API
 * after a successful user creation request.
 * 
 * 🔁 It allows us to map JSON response fields (like id, name, email) into
 * Java object fields so we can access them easily in tests.
 * 
 * 🧩 Used by:
 * - Test classes that validate response content.
 * - Service classes that deserialize API response to Java.
 */

// ✅ Explanation:
// This annotation tells Jackson (the JSON parser) to **ignore any unknown fields** in the JSON response
// that are not defined in this class.
// Useful when the API returns extra fields like `created_at`, `updated_at`, etc. which we haven't defined.
//
// This avoids exceptions like: UnrecognizedPropertyException
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponse {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;

    // ➕ Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
