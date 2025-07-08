package com.api.constants;

/**
 * 📌 Endpoints – Centralized location for all REST API endpoint paths
 * 
 * ✅ Purpose:
 * - Avoids hardcoding endpoints across service/test classes
 * - Supports both static and dynamic REST paths
 */
public class Endpoints {

    // 🌐 Base path (used internally or in logs if needed)
    public static final String BASE_PATH = "/public/v2";

    // 📁 Base endpoints
    public static final String USERS = "/users";
    public static final String POSTS = "/posts";
    public static final String COMMENTS = "/comments";

    // 🔍 User-specific operations
    public static String userById(int userId) {
        return USERS + "/" + userId;
    }

    // 📝 Posts under a specific user: /users/{userId}/posts
    public static String postsByUserId(int userId) {
        return USERS + "/" + userId + "/posts";
    }

    // 🔍 Single post by ID: /posts/{postId}
    public static String postById(int postId) {
        return POSTS + "/" + postId;
    }

    // 💬 Comments under a specific post: /posts/{postId}/comments
    public static String commentsByPostId(int postId) {
        return POSTS + "/" + postId + "/comments";
    }

    // 🔍 Single comment by ID: /comments/{commentId}
    public static String commentById(int commentId) {
        return COMMENTS + "/" + commentId;
    }
}
