package com.api.constants;

/**
 * 🔖 APIConstants.java
 *
 * 📌 This class holds all global constant values related to the API framework.
 * These constants are used across the framework (tests, services, validators, etc.)
 * to ensure consistency, reusability, and easy maintenance.
 *
 * 👨‍💻 Even non-technical users can modify API endpoints or status codes here
 * without touching complex code.
 */
public class APIConstants {

    // ✅ BASE URI for GoRest API (with access token support if needed)
	// ✅ NEW - Just domain + scheme
	public static final String BASE_URI = "https://gorest.co.in";
    public static final String BASE_URL = "https://gorest.co.in/public/v2";

    // ✅ Authentication Token (You can override this in BaseService via header if needed)
    public static final String ACCESS_TOKEN = "05c1c3a7f823bb7ba137eadd1dbd257842b53f1e75234fe75d0c391467104e3c";

    // ✅ Common Endpoints — These paths will be appended to the base URI
    public static final String USERS_ENDPOINT = "/users";
    public static final String POSTS_ENDPOINT = "/posts";
    public static final String COMMENTS_ENDPOINT = "/comments";

    // ✅ Common HTTP Status Codes — Useful for assertions and validations
    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_CREATED = 201;
    public static final int STATUS_CODE_NO_CONTENT = 204;
    public static final int STATUS_CODE_BAD_REQUEST = 400;
    public static final int STATUS_CODE_UNAUTHORIZED = 401;
    public static final int STATUS_CODE_FORBIDDEN = 403;
    public static final int STATUS_CODE_NOT_FOUND = 404;
    public static final int STATUS_CODE_UNPROCESSABLE_ENTITY = 422;
    public static final int STATUS_CODE_INTERNAL_SERVER_ERROR = 500;

    // ✅ JSON Schema Paths (Optional)
    public static final String USER_SCHEMA_PATH = "src/test/resources/schemas/UserSchema.json";
    public static final String ERROR_SCHEMA_PATH = "src/test/resources/schemas/ErrorSchema.json";

    /**
     * 🚫 Constructor made private to prevent instantiation.
     * This class is meant to be used statically.
     */
    private APIConstants() {}

}
