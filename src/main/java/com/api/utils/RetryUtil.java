package com.api.utils;

import io.qameta.allure.Allure;

import java.util.function.Supplier;

public class RetryUtil {

    public static <T> T retry(Supplier<T> action, int maxRetries, long waitMillis, String contextInfo) {
        int attempt = 1;
        while (true) {
            try {
                return action.get(); // ✅ Execute the lambda
            } catch (Exception e) {
                if (attempt >= maxRetries) {
                    String message = "❌ Retry failed after " + maxRetries + " attempts for: " + contextInfo;
                    Allure.step(message);
                    throw new RuntimeException(message, e);
                }

                String retryMsg = "⚠️ Retry attempt #" + attempt + " for: " + contextInfo +
                                  " | Reason: " + e.getMessage();
                Allure.step(retryMsg);

                try {
                    Thread.sleep(waitMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted during retry", ie);
                }
                attempt++;
            }
        }
    }

    public static <T> T retry(Supplier<T> action, String contextInfo) {
        return retry(action, 3, 1000, contextInfo);
    }
}
