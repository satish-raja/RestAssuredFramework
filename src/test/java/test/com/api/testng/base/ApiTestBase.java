package test.com.api.testng.base;

import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;
import test.com.api.testng.utils.RetryFilter;

public class ApiTestBase {

    @BeforeSuite
    public void setupBeforeSuite() {
        // Register RetryFilter globally for all tests
        RestAssured.filters(new RetryFilter(3, 2000)); // 3 retries, 2-second interval
    }
}
