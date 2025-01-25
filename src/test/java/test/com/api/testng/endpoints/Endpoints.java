package test.com.api.testng.endpoints;

/**
 * This class contains the API endpoint constants for The Cat API.
 * It includes the base URL and various endpoint paths used for interacting with the API.
 */
public class Endpoints {

    /**
     * The base URL for the API.
     */
    public static final String BASE_URL = "https://api.thecatapi.com/v1";
    
    /**
     * The base URL for the BookingTest API.
     */
    public static final String BOOKING_BASE_URL = "https://restful-booker.herokuapp.com";

    /**
     * The API key for authentication to The Cat API.
     * Note: It should be securely stored and not exposed in production code.
     */
    public static final String API_KEY = "live_hqmX64b4j4RBGMgKaXT4YokmXvCBDWKk9H4HdifiRRDuUxPVqgJqmjCP4AgB34OJ";

    /**
     * Endpoint for searching images from the API.
     */
    public static final String SEARCH_IMAGES = "/images/search";

    /**
     * Endpoint for uploading images to the API.
     */
    public static final String UPLOAD_IMAGE = "/images/upload";

    /**
     * Endpoint for retrieving an image by its ID.
     * The {id} parameter is a placeholder for the image ID.
     */
    public static final String IMAGE_BY_ID = "/images/{id}";

    /**
     * Endpoint for retrieving the analysis of an image by its ID.
     * The {id} parameter is a placeholder for the image ID.
     */
    public static final String IMAGE_ANALYSIS_BY_ID = "/images/{id}/analysis";

    /**
     * Endpoint for retrieving all images from the API.
     */
    public static final String IMAGES = "/images";
    
    
    public static final String BASE_ENDPOINT = "/booking";
    public static final String BOOKING_BY_ID = "/booking/{bookingId}";
    public static final String AUTH_ENDPOINT = "/auth";
    public static final String PING_ENDPOINT = "/ping";
    
    
    /**
     * Schemas
     */

    public static final String CREATE_BOOKING_SCHEMA = "schemas/CreateBooking_Schema.json";
    public static final String BOOKING_DETAILS_SCHEMA = "schemas/GetBookingDetails_Schema.json";
    
}
