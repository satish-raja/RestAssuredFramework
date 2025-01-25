package test.com.api.testng.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import test.com.api.testng.endpoints.Endpoints;
import test.com.api.testng.utils.AllureRestAssuredUtil;

/**
 * Provides common request specifications for interacting with the API.
 * This class contains methods for creating request specifications with default headers, content types, and base URI.
 */
public class RequestSpecProvider {

    /**
     * Provides a common request specification with default headers and content type set to JSON.
     * This specification is used for API requests that require JSON payloads.
     *
     * @return a RequestSpecification for common API requests
     */
    public static RequestSpecification getCommonRequestSpec() {
        return new RequestSpecBuilder()
            .addHeader("x-api-key", Endpoints.API_KEY)
            .setContentType(ContentType.JSON) // Set default content type if applicable
            .build();
    }
	
    public static RequestSpecification getCommonRequestSpec(String url, String headerKey, String headerValue, ContentType contentType) {
        return new RequestSpecBuilder()
        	.setBaseUri(url)        		
        	.addFilter(AllureRestAssuredUtil.getAllureFilter())
            .addHeader(headerKey, headerValue)
            .setContentType(contentType)
            .build();
    }
    
    public static RequestSpecification getCommonRequestSpec(String url, String headerKey, String headerValue, ContentType contentType, Object sBody) {
        return new RequestSpecBuilder()
        	.setBaseUri(url)
        	.addFilter(AllureRestAssuredUtil.getAllureFilter())
            .addHeader(headerKey, headerValue)
            .setContentType(contentType)
            .setBody(sBody)
            .build();
    }
    
    public static RequestSpecification getCommonRequestSpec(String url, ContentType contentType, Object sBody) {
        return new RequestSpecBuilder()
        	.setBaseUri(url)
        	.addFilter(AllureRestAssuredUtil.getAllureFilter())
            .setContentType(contentType)
            .setBody(sBody)
            .build();
    }
    
    public static RequestSpecification getCommonRequestSpec(String url) {
        return new RequestSpecBuilder()
        	.setBaseUri(url)
        	.addFilter(AllureRestAssuredUtil.getAllureFilter())
            .build();
    }

    /**
     * Provides a request specification for file upload requests.
     * This specification sets the content type to "multipart/form-data" for handling file uploads.
     *
     * @return a RequestSpecification for file upload API requests
     */
    public static RequestSpecification getFileUploadRequestSpec() {
        return new RequestSpecBuilder()
            .setBaseUri(Endpoints.BASE_URL)
            .addHeader("x-api-key", Endpoints.API_KEY)
            .setContentType("multipart/form-data")
            .build();
    }
}
