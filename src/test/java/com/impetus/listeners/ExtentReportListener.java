package com.impetus.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.impetus.utils.ExtentManager;
import com.impetus.utils.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

//import static com.gonuclei.base.LaunchBrowser.captureScreenshot;
//import static com.gonuclei.base.LaunchBrowser.getScreenshotPath;

public class ExtentReportListener implements ITestListener {


    @Override
    public void onStart(ITestContext context) {
        System.out.println("*** Test Suite " + context.getName() + " started ***");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("*** Test Suite " + context.getName() + " ending ***");

        int passCount = context.getPassedTests().size();
        int failCount = context.getFailedTests().size();
        int skipCount = context.getSkippedTests().size();
        int total = passCount + failCount + skipCount;
       // int total = passCount + failCount;
        double successRate = total > 0 ? (passCount * 100.0 / total) : 0.0;
        long duration = (context.getEndDate().getTime() - context.getStartDate().getTime()) / 1000;

        String retryTests = RetryAnalyzer.retriedTests.isEmpty()
                ? "None"
                : String.join(", ", RetryAnalyzer.retriedTests);

        int retryCount = RetryAnalyzer.retriedTests.size();

        String resultSummary = String.format(
                "Test Execution Summary<br>" +
                        "====================<br>" +
                        "Total Tests: %d<br>" +
                        "Passed     : %d<br>" +
                        "Failed     : %d<br>" +
                        "Skipped    : %d<br>" +
                        "Retried Count : %d<br>" +
                        "Retried Tests : %s<br>" +
                        "Success Rate: %.2f%%<br>" +
                        "Duration    : %d seconds<br>" +
                        "====================",
                total,
                passCount,
                failCount,
                skipCount,
                retryCount,
                retryTests,
                successRate,
                duration
        );

        // Set in system info (appears in Environment tab)
//        ExtentManager.getInstance().setSystemInfo("Total Tests", String.valueOf(total));
//        ExtentManager.getInstance().setSystemInfo("Passed", String.valueOf(passCount));
//        ExtentManager.getInstance().setSystemInfo("Failed", String.valueOf(failCount));
//        ExtentManager.getInstance().setSystemInfo("Skipped", String.valueOf(skipCount));
//        ExtentManager.getInstance().setSystemInfo("Retried Count", String.valueOf(retryCount));
//        ExtentManager.getInstance().setSystemInfo("Retried Tests", retryTests);
//        ExtentManager.getInstance().setSystemInfo("Success Rate", String.format("%.2f%%", successRate));
//        ExtentManager.getInstance().setSystemInfo("Duration (sec)", String.valueOf(duration));

        com.impetus.utils.ExtentManager.getInstance().setSystemInfo("Execution Summary", resultSummary);
     //   ExtentManager.getInstance().setSystemInfo("Retried Test Cases", retryTests);

        // Flush report
        ExtentManager.getInstance().flush();
    }


    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(("*** Running test method " + result.getMethod().getMethodName() + "..."));
        ExtentTestManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("*** Executed " + result.getMethod().getMethodName() + " test successfully...");
        ExtentTestManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("*** Test execution " + result.getMethod().getMethodName() + " failed...");

        ExtentTest test = ExtentTestManager.getTest();
        if (test == null) {
            System.err.println("❌ ExtentTest not found for this thread. Did you forget to call startTest()?");
            return;
        }

        test.log(Status.FAIL, "Test Failed");
        test.fail(result.getThrowable());

//        try {
//        //    captureScreenshot("failure_" + result.getMethod().getMethodName());
//        //    ((com.aventstack.extentreports.ExtentTest) test).addScreenCaptureFromPath(getScreenshotPath());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
    }


    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        // Optional handling
    }
}


