package test.com.api.testng.utils;

import static org.testng.Assert.assertFalse;

import main.com.api.testng.models.Image;

/**
 * Utility class for validating image objects.
 * This class provides a method to validate various fields of an image object, such as ID, URL, 
 * dimensions, and Sub ID.
 */
public class ImageValidator {

    /**
     * Validates the fields of an image object.
     * This method checks if the image ID is valid, the URL is well-formed, the dimensions are 
     * non-zero, and ensures that the Sub ID is not empty if present.
     * 
     * @param image The image object to be validated.
     * @throws IllegalArgumentException if any of the validations fail.
     */
    public static void validate(Image image) {
        // Validate Image ID
        ValidationUtils.validateImageId(image.getId());

        // Validate Image URL
        ValidationUtils.validateUrl(image.getUrl());

        // Validate Image Dimensions (Width and Height)
        ValidationUtils.validateDimensions(image.getWidth(), image.getHeight());

        // Validate Sub ID if present
        if (image.getSubId() != null) {
            assertFalse(image.getSubId().isEmpty(), "Sub ID should not be empty");
        }
    }
}
