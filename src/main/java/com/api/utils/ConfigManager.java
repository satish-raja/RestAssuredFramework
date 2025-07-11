package com.api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 🔧 ConfigManager.java
 *
 * 📌 Singleton utility to load environment-specific config from `{env}.properties`.
 * Defaults to `dev.properties` if no `-Denv` system property is passed.
 *
 * Example: `-Denv=qa` loads `src/test/resources/config/qa.properties`
 */
public class ConfigManager {

    private static ConfigManager configManager;
    private final Properties properties = new Properties();

    // 🛠️ Private constructor to enforce Singleton
    private ConfigManager() {
        String env = System.getProperty("env", "qa");
        String configPath = "src/test/resources/config/" + env + ".properties";

        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load configuration for environment: " + env, e);
        }
    }

    // 🔁 Thread-safe lazy-loaded Singleton accessor
    public static ConfigManager getInstance() {
        if (configManager == null) {
            synchronized (ConfigManager.class) {
                if (configManager == null) {
                    configManager = new ConfigManager();
                }
            }
        }
        return configManager;
    }

    // 📦 Get property value by key
    public String get(String key) {
        return properties.getProperty(key);
    }
}
