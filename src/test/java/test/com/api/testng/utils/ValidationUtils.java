package test.com.api.testng.utils;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

/**
 * Utility class for validating common attributes related to images.
 * This class contains methods to validate various fields of an image, such as its ID,
 * URL, dimensions, filename, and approval status.
 */
public class ValidationUtils {

    /**
     * Validates the image ID.
     * Ensures that the ID is not null or empty.
     * 
     * @param id The image ID to be validated.
     * @throws AssertionError if the ID is null or empty.
     */
    public static void validateImageId(String id) {
        assertNotNull(id, "Image ID should not be null");
        assertFalse(id.isEmpty(), "Image ID should not be empty");
    }

    /**
     * Validates the image URL.
     * Ensures that the URL is not null and starts with the expected prefix.
     * 
     * @param url The image URL to be validated.
     * @throws AssertionError if the URL is null or does not start with the expected prefix.
     */
    public static void validateUrl(String url) {
        assertNotNull(url, "URL should not be null");
        assertTrue(url.startsWith("https://cdn2.thecatapi.com/images/"), "URL should start with the expected prefix");
    }

    /**
     * Validates the image dimensions.
     * Ensures that the width and height are both greater than 0.
     * 
     * @param width The width of the image.
     * @param height The height of the image.
     * @throws AssertionError if the width or height is less than or equal to 0.
     */
    public static void validateDimensions(int width, int height) {
        assertTrue(width > 0, "Width should be greater than 0");
        assertTrue(height > 0, "Height should be greater than 0");
    }

    /**
     * Validates the original filename.
     * Ensures that the original filename is not null and matches the expected filename.
     * 
     * @param originalFilename The original filename of the uploaded image.
     * @param filename The expected filename.
     * @throws AssertionError if the original filename does not match the expected filename or is null.
     */
    public static void validateFilename(String originalFilename, String filename) {
        assertNotNull(originalFilename, "Original filename should not be null");
        assertEquals(originalFilename, filename, "Original filename should match the uploaded file's name");
    }

    /**
     * Validates the image status.
     * Ensures that the pending status is 0 and the approved status is 1.
     * 
     * @param pending The pending status of the image.
     * @param approved The approved status of the image.
     * @throws AssertionError if the pending or approved status is incorrect.
     */
    public static void validateStatus(int pending, int approved) {
        assertEquals(pending, 0, "Pending status should be 0");
        assertEquals(approved, 1, "Approved status should be 1");
    }
}
