package test.com.api.testng.tests;

import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import main.com.api.testng.models.AuthRequest;
import main.com.api.testng.models.BookingData;
import main.com.api.testng.models.CreateBookingResponse;
import main.com.api.testng.models.CreateUpdateBookingRequest;
import main.com.api.testng.models.UpdateBookingResponse;
import test.com.api.testng.base.ApiTestBase;
import test.com.api.testng.endpoints.Endpoints;
import test.com.api.testng.specs.RequestSpecProvider;
import test.com.api.testng.utils.APIHelper;


@Epic("Booking Management API Tests")
@Feature("API Methods POST, GET, PUT, PATCH and DELETE")
public class BookingTest extends ApiTestBase {

    private static final Logger logger = LogManager.getLogger(BookingTest.class);

    private static String token;
    private static int bookingId;
    private BookingData bookingData = new BookingData();
    private BookingData updateBookingData = new BookingData();
    private UpdateBookingResponse responseBooking = new UpdateBookingResponse();


    /**
     * Test to ping the health check endpoint.
     */
    @Description("Test to ping the health check endpoint.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story-01")
    @Step("Executing health check ping test")
    @Test(priority = 1)  
    public void testPingHealthCheck() {
        logTestStart("testPingHealthCheck");
        RequestSpecification requestSpec = RequestSpecProvider.getCommonRequestSpec(Endpoints.BOOKING_BASE_URL);
        Response response = APIHelper.sendGetRequest(requestSpec, Endpoints.PING_ENDPOINT, 201);
        assertEquals(response.asString(), "Created");
        logTestCompletion("testPingHealthCheck");
    }

    /**
     * Test to authenticate and create a token.
     */
    @Description("Test to authenticate and create a token.")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Story-02")
    @Step("Authenticating and creating a token")
    @Test(priority = 2, dependsOnMethods = "testPingHealthCheck", alwaysRun = true)
    public void testAuthCreateToken() {
        logTestStart("testAuthCreateToken");

        // Fetch authentication token request payload
        AuthRequest authRequest = fetchAuthToken();
        RequestSpecification requestSpec = RequestSpecProvider.getCommonRequestSpec(Endpoints.BOOKING_BASE_URL, ContentType.JSON, authRequest);
        
        // Send the authentication request and get the response
        Response response = APIHelper.sendPostRequest(requestSpec, Endpoints.AUTH_ENDPOINT, 200);

        // Extract the token from the response
        token = response.path("token");

        // Ensure token is not null
        if (token == null) {
            logTestFailure("Token is null. Response: " + response.asString());
            Assert.fail("Token is null");
        }

        logTestCompletion("testAuthCreateToken");
    }

    /**
     * Test to create a new booking.
     */
    @Description("Test to create a new booking.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story-03")
    @Step("Creating a new booking")
    @Test(priority = 3, dependsOnMethods = "testAuthCreateToken", alwaysRun = true)
    public void testCreateBooking() {
        logTestStart("testCreateBooking");
        // Generate and populate booking data
        bookingData = generateAndPopulateBookingData();        
      // Call the method to create booking request from faker data
      CreateUpdateBookingRequest createBookingRequest = createBookingRequestFromFakerData(bookingData);
      // Create RequestSpecification with the passed header and content type
      RequestSpecification requestSpec = RequestSpecProvider.getCommonRequestSpec(Endpoints.BOOKING_BASE_URL,ContentType.JSON,createBookingRequest);
      // Send POST request to create booking
        Response response = APIHelper.sendPostRequest(requestSpec, Endpoints.BASE_ENDPOINT, 200);
        // Validate response schema
        APIHelper.validateResponseSchema(response, Endpoints.CREATE_BOOKING_SCHEMA);
        // Validate and deserialize the response using helper methods
        CreateBookingResponse responseBooking = APIHelper.validateAndDeserializeResponse(
            response, CreateBookingResponse.class
        );

        // Extract the booking ID
        bookingId = responseBooking.getBookingid();

        // Test-specific assertions
        if (bookingId == 0) {
            Assert.fail("Booking ID is null or invalid");
        }
        Assert.assertEquals(responseBooking.getBooking().getFirstname(), bookingData.getFirstname(), "First name mismatch");
        Assert.assertEquals(responseBooking.getBooking().getLastname(), bookingData.getLastname(), "Last name mismatch");
        Assert.assertEquals(responseBooking.getBooking().getTotalprice(), bookingData.getTotalprice(), "Total price mismatch");
        Assert.assertEquals(responseBooking.getBooking().isDepositpaid(), bookingData.isDepositpaid(), "Deposit paid status mismatch");
        Assert.assertEquals(responseBooking.getBooking().getBookingdates().getCheckin(), bookingData.getBookingdates().getCheckin(), "Check-in date mismatch");
        Assert.assertEquals(responseBooking.getBooking().getBookingdates().getCheckout(), bookingData.getBookingdates().getCheckout(), "Check-out date mismatch");
        Assert.assertEquals(responseBooking.getBooking().getAdditionalneeds(), bookingData.getAdditionalneeds(), "Additional needs mismatch");

        logTestCompletion("testCreateBooking");
    }

    /**
     * Test to get the details of a specific booking.
     */
    @Description("Test to get the details of a specific booking.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Story-04")
    @Step("Retrieving booking details")
    @Test(priority = 4, dependsOnMethods = "testCreateBooking", alwaysRun = true)
    public void testGetBookingDetails() {
        logTestStart("testGetBookingDetails");
        // Create RequestSpecification with the passed header and content type
        RequestSpecification requestSpec = RequestSpecProvider.getCommonRequestSpec(Endpoints.BOOKING_BASE_URL);
        Response response = APIHelper.sendGetRequest(requestSpec, Endpoints.BOOKING_BY_ID.replace("{bookingId}", String.valueOf(bookingId)), 200);
        // Validate response schema
        APIHelper.validateResponseSchema(response, Endpoints.BOOKING_DETAILS_SCHEMA);
        // Validate and deserialize the response using helper methods
        responseBooking = APIHelper.validateAndDeserializeResponse(
            response, UpdateBookingResponse.class
        );
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

    /**
     * Test to update an existing booking.
     */
    @Description("Test to update an existing booking.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story-05")
    @Step("Updating an existing booking")
    @Test(priority = 5, dependsOnMethods = "testCreateBooking", alwaysRun = true)
    public void testUpdateBooking() {
        logTestStart("testUpdateBooking");
        // Generate and populate booking data
        updateBookingData = generateAndPopulateBookingData();
        // Call the method to create booking request from faker data
        CreateUpdateBookingRequest updateBookingRequest = createBookingRequestFromFakerData(updateBookingData);
        // Create RequestSpecification with the passed header and content type
        RequestSpecification requestSpec = RequestSpecProvider.getCommonRequestSpec(Endpoints.BOOKING_BASE_URL,"cookie", "token=" + token, ContentType.JSON,updateBookingRequest);
        // Send PUT request to update booking
        Response response = APIHelper.sendPutRequest(requestSpec, Endpoints.BOOKING_BY_ID.replace("{bookingId}", String.valueOf(bookingId)), 200);
        // Validate response schema
        APIHelper.validateResponseSchema(response, Endpoints.BOOKING_DETAILS_SCHEMA);
        // Validate and deserialize the response using helper methods
        responseBooking = APIHelper.validateAndDeserializeResponse(response, UpdateBookingResponse.class);
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

    /**
     * Test to partially update an existing booking.
     */
    @Description("Test to partially update an existing booking.")
    @Severity(SeverityLevel.MINOR)
    @Story("Story-06")
    @Step("Partially updating a booking")
    @Test(priority = 6, dependsOnMethods = "testCreateBooking", alwaysRun = true)
    public void testPartialUpdateBooking() {
        logTestStart("testPartialUpdateBooking");
        // Call the method to generate partial update booking data
        Map<String, Object> partialUpdateBookingDataMap = generatePartialUpdateBookingData();        
        // Create RequestSpecification with the passed header and content type
        RequestSpecification requestSpec = RequestSpecProvider.getCommonRequestSpec(Endpoints.BOOKING_BASE_URL,"cookie", "token=" + token, ContentType.JSON,partialUpdateBookingDataMap);
        // Send PATCH request to partially update the booking
        Response response = APIHelper.sendPatchRequest(requestSpec, Endpoints.BOOKING_BY_ID.replace("{bookingId}", String.valueOf(bookingId)), 200);
        // Validate response schema
        APIHelper.validateResponseSchema(response, Endpoints.BOOKING_DETAILS_SCHEMA);
        // Validate and deserialize the response using helper methods
        responseBooking = APIHelper.validateAndDeserializeResponse(response, UpdateBookingResponse.class);
        // Verify that the response contains expected values
        Assert.assertEquals(responseBooking.getAdditionalneeds(), partialUpdateBookingDataMap.get("additionalneeds"));
        logTestCompletion("testPartialUpdateBooking");
    }    

    /**
     * Test to delete a booking.
     */
    @Description("Test to delete a booking.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Story-07")
    @Step("Deleting a booking")
    @Test(priority = 7, dependsOnMethods = "testCreateBooking", alwaysRun = true)
    public void testDeleteBooking() {
        logTestStart("testDeleteBooking");
        
     // Create RequestSpecification with the passed header and content type
        RequestSpecification requestSpec = RequestSpecProvider.getCommonRequestSpec(Endpoints.BOOKING_BASE_URL,"cookie", "token=" + token, ContentType.JSON);
        // Send DELETE request to delete the booking
        Response response = APIHelper.sendDeleteRequest(requestSpec, Endpoints.BOOKING_BY_ID.replace("{bookingId}", String.valueOf(bookingId)), 201);
        assertEquals(response.asString(), "Created");
        logTestCompletion("testDeleteBooking");
    }
}
