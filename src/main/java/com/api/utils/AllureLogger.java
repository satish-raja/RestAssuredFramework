package com.api.utils;

import com.google.gson.Gson;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

public class AllureLogger {

    private static final Gson gson = new Gson();

    private static String lastRequestJson;
    private static String lastResponseBody;
    private static Response lastResponse;

    private static String lastRequestURI;
    private static Map<String, String> lastQueryParams;
    private static Headers lastHeaders;

    // ✅ Attach POJO as JSON
    @Step("📎 Attach JSON Payload: {name}")
    public static void attachJson(String name, Object payload) {
        Allure.addAttachment(name, "application/json", gson.toJson(payload), ".json");
    }

    // ✅ Attach Response (formatted)
    @Step("📎 Attach API Response: {name}")
    public static void attachResponse(String name, Response response) {
        Allure.addAttachment(name, "application/json", response.asPrettyString(), ".json");
        Allure.step("⏱ Response Time: " + response.time() + " ms");
        lastResponse = response;
        lastResponseBody = response.asPrettyString();
    }

    // ✅ Attach plain text
    @Step("📝 Log Note: {name}")
    public static void attachText(String name, String message) {
        Allure.addAttachment(name, "text/plain", message);
    }

    public static void logParameter(String key, String value) {
        Allure.parameter(key, value);
    }

    public static void step(String message) {
        Allure.step(message);
    }

    // ✅ Track Last Request JSON Payload (POJO converted)
    public static void setLastRequestJson(Object requestPojo) {
        lastRequestJson = gson.toJson(requestPojo);
    }

    public static String getLastRequestJson() {
        return lastRequestJson;
    }

    public static String getLastResponseBody() {
        return lastResponseBody;
    }

    public static Response getLastResponse() {
        return lastResponse;
    }

    // ✅ NEW: Track request metadata
    public static void setLastRequestURI(String uri) {
        lastRequestURI = uri;
    }

    public static void setLastQueryParams(Map<String, String> params) {
        lastQueryParams = params;
    }

    public static void setLastHeaders(Headers headers) {
        lastHeaders = headers;
    }

    public static String getLastRequestURI() {
        return lastRequestURI;
    }

    public static Map<String, String> getLastQueryParams() {
        return lastQueryParams;
    }

    public static Headers getLastHeaders() {
        return lastHeaders;
    }

    public static void reset() {
        lastRequestJson = null;
        lastResponse = null;
        lastResponseBody = null;
        lastRequestURI = null;
        lastQueryParams = null;
        lastHeaders = null;
    }
}
