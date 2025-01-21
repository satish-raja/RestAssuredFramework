package test.com.api.testng.tests;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import main.com.api.testng.models.AuthRequest;
import main.com.api.testng.models.BookingData;
import main.com.api.testng.models.CreateBookingResponse;
import main.com.api.testng.models.CreateUpdateBookingRequest;
import main.com.api.testng.models.UpdateBookingResponse;
import test.com.api.testng.base.ApiTestBase;
import test.com.api.testng.endpoints.Endpoints;
import test.com.api.testng.utils.AllureRestAssuredUtil;
import test.com.api.testng.utils.ConfigLoader;
import test.com.api.testng.utils.FakerDataGenerator;

@Epic("Booking Management API Tests")
@Feature("API Methods POST, GET, PUT, PATCH and DELETE")
public class BookingTest extends ApiTestBase {

    private static final Logger logger = LogManager.getLogger(BookingTest.class);

    static {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    private static String token;
    private static int bookingId;
    private Faker fake = new Faker();
    private BookingData bookingData = new BookingData();

    private void logTestStart(String testName) {
        logger.info("TEST: " + testName + " execution started");
    }

    private void logTestCompletion(String testName) {
        logger.info("TEST: " + testName + " execution completed successfully");
    }
    
    public void logTestFailure(String message) {
        logger.error(message);
    }

    @Description("Test to ping the health check endpoint.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story-01")
    @Step("Executing health check ping test")
    @Test(priority = 1)
    public void testPingHealthCheck() {
        logTestStart("testPingHealthCheck");
        String resp = given()
                .filter(AllureRestAssuredUtil.getAllureFilter())
            .when()
                .get(Endpoints.PING_ENDPOINT)
            .then()
            	.log().body()
                .assertThat()
                .statusCode(201)
                .extract().response().asString();
        assertEquals(resp, "Created");
        logTestCompletion("testPingHealthCheck");
    }

    @Description("Test to authenticate and create a token.")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Story-02")
    @Step("Authenticating and creating a token")
    @Test(priority = 2, dependsOnMethods = "testPingHealthCheck", alwaysRun = true)
    public void testAuthCreateToken() {
        logTestStart("testAuthCreateToken");

        // Fetch credentials from the properties file
        String username = ConfigLoader.getProperty("API__BOOKINGTEST_USERNAME");
        String password = ConfigLoader.getProperty("API__BOOKINGTEST_PASSWORD");

        // Ensure credentials are not null or empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            Assert.fail("Username or password is null or empty");
        }

        // Using the AuthRequest POJO
        AuthRequest authRequest = new AuthRequest(username, password);

        // RestAssured automatically serializes the AuthRequest object to JSON
        Response resp = given()
                            .filter(AllureRestAssuredUtil.getAllureFilter())
                            .header("Content-Type", "application/json")
                            .body(authRequest) // AuthRequest is automatically serialized to JSON
                            .log().body()
                        .when()
                            .post(Endpoints.AUTH_ENDPOINT)
                        .then()
                            .log().body()
                            .assertThat()
                            .statusCode(200)
                            .extract().response();

        // Extracting the token from the response
        token = resp.path("token");

        // Ensure token is not null
        if (token == null) {
            logTestFailure("Token is null. Response: " + resp.asString());
            Assert.fail("Token is null");
        }

        logTestCompletion("testAuthCreateToken");
    }

    @Description("Test to create a new booking.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story-03")
    @Step("Creating a new booking")
    @Test(priority = 3, dependsOnMethods = "testAuthCreateToken", alwaysRun = true)
    public void testCreateBooking() {
        logTestStart("testCreateBooking");

        // Instantiate FakerDataGenerator to generate random booking data
        FakerDataGenerator fakerDataGenerator = new FakerDataGenerator();
        Map<String, Object> bookingDataMap = fakerDataGenerator.generate_booking_data();

        // Create BookingData object and populate it from the Faker data
        bookingData.setFirstname((String) bookingDataMap.get("firstname"));
        bookingData.setLastname((String) bookingDataMap.get("lastname"));
        bookingData.setTotalprice((Integer) bookingDataMap.get("totalprice"));
        bookingData.setDepositpaid((Boolean) bookingDataMap.get("depositpaid"));

        // Set booking dates with type checking
        Object bookingDatesObject = bookingDataMap.get("bookingdates");
        if (bookingDatesObject instanceof Map) {
            Map<?, ?> bookingDatesMap = (Map<?, ?>) bookingDatesObject;
            
            // Ensure the Map contains String keys and values
            if (bookingDatesMap instanceof Map<?, ?>) {
                CreateUpdateBookingRequest.BookingDates bookingDates = new CreateUpdateBookingRequest.BookingDates();
                bookingDates.setCheckin((String) bookingDatesMap.get("checkin"));
                bookingDates.setCheckout((String) bookingDatesMap.get("checkout"));
                bookingData.setBookingdates(bookingDates);
            } else {
                // Log error if Map is not of the correct type
            	logger.error("Expected a Map<String, String> for booking dates, but received a different type.");
            }
        } else {
        	logger.error("Booking dates data is not of expected type Map<String, String>.");
        }

        bookingData.setAdditionalneeds((String) bookingDataMap.get("additionalneeds"));

        // Create the request object and populate it from BookingData
        CreateUpdateBookingRequest createBookingRequest = new CreateUpdateBookingRequest();
        createBookingRequest.populateFromBookingData(bookingData);

        // Send POST request to create booking
        Response resp = given()
                            .filter(AllureRestAssuredUtil.getAllureFilter())
                            .header("Content-Type", "application/json")
                            .body(createBookingRequest)
                            .log().body()
                        .when()
                            .post(Endpoints.BASE_ENDPOINT)
                        .then()
                            .log().body()
                            .body(matchesJsonSchemaInClasspath("schemas/CreateBooking_Schema.json"))
                            .assertThat()
                            .statusCode(200)
                            .extract().response();

        // Deserialize the response to CreateUpdateBookingResponse class
        CreateBookingResponse responseBooking = resp.as(CreateBookingResponse.class);

        // Verify that the response contains expected values
        bookingId = responseBooking.getBookingid();
        if (bookingId == 0) {
            Assert.fail("Booking ID is null or invalid");
        }
        Assert.assertEquals(responseBooking.getBooking().getFirstname(), bookingData.getFirstname());
        Assert.assertEquals(responseBooking.getBooking().getLastname(), bookingData.getLastname());
        Assert.assertEquals(responseBooking.getBooking().getTotalprice(), bookingData.getTotalprice());
        Assert.assertEquals(responseBooking.getBooking().isDepositpaid(), bookingData.isDepositpaid());
        Assert.assertEquals(responseBooking.getBooking().getBookingdates().getCheckin(), bookingData.getBookingdates().getCheckin());
        Assert.assertEquals(responseBooking.getBooking().getBookingdates().getCheckout(), bookingData.getBookingdates().getCheckout());
        Assert.assertEquals(responseBooking.getBooking().getAdditionalneeds(), bookingData.getAdditionalneeds());

        logTestCompletion("testCreateBooking");
    }

    @Description("Test to get the details of a specific booking.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Story-04")
    @Step("Retrieving booking details")
    @Test(priority = 4, dependsOnMethods = "testCreateBooking", alwaysRun = true)
    public void testGetBookingDetails() {
        logTestStart("testGetBookingDetails");
        Response resp = given()
        					.filter(AllureRestAssuredUtil.getAllureFilter())
        				.when()
        					.get(Endpoints.BOOKING_BY_ID.replace("{bookingId}", String.valueOf(bookingId)))
        				.then()
                        	.log().body()
				            .body(matchesJsonSchemaInClasspath("schemas/GetBookingDetails_Schema.json"))
				            .assertThat()
				            .statusCode(200)
				            .extract().response();
        
        // Deserialize the response to CreateUpdateBookingResponse class
        UpdateBookingResponse responseBooking = resp.as(UpdateBookingResponse.class);

        // Verify that the response contains expected values
        Assert.assertEquals(responseBooking.getFirstname(), bookingData.getFirstname());
        Assert.assertEquals(responseBooking.getLastname(), bookingData.getLastname());
        Assert.assertEquals(responseBooking.getTotalprice(), bookingData.getTotalprice());
        Assert.assertEquals(responseBooking.isDepositpaid(), bookingData.isDepositpaid());
        Assert.assertEquals(responseBooking.getBookingdates().getCheckin(), bookingData.getBookingdates().getCheckin());
        Assert.assertEquals(responseBooking.getBookingdates().getCheckout(), bookingData.getBookingdates().getCheckout());
        Assert.assertEquals(responseBooking.getAdditionalneeds(), bookingData.getAdditionalneeds());
        
        
        logTestCompletion("testGetBookingDetails");
    }

    @Description("Test to update an existing booking.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story-05")
    @Step("Updating an existing booking")
    @Test(priority = 5, dependsOnMethods = "testCreateBooking", alwaysRun = true)
    public void testUpdateBooking() {
        logTestStart("testUpdateBooking");

        // Instantiate FakerDataGenerator to generate random booking data
        FakerDataGenerator fakerDataGenerator = new FakerDataGenerator();
        Map<String, Object> updateBookingDataMap = fakerDataGenerator.generate_booking_data();

        // Create BookingData object and populate it from the Faker data
        BookingData updateBookingData = new BookingData();
        updateBookingData.setFirstname((String) updateBookingDataMap.get("firstname"));
        updateBookingData.setLastname((String) updateBookingDataMap.get("lastname"));
        updateBookingData.setTotalprice((Integer) updateBookingDataMap.get("totalprice"));
        updateBookingData.setDepositpaid((Boolean) updateBookingDataMap.get("depositpaid"));

        // Set booking dates with type checking
        Object bookingDatesObject = updateBookingDataMap.get("bookingdates");
        if (bookingDatesObject instanceof Map) {
            Map<?, ?> bookingDatesMap = (Map<?, ?>) bookingDatesObject;
            
            // Ensure the Map contains String keys and values
            if (bookingDatesMap instanceof Map<?, ?>) {
                CreateUpdateBookingRequest.BookingDates bookingDates = new CreateUpdateBookingRequest.BookingDates();
                bookingDates.setCheckin((String) bookingDatesMap.get("checkin"));
                bookingDates.setCheckout((String) bookingDatesMap.get("checkout"));
                updateBookingData.setBookingdates(bookingDates);
            } else {
                // Log error if Map is not of the correct type
                logger.error("Expected a Map<String, String> for booking dates, but received a different type.");
            }
        } else {
            logger.error("Booking dates data is not of expected type Map<String, String>.");
        }

        updateBookingData.setAdditionalneeds((String) updateBookingDataMap.get("additionalneeds"));

        // Create the request object and populate it from BookingData
        CreateUpdateBookingRequest updateBookingRequest = new CreateUpdateBookingRequest();
        updateBookingRequest.populateFromBookingData(updateBookingData);

        // Send PUT request to update booking
        Response resp = given()
                            .filter(AllureRestAssuredUtil.getAllureFilter())
                            .header("Content-Type", "application/json")
                            .header("cookie", "token=" + token)
                            .body(updateBookingRequest)
                            .log().body()
                        .when()
                            .put(Endpoints.BOOKING_BY_ID.replace("{bookingId}", String.valueOf(bookingId)))
                        .then()
                            .log().body()
                            .body(matchesJsonSchemaInClasspath("schemas/GetBookingDetails_Schema.json"))
                            .assertThat()
                            .statusCode(200)
                            .extract().response();

        // Deserialize the response to CreateUpdateBookingResponse class
        UpdateBookingResponse responseBooking = resp.as(UpdateBookingResponse.class);

        // Verify that the response contains expected values
        Assert.assertEquals(responseBooking.getFirstname(), updateBookingData.getFirstname());
        Assert.assertEquals(responseBooking.getLastname(), updateBookingData.getLastname());
        Assert.assertEquals(responseBooking.getTotalprice(), updateBookingData.getTotalprice());
        Assert.assertEquals(responseBooking.isDepositpaid(), updateBookingData.isDepositpaid());
        Assert.assertEquals(responseBooking.getBookingdates().getCheckin(), updateBookingData.getBookingdates().getCheckin());
        Assert.assertEquals(responseBooking.getBookingdates().getCheckout(), updateBookingData.getBookingdates().getCheckout());
        Assert.assertEquals(responseBooking.getAdditionalneeds(), updateBookingData.getAdditionalneeds());

        logTestCompletion("testUpdateBooking");
    }


    @Description("Test to partially update an existing booking.")
    @Severity(SeverityLevel.MINOR)
    @Story("Story-06")
    @Step("Partially updating a booking")
    @Test(priority = 6, dependsOnMethods = "testCreateBooking", alwaysRun = true)
    public void testPartialUpdateBooking() {
        logTestStart("testPartialUpdateBooking");

        Map<String, Object> partialUpdateBookingDataMap = new HashMap<>();
     // List of real-time additional needs
        List<String> additionalNeedsList = Arrays.asList(
            "Breakfast", "Late check-out", "Extra pillows", "Wheelchair access", "Non-smoking room",
            "Pet-friendly room", "Air conditioning", "Airport shuttle", "Massage service", "Vegetarian meals"
        );

        // Randomly select an additional need from the list
        partialUpdateBookingDataMap.put("additionalneeds", additionalNeedsList.get(fake.number().numberBetween(0, additionalNeedsList.size())));


        // Create BookingData object and populate it from the Faker data
        UpdateBookingResponse updateBookingResponse = new UpdateBookingResponse();
        updateBookingResponse.setAdditionalneeds((String) partialUpdateBookingDataMap.get("additionalneeds"));

        // Send PATCH request to partially update the booking
        Response resp = given()
                            .filter(AllureRestAssuredUtil.getAllureFilter())
                            .header("Content-Type", "application/json")
                            .header("cookie", "token=" + token)
                            .body(partialUpdateBookingDataMap)
                            .log().body()
                        .when()
                            .patch(Endpoints.BOOKING_BY_ID.replace("{bookingId}", String.valueOf(bookingId)))
                        .then()
                            .log().body()
                            .body(matchesJsonSchemaInClasspath("schemas/GetBookingDetails_Schema.json"))
                            .assertThat()
                            .statusCode(200)
                            .extract().response();

        // Deserialize the response to UpdateBookingResponse class
        UpdateBookingResponse responseBooking = resp.as(UpdateBookingResponse.class);

        // Verify that the response contains expected values
        Assert.assertEquals(responseBooking.getAdditionalneeds(), updateBookingResponse.getAdditionalneeds());

        logTestCompletion("testPartialUpdateBooking");
    }


    @Description("Test to delete a booking.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story-07")
    @Step("Deleting a booking")
    @Test(priority = 7, dependsOnMethods = "testCreateBooking", alwaysRun = true)
    public void testDeleteBooking() {
        logTestStart("testDeleteBooking");
        String resp = given()
        		.filter(AllureRestAssuredUtil.getAllureFilter())        		
                .header("Content-Type", "application/json")
                .header("cookie", "token=" + token)
            .when()
                .delete(Endpoints.BOOKING_BY_ID.replace("{bookingId}", String.valueOf(bookingId)))
            .then()
	            .log().body()
	            .assertThat()
	            .statusCode(201)
	            .extract().response().asString();
        assertEquals(resp, "Created");
        logTestCompletion("testDeleteBooking");
    }
}
