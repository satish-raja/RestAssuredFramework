package com.api.listeners;

import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * 🎧 AllureTestListener — Attaches only failure details to Allure.
 * Fully compatible with AllureLoggingFilter.
 */
public class AllureTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Optional: Log test start (no need to rename)
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.addAttachment("✅ Test Passed", "text/plain", "Test executed successfully", ".txt");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable ex = result.getThrowable();
        String error = ex != null ? ex.toString() : "Unknown failure";
        Allure.addAttachment("❌ Test Failed", "text/plain", error, ".txt");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String reason = result.getThrowable() != null
                ? result.getThrowable().toString()
                : "No reason provided";
        Allure.addAttachment("⚠️ Test Skipped", "text/plain", reason, ".txt");
    }

    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
}
