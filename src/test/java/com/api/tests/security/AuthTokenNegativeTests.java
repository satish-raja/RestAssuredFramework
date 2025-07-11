package com.api.tests.security;

import static com.api.constants.APIConstants.*;

import com.api.tests.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Epic("🔐 Security & Authorization")
@Feature("🔑 Auth Token Validation")
@Owner("Satish Raja")
public class AuthTokenNegativeTests extends BaseTest {

    private static final int DUMMY_USER_ID = 123456;

    @Test(groups = "security", description = "🔴 Accessing API without token returns 401 or 404")
    @Story("❌ Missing Token Access")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that API access without Authorization token fails with 401 or 404")
    public void testMissingToken_ShouldReturn401Or404() {
        Allure.step("🧪 Trying to fetch user without Authorization token");
        
        Response response = given()
                .baseUri(BASE_URL)
                .basePath("/users/" + DUMMY_USER_ID)
                .header("Accept", "application/json")
                .when()
                .get()
                .then()
                .extract()
                .response();

        Allure.step("🔐 Response status: " + response.statusCode());
        Allure.attachment("Response Body", response.asPrettyString());

        int status = response.statusCode();
        Assert.assertTrue(
                status == STATUS_CODE_UNAUTHORIZED || status == STATUS_CODE_NOT_FOUND,
                "Expected 401 or 404 for missing token, but got: " + status
        );
    }

    @Test(groups = "security", description = "🔴 Accessing API with invalid token returns 401")
    @Story("❌ Invalid Token Access")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that API access with an invalid token fails with 401 Unauthorized")
    public void testInvalidToken_ShouldReturn401() {
        String invalidToken = "Bearer invalid_token_123";

        Allure.step("🧪 Trying to fetch user with invalid token");

        Response response = given()
                .baseUri(BASE_URL)
                .basePath("/users/" + DUMMY_USER_ID)
                .header("Accept", "application/json")
                .header("Authorization", invalidToken)
                .when()
                .get()
                .then()
                .extract()
                .response();

        Allure.step("🔐 Response status: " + response.statusCode());
        Allure.attachment("Response Body", response.asPrettyString());

        Assert.assertEquals(response.statusCode(), STATUS_CODE_UNAUTHORIZED, "Expected 401 for invalid token");
    }

    @Test(enabled = true, groups = "security", description = "🔴 Expired token - returns 401 or 403")
    @Story("❌ Expired Token Access")
    @Severity(SeverityLevel.MINOR)
    @Description("Simulates behavior when accessing with an expired token (401 or 403)")
    public void testExpiredToken_ShouldReturn401Or403() {
    	throw new SkipException("Skipping test — expired token not available");
    }
}
