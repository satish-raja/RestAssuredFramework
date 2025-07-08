package com.api.utils;

import com.github.javafaker.Faker;

/**
 * 🧪 TestDataGenerator.java
 *
 * 📌 Utility class to generate fake but realistic test data (like names, emails, etc.)
 * using Java Faker library. This ensures our API tests can run with unique inputs.
 */
public class TestDataGenerator {

    private static final Faker faker = new Faker();

    // 👤 Generate a random full name
    public static String generateName() {
        return faker.name().fullName();
    }

    // 📧 Generate a unique email (important to avoid conflicts on POST)
    public static String generateEmail() {
        return "auto_" + System.currentTimeMillis() + "@test.com";
    }

    // 📱 Generate a fake 10-digit phone number
    public static String generatePhone() {
        return faker.phoneNumber().subscriberNumber(10);
    }

    // 🗺️ Generate a fake country
    public static String generateCountry() {
        return faker.country().name();
    }

    // 🧑‍💼 Generate a fake job title
    public static String generateJobTitle() {
        return faker.job().title();
    }
}
