package test.com.api.testng.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;
import main.com.api.testng.models.AuthRequest;
import main.com.api.testng.models.BookingData;
import main.com.api.testng.models.CreateUpdateBookingRequest;
import test.com.api.testng.tests.BookingTest;
import test.com.api.testng.utils.ConfigLoader;
import test.com.api.testng.utils.FakerDataGenerator;
import test.com.api.testng.utils.RetryFilter;

public class ApiTestBase {
	
	private static final Logger logger = LogManager.getLogger(ApiTestBase.class);

    @BeforeSuite
    public void setupBeforeSuite() {
        // Register RetryFilter globally for all tests
        RestAssured.filters(new RetryFilter(3, 2000)); // 3 retries, 2-second interval
    }
    
    /**
     * Logs the start of a test.
     *
     * @param testName the name of the test being executed
     */
    protected void logTestStart(String testName) {
        logger.info("TEST: " + testName + " execution started");
    }

    /**
     * Logs the completion of a test.
     *
     * @param testName the name of the test completed
     */
    protected void logTestCompletion(String testName) {
        logger.info("TEST: " + testName + " execution completed successfully");
    }
    
    /**
     * Logs a test failure message.
     *
     * @param message the failure message to log
     */
    protected void logTestFailure(String message) {
        logger.error(message);
    }
    
 // Method to fetch the token
    public static AuthRequest fetchAuthToken() {
    	
        // Fetch credentials from the properties file
        String username = ConfigLoader.getProperty("API__BOOKINGTEST_USERNAME");
        String password = ConfigLoader.getProperty("API__BOOKINGTEST_PASSWORD");

        // Ensure credentials are not null or empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            Assert.fail("Username or password is null or empty");
        }

        // Create the AuthRequest POJO
        AuthRequest authRequest = new AuthRequest(username, password);
        
        return authRequest ;

    }
    
    
 // Method to generate and populate booking data
    public static BookingData generateAndPopulateBookingData() {
        // Instantiate FakerDataGenerator to generate random booking data
        FakerDataGenerator fakerDataGenerator = new FakerDataGenerator();
        Map<String, Object> bookingDataMap = fakerDataGenerator.generate_booking_data();

        // Create BookingData object
        BookingData bookingData = new BookingData();

        // Populate bookingData from the Faker data
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
                System.err.println("Expected a Map<String, String> for booking dates, but received a different type.");
            }
        } else {
            System.err.println("Booking dates data is not of expected type Map<String, String>.");
        }

        bookingData.setAdditionalneeds((String) bookingDataMap.get("additionalneeds"));

        return bookingData;
    }

    
    public static CreateUpdateBookingRequest createBookingRequestFromFakerData(BookingData bookingData) {

        // Create the request object and populate it from BookingData
        CreateUpdateBookingRequest createBookingRequest = new CreateUpdateBookingRequest();
        createBookingRequest.populateFromBookingData(bookingData);

        return createBookingRequest;
    }
    
 // Method to generate random additional need for partial update
    public static Map<String, Object> generatePartialUpdateBookingData() {
        Map<String, Object> partialUpdateBookingDataMap = new HashMap<>();

        // List of real-time additional needs
        List<String> additionalNeedsList = Arrays.asList(
            "Breakfast", "Late check-out", "Extra pillows", "Wheelchair access", "Non-smoking room",
            "Pet-friendly room", "Air conditioning", "Airport shuttle", "Massage service", "Vegetarian meals"
        );

        // Randomly select an additional need from the list
        String randomAdditionalNeed = additionalNeedsList.get(new Random().nextInt(additionalNeedsList.size()));
        partialUpdateBookingDataMap.put("additionalneeds", randomAdditionalNeed);

        return partialUpdateBookingDataMap;
    }
    

    
}
