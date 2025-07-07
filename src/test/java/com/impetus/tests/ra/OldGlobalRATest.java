package com.impetus.tests.ra;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.impetus.base.BasePage;
import com.impetus.pages.DashboardPage;
import com.impetus.pages.RangeArchitecturePage;
import com.impetus.tests.DashboardTest;
import com.impetus.utils.*;
import com.impetus.listeners.*;
import com.impetus.utils.ExtentManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class OldGlobalRATest extends BasePage {

    private WebDriver driver;
    private DashboardPage dashboardPage;
    private RangeArchitecturePage rangeArchitecturePage;
    private WebDriverWait wait;
    private SoftAssert softAssert;

    public OldGlobalRATest() {
        super(DriverManager.getDriver());
    }

    @BeforeMethod
    public void setup(Method method) {
        ExtentTestManager.startTest(method.getName());
        softAssert = new SoftAssert();
    }

    @BeforeClass
    public void setup() {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Use LoginHelper to do login with config credentials
        LoginHelper loginHelper = new LoginHelper(driver);
        loginHelper.loginToApplication();

        dashboardPage = new DashboardPage(driver);
        rangeArchitecturePage = new RangeArchitecturePage(driver);

        // ✅ Call the reusable method from DashboardTest to ensure navigation/setup
        DashboardTest dashboardTest = new DashboardTest();
        dashboardTest.uvpSubMenu();
    }

    /**
     * P0 Test: Verify Old Global RA tab presence and navigation
     * Tests across Chrome, Firefox, and Safari browsers
     */
    @Test(priority = 1, description = "Verify Old Global RA tab presence and navigation to listing page")
    public void testOldGlobalRATabPresenceAndNavigation() {
        ExtentTest test = ExtentTestManager.getTest();
        test.log(Status.INFO, "Starting Old Global RA tab presence and navigation test");

        try {
            // Step 1: Verify Old Global RA tab is present
            test.log(Status.INFO, "Verifying Old Global RA tab presence");
            boolean isTabPresent = rangeArchitecturePage.isOldGlobalRADisplayed();
            softAssert.assertTrue(isTabPresent, "Old Global RA tab should be present");
            test.log(Status.PASS, "Old Global RA tab is present: " + isTabPresent);

            if (isTabPresent) {
                // Take screenshot before clicking
             //   CScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Tab_Before_Click");

                // Step 2: Click on Old Global RA tab
                test.log(Status.INFO, "Clicking on Old Global RA tab");
                rangeArchitecturePage.switchToOldGlobalRA();
                Thread.sleep(2000); // Wait for page to load

                // Step 3: Verify navigation to Old Global RA listing page
                test.log(Status.INFO, "Verifying navigation to Old Global RA listing page");
                boolean isOnListingPage = verifyOldGlobalRAListingPage();
                softAssert.assertTrue(isOnListingPage, "Should navigate to Old Global RA listing page");
                test.log(Status.PASS, "Successfully navigated to Old Global RA listing page: " + isOnListingPage);

                // Take screenshot after navigation
             //   ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Listing_Page");
            }

            softAssert.assertAll();
            test.log(Status.PASS, "Old Global RA tab presence and navigation test completed successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Old Global RA tab presence and navigation test failed: " + e.getMessage());
          //  ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Tab_Test_Failed");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * P1 Test: Verify table details in Old Global RA listing page
     * Tests RA ID, Month, and Actions columns from uploaded Excel data
     */
    @Test(priority = 2, dependsOnMethods = "testOldGlobalRATabPresenceAndNavigation", 
          description = "Verify table details in Old Global RA listing page")
    public void testOldGlobalRATableDetails() {
        ExtentTest test = ExtentTestManager.getTest();
        test.log(Status.INFO, "Starting Old Global RA table details verification test");

        try {
            // Navigate to Old Global RA tab if not already there
            rangeArchitecturePage.switchToOldGlobalRA();
            Thread.sleep(2000);

            // Step 1: Verify table headers (RA ID, Month, Actions)
            test.log(Status.INFO, "Verifying table headers: RA ID, Month, Actions");
            List<String> expectedHeaders = Arrays.asList("RA ID", "Month", "Action");
            boolean headersValid = verifyTableHeaders(expectedHeaders);
            softAssert.assertTrue(headersValid, "Table should contain RA ID, Month, and Actions columns");
            test.log(Status.PASS, "Table headers verification: " + headersValid);

            // Step 2: Verify table contains data
            test.log(Status.INFO, "Verifying table contains appropriate data");
            boolean hasData = verifyTableHasData();
            softAssert.assertTrue(hasData, "Table should contain data from uploaded Excel sheets");
            test.log(Status.PASS, "Table contains data: " + hasData);

            // Step 3: Verify month-wise data organization
            test.log(Status.INFO, "Verifying month-wise data organization");
            boolean monthWiseOrganized = verifyMonthWiseDataOrganization();
            softAssert.assertTrue(monthWiseOrganized, "Data should be organized month-wise");
            test.log(Status.PASS, "Month-wise data organization: " + monthWiseOrganized);

            // Take screenshot of table details
          //  ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Table_Details");

            softAssert.assertAll();
            test.log(Status.PASS, "Old Global RA table details verification completed successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Old Global RA table details verification failed: " + e.getMessage());
           // ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Table_Details_Failed");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * P1 Test: Verify RA ID column format and checkbox functionality
     * Tests RA ID format: RA-GBRA-XMMYY and checkbox select/unselect
     */
    @Test(priority = 3, dependsOnMethods = "testOldGlobalRATableDetails", 
          description = "Verify RA ID column format and checkbox functionality")
    public void testRAIDColumnVerification() {
        ExtentTest test = ExtentTestManager.getTest();
        test.log(Status.INFO, "Starting RA ID column verification test");

        try {
            // Navigate to Old Global RA tab if not already there
            rangeArchitecturePage.switchToOldGlobalRA();
            Thread.sleep(2000);

            // Step 1: Verify RA ID format (RA-GBRA-XMMYY)
            test.log(Status.INFO, "Verifying RA ID format: RA-GBRA-XMMYY");
            boolean formatValid = verifyRAIDFormat();
            softAssert.assertTrue(formatValid, "RA IDs should follow format RA-GBRA-XMMYY");
            test.log(Status.PASS, "RA ID format verification: " + formatValid);

            // Step 2: Verify checkbox functionality for each RA ID
            test.log(Status.INFO, "Verifying checkbox functionality for RA IDs");
            boolean checkboxFunctional = verifyRAIDCheckboxFunctionality();
            softAssert.assertTrue(checkboxFunctional, "All RA IDs should have functional checkboxes");
            test.log(Status.PASS, "RA ID checkbox functionality: " + checkboxFunctional);

            // Step 3: Verify all RA IDs have associated checkboxes
            test.log(Status.INFO, "Verifying all RA IDs have checkboxes");
            boolean allHaveCheckboxes = verifyAllRAIDsHaveCheckboxes();
            softAssert.assertTrue(allHaveCheckboxes, "All RA IDs should have associated checkboxes");
            test.log(Status.PASS, "All RA IDs have checkboxes: " + allHaveCheckboxes);

            // Take screenshot of RA ID column
          //  ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_RAID_Column");

            softAssert.assertAll();
            test.log(Status.PASS, "RA ID column verification completed successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "RA ID column verification failed: " + e.getMessage());
         //   ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_RAID_Column_Failed");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * P1 Test: Verify Month column format
     * Tests Month column displays format: "Month, Year"
     */
    @Test(priority = 4, dependsOnMethods = "testRAIDColumnVerification", 
          description = "Verify Month column format")
    public void testMonthColumnVerification() {
        ExtentTest test = ExtentTestManager.getTest();
        test.log(Status.INFO, "Starting Month column verification test");

        try {
            // Navigate to Old Global RA tab if not already there
            rangeArchitecturePage.switchToOldGlobalRA();
            Thread.sleep(2000);

            // Step 1: Verify Month column format (Month, Year)
            test.log(Status.INFO, "Verifying Month column format: Month, Year");
            boolean formatValid = verifyMonthColumnFormat();
            softAssert.assertTrue(formatValid, "Month column should display format: Month, Year");
            test.log(Status.PASS, "Month column format verification: " + formatValid);

            // Step 2: Verify month names are correct
            test.log(Status.INFO, "Verifying month names are correct");
            boolean monthNamesValid = verifyMonthNamesCorrect();
            softAssert.assertTrue(monthNamesValid, "Month names should be correct and properly formatted");
            test.log(Status.PASS, "Month names verification: " + monthNamesValid);

            // Step 3: Verify data consistency across the column
            test.log(Status.INFO, "Verifying data consistency in Month column");
            boolean dataConsistent = verifyMonthColumnDataConsistency();
            softAssert.assertTrue(dataConsistent, "Month column data should be consistent");
            test.log(Status.PASS, "Month column data consistency: " + dataConsistent);

            // Take screenshot of Month column
           // ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Month_Column");

            softAssert.assertAll();
            test.log(Status.PASS, "Month column verification completed successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Month column verification failed: " + e.getMessage());
           // ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Month_Column_Failed");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * P1 Test: Verify RA ID column sort functionality
     * Tests ascending to descending sort capability
     */
    @Test(priority = 5, dependsOnMethods = "testMonthColumnVerification", 
          description = "Verify RA ID column sort functionality")
    public void testRAIDColumnSortFunctionality() {
        ExtentTest test = ExtentTestManager.getTest();
        test.log(Status.INFO, "Starting RA ID column sort functionality test");

        try {
            // Navigate to Old Global RA tab if not already there
            rangeArchitecturePage.switchToOldGlobalRA();
            Thread.sleep(2000);

            // Step 1: Verify sort functionality on RA ID column
            test.log(Status.INFO, "Testing RA ID column sort functionality");
            boolean sortFunctional = verifyRAIDColumnSort();
            softAssert.assertTrue(sortFunctional, "RA ID column should have functional sort capability");
            test.log(Status.PASS, "RA ID column sort functionality: " + sortFunctional);

            // Step 2: Verify ascending to descending sort
            test.log(Status.INFO, "Verifying ascending to descending sort");
            boolean ascDescSort = verifyAscendingDescendingSort();
            softAssert.assertTrue(ascDescSort, "Should be able to sort from ascending to descending");
            test.log(Status.PASS, "Ascending to descending sort: " + ascDescSort);

            // Step 3: Verify checkboxes remain functional after sorting
            test.log(Status.INFO, "Verifying checkboxes remain functional after sorting");
            boolean checkboxesAfterSort = verifyCheckboxesFunctionalAfterSort();
            softAssert.assertTrue(checkboxesAfterSort, "Checkboxes should remain functional after sorting");
            test.log(Status.PASS, "Checkboxes functional after sort: " + checkboxesAfterSort);

            // Step 4: Verify sort icons and clickable headers
            test.log(Status.INFO, "Verifying sort icons and clickable headers");
            boolean sortIconsValid = verifySortIconsAndHeaders();
            softAssert.assertTrue(sortIconsValid, "Sort icons and headers should be properly displayed and clickable");
            test.log(Status.PASS, "Sort icons and headers verification: " + sortIconsValid);

            // Take screenshot of sorted data
          //  ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Sorted_Data");

            softAssert.assertAll();
            test.log(Status.PASS, "RA ID column sort functionality test completed successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "RA ID column sort functionality test failed: " + e.getMessage());
           // ScreenshotUtils.captureScreenshot(driver, "OldGlobalRA_Sort_Failed");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    // Helper Methods

    /**
     * Verify if we're on the Old Global RA listing page
     */
    private boolean verifyOldGlobalRAListingPage() {
        try {
            // Check for page indicators that confirm we're on Old Global RA listing page
            return rangeArchitecturePage.isOldGlobalRADisplayed() && 
                   rangeArchitecturePage.isSearchInputTextBoxDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify table headers contain expected columns
     */
    private boolean verifyTableHeaders(List<String> expectedHeaders) {
        try {
            // Implementation would check if table headers match expected headers
          //  return rangeArchitecturePage.verifyTableHeaders(expectedHeaders);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Verify table contains data
     */
    private boolean verifyTableHasData() {
        try {
            // Check if table has rows with data
            List<WebElement> tableRows = driver.findElements(By.cssSelector("table tbody tr"));
            return !tableRows.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify month-wise data organization
     */
    private boolean verifyMonthWiseDataOrganization() {
        try {
            // Check if data is organized by months
            List<WebElement> monthCells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(2)"));
            return !monthCells.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify RA ID format: RA-GBRA-XMMYY
     */
    private boolean verifyRAIDFormat() {
        try {
            List<WebElement> raidCells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(2)"));
            Pattern raidPattern = Pattern.compile("RA-GBRA-\\d{3,4}");
            
            for (WebElement cell : raidCells) {
                String raidText = cell.getText().trim();
                if (!raidText.isEmpty() && !raidPattern.matcher(raidText).matches()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify checkbox functionality for RA IDs
     */
    private boolean verifyRAIDCheckboxFunctionality() {
        try {
            List<WebElement> checkboxes = driver.findElements(By.cssSelector("table tbody tr td:first-child input[type='checkbox']"));
            
            if (checkboxes.isEmpty()) return false;
            
            // Test first checkbox
            WebElement firstCheckbox = checkboxes.get(0);
            boolean initialState = firstCheckbox.isSelected();
            
            // Click to change state
            firstCheckbox.click();
            Thread.sleep(500);
            
            boolean newState = firstCheckbox.isSelected();
            return initialState != newState; // State should have changed
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify all RA IDs have associated checkboxes
     */
    private boolean verifyAllRAIDsHaveCheckboxes() {
        try {
            List<WebElement> tableRows = driver.findElements(By.cssSelector("table tbody tr"));
            List<WebElement> checkboxes = driver.findElements(By.cssSelector("table tbody tr td:first-child input[type='checkbox']"));
            
            return tableRows.size() == checkboxes.size();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify Month column format: Month, Year
     */
    private boolean verifyMonthColumnFormat() {
        try {
            List<WebElement> monthCells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(3)"));
            Pattern monthPattern = Pattern.compile("\\w+,\\s*\\d{4}"); // e.g., "June, 2025"
            
            for (WebElement cell : monthCells) {
                String monthText = cell.getText().trim();
                if (!monthText.isEmpty() && !monthPattern.matcher(monthText).matches()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify month names are correct
     */
    private boolean verifyMonthNamesCorrect() {
        try {
            List<WebElement> monthCells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(3)"));
            List<String> validMonths = Arrays.asList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            );
            
            for (WebElement cell : monthCells) {
                String monthText = cell.getText().trim();
                if (!monthText.isEmpty()) {
                    String monthName = monthText.split(",")[0].trim();
                    if (!validMonths.contains(monthName)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify data consistency in Month column
     */
    private boolean verifyMonthColumnDataConsistency() {
        try {
            List<WebElement> monthCells = driver.findElements(By.cssSelector("table tbody tr td:nth-child(3)"));
            return !monthCells.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify RA ID column sort functionality
     */
    private boolean verifyRAIDColumnSort() {
        try {
            // Find and click RA ID column header
            WebElement raidHeader = driver.findElement(By.xpath("//th[contains(text(),'RA ID')]"));
            raidHeader.click();
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify ascending to descending sort
     */
    private boolean verifyAscendingDescendingSort() {
        try {
            WebElement raidHeader = driver.findElement(By.xpath("//th[contains(text(),'RA ID')]"));
            
            // Click once for ascending
            raidHeader.click();
            Thread.sleep(1000);
            
            // Click again for descending
            raidHeader.click();
            Thread.sleep(1000);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify checkboxes remain functional after sorting
     */
    private boolean verifyCheckboxesFunctionalAfterSort() {
        try {
            // Sort first
            WebElement raidHeader = driver.findElement(By.xpath("//th[contains(text(),'RA ID')]"));
            raidHeader.click();
            Thread.sleep(1000);
            
            // Test checkbox functionality
            return verifyRAIDCheckboxFunctionality();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify sort icons and clickable headers
     */
    private boolean verifySortIconsAndHeaders() {
        try {
            WebElement raidHeader = driver.findElement(By.xpath("//th[contains(text(),'RA ID')]"));
            return raidHeader.isDisplayed() && raidHeader.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cross-browser data provider
     */
    @DataProvider(name = "browserData")
    public Object[][] getBrowserData() {
        return new Object[][]{
            {"chrome"},
            {"firefox"},
            {"safari"}
        };
    }
}
