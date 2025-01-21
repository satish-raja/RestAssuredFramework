package test.com.api.testng.utils;

import main.com.api.testng.models.UploadImageResponse;

/**
 * Utility class for validating the response after uploading an image.
 * This class validates various fields of the upload image response, including the image ID,
 * URL, dimensions, original filename, and approval status.
 */
public class UploadImageResponseValidator {

    /**
     * Validates the fields of the UploadImageResponse object.
     * This method checks if the image ID is valid, the URL is well-formed, the dimensions are 
     * non-zero, the original filename matches the expected one, and the image status (pending or approved) is valid.
     * 
     * @param response The response object to be validated after uploading the image.
     * @param filename The expected original filename of the image.
     * @throws IllegalArgumentException if any of the validations fail.
     */
    public static void validate(UploadImageResponse response, String filename) {
        // Validate Image ID
        ValidationUtils.validateImageId(response.getId());

        // Validate Image URL
        ValidationUtils.validateUrl(response.getUrl());

        // Validate Image Dimensions (Width and Height)
        ValidationUtils.validateDimensions(response.getWidth(), response.getHeight());

        // Validate Original Filename
        ValidationUtils.validateFilename(response.getOriginalFilename(), filename);

        // Validate Image Status (Pending or Approved)
        ValidationUtils.validateStatus(response.getPending(), response.getApproved());
    }
}
