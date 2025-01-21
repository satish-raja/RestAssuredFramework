package test.com.api.testng.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

/**
 * Utility class to generate random data for booking-related fields using the Faker library.
 * This class provides methods to generate full and partial booking data, including customer names, 
 * prices, booking dates, and additional needs.
 */
public class FakerDataGenerator {
    
    private Faker fake = new Faker();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Generates a full set of booking data with randomized values for customer details, price, 
     * booking dates, and additional needs.
     * 
     * @return A map containing all booking data, including firstname, lastname, total price, deposit paid, 
     *         booking dates, and additional needs.
     */
    public Map<String, Object> generate_booking_data() {
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("firstname", fake.name().firstName());
        bookingData.put("lastname", fake.name().lastName());
        bookingData.put("totalprice", fake.number().numberBetween(50, 500));
        bookingData.put("depositpaid", fake.bool().bool());

        Map<String, String> bookingDates = Map.of(
            "checkin", dateFormat.format(fake.date().future(10, TimeUnit.DAYS)),
            "checkout", dateFormat.format(fake.date().future(15, TimeUnit.DAYS))
        );
        bookingData.put("bookingdates", bookingDates);

     // List of real-time additional needs
        List<String> additionalNeedsList = Arrays.asList(
            "Breakfast", "Late check-out", "Extra pillows", "Wheelchair access", "Non-smoking room",
            "Pet-friendly room", "Air conditioning", "Airport shuttle", "Massage service", "Vegetarian meals"
        );

        // Randomly select an additional need from the list
        bookingData.put("additionalneeds", additionalNeedsList.get(fake.number().numberBetween(0, additionalNeedsList.size())));


        return bookingData;
    }


    /**
     * Generates a partial set of booking data with randomized values for price, booking dates, and additional needs.
     * This method is intended for cases where only a subset of booking data is required.
     * 
     * @return A map containing partial booking data, including total price, booking dates, and additional needs.
     */
    public Map<String, Object> generate_partial_booking_data() {
        Map<String, Object> partialBookingData = new HashMap<>();
        partialBookingData.put("totalprice", fake.number().numberBetween(50, 500));  // Generating random total price

        // Generating random check-in and check-out dates
        Map<String, Object> bookingDates = new HashMap<>();
        bookingDates.put("checkin", dateFormat.format(fake.date().future(10, java.util.concurrent.TimeUnit.DAYS)));
        bookingDates.put("checkout", dateFormat.format(fake.date().future(15, java.util.concurrent.TimeUnit.DAYS)));
        partialBookingData.put("bookingdates", bookingDates);

        partialBookingData.put("additionalneeds", fake.lorem().word());  // Generating random word for additional needs

        return partialBookingData;
    }
}
