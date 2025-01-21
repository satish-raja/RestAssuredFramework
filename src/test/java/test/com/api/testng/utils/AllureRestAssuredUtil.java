package test.com.api.testng.utils;

import io.qameta.allure.restassured.AllureRestAssured;

public class AllureRestAssuredUtil {

    private static final AllureRestAssured allureRestAssuredFilter;

    static {
        allureRestAssuredFilter = new AllureRestAssured()
            .setRequestTemplate("my-http-request.ftl") // Customize the request template
            .setResponseTemplate("my-http-response.ftl") // Customize the response template
            .setRequestAttachmentName("Here is what we asked") // Custom request name
            .setResponseAttachmentName("Here is what we got"); // Custom response name
    }

    // Provide a public static method to access the filter
    public static AllureRestAssured getAllureFilter() {
        return allureRestAssuredFilter;
    }
}
