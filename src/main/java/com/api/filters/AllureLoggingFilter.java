package com.api.filters;

import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class AllureLoggingFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        long startTime = System.currentTimeMillis();
        Response response = ctx.next(requestSpec, responseSpec);
        long duration = System.currentTimeMillis() - startTime;

        // Request Logging
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("📌 Test Name: ").append(getTestName()).append("\n");
        requestLog.append("📤 Method: ").append(requestSpec.getMethod()).append("\n");
        requestLog.append("🔗 URI: ").append(requestSpec.getURI()).append("\n");
        requestLog.append("📦 Headers:\n").append(maskHeaders(requestSpec.getHeaders())).append("\n");

        Object body = requestSpec.getBody();
        String bodyString = (body != null) ? prettyJson(body.toString()) : "null";
        requestLog.append("📤 Body:\n").append(bodyString).append("\n");

        // Response Logging
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("✅ Status Code: ").append(response.getStatusCode()).append("\n");
        responseLog.append("📥 Headers:\n").append(response.getHeaders().toString()).append("\n");
        responseLog.append("📄 Body:\n").append(prettyJson(response.getBody().asString())).append("\n");
        responseLog.append("⏱️ Duration: ").append(duration).append(" ms\n");

        Allure.addAttachment("🔍 Request", "text/plain", requestLog.toString(), ".txt");
        Allure.addAttachment("📬 Response", "text/plain", responseLog.toString(), ".txt");

        return response;
    }

    private String prettyJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object jsonObj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        } catch (Exception e) {
            return json; // fallback to raw if not JSON
        }
    }

    private String maskHeaders(Headers headers) {
        return headers.asList().stream()
                .map(h -> h.getName().equalsIgnoreCase("Authorization")
                        ? h.getName() + ": *****MASKED*****"
                        : h.getName() + ": " + h.getValue())
                .collect(Collectors.joining("\n"));
    }

    private String getTestName() {
        try {
            return Allure.getLifecycle().getCurrentTestCaseOrStep().orElse("Unknown Test");
        } catch (Exception e) {
            return "Unknown Test";
        }
    }
}
