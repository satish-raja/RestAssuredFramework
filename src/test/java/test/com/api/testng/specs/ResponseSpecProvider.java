package test.com.api.testng.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

/**
 * Provides response specifications for validating API responses.
 * This class contains methods for creating response specifications that define expected status codes.
 */
public class ResponseSpecProvider {

    /**
     * Provides a common response specification that expects a status code of 200 (OK).
     * This specification is used for validating successful responses from the API.
     *
     * @return a ResponseSpecification for common API responses with status code 200
     */
    public static ResponseSpecification getCommonResponseSpec() {
        return new ResponseSpecBuilder()
            .expectStatusCode(200) // Status code 200
            .build();
    }
}
