package com.api.services;

import static io.restassured.RestAssured.given;
import static com.api.constants.APIConstants.Tokens;

import com.api.filters.AllureLoggingFilter;
import com.api.utils.ConfigManager;
import com.api.utils.RetryUtil;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseService {

	private static final String BASE_URL = ConfigManager.getInstance().get("base.url");

    private static final String DEFAULT_TOKEN = Tokens.ACCESS_TOKEN;

    protected RequestSpecification request;

    static {
        RestAssured.filters(new AllureLoggingFilter()); // ✅ Allure-based logging
    }

    public BaseService() {
        this.request = createRequestSpecification(DEFAULT_TOKEN);
    }

    public void setAuthToken(String token) {
        this.request = createRequestSpecification(token);
    }

    private RequestSpecification createRequestSpecification(String token) {
        return given()
                .baseUri(BASE_URL)
                .header("Accept", ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    protected Response get(String endpoint) {
        return request.when().get(endpoint);
    }

    protected Response post(String endpoint, Object payload) {
        return request.body(payload).when().post(endpoint);
    }

    protected Response put(String endpoint, Object payload) {
        return request.body(payload).when().put(endpoint);
    }

    protected Response delete(String endpoint) {
        return request.when().delete(endpoint);
    }

    // ✅ Retriable variants using RetryUtil
    protected Response getWithRetry(String endpoint, int maxRetries) {
        return RetryUtil.retry(() -> get(endpoint), maxRetries, 1000, "GET " + endpoint);
    }

    protected Response postWithRetry(String endpoint, Object payload, int maxRetries) {
        return RetryUtil.retry(() -> post(endpoint, payload), maxRetries, 1000, "POST " + endpoint);
    }

    protected Response putWithRetry(String endpoint, Object payload, int maxRetries) {
        return RetryUtil.retry(() -> put(endpoint, payload), maxRetries, 1000, "PUT " + endpoint);
    }

    protected Response deleteWithRetry(String endpoint, int maxRetries) {
        return RetryUtil.retry(() -> delete(endpoint), maxRetries, 1000, "DELETE " + endpoint);
    }
}
