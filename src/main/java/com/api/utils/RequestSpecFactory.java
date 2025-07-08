package com.api.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;

public class RequestSpecFactory {

    public static RequestSpecification getRequestSpecWithToken() {
        return new RequestSpecBuilder()
            .setBaseUri("https://gorest.co.in/public/v2")
            .setContentType(ContentType.JSON)
            .addHeader("Authorization", "Bearer " + System.getProperty("api.token", "your_actual_token_here"))
            .build();
    }

    public static RequestSpecification getRequestSpecWithoutToken() {
        return new RequestSpecBuilder()
            .setBaseUri("https://gorest.co.in/public/v2")
            .setContentType(ContentType.JSON)
            .build();
    }
}
