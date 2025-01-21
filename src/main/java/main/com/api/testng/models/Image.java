/**
 * Represents an image object in the API with details such as ID, URL, filename, 
 * dimensions, and a sub-identifier.
 */
package main.com.api.testng.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

    /**
     * Unique identifier for the image.
     */
    private String id;

    /**
     * URL where the image is hosted.
     */
    private String url;

    /**
     * Original filename of the uploaded image.
     */
    private String originalFilename;

    /**
     * Width of the image in pixels.
     */
    private int width;

    /**
     * Height of the image in pixels.
     */
    private int height;

    /**
     * Optional sub-identifier for the image, mapped from the JSON field "sub_id".
     */
    @JsonProperty("sub_id")
    private String subId;

    /**
     * Retrieves the unique identifier for the image.
     * 
     * @return the image ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the image.
     * 
     * @param id the image ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the URL of the image.
     * 
     * @return the image URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the image.
     * 
     * @param url the image URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Retrieves the original filename of the image.
     * 
     * @return the original filename
     */
    public String getOriginalFilename() {
        return originalFilename;
    }

    /**
     * Sets the original filename of the image.
     * 
     * @param originalFilename the original filename
     */
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    /**
     * Retrieves the width of the image in pixels.
     * 
     * @return the width of the image
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the image in pixels.
     * 
     * @param width the width of the image
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Retrieves the height of the image in pixels.
     * 
     * @return the height of the image
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the image in pixels.
     * 
     * @param height the height of the image
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Retrieves the sub-identifier for the image.
     * 
     * @return the sub-identifier
     */
    public String getSubId() {
        return subId;
    }

    /**
     * Sets the sub-identifier for the image.
     * 
     * @param subId the sub-identifier
     */
    public void setSubId(String subId) {
        this.subId = subId;
    }
}
