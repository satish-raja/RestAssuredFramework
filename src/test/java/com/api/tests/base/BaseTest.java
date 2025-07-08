package com.api.tests.base;

import com.api.utils.AllureExecutorWriter;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 📋 BaseTest – Automatically logs test start/end for clean console output.
 * ✅ Extend this class in all your test classes.
 */
public class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void generateAllureEnvironment() {
        // 💾 Write environment.properties
        Properties props = new Properties();
        props.setProperty("BaseURL", "https://gorest.co.in/public/v2");
        props.setProperty("Environment", "DEV");
        props.setProperty("Token", "*****MASKED*****");
        props.setProperty("JavaVersion", System.getProperty("java.version"));
        props.setProperty("OS", System.getProperty("os.name"));

        try {
            File envFile = new File("allure-results/environment.properties");
            envFile.getParentFile().mkdirs(); // Ensure folder exists
            try (FileOutputStream fos = new FileOutputStream(envFile)) {
                props.store(fos, "Environment Details for Allure");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 🧾 Write executor.json
        AllureExecutorWriter.writeExecutorInfo();
    }

    @BeforeMethod(alwaysRun = true)
    public void logTestStart(Method method) {
        System.out.println("\n🚀 STARTING: " + method.getName());
        Allure.step("🚀 STARTING TEST: `" + method.getName() + "`");
    }

    @AfterMethod(alwaysRun = true)
    public void logTestResult(ITestResult result) {
        String methodName = result.getMethod().getMethodName();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> {
                System.out.println("✅ PASSED: " + methodName);
                Allure.step("✅ PASSED TEST: `" + methodName + "`");
            }
            case ITestResult.FAILURE -> {
                System.out.println("❌ FAILED: " + methodName);
                Throwable throwable = result.getThrowable();
                if (throwable != null) {
                    String reason = throwable.getMessage();
                    System.out.println("💥 Reason: " + reason);
                    Allure.step("💥 FAILURE REASON: `" + reason + "`");
                    Allure.addAttachment("Exception Stack Trace", throwable.toString());
                }
            }
            case ITestResult.SKIP -> {
                System.out.println("⏭️ SKIPPED: " + methodName);
                Allure.step("⏭️ SKIPPED TEST: `" + methodName + "`");
            }
        }

        System.out.println("--------------------------------------------------");
    }
}
