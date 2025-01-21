package test.com.api.testng.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RetryFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(RetryFilter.class);

    private final int maxRetries;
    private final long retryIntervalMillis;

    public RetryFilter(int maxRetries, long retryIntervalMillis) {
        this.maxRetries = maxRetries;
        this.retryIntervalMillis = retryIntervalMillis;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        int attempt = 0;
        Response response = null;
        List<String> retryLogs = new ArrayList<>();

        while (attempt < maxRetries) {
            try {
                attempt++;
                logRetryAttempt(attempt, requestSpec, retryLogs);

                // Log the retry interval before making the request
                logRetryInterval(attempt, retryIntervalMillis, retryLogs);

                response = ctx.next(requestSpec, responseSpec);

                if (isSuccessful(response)) {
                    logSuccess(attempt, response, retryLogs);
                    attachRetryLogsToAllure(retryLogs);
                    return response;
                }

                logFailure(attempt, response, retryLogs);
                Thread.sleep(retryIntervalMillis);

            } catch (Exception e) {
                logException(attempt, e, retryLogs);
                try {
                    Thread.sleep(retryIntervalMillis);
                } catch (InterruptedException ignored) {
                }
            }
        }

        logFinalFailure(requestSpec, retryLogs);
        attachRetryLogsToAllure(retryLogs);
        return response;
    }

    private boolean isSuccessful(Response response) {
        return response != null && response.getStatusCode() >= 200 && response.getStatusCode() < 300;
    }

    private void logRetryAttempt(int attempt, FilterableRequestSpecification requestSpec, List<String> retryLogs) {
        long startTime = System.currentTimeMillis();
        retryLogs.add(String.format("Attempt %d: %s %s", attempt, requestSpec.getMethod(), requestSpec.getURI()));

        // Log request headers
        String requestHeaders = requestSpec.getHeaders().toString();
        retryLogs.add(String.format("Request Headers: %s", requestHeaders));

        long elapsedTime = System.currentTimeMillis() - startTime;
        retryLogs.add(String.format("Elapsed Time: %dms", elapsedTime));
    }

    private void logSuccess(int attempt, Response response, List<String> retryLogs) {
        String message = String.format("Attempt %d succeeded with status code: %d", attempt, response.getStatusCode());
        retryLogs.add(message);
        LOGGER.info(message);
    }

    private void logFailure(int attempt, Response response, List<String> retryLogs) {
        String statusCode = response != null ? String.valueOf(response.getStatusCode()) : "null";
        String responseBody = response != null ? response.getBody().asString() : "No response body";
        String explanation = getStatusExplanation(statusCode);
        retryLogs.add(String.format("Attempt %d: Status Code: %s\nResponse Body: %s\nExplanation: %s",
                attempt, statusCode, responseBody, explanation));
        LOGGER.warn("Attempt {}: Status Code: {} Response Body: {} Explanation: {}",
                attempt, statusCode, responseBody, explanation);
    }

    private void logException(int attempt, Exception e, List<String> retryLogs) {
        String message = String.format("Attempt %d failed due to exception: %s", attempt, e.getMessage());
        retryLogs.add(message);
        LOGGER.error(message, e);
    }

    private void logFinalFailure(FilterableRequestSpecification requestSpec, List<String> retryLogs) {
        String message = String.format("All %d attempts failed for request: %s %s", maxRetries, requestSpec.getMethod(),
                requestSpec.getURI());
        retryLogs.add(message);
        LOGGER.error(message);
    }

    private void attachRetryLogsToAllure(List<String> retryLogs) {
        String logs = String.join("\n", retryLogs);
        Allure.addAttachment("Retry Logs", "text/plain", logs);
    }

    private String getStatusExplanation(String statusCode) {
        switch (statusCode) {
            case "401":
                return "Invalid credentials, please check your authorization token.";
            case "403":
                return "Insufficient permissions.";
            case "408":
                return "Request Timeout — The request took too long to process.";
            default:
                return "Possible timeout or network issue.";
        }
    }

    private void logRetryInterval(int attempt, long retryIntervalMillis, List<String> retryLogs) {
        String message = String.format("Retry Interval: %dms", retryIntervalMillis);
        retryLogs.add(message);
        LOGGER.info(message);
    }
}
