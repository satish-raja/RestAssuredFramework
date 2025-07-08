package com.api.services;

import static com.api.constants.Endpoints.USERS;
import static com.api.constants.Endpoints.userById;

import com.api.models.request.CreateUserRequest;
import com.api.utils.RetryUtil;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserService extends BaseService {

    public UserService() {
        super();
    }

    @Step("👤 Creating user with name: {0.name}, email: {0.email}")
    public Response createUser(CreateUserRequest payload) {
        return RetryUtil.retry(
            () -> post(USERS, payload),
            3,
            1000,
            "Create User: " + payload.getName() + " (" + payload.getEmail() + ")"
        );
    }

    @Step("👤 Creating user (shortcut method) with name: {0.name}")
    public Response createUserAndReturnResponse(CreateUserRequest payload) {
        return createUser(payload); // already retry-enabled
    }

    @Step("🔍 Fetching user by ID: {0}")
    public Response getUserById(int userId) {
        return get(userById(userId));
    }

    @Step("🔁 Updating user ID: {0} with name: {1.name}, email: {1.email}")
    public Response updateUser(int userId, CreateUserRequest payload) {
        return put(userById(userId), payload);
    }

    @Step("🗑 Deleting user by ID: {0}")
    public Response deleteUser(int userId) {
        return delete(userById(userId));
    }

    @Step("📋 Fetching all users")
    public Response getAllUsers() {
        return get(USERS);
    }
}
