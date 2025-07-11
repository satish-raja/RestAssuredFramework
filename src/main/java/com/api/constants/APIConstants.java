package com.api.constants;

import com.api.utils.ConfigManager;

/**
 * 🔖 APIConstants.java
 *
 * 📌 Holds all global constant values related to the API framework.
 * Grouped for better maintainability and separation of concerns.
 */
public class APIConstants {

    // ✅ Base URIs
    public static final String BASE_URI = "https://gorest.co.in";
    public static final String BASE_URL = "https://gorest.co.in/public/v2";

    // 🚫 DO NOT hardcode tokens — use ConfigManager instead (loaded from qa.properties)
    // public static final String ACCESS_TOKEN = "HARDCODED_VALUE"; ❌
    // Use: ConfigManager.getInstance().get("api.token");
    
    public static class Tokens {
        public static final String ACCESS_TOKEN = ConfigManager.getInstance().get("api.token");
    }

    // ✅ Endpoints
    public static final class Endpoints {
        public static final String USERS = "/users";
        public static final String POSTS = "/posts";
        public static final String COMMENTS = "/comments";

        private Endpoints() {}
    }

    // ✅ Status Codes (grouped)
    public static final class StatusCodes {
        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int NO_CONTENT = 204;
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int UNPROCESSABLE_ENTITY = 422;
        public static final int INTERNAL_SERVER_ERROR = 500;

        private StatusCodes() {}
    }

    // ✅ Schema Paths (classpath-based recommended)
    public static final class Schemas {

        public static final class User {
            public static final String CREATE = "schemas/user/create_user_response_schema.json";
            public static final String UPDATE = "schemas/user/update_user_response_schema.json";
        }

        public static final class Post {
            public static final String CREATE = "schemas/post/create_post_response_schema.json";
            public static final String UPDATE = "schemas/post/update_post_response_schema.json";
        }

        public static final class Comment {
            public static final String CREATE = "schemas/comment/create_comment_response_schema.json";
            public static final String UPDATE = "schemas/comment/update_comment_response_schema.json";
        }

        private Schemas() {}
    }



    private APIConstants() {}
}
