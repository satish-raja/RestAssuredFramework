package test.com.api.testng.utils;

import static io.restassured.RestAssured.given; // Import RestAssured's given() method
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

import java.io.File;
import java.util.Map;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.com.api.testng.models.Image;
import main.com.api.testng.models.UploadImageResponse;
import test.com.api.testng.endpoints.Endpoints;

public class APIHelper {

    // Set the base URI for API requests
    public static void setBaseURI(String baseURI) {
        io.restassured.RestAssured.baseURI = baseURI;
    }

    // Sending GET or POST request and validating schema
    public static Response sendAPIRequest(RequestSpecification spec, String endpoint, String method, int expectedStatusCode, String schemaPath) {
        Response response = given(spec)
                .filter(AllureRestAssuredUtil.getAllureFilter())
                .when()
                    .request(method, endpoint)
                .then()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath))
                    .statusCode(expectedStatusCode)
                    .extract().response();
        return response;
    }

    // Upload image
    public static UploadImageResponse uploadImage(RequestSpecification spec, File file, String endpoint) {
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("File does not exist at: " + file.getAbsolutePath());
        }

        Response response = given(spec)
                .multiPart("file", file, "image/jpeg")
            .when()
                .post(endpoint)
            .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/UploadImage_Schema.json"))
                .statusCode(201)
                .extract().response();

        return response.as(UploadImageResponse.class);
    }

    // Get Image by ID
    public static Image getImageById(RequestSpecification spec, String imageId) {
        Response response = given(spec)
                .when()
                    .get(Endpoints.IMAGE_BY_ID.replace("{id}", imageId))
                .then()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/GetImageById_Schema.json"))
                    .statusCode(200)
                    .extract().response();

        return response.as(Image.class);
    }

 // Get Image analysis by ID
    public static Response getImageAnalysisById(RequestSpecification spec, String imageId, String schemaPath) {
        return given(spec)
                .when()
                    .get(Endpoints.IMAGE_ANALYSIS_BY_ID.replace("{id}", imageId))
                .then()
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath))
                    .statusCode(200)
                    .body("[0].moderation_labels", is(empty()))
                    .body("[0].vendor", equalTo("AWS Rekognition"))
                    .body("[0].image_id", equalTo(imageId))
                    .body("[0].created_at", matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z"))
                .extract().response(); // Extract and return the response
    }


    // Get Images
    public static void getImages(RequestSpecification spec, Map<String, Object> queryParams, String schemaPath, String imageId, String expectedUrl, String filename) {
        given(spec)
            .queryParams(queryParams)
        .when()
            .get(Endpoints.IMAGES)
        .then()
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath))
            .statusCode(200)
            .body("id", hasItem(imageId))
            .body("url", hasItem(expectedUrl))
            .body("original_filename", hasItem(filename));
    }

    // Delete Image
    public static void deleteImage(RequestSpecification spec, String imageId, int expectedStatusCode) {
        given(spec)
        .when()
            .delete(Endpoints.IMAGE_BY_ID.replace("{id}", imageId))
        .then()
            .statusCode(expectedStatusCode);
    }

    // Send GET request
    public static Response sendGetRequest(String endpoint) {
        return given().when().get(endpoint);
    }

    // Send POST request
    public static Response sendPostRequest(String endpoint, Map<String, Object> body) {
        return given()
                .body(body)
            .when()
                .post(endpoint);
    }

    // Send PUT request
    public static Response sendPutRequest(String endpoint, Map<String, Object> body, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .body(body)
            .when()
                .put(endpoint);
    }

    // Send DELETE request
    public static Response sendDeleteRequest(String endpoint, int id, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
            .when()
                .delete(endpoint.replace("{bookingId}", String.valueOf(id)));
    }
}
