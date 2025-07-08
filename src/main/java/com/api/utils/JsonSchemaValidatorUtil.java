package com.api.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;

import java.io.InputStream;

public class JsonSchemaValidatorUtil {

    public static void validateJsonSchema(Response response, String schemaPath ) {
        InputStream schemaStream = Thread.currentThread().getContextClassLoader()
                                           .getResourceAsStream(schemaPath);

        Assert.assertNotNull(schemaStream, "❌ Schema file not found: " + schemaPath);

        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaStream));
    }
}
