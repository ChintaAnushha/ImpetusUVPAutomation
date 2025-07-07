package com.impetus.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.impetus.utils.ExtentManager;
import com.impetus.utils.ExtentTestManager;
import org.apache.commons.io.FileUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1;
    private static final long WAIT_TIME_MS = 20000;

    public static Set<String> retriedTests = new HashSet<>();

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            retriedTests.add(result.getMethod().getMethodName());

            ExtentTest test = com.impetus.utils.ExtentTestManager.getTest(); // Get current test
                  if(test!=null) {
                      test.warning("Retrying test: " + result.getName() + " - Attempt " + retryCount);
                   }
          //  ExtentReports test = ExtentManager.getInstance(); // Get current test
         //   test.warning("Retrying test: " + result.getName() + " - Attempt " + retryCount);

            // Log error on first failure attempt
//            if (retryCount == 2 && result.getThrowable() != null) {
//                captureFailureDetails(result,test);
//            }
            try {
                Thread.sleep(WAIT_TIME_MS);
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        ExtentTest finalTest = ExtentTestManager.getTest();
        if (finalTest != null) {
            finalTest.fail("Test failed after max retries: " + result.getName());
        }
        // Final failure - optionally you can add another log here
     //   ExtentManager.getExtentTest().fail("Test failed after max retries: " + result.getName());
        return false;
    }

    private void captureFailureDetails(ITestResult result,ExtentTest test) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String testName = result.getMethod().getMethodName();
        String safeTestName = testName.replaceAll("[^a-zA-Z0-9\\-_]", "_"); // Sanitize filename

        // Log error message
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
        //    Allure.addAttachment("Failure Exception (Attempt " + retryCount + ")", throwable.getMessage());
            test.info("Failure captured at: " + timestamp);
            test.fail("Error Message: " + (throwable != null ? throwable.getMessage() : "None"));

        }

        // Attach failed API response
        Object response = result.getAttribute("apiResponse");
        if (response != null) {
         //   Allure.addAttachment("Failed API Response", response.toString());
            test.info("API Response: " + response.toString());
        }

        // Create output folder if not exist
        File failureDir = new File("test-output/failures");
        if (!failureDir.exists()) {
            failureDir.mkdirs();
        }

        // Build report content
        try {
            String reportPath = String.format("test-output/failures/%s_%s_failure_report.txt",safeTestName, timestamp);
            String reportContent = String.format(
                    "Test: %s\nTimestamp: %s\nError: %s\nResponse: %s",
                    testName,
                    timestamp,
                    throwable != null ? throwable.getMessage() : "No error message",
                    response != null ? response.toString() : "No response available"
            );

            FileUtils.writeStringToFile(new File(reportPath), reportContent, "UTF-8");
            System.out.println("Failure report saved at: " + reportPath);
            test.info("Failure report saved: " + reportPath);
          //  Allure.addAttachment("Failure Report", "text/plain", reportContent);
        } catch (IOException e) {
         //   Allure.addAttachment("Report Creation Failed", e.getMessage());
            test.warning("Failed to write failure report: " + e.getMessage());

        }
    }
}


