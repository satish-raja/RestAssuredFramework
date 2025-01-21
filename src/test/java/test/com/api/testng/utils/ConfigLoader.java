package test.com.api.testng.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties;

    static {
        properties = new Properties();
        try {
            // Load the properties file from the given path
            FileInputStream inputStream = new FileInputStream("src/test/resources/config/ApiTest.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
