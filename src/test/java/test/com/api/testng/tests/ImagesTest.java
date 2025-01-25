package test.com.api.testng.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.com.api.testng.models.Image;
import main.com.api.testng.models.UploadImageResponse;
import test.com.api.testng.base.ApiTestBase;
import test.com.api.testng.endpoints.Endpoints;
import test.com.api.testng.specs.RequestSpecProvider;
import test.com.api.testng.utils.APIHelper;
import test.com.api.testng.utils.ImageValidator;
import test.com.api.testng.utils.UploadImageResponseValidator;

@Epic("Image Upload and Management API Tests")
@Feature("Upload, Get and Delete Images")
public class ImagesTest   extends ApiTestBase {

    private static final Logger logger = LogManager.getLogger(ImagesTest.class);
    private RequestSpecification commonRequestSpec;
    private RequestSpecification fileUploadRequestSpec;
    private String imageId;
    private String filename;

    private String getSchemaPath(String schemaName) {
        return "schemas/" + schemaName;
    }

    @BeforeClass
    public void setup() {
    	APIHelper.setBaseURI(Endpoints.BASE_URL);
        commonRequestSpec = RequestSpecProvider.getCommonRequestSpec();
        fileUploadRequestSpec = RequestSpecProvider.getFileUploadRequestSpec();
        logger.info("Base URI set to: " + Endpoints.BASE_URL);
    }

    @Story("Story-01")
    @Test(priority = 1)
    @Description("Test to validate the search functionality for images.")
    @Severity(SeverityLevel.NORMAL)
    @Step("Validating the search functionality for images.")
    public void testSearchImages() {
        logTestStart("testSearchImages");
        APIHelper.sendAPIRequest(commonRequestSpec, Endpoints.SEARCH_IMAGES, "GET", 200, getSchemaPath("SearchImages_Schema.json"));
        logTestCompletion("testSearchImages");
    }

    @Story("Story-02")
    @Test(priority = 2, dependsOnMethods = "testSearchImages", alwaysRun = true)
    @Description("Test to upload an image and validate the response schema.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Uploading image and validating response schema.")
    public void testUploadImage() {
        logTestStart("testUploadImage");

        String projectPath = System.getProperty("user.dir");
        String relativePath = "resources/image_1.jpeg";
        File file = new File(projectPath, relativePath);

        // Check if the file exists and is a regular file
        if (!file.exists() || !file.isFile()) {  
            String errorMessage = "File not found at: " + file.getAbsolutePath();
            logger.error(errorMessage);
            throw new RuntimeException("Required test resource missing: " + relativePath);
        }

        filename = file.getName();

        UploadImageResponse response = APIHelper.uploadImage(fileUploadRequestSpec, file, Endpoints.UPLOAD_IMAGE);
        UploadImageResponseValidator.validate(response, filename);

        imageId = response.getId();
        logger.info("Image uploaded successfully with ID: " + imageId);

        logTestCompletion("testUploadImage");
    }

    @Story("Story-03")
    @Test(priority = 3, dependsOnMethods = "testUploadImage", alwaysRun = true)
    @Description("Test to retrieve image by ID.")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Retrieving image by ID and validating response schema.")
    public void testGetImageById() {
        logTestStart("testGetImageById");
        Image image = APIHelper.getImageById(commonRequestSpec, imageId);
        ImageValidator.validate(image);
        logger.info("Image URL: " + image.getUrl());

        logTestCompletion("testGetImageById");
    }

    @Story("Story-04")
    @Test(priority = 4, dependsOnMethods = "testGetImageById", alwaysRun = true)
    @Description("Test to retrieve image analysis by ID.")
    @Severity(SeverityLevel.NORMAL)
    @Step("Retrieving image analysis by ID and validating response schema.")
    public void testGetImageAnalysisById() {
        logTestStart("testGetImageAnalysisById");
        
        // Get the response
        Response response = APIHelper.getImageAnalysisById(commonRequestSpec, imageId, getSchemaPath("GetImageAnalysisById_Schema.json"));

        // Check if response is null
        if (response == null) {
            logger.error("Received null response from API for image analysis with ID: " + imageId);
            Assert.fail("API call to retrieve image analysis returned null");
        } else {
            // Additional assertions can go here if needed
            logger.info("Received response for image analysis: " + response.asString());
        }

        logTestCompletion("testGetImageAnalysisById");
    }


    @Story("Story-05")
    @Test(priority = 5, dependsOnMethods = "testGetImageAnalysisById", alwaysRun = true)
    @Description("Test to get a list of images and validate the response.")
    @Severity(SeverityLevel.NORMAL)
    @Step("Retrieving list of images and validating response schema.")
    public void testGetImages() {
        logTestStart("testGetImages");
        String expectedUrl = "https://cdn2.thecatapi.com/images/" + imageId + ".jpg";
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("limit", 10);
        queryParams.put("page", 0);
        queryParams.put("order", "DESC");

        APIHelper.getImages(commonRequestSpec, queryParams, getSchemaPath("GetImages_Schema.json"), imageId, expectedUrl, filename);

        logTestCompletion("testGetImages");
    }

    @Story("Story-06")
    @Test(priority = 6, dependsOnMethods = {"testUploadImage", "testGetImages"}, alwaysRun = true)
    @Description("Test to delete image by ID.")
    @Severity(SeverityLevel.BLOCKER)
    @Step("Deleting image by ID and validating response.")
    public void testDeleteImage() {
        logTestStart("testDeleteImage");
        APIHelper.deleteImage(commonRequestSpec, imageId, 204);
        logTestCompletion("testDeleteImage");
    }
}
