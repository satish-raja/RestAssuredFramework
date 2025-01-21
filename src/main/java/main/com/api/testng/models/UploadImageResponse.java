package main.com.api.testng.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response for an image upload.
 * This class contains details about the uploaded image, such as its ID, URL, dimensions,
 * original filename, and approval status.
 */
public class UploadImageResponse {

    private String id;
    private String url;
    private int width;
    private int height;

    @JsonProperty("original_filename")
    private String originalFilename; // Use camelCase for Java

    private int pending;
    private int approved;

    /**
     * Gets the ID of the uploaded image.
     *
     * @return the ID of the image
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the uploaded image.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the URL of the uploaded image.
     *
     * @return the URL of the image
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the uploaded image.
     *
     * @param url the URL to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the width of the uploaded image.
     *
     * @return the width of the image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the uploaded image.
     *
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the uploaded image.
     *
     * @return the height of the image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the uploaded image.
     *
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the original filename of the uploaded image.
     *
     * @return the original filename of the image
     */
    public String getOriginalFilename() {
        return originalFilename;
    }

    /**
     * Sets the original filename of the uploaded image.
     *
     * @param originalFilename the filename to set
     */
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    /**
     * Gets the number of pending approvals for the uploaded image.
     *
     * @return the number of pending approvals
     */
    public int getPending() {
        return pending;
    }

    /**
     * Sets the number of pending approvals for the uploaded image.
     *
     * @param pending the pending approval count to set
     */
    public void setPending(int pending) {
        this.pending = pending;
    }

    /**
     * Gets the number of approved statuses for the uploaded image.
     *
     * @return the number of approved statuses
     */
    public int getApproved() {
        return approved;
    }

    /**
     * Sets the number of approved statuses for the uploaded image.
     *
     * @param approved the approved status count to set
     */
    public void setApproved(int approved) {
        this.approved = approved;
    }
}
