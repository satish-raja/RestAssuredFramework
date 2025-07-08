package com.api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "dev"); // default = dev
        try (FileInputStream fis = new FileInputStream("src/test/resources/config/" + env + ".properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load configuration for environment: " + env, e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
