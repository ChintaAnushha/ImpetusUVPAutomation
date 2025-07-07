package com.impetus.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;
    private static final String REPORT_DIR = System.getProperty("user.dir") + "/test-output/ExtentReport";
    private static final String REPORT_PATH = REPORT_DIR + "/TestReport.html";

    public static ExtentReports getInstance() {
        if (extent == null) {
            extent = createInstance();
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        new File(REPORT_DIR).mkdirs();
     //   ExtentSparkReporter sparkReporter = new ExtentSparkReporter(ApplicationConstants.EXTENT_REPORT_PATH + "/TestReport.html");
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
        sparkReporter.config().setDocumentTitle("UVP Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTheme(Theme.DARK);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Optional: Add environment/system info
        extent.setSystemInfo("Project", "UVP-RAModule");
        extent.setSystemInfo("Environment", "SIT");
        extent.setSystemInfo("Tester", "Anusha");
        extent.setSystemInfo("Browser", "Chrome");

        return extent;
    }

}