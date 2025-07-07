package com.impetus.tests.ra;

import com.impetus.base.BasePage;
import com.impetus.pages.DashboardPage;
import com.impetus.pages.RangeArchitecturePage;
import com.impetus.utils.CommonUtils;
import com.impetus.utils.DriverManager;
import com.impetus.utils.ExtentTestManager;
import com.impetus.utils.LoginHelper;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.aventstack.extentreports.Status;

/**
 * Advanced File Upload Scenarios Test Suite
 * Tests for network error handling, upload cancellation, and refresh behavior
 * Priority: P1 for Chrome, Firefox, Safari
 */
public class FileUploadAdvancedScenariosTest extends BasePage {

    private WebDriver driver;
    private DashboardPage dashboardPage;
    private RangeArchitecturePage rangeArchitecturePage;
    
    // Test file paths
    private static final String RA_FILE_PATH = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
    private static final String LARGE_TEST_FILE_PATH = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/LargeRA.xlsx";

    public FileUploadAdvancedScenariosTest() {
        super(DriverManager.getDriver());
    }

    @BeforeClass
    public void setup() {
        driver = DriverManager.getDriver();
        
        // Use LoginHelper to do login with config credentials
        LoginHelper loginHelper = new LoginHelper(driver);
        loginHelper.loginToApplication();
        
        // Initialize page objects
        dashboardPage = new DashboardPage(driver);
        rangeArchitecturePage = new RangeArchitecturePage(driver);
        
        // Navigate to Range Architecture section
        dashboardPage.clickRangeArchitecture();
        rangeArchitecturePage.switchToCurrentGlobalRA();
        
        ExtentTestManager.getTest().log(Status.INFO, "Setup completed - navigated to Range Architecture section");
    }

    /**
     * Test Case: Network Error During File Upload with Resumption
     * Priority: P1
     * Browsers: Chrome, Firefox, Safari
     * 
     * Scenario: Verify the process when there is a network problem during file upload
     * Expected: File upload should resume once network restores
     */
    @Test(priority = 1, description = "Verify file upload resumes after network error")
    @Description("Test network error handling during file upload process")
    @Severity(SeverityLevel.CRITICAL)
    public void testNetworkErrorDuringUploadWithResumption() {
        SoftAssert softAssert = new SoftAssert();
        
        try {
            ExtentTestManager.getTest().log(Status.INFO, "Starting network error during upload test");
            
            // Step 1: Trigger file upload dialog
            rangeArchitecturePage.triggerFileUploadDialog();
            CommonUtils.waitForSeconds(2);
            
            // Step 2: Start file upload
            CommonUtils.uploadFileUsingRobot(RA_FILE_PATH);
            CommonUtils.waitForSeconds(3);
            
            // Step 3: Verify file is selected
            softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "File should be selected and displayed before network interruption");
            
            String selectedFileName = rangeArchitecturePage.getSelectedFileName();
            softAssert.assertFalse(selectedFileName.isEmpty(),
                    "Selected file name should not be empty");
            
            ExtentTestManager.getTest().log(Status.INFO, "File selected: " + selectedFileName);
            
            // Step 4: Simulate network interruption using JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Simulate network disconnection by overriding fetch/XMLHttpRequest
            js.executeScript(
                "window.originalFetch = window.fetch;" +
                "window.originalXHR = window.XMLHttpRequest;" +
                "window.fetch = function() { return Promise.reject(new Error('Network Error')); };" +
                "window.XMLHttpRequest = function() {" +
                "  var xhr = new window.originalXHR();" +
                "  var originalSend = xhr.send;" +
                "  xhr.send = function() { throw new Error('Network Error'); };" +
                "  return xhr;" +
                "};"
            );
            
            ExtentTestManager.getTest().log(Status.INFO, "Network interruption simulated");
            
            // Step 5: Attempt to continue upload during network error
            rangeArchitecturePage.clickContinue();
            CommonUtils.waitForSeconds(5);
            
            // Step 6: Check for network error indication
            boolean networkErrorDetected = false;
            
            // Check for various error indicators
            if (rangeArchitecturePage.isUploadFailureMessageDisplayed()) {
                String errorMessage = rangeArchitecturePage.getUploadFailureMessage();
                networkErrorDetected = errorMessage.toLowerCase().contains("network") ||
                                     errorMessage.toLowerCase().contains("connection") ||
                                     errorMessage.toLowerCase().contains("failed") ||
                                     errorMessage.toLowerCase().contains("error");
                ExtentTestManager.getTest().log(Status.INFO, "Error message detected: " + errorMessage);
            }
            
            // Step 7: Restore network connection
            js.executeScript(
                "window.fetch = window.originalFetch;" +
                "window.XMLHttpRequest = window.originalXHR;"
            );
            
            ExtentTestManager.getTest().log(Status.INFO, "Network connection restored");
            CommonUtils.waitForSeconds(3);
            
            // Step 8: Verify upload can resume/retry
            if (networkErrorDetected || !rangeArchitecturePage.isUploadSuccessfullMessageDisplayed()) {
                // If there was an error, try to resume/retry upload
                if (rangeArchitecturePage.isSelectedFileDisplayed()) {
                    // File is still selected, try continue again
                    rangeArchitecturePage.clickContinue();
                } else {
                    // Re-upload the file
                    rangeArchitecturePage.triggerFileUploadDialog();
                    CommonUtils.uploadFileUsingRobot(RA_FILE_PATH);
                    CommonUtils.waitForSeconds(3);
                    rangeArchitecturePage.clickContinue();
                }
                
                // Wait for upload completion
                boolean uploadCompleted = rangeArchitecturePage.waitForUploadCompletion(30);
                softAssert.assertTrue(uploadCompleted,
                        "Upload should complete successfully after network restoration");
                
                // Verify success message
                softAssert.assertTrue(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(),
                        "Upload success message should be displayed after network restoration");
                
                ExtentTestManager.getTest().log(Status.PASS, "Upload resumed successfully after network restoration");
            } else {
                ExtentTestManager.getTest().log(Status.INFO, "Upload completed without network interruption detection");
            }
            
            CommonUtils.takeScreenshot(driver, "network_error_upload_test");
            
        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL, "Network error upload test failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "network_error_upload_test_failed");
            softAssert.fail("Network error upload test failed: " + e.getMessage());
        }
        
        softAssert.assertAll();
    }

    /**
     * Test Case: Cancel File Upload
     * Priority: P1
     * Browsers: Chrome, Firefox, Safari
     * 
     * Scenario: Verify that when user presses cancel button, the file is not uploaded
     * Expected: File should not be uploaded when cancel is pressed
     */
    @Test(priority = 2, description = "Verify file upload cancellation functionality")
    @Description("Test that cancel button prevents file upload")
    @Severity(SeverityLevel.CRITICAL)
    public void testCancelFileUpload() {
        SoftAssert softAssert = new SoftAssert();
        
        try {
            ExtentTestManager.getTest().log(Status.INFO, "Starting cancel file upload test");
            
            // Step 1: Trigger file upload dialog
            rangeArchitecturePage.triggerFileUploadDialog();
            CommonUtils.waitForSeconds(2);
            
            // Step 2: Select a file
            CommonUtils.uploadFileUsingRobot(RA_FILE_PATH);
            CommonUtils.waitForSeconds(3);
            
            // Step 3: Verify file is selected
            softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "File should be selected and displayed");
            
            String selectedFileName = rangeArchitecturePage.getSelectedFileName();
            softAssert.assertFalse(selectedFileName.isEmpty(),
                    "Selected file name should not be empty");
            
            ExtentTestManager.getTest().log(Status.INFO, "File selected for upload: " + selectedFileName);
            
            // Step 4: Verify cancel button is available and enabled
            softAssert.assertTrue(rangeArchitecturePage.isCancelButtonDisplayed(),
                    "Cancel button should be displayed");
            softAssert.assertTrue(rangeArchitecturePage.isCancelButtonEnabled(),
                    "Cancel button should be enabled");
            
            // Step 5: Click cancel button
            rangeArchitecturePage.clickCancelButton();
            CommonUtils.waitForSeconds(3);
            
            ExtentTestManager.getTest().log(Status.INFO, "Cancel button clicked");
            
            // Step 6: Verify file upload was cancelled
            // Check that upload process was not initiated
            softAssert.assertFalse(rangeArchitecturePage.isUploadInProgress(),
                    "Upload should not be in progress after cancellation");
            
            // Check that success message is not displayed
            softAssert.assertFalse(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(),
                    "Upload success message should not be displayed after cancellation");
            
            // Check that failure message is not displayed (unless it's a cancellation confirmation)
            if (rangeArchitecturePage.isUploadFailureMessageDisplayed()) {
                String errorMessage = rangeArchitecturePage.getUploadFailureMessage();
                // Allow cancellation-related messages
                boolean isCancellationMessage = errorMessage.toLowerCase().contains("cancel") ||
                                              errorMessage.toLowerCase().contains("abort") ||
                                              errorMessage.toLowerCase().contains("stopped");
                if (!isCancellationMessage) {
                    softAssert.fail("Unexpected error message after cancellation: " + errorMessage);
                }
            }
            
            // Step 7: Verify that file selection is cleared or modal is closed
            CommonUtils.waitForSeconds(2);
            boolean fileStillSelected = rangeArchitecturePage.isSelectedFileDisplayed();
            
            // Either file should be cleared or we should be back to initial state
            if (fileStillSelected) {
                // If file is still shown, verify we can start fresh
                ExtentTestManager.getTest().log(Status.INFO, "File still selected after cancel - verifying fresh start capability");
            } else {
                ExtentTestManager.getTest().log(Status.INFO, "File selection cleared after cancel");
            }
            
            // Step 8: Verify we can start a new upload after cancellation
            rangeArchitecturePage.triggerFileUploadDialog();
            CommonUtils.uploadFileUsingRobot(RA_FILE_PATH);
            CommonUtils.waitForSeconds(3);
            
            softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "Should be able to select file again after cancellation");
            
            ExtentTestManager.getTest().log(Status.PASS, "File upload cancellation working correctly");
            CommonUtils.takeScreenshot(driver, "cancel_upload_test");
            
        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL, "Cancel upload test failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "cancel_upload_test_failed");
            softAssert.fail("Cancel upload test failed: " + e.getMessage());
        }
        
        softAssert.assertAll();
    }

    /**
     * Test Case: File Presence After Page Refresh
     * Priority: P1
     * Browsers: Chrome, Firefox, Safari
     * 
     * Scenario: Verify that when user hits refresh, uploaded file is not present before saving
     * Expected: Uploaded file should not be present when user hits refresh before saving
     */
    @Test(priority = 3, description = "Verify file is not present after page refresh before saving")
    @Description("Test that uploaded file is cleared after page refresh if not saved")
    @Severity(SeverityLevel.CRITICAL)
    public void testFilePresenceAfterPageRefresh() {
        SoftAssert softAssert = new SoftAssert();
        
        try {
            ExtentTestManager.getTest().log(Status.INFO, "Starting file presence after refresh test");
            
            // Step 1: Trigger file upload dialog
            rangeArchitecturePage.triggerFileUploadDialog();
            CommonUtils.waitForSeconds(2);
            
            // Step 2: Upload a file
            CommonUtils.uploadFileUsingRobot(RA_FILE_PATH);
            CommonUtils.waitForSeconds(3);
            
            // Step 3: Verify file is selected
            softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "File should be selected and displayed before refresh");
            
            String selectedFileNameBeforeRefresh = rangeArchitecturePage.getSelectedFileName();
            softAssert.assertFalse(selectedFileNameBeforeRefresh.isEmpty(),
                    "Selected file name should not be empty before refresh");
            
            ExtentTestManager.getTest().log(Status.INFO, "File uploaded before refresh: " + selectedFileNameBeforeRefresh);
            
            // Step 4: Continue with upload process (but don't save/complete)
            rangeArchitecturePage.clickContinue();
            CommonUtils.waitForSeconds(5);
            
            // Step 5: Verify upload is in progress or completed but not saved
            boolean uploadInProgress = rangeArchitecturePage.isUploadInProgress();
            boolean uploadCompleted = rangeArchitecturePage.isUploadSuccessfullMessageDisplayed();
            
            ExtentTestManager.getTest().log(Status.INFO, 
                    "Before refresh - Upload in progress: " + uploadInProgress + 
                    ", Upload completed: " + uploadCompleted);
            
            // Step 6: Refresh the page
            String currentUrl = driver.getCurrentUrl();
            ExtentTestManager.getTest().log(Status.INFO, "Refreshing page: " + currentUrl);
            
            DriverManager.refreshPage();
            CommonUtils.waitForSeconds(5);
            
            // Step 7: Navigate back to the upload section
            dashboardPage.clickRangeArchitecture();
            rangeArchitecturePage.switchToCurrentGlobalRA();
            CommonUtils.waitForSeconds(3);
            
            ExtentTestManager.getTest().log(Status.INFO, "Navigated back to upload section after refresh");
            
            // Step 8: Verify uploaded file is not present
            boolean fileStillPresent = rangeArchitecturePage.isSelectedFileDisplayed();
            
            softAssert.assertFalse(fileStillPresent,
                    "Uploaded file should not be present after page refresh before saving");
            
            // Step 9: Verify upload success message is not displayed
            softAssert.assertFalse(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(),
                    "Upload success message should not be displayed after refresh");
            
            // Step 10: Verify upload is not in progress
            softAssert.assertFalse(rangeArchitecturePage.isUploadInProgress(),
                    "Upload should not be in progress after refresh");
            
            // Step 11: Verify we're back to initial state - can upload again
            rangeArchitecturePage.triggerFileUploadDialog();
            CommonUtils.waitForSeconds(2);
            
            // Should be able to select file again (indicates clean state)
            CommonUtils.uploadFileUsingRobot(RA_FILE_PATH);
            CommonUtils.waitForSeconds(3);
            
            softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "Should be able to upload file again after refresh (indicates clean state)");
            
            ExtentTestManager.getTest().log(Status.PASS, 
                    "File correctly cleared after page refresh - upload state reset");
            CommonUtils.takeScreenshot(driver, "refresh_file_presence_test");
            
        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL, "File presence after refresh test failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "refresh_file_presence_test_failed");
            softAssert.fail("File presence after refresh test failed: " + e.getMessage());
        }
        
        softAssert.assertAll();
    }

    /**
     * Test Case: Network Error Recovery with Multiple Retry Attempts
     * Priority: P1
     * Browsers: Chrome, Firefox, Safari
     * 
     * Additional scenario to test multiple network interruptions and recovery
     */
    @Test(priority = 4, description = "Verify upload recovery after multiple network interruptions")
    @Description("Test upload resilience with multiple network error scenarios")
    @Severity(SeverityLevel.NORMAL)
    public void testMultipleNetworkErrorRecovery() {
        SoftAssert softAssert = new SoftAssert();
        
        try {
            ExtentTestManager.getTest().log(Status.INFO, "Starting multiple network error recovery test");
            
            // Step 1: Initial upload setup
            rangeArchitecturePage.triggerFileUploadDialog();
            CommonUtils.uploadFileUsingRobot(RA_FILE_PATH);
            CommonUtils.waitForSeconds(3);
            
            softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "File should be selected initially");
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Step 2: First network interruption
            js.executeScript("window.navigator.onLine = false;");
            rangeArchitecturePage.clickContinue();
            CommonUtils.waitForSeconds(3);
            
            // Step 3: Restore and retry
            js.executeScript("window.navigator.onLine = true;");
            CommonUtils.waitForSeconds(2);
            
            // Step 4: Second network interruption (simulate timeout)
            js.executeScript(
                "window.originalFetch = window.fetch;" +
                "window.fetch = function() { " +
                "  return new Promise((resolve, reject) => {" +
                "    setTimeout(() => reject(new Error('Timeout')), 1000);" +
                "  });" +
                "};"
            );
            
            if (rangeArchitecturePage.isSelectedFileDisplayed()) {
                rangeArchitecturePage.clickContinue();
                CommonUtils.waitForSeconds(5);
            }
            
            // Step 5: Final recovery
            js.executeScript("window.fetch = window.originalFetch;");
            
            // Retry upload if needed
            if (!rangeArchitecturePage.isUploadSuccessfullMessageDisplayed()) {
                if (rangeArchitecturePage.isSelectedFileDisplayed()) {
                    rangeArchitecturePage.clickContinue();
                } else {
                    rangeArchitecturePage.triggerFileUploadDialog();
                    CommonUtils.uploadFileUsingRobot(RA_FILE_PATH);
                    CommonUtils.waitForSeconds(3);
                    rangeArchitecturePage.clickContinue();
                }
                
                boolean finalUploadSuccess = rangeArchitecturePage.waitForUploadCompletion(30);
                softAssert.assertTrue(finalUploadSuccess,
                        "Upload should eventually succeed after multiple network error recovery attempts");
            }
            
            ExtentTestManager.getTest().log(Status.PASS, "Multiple network error recovery test completed");
            CommonUtils.takeScreenshot(driver, "multiple_network_error_recovery_test");
            
        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL, "Multiple network error recovery test failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "multiple_network_error_recovery_test_failed");
            softAssert.fail("Multiple network error recovery test failed: " + e.getMessage());
        }
        
        softAssert.assertAll();
    }
}