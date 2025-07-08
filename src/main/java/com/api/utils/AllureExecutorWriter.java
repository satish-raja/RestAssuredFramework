package com.api.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AllureExecutorWriter {

    public static void writeExecutorInfo() {
        String path = "allure-results/executor.json";
        String content = "{\n" +
                "  \"name\": \"Local Execution via Maven\",\n" +
                "  \"type\": \"freelancer\",\n" +
                "  \"url\": \"https://github.com/satish-raja/RestAssuredFramework\",\n" +
                "  \"buildName\": \"Manual Trigger\",\n" +
                "  \"buildOrder\": \"001\"\n" +
                "}";

        try {
            Files.createDirectories(Paths.get("allure-results"));
            Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("⚠️ Failed to write Allure executor.json");
            e.printStackTrace();
        }
    }
}
