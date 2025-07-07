package com.impetus.tests.ra;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.impetus.base.BasePage;
import com.impetus.pages.DashboardPage;
import com.impetus.pages.RangeArchitecturePage;
import com.impetus.tests.DashboardTest;
import com.impetus.utils.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CurrentClusterRATest extends BasePage {

    private WebDriver driver;
    private DashboardPage dashboardPage;
    private RangeArchitecturePage rangeArchitecturePage;

    //  private static final String RA_FILE_PATH = "src/test/java/com/impetus/data/RA.xlsx";
    String RA_FILE_PATH = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
    private String downloadPath = System.getProperty("user.home") + "/Downloads";
    private static final String TEST_FILES_PATH = "src/test/resources/testfiles/";
    private static final String LARGE_TEST_FILE_PATH = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/LargeRA.xlsx";


    public CurrentClusterRATest() {
        super(DriverManager.getDriver());
    }

    @BeforeClass
        public void setup() {
            driver = DriverManager.getDriver();

            // Use LoginHelper to do login with config credentials
            LoginHelper loginHelper = new LoginHelper(driver);
            loginHelper.loginToApplication();

            dashboardPage = new DashboardPage(driver);
            rangeArchitecturePage = new RangeArchitecturePage(driver);

            // ✅ Call the reusable method from DashboardTest to ensure navigation/setup
            DashboardTest dashboardTest = new DashboardTest();
            // dashboardTest.verifyOdmBuyerDashboard();
            dashboardTest.uvpSubMenu();
        }

        @Test(description = "Verify Range Architecture Navigation and Sections")
        public void testRangeArchitectureNavigation() {
            try {
                ExtentTest test = ExtentTestManager.getTest();
                CommonUtils.waitForPageLoad(driver);
                Thread.sleep(2000);

                // Retry mechanism for page load verification
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                boolean isLoaded = wait.until(driver -> {
                    try {
                        return rangeArchitecturePage.isPageLoaded();
                    } catch (StaleElementReferenceException e) {
                        return false;
                    }
                });

                ExtentTestManager.getTest().log(com.aventstack.extentreports.Status.INFO, "RA page should be loading");
                Assert.assertTrue(isLoaded, "Range Architecture page should be loaded");
            } catch (Exception e) {
                Assert.fail("Failed to verify Range Architecture page load: " + e.getMessage());
            }
            Assert.assertTrue(rangeArchitecturePage.isPageLoaded(), "Range Architecture page should be loaded");
            CommonUtils.takeScreenshot(driver, "Verify RA Page loaded");
            ExtentTestManager.getTest().log(Status.INFO, "Current Global RA Tab id displayed");
            Assert.assertTrue(rangeArchitecturePage.isCurrentGlobalRADisplayed(), "Current Global RA Tab id displayed");
            CommonUtils.takeScreenshot(driver, "Verify Current Global RA tab");
            ExtentTestManager.getTest().log(Status.INFO, "Current Cluster RA should be displayed");
            Assert.assertTrue(rangeArchitecturePage.isCurrentClusterRADisplayed(), "Current Cluster RA should be displayed");
            CommonUtils.takeScreenshot(driver, "Verify Current Cluster RA Tab");
            ExtentTestManager.getTest().log(Status.INFO, "Old Global RA should be displayed");
            Assert.assertTrue(rangeArchitecturePage.isOldGlobalRADisplayed(), "Old Global RA should be displayed");
            CommonUtils.takeScreenshot(driver, "Verify Old Global RA Tab");
            ExtentTestManager.getTest().log(Status.INFO, "Old Cluster RA should be displayed");
            Assert.assertTrue(rangeArchitecturePage.isOldClusterRADisplayed(), "Old Cluster RA should be displayed");
            CommonUtils.takeScreenshot(driver, "Verify Old Cluster RA Tab");

        }

        @Test(description = "Verify Range Architecture Content", dependsOnMethods = "testRangeArchitectureNavigation")
        public void tableContentsInCurrentClusterRATab() {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the table headers and content");

            //click on Current Cluster RA Tab
            rangeArchitecturePage.clickCurrentClusterRAButton();
            CommonUtils.waitForPageLoad(driver);

            // Verify table headers and content
            ExtentTestManager.getTest().log(Status.INFO, "Search Input text box should be displayed");
            Assert.assertTrue(rangeArchitecturePage.isSearchInputTextBoxDisplayed(), "Search Input text box should be displayed");
            CommonUtils.takeScreenshot(driver, "Verify search input");
            ExtentTestManager.getTest().log(Status.INFO, "Filter button should be displayed");
            Assert.assertTrue(rangeArchitecturePage.isFilterButtonDisplayed(), "Filter button should be displayed");
            CommonUtils.takeScreenshot(driver, "verifyFilter button");
            ExtentTestManager.getTest().log(Status.INFO, "Table headers should be present");
            Assert.assertTrue(rangeArchitecturePage.verifyTableHeaders(), "Table headers should be present");
            CommonUtils.takeScreenshot(driver, "verifyTable headers");
            ExtentTestManager.getTest().log(Status.INFO, "All headers and data should be present");
            Assert.assertTrue(rangeArchitecturePage.validateAllHeaders(), "All headers and data should be present");
            CommonUtils.takeScreenshot(driver, "verifyAll data headers");
        }

        @Test(description = "Verify Search Bar Functionality", dependsOnMethods = "testRangeArchitectureNavigation")
        public void verifySearchBar() {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentTestManager.getTest().log(Status.INFO, "Verifying search bar functionality in Range Architecture page");
            CommonUtils.takeScreenshot(driver, "verifySearchBar");
            Assert.assertTrue(rangeArchitecturePage.verifySearchBarFunctionality(),
                    "Search bar should be clickable and functional");
        }

        @Test(description = "Verify search bar presence and functionality", dependsOnMethods = "testRangeArchitectureNavigation")
        public void verifySearchBarBasics() {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentTestManager.getTest().log(Status.INFO, "Verifying search bar Presence in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.verifySearchBarPresence(),
                    "Search bar should be present and enabled");
            CommonUtils.takeScreenshot(driver,"Search bar is present");
        }

        @Test(description = "Verify exact search functionality", dependsOnMethods = "testRangeArchitectureNavigation")
        public void verifyExactSearch() {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentTestManager.getTest().log(Status.INFO, "Entering the text in search bar in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.enterSearchText("Western Wear"),
                    "Should be able to enter western wear in search bar");
            CommonUtils.takeScreenshot(driver,"Entered the text");
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the search results in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.verifySearchResults("Western Wear"),
                    "Should find exact matches");
            CommonUtils.takeScreenshot(driver,"Search results");
        }

        @Test(description = "Verify partial search functionality", dependsOnMethods = "testRangeArchitectureNavigation")
        public void verifyPartialSearch() {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentTestManager.getTest().log(Status.INFO, "Entering the partial text in search  Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.verifyPartialSearch("West"),
                    "Should find partial matches");
            CommonUtils.takeScreenshot(driver,"Partial search is successful");
        }

        @Test(description = "Verify pagination functionality", dependsOnMethods = "testRangeArchitectureNavigation")
        public void verifyPagination() {
            try {
                ExtentTest test = ExtentTestManager.getTest();

                // First verify the table is loaded

                ExtentTestManager.getTest().log(Status.INFO, "Verifying the table is loaded in Range Architecture page");
                CommonUtils.waitForVisibility(driver, rangeArchitecturePage.getFirstTableRow(), 20);

                // Step 1: Verify pagination range display
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the table is loaded in Range Architecture page");
                Assert.assertTrue(rangeArchitecturePage.verifyPaginationRange(),
                        "Pagination range should be correctly displayed");
                CommonUtils.takeScreenshot(driver,"Pagination range is displayed correctly");

                // Step 2: Verify first page data
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the table data for the selected page is loaded in Range Architecture page");
                List<Map<String, String>> firstPageData = rangeArchitecturePage.getTableDataForPage();
                Assert.assertFalse(firstPageData.isEmpty(), "First page should contain data");
                Assert.assertTrue(firstPageData.size() <= 25, "First page should have maximum 25 rows");
                CommonUtils.takeScreenshot(driver,"first page should contain the data");

                // Step 3: Verify next page navigation
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the next page navigation in Range Architecture page");
                if (rangeArchitecturePage.isNextButtonEnabled()) {
                    Assert.assertTrue(rangeArchitecturePage.verifyNextPageNavigation(),
                            "Next page navigation should work correctly");
                    CommonUtils.takeScreenshot(driver,"Next page navigation");

                    // Step 4: Verify page number updates after navigation
                    ExtentTestManager.getTest().log(Status.INFO, "Verifying the page number update in Range Architecture page");
                    Assert.assertTrue(rangeArchitecturePage.verifyPageNumberUpdate(),
                            "Page number should update correctly");
                    CommonUtils.takeScreenshot(driver,"Page number should updated");
                }

                // Step 5: Verify previous button on first page
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the first page navigation and previous button in Range Architecture page");
                rangeArchitecturePage.navigateToFirstPage();
                Assert.assertTrue(rangeArchitecturePage.verifyFirstPagePreviousButton(),
                        "Previous button should be disabled on first page");
                CommonUtils.takeScreenshot(driver,"Previous button should be disabled");

                // Step 6: Verify data consistency across pages
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the table data in all the pages in Range Architecture page");
                List<Map<String, String>> allPagesData = rangeArchitecturePage.getAllPagesData();
                Assert.assertFalse(allPagesData.isEmpty(),
                        "Should have data across all pages");
                CommonUtils.takeScreenshot(driver,"Data should be available");

                // Step 7: Verify next button on last page
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the Next button in last page in Range Architecture page");
                Assert.assertTrue(rangeArchitecturePage.verifyLastPageNextButton(),
                        "Next button should be disabled on last page");
                CommonUtils.takeScreenshot(driver,"Last page next button");
            } catch (Exception e) {
                Assert.fail("Pagination test failed: " + e.getMessage());
            }
        }

        @Test(description = "Verify sort functionality of the headers")//,dependsOnMethods = "verifyPagination")
        public void verifySortingFunctionality() {
            ExtentTest test = ExtentTestManager.getTest();
            ExtentTestManager.getTest().log(Status.INFO, "Verifying to get the table first row data  in Range Architecture page");
            CommonUtils.waitForVisibility(driver, rangeArchitecturePage.getFirstTableRow(), 20);

            // Verify column order
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the column order for the table data in Range Architecture page");
            List<String> expectedColumns = Arrays.asList(
                    "Family", "Class Name", "Brick Name", "Top Brick",
                    "Brick", "Enrichment", "MRP Range", "Options",
                    "ODM", "OEM", "Total", "Fill Rate", "Action"
            );
            Assert.assertTrue(CommonUtils.verifyColumnOrder(driver, expectedColumns));
            CommonUtils.takeScreenshot(driver,"Column order for table data");

            // Verify sort symbols present
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the sort symbol present in Range Architecture page");
            List<String> sortableColumns = Arrays.asList(
                    "Family", "Class Name", "Brick Name", "Top Brick",
                    "Brick", "Enrichment", "Fill Rate"
            );
            Assert.assertTrue(CommonUtils.verifySortingSymbolsPresent(driver, sortableColumns));
            CommonUtils.takeScreenshot(driver,"Sort symbol");

            // Get table rows
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the table rows in Range Architecture page");
            List<WebElement> rows = driver.findElements(By.xpath("//table//tbody/tr"));

            // Verify sorting for each column
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the sorting for each column  in Range Architecture page");
            Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Family", rows, 1));
            CommonUtils.takeScreenshot(driver,"Family header");
            Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Class Name", rows, 2));
            CommonUtils.takeScreenshot(driver,"ClassName header");
            Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Brick Name", rows, 3));
            CommonUtils.takeScreenshot(driver,"BrickName header");
            Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Top Brick", rows, 4));
            CommonUtils.takeScreenshot(driver,"TopBrick header");
            Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Brick", rows, 5));
            CommonUtils.takeScreenshot(driver,"Brick header");
            Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Enrichment", rows, 6));
            CommonUtils.takeScreenshot(driver,"Enrichment header");
            Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Fill Rate", rows, 12));
            CommonUtils.takeScreenshot(driver,"FillRate header");
        }

        @Test(priority = 1, description = "Verify View button redirects to detailed page")
        public void testViewButtonRedirectsToDetailedPage() {
        ExtentTest test = ExtentTestManager.getTest();

            // Verify listing page is displayed
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the List page display in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
                    "RAID listing page should be displayed");

            // Take screenshot of listing page
            CommonUtils.takeScreenshot(driver,"listing_page_before_click");

            // Get current URL and RAID ID
          //  ExtentTestManager.getTest().log(Status.INFO, "Verifying the first page navigation and previous button in Range Architecture page");
            String listingPageUrl = rangeArchitecturePage.getCurrentUrl();
            String raidId = rangeArchitecturePage.getRaidIdFromRow(0);
            CommonUtils.takeScreenshot(driver,"View button view");

            // Click on first View button
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the first view button in Range Architecture page");
            rangeArchitecturePage.clickFirstViewButton();

            // Verify navigation to details page
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the raid page navigation in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
                    "Should navigate to RAID details page");

            // Verify URL has changed
            String detailsPageUrl = rangeArchitecturePage.getCurrentUrl();
            Assert.assertNotEquals(listingPageUrl, detailsPageUrl,
                    "URL should change after clicking View button");

            // Take screenshot of details page
            CommonUtils.takeScreenshot(driver,"details_page_after_click");

            System.out.println("✓ View button successfully redirects to detailed page");
        }

        @Test(priority = 2, description = "Verify back button is present on redirected page")
        public void testBackButtonPresenceOnDetailedPage() {

        ExtentTest test = ExtentTestManager.getTest();
            // Navigate to details page
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the navigation to the details page in Range Architecture page");
            rangeArchitecturePage.clickFirstViewButton();

            // Verify back button is displayed
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the back button in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isBackButtonDisplayed(),
                    "Back button should be displayed on details page");

            // Verify back button is clickable
            Assert.assertTrue(rangeArchitecturePage.isBackButtonClickable(),
                    "Back button should be clickable");

            // Take screenshot
            CommonUtils.takeScreenshot(driver,"back_button_verification");

            System.out.println("✓ Back button is present and clickable on details page");
        }

        @Test(priority = 3, description = "Verify RAID is displayed on top left next to back button")
        public void testRaidDisplayedTopLeft() {
        ExtentTest test = ExtentTestManager.getTest();

            // Navigate to details page
            String expectedRaidId = rangeArchitecturePage.getRaidIdFromRow(0);
            rangeArchitecturePage.clickFirstViewButton();

            // Verify RAID is displayed on top left
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the raid display on top left in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isRaidDisplayedTopLeft(),
                    "RAID should be displayed on top left next to back button");

            // Verify RAID ID matches
            String displayedRaidId = rangeArchitecturePage.getRaidDisplayText();
            Assert.assertTrue(displayedRaidId.contains(expectedRaidId),
                    "Displayed RAID ID should match the selected RAID");

            // Take screenshot
            CommonUtils.takeScreenshot(driver,"raid_display_top_left");

            System.out.println("✓ RAID is correctly displayed on top left next to back button");
        }

        @Test(priority = 4, description = "Verify page lands with two tabs, default ODM tab with table, search, filter")
        public void testDefaultOdmTabWithFeatures() {
        ExtentTest test = ExtentTestManager.getTest();
            // Navigate to details page
            rangeArchitecturePage.clickFirstViewButton();

            // Verify tabs are displayed
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the tabs display in raid page in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.areTabsDisplayed(),
                    "Tabs should be displayed on details page");

            // Verify ODM tab is active by default
            Assert.assertTrue(rangeArchitecturePage.isOdmTabActive(),
                    "ODM tab should be active by default");

            // Verify table is displayed in ODM tab
            Assert.assertTrue(rangeArchitecturePage.isTableDisplayedInOdmTab(),
                    "Table should be displayed in ODM tab");

            // Verify search box is displayed
            Assert.assertTrue(rangeArchitecturePage.isSearchBoxDisplayed(),
                    "Search box should be displayed");

            // Verify filter button is displayed
            Assert.assertTrue(rangeArchitecturePage.isFilterButtonDisplayed(),
                    "Filter button should be displayed");

            // Take screenshot
            CommonUtils.takeScreenshot(driver,"odm_tab_with_features");

            System.out.println("✓ Page correctly lands with ODM tab active and all features present");
        }

        @Test(priority = 5, description = "Verify back button navigates to listing page")
        public void testBackButtonNavigatesToListingPage() {
        ExtentTest test = ExtentTestManager.getTest();
            // Get listing page URL
            String originalListingUrl = rangeArchitecturePage.getCurrentUrl();

            // Navigate to details page
            rangeArchitecturePage.clickFirstViewButton();

            // Verify we're on details page
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the details page in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
                    "Should be on details page");

            // Take screenshot before clicking back
            CommonUtils.takeScreenshot(driver,"before_clicking_back_button");

            // Click back button
            rangeArchitecturePage.clickBackButton();

            // Verify we're back on listing page
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the listing page in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
                    "Should navigate back to listing page");

            // Verify URL is back to listing page
            String currentUrl = rangeArchitecturePage.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("raids") || currentUrl.equals(originalListingUrl),
                    "Should be back on the main listing page");

            // Take screenshot after navigation
            CommonUtils.takeScreenshot(driver,"after_clicking_back_button");

            System.out.println("✓ Back button successfully navigates to listing page");
        }

        @Test(priority = 6, description = "Verify View buttons are clickable for all RAIDs")
        public void testAllViewButtonsClickable() {
        ExtentTest test = ExtentTestManager.getTest();

            // Verify all view buttons are clickable
            ExtentTestManager.getTest().log(Status.INFO, "Verifying all the view buttons in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.areViewButtonsClickable(),
                    "All View buttons should be clickable");

            // Get count of view buttons
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the count of view buttons in Range Architecture page");
            int viewButtonCount = rangeArchitecturePage.getViewButtonsCount();
            Assert.assertTrue(viewButtonCount > 0,
                    "There should be at least one View button");

            // Take screenshot
            CommonUtils.takeScreenshot(driver,"all_view_buttons_verification");

            System.out.println("✓ All " + viewButtonCount + " View buttons are clickable");
        }

        @Test(priority = 7, description = "Test complete navigation flow for multiple RAIDs")
        public void testCompleteNavigationFlowMultipleRaids() {

        ExtentTest test = ExtentTestManager.getTest();
            int viewButtonCount = Math.min(rangeArchitecturePage.getViewButtonsCount(), 3); // Test first 3 RAIDs

            for (int i = 0; i < viewButtonCount; i++) {
                // Get RAID ID
                String raidId = rangeArchitecturePage.getRaidIdFromRow(i);

                // Click View button for current row
                rangeArchitecturePage.clickViewButtonForRow(i);

                // Verify details page
                Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
                        "Details page should be displayed for RAID: " + raidId);

                // Take screenshot
                CommonUtils.takeScreenshot(driver,"raid_" + raidId + "_details_page");

                // Click back button
                rangeArchitecturePage.clickBackButton();

                // Verify back on listing page
                Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
                        "Should be back on listing page after viewing RAID: " + raidId);

                System.out.println("✓ Navigation flow verified for RAID: " + raidId);
            }

//    @Test(description = "Verify table data across pages",dependsOnMethods = "verifyPagination")
//    public void verifyTableDataAcrossPages() {
//        // Get data from first page
//        List<Map<String, String>> firstPageData = rangeArchitecturePage.getTableDataForPage();
//        Assert.assertFalse(firstPageData.isEmpty(), "First page should contain data");
//        Assert.assertTrue(firstPageData.size() <= 25, "First page should have maximum 25 rows");
//
//        // Get data from all pages
//        List<Map<String, String>> allPagesData = rangeArchitecturePage.getAllPagesData();
//        Assert.assertFalse(allPagesData.isEmpty(), "Should have data across all pages");
//    }

        }

        @Test(description = "Verify Download button functionality")
        public void verifyDownloadFunctionality () {
        ExtentTest test = ExtentTestManager.getTest();

            // Click the Download button
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the download button display in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isDownloadButtonDisplayed(), "Download button should be displayed");
            CommonUtils.takeScreenshot(driver,"Download button verification");
            //Assert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(), "Upload modal should be displayed")
            // isDisplayed() && downloadBtn.isEnabled(), "Download button not clickable");
            //  downloadBtn.click();
        }

        @Test(description = "Verify Download button functionality")
        public void testCompleteDownloadFunctionality () throws Exception {
        ExtentTest test = ExtentTestManager.getTest();

            // 1. Trigger download
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the download and file path in Range Architecture page");
            String downloadedFile = rangeArchitecturePage.triggerDownloadAndGetFilePath();
            System.out.println("Downloaded file at: " + downloadedFile);

            // ✅ Validate success message
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the download success message in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isDownloadSuccessMessageDisplayed(), "Download Successful message should be displayed");

            // 2. Validate file exists and naming convention
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the Excel file and Naming convention in Range Architecture page");
            Assert.assertTrue(ExcelUtils.isValidExcelFile(downloadedFile));
            Assert.assertTrue(ExcelUtils.validateFileNamingConvention(
                    new File(downloadedFile).getName()));
            CommonUtils.takeScreenshot(driver,"Excel file and naming convention");

            // 3. Validate headers
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the excel headers in Range Architecture page");
            Assert.assertTrue(ExcelUtils.validateExcelHeaders(downloadedFile, "RA-250605-0001"));
            CommonUtils.takeScreenshot(driver,"Excel headers verification");

            // 4. Read and validate data
            ExtentTestManager.getTest().log(Status.INFO, "Verifying and validating the data table in Range Architecture page");
            List<Map<String, String>> data = ExcelUtils.readExcelData(downloadedFile, "RA-250605-0001");
            Assert.assertTrue(ExcelUtils.validateDataTypes(data));
            Assert.assertTrue(ExcelUtils.validateTotalQtyCalculation(data));
            Assert.assertTrue(ExcelUtils.validateFillPercentageCalculation(data));
            CommonUtils.takeScreenshot(driver,"Excel data verification");
        }

        @Test(priority = 1, description = "Verify Filter button visibility")
        public void testFilterButtonVisibility () {
        ExtentTest test = ExtentTestManager.getTest();
            // Verify if the Filter button is present right next to the Search bar in blue color
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter button display in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isFilterButtonDisplayed(),
                    "Filter button should be visible");

            Assert.assertTrue(rangeArchitecturePage.isFilterButtonNextToSearchBar(),
                    "Filter button should be next to search bar");

            Assert.assertTrue(rangeArchitecturePage.isFilterButtonBlue(),
                    "Filter button should be blue in color");

            CommonUtils.takeScreenshot(driver, "filter_button_visibility");
        }

        @Test(priority = 2, description = "Verify Filter button functionality")
        public void testFilterButtonFunctionality () {
        ExtentTest test = ExtentTestManager.getTest();

            // Verify that when the Filter button is clicked, all table headers appear as dropdown fields
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the Filter button is clickable and table headers appear in Range Architecture page");
            rangeArchitecturePage.clickFilterButton();

            Assert.assertTrue(rangeArchitecturePage.areFilterHeadersDisplayed(),
                    "All filter headers should be displayed after clicking filter button");

            Assert.assertTrue(rangeArchitecturePage.isClearAllButtonDisplayed(),
                    "Clear All button should be displayed");

            Assert.assertTrue(rangeArchitecturePage.isClearAllButtonBlue(),
                    "Clear All button should be blue in color");

            CommonUtils.takeScreenshot(driver, "filter_button_functionality");
        }

        @Test(priority = 3, description = "Verify headers on clicking Filter button")
        public void testFilterHeaders () {
        ExtentTest test = ExtentTestManager.getTest();
            rangeArchitecturePage.clickFilterButton();

            // Verify all table headers are displayed in correct order
            ExtentTestManager.getTest().log(Status.INFO, "Verifying all the table headers are displaying in Range Architecture page");
            List<String> expectedHeaders = Arrays.asList(
                    "Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment"
            );

            List<String> actualHeaders = rangeArchitecturePage.getFilterHeadersOrder();

            Assert.assertEquals(actualHeaders, expectedHeaders,
                    "Filter headers should appear in the correct order");

            Assert.assertTrue(rangeArchitecturePage.isClearAllButtonDisplayed(),
                    "Clear All button should be displayed next to headers");

            CommonUtils.takeScreenshot(driver, "filter_headers_display");
        }

        @Test(priority = 4, description = "Verify functionality of each filter header")
        public void testFilterHeaderFunctionality () {

        ExtentTest test = ExtentTestManager.getTest();
            rangeArchitecturePage.clickFilterButton();

            String[] headers = {"Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment"};

            for (String header : headers) {
                // Click on filter header arrow
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter header arrow in Range Architecture page");
                rangeArchitecturePage.clickFilterHeader(header);

                // Verify dropdown content is displayed
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the dropdown content is displayed in Range Architecture page");
                Assert.assertTrue(rangeArchitecturePage.isFilterDropdownDisplayed(),
                        header + " filter dropdown should be displayed");

                // Verify search bar is present
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the search bar is present in Range Architecture page");
                Assert.assertTrue(rangeArchitecturePage.isFilterSearchBoxDisplayed(),
                        header + " filter should have search bar");

                // Verify scrollable list is present
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the scrollable list in Range Architecture page");
                Assert.assertTrue(rangeArchitecturePage.isScrollableListDisplayed(),
                        header + " filter should have scrollable list");

                // Verify clear button is present
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the clear button in Range Architecture page");
                Assert.assertTrue(rangeArchitecturePage.isFilterClearButtonDisplayed(),
                        header + " filter should have clear button");

                CommonUtils.takeScreenshot(driver, "filter_" + header.toLowerCase().replace(" ", "_") + "_dropdown");

                // Close dropdown by clicking elsewhere or pressing escape
                rangeArchitecturePage.clickFilterButton(); // Close current dropdown
            }
        }

        @Test(priority = 5, description = "Verify select/unselect dropdown values & clear")
        public void testFilterSelectionAndClear () {
        ExtentTest test = ExtentTestManager.getTest();
            rangeArchitecturePage.clickFilterButton();
            rangeArchitecturePage.clickFilterHeader("Family");

            // Test single selection
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the single selection in dropdown in Range Architecture page");
            rangeArchitecturePage.selectFilterOption("Women");
            CommonUtils.takeScreenshot(driver, "single_option_selected");

            // Test multiple selection
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the Multiple selection in dropdown in Range Architecture page");
            List<String> multipleOptions = Arrays.asList("Men", "Kids");
            rangeArchitecturePage.selectMultipleFilterOptions(multipleOptions);
            CommonUtils.takeScreenshot(driver, "multiple_options_selected");

            // Test unselect
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the unselect in dropdown in Range Architecture page");
            rangeArchitecturePage.unselectFilterOption("Women");
            CommonUtils.takeScreenshot(driver, "option_unselected");

            // Test clear button functionality
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the clear button functionality in dropdown in Range Architecture page");
            rangeArchitecturePage.clickFilterClearButton();
            Assert.assertTrue(rangeArchitecturePage.areAllOptionsUnselected(),
                    "All options should be unselected after clicking clear button");

            CommonUtils.takeScreenshot(driver, "all_options_cleared");
        }


        @Test(priority = 6, description = "Verify search bar under filter dropdown")
        public void testFilterSearchBar () {
        ExtentTest test = ExtentTestManager.getTest();
            rangeArchitecturePage.clickFilterButton();
            rangeArchitecturePage.clickFilterHeader("Family");

            // Verify search bar is editable
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the search bar is editable in dropdown in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.isFilterSearchBoxEditable(),
                    "Filter search bar should be editable");

            // Test complete value search
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the text entering in search bar in dropdown in Range Architecture page");
            rangeArchitecturePage.searchInFilter("Women");
            List<String> completeSearchResults = rangeArchitecturePage.getFilteredOptions();
            Assert.assertTrue(completeSearchResults.contains("Women"),
                    "Complete search should return exact match");
            CommonUtils.takeScreenshot(driver, "complete_search_results");

            // Test partial value search
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the partial text entering in search bar in dropdown in Range Architecture page");
            rangeArchitecturePage.searchInFilter("Wo");
            Assert.assertTrue(rangeArchitecturePage.verifyPartialSearch("Wo"),
                    "Partial search should return matching results");
            CommonUtils.takeScreenshot(driver, "partial_search_results");

            // Clear search
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the clear search in dropdown in Range Architecture page");
            rangeArchitecturePage.searchInFilter("");
            CommonUtils.takeScreenshot(driver, "search_cleared");
        }

//    @Test(priority = 7, description = "Verify Clear All functionality")
//    public void testClearAllFunctionality() {
//        rangeArchitecturePage.clickFilterButton();
//
//        // Select some options from different filters
//        rangeArchitecturePage.clickFilterHeader("Family");
//        rangeArchitecturePage.selectFilterOption("Women");
//
//        rangeArchitecturePage.clickFilterHeader("Class Name");
//        rangeArchitecturePage.selectFilterOption("Western Wear");
//
//        CommonUtils.takeScreenshot(driver,"before_clear_all");
//
//        // Click Clear All
//        rangeArchitecturePage.clickClearAllButton();
//
//        CommonUtils.takeScreenshot(driver,"after_clear_all");
//
//        // Verify all filters are cleared (this would need additional verification logic)
//        // You might need to add methods to check if filters are applied
//    }

    @Test(priority = 1, description = "Verify that each filter has all list items with checkboxes")
    public void testFilterListItemsWithCheckboxes() {
            try {
                ExtentTest test = ExtentTestManager.getTest();
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter has list items in checkboxes in dropdown in Range Architecture page");
                rangeArchitecturePage.clickFilterButton();
                List<String> filterNames = Arrays.asList("Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment");

                for (String filterName : filterNames) {
                    int count = rangeArchitecturePage.verifyFilterListItemsWithCheckboxes(filterName);
                    Assert.assertTrue(count > 0, "Filter '" + filterName + "' should have checkboxes for all list items");
                }

                CommonUtils.takeScreenshot(driver,"filter_list_items_with_checkboxes");
            } catch (Exception e) {
                CommonUtils.takeScreenshot(driver,"filter_list_items_error");
                Assert.fail("Test failed: " + e.getMessage());
            }
        }

    @Test(priority = 2, description = "Verify search bar presence in each table filter")
    public void testSearchBarPresenceInFilters() {
            try {
                ExtentTest test = ExtentTestManager.getTest();
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the search bar presence in filter dropdown in Range Architecture page");
                List<String> filterNames = Arrays.asList("Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment");

                for (String filterName : filterNames) {
                    boolean hasSearchBar = rangeArchitecturePage.verifySearchBarInFilter(filterName);
                    Assert.assertTrue(hasSearchBar, "Filter '" + filterName + "' should have a search bar");
                }

                CommonUtils.takeScreenshot(driver,"search_bar_presence_verification");
            } catch (Exception e) {
                CommonUtils.takeScreenshot(driver,"search_bar_presence_error");
                Assert.fail("Test failed: " + e.getMessage());
            }
        }

    @Test(priority = 3, description = "Verify search functionality in filter dropdowns")
    public void testSearchFunctionalityInFilters() {
            try {
                ExtentTest test = ExtentTestManager.getTest();

                // Test partial search in different filters
                ExtentTestManager.getTest().log(Status.INFO, "Verifying the partial text entering in searchbar in dropdown in Range Architecture page");
                boolean attributeSearch = rangeArchitecturePage.testSearchFunctionality("Brick Name", "Short");
                Assert.assertTrue(attributeSearch, "Search for 'Short' should filter relevant items");

                boolean partialSearch = rangeArchitecturePage.testSearchFunctionality("Family", "Shirt");
                Assert.assertTrue(partialSearch, "Partial search should work correctly");

                CommonUtils.takeScreenshot(driver, "search_functionality_verification");
            } catch (Exception e) {
                CommonUtils.takeScreenshot(driver, "search_functionality_error");
                Assert.fail("Test failed: " + e.getMessage());
            }
        }


    @Test(priority = 4, description = "Verify multi-select functionality in filters")
    public void testMultiSelectFunctionality() {
            try {
                ExtentTest test = ExtentTestManager.getTest();
                List<String> itemsToSelect = Arrays.asList("Item1", "Item2", "Item3");
                boolean multiSelect = rangeArchitecturePage.verifyMultiSelectFunctionality("Family", itemsToSelect);
                Assert.assertTrue(multiSelect, "Filter should support multi-select functionality");

                CommonUtils.takeScreenshot(driver, "multi_select_functionality");
            } catch (Exception e) {
                CommonUtils.takeScreenshot(driver, "multi_select_error");
                Assert.fail("Test failed: " + e.getMessage());
            }
        }

    @Test(description = "Verify that all table header filters support multi-select")
    public void verifyMultiSelectOnAllTableHeaderFilters() {
            ExtentTestManager.getTest().log(Status.INFO, "Validating multi-select capability of table header filters");

            boolean result = rangeArchitecturePage.verifyAllFiltersAreMultiSelectable();

            Assert.assertTrue(result, "Each filter dropdown should support multi-select functionality");
        }

    @Test(priority = 5, description = "Verify sort functionality after filtering")
    public void testSortAfterFiltering() {
            try {
                // Test sorting on different columns after applying filter
                boolean brickSort = rangeArchitecturePage.testSortAfterFiltering("Family", "Men", "Brick Name");
                Assert.assertTrue(brickSort, "Sort should work on Brick Name column after filtering");

                boolean attributeSort = rangeArchitecturePage.testSortAfterFiltering("Class Name", "TestClass", "Attributes");
                Assert.assertTrue(attributeSort, "Sort should work on Attributes column after filtering");

                boolean costSort = rangeArchitecturePage.testSortAfterFiltering("Top Brick", "TestBrick", "Cost Range");
                Assert.assertTrue(costSort, "Sort should work on Cost Range column after filtering");

                CommonUtils.takeScreenshot(driver, "sort_after_filtering");
            } catch (Exception e) {
                CommonUtils.takeScreenshot(driver, "sort_after_filtering_error");
                Assert.fail("Test failed: " + e.getMessage());
            }
        }

    @Test(priority = 6, description = "Verify Clear All functionality")
    public void testClearAllFunctionality() {
            try {
                // First select some filters
                rangeArchitecturePage.verifyMultiSelectFunctionality("Family", Arrays.asList("Item1", "Item2"));

                // Then clear all
                boolean clearAll = rangeArchitecturePage.verifyClearAllFunctionality();
                Assert.assertTrue(clearAll, "Clear All should unselect all filter items");

                CommonUtils.takeScreenshot(driver,"clear_all_functionality");
            } catch (Exception e) {
                CommonUtils.takeScreenshot(driver,"clear_all_error");
                Assert.fail("Test failed: " + e.getMessage());
            }
        }

    @Test(priority = 7, description = "Verify filter header order and clickability")
    public void testFilterHeaderOrderAndClickability() {
            try {
                List<String> expectedOrder = Arrays.asList("Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment");
                boolean correctOrder = rangeArchitecturePage.verifyFilterHeaderOrder(expectedOrder);
                Assert.assertTrue(correctOrder, "Filter headers should be in correct order");

                CommonUtils.takeScreenshot(driver, "filter_header_order");
            } catch (Exception e) {
                CommonUtils.takeScreenshot(driver, "filter_header_order_error");
                Assert.fail("Test failed: " + e.getMessage());
            }
        }

    @Test(priority = 1, description = "Verify modal does not close when clicking outside")
    public void testModalDoesNotCloseOnOutsideClick() {
        // Verify modal is displayed
        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(), "Modal should be displayed");
        CommonUtils.takeScreenshot(driver,"modal_displayed_before_outside_click");

        // Click outside the modal
        rangeArchitecturePage.clickOutsideModal();

        // Wait a moment and verify modal is still displayed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(), "Modal should still be displayed after clicking outside");
        CommonUtils.takeScreenshot(driver,"modal_still_displayed_after_outside_click");
    }

    @Test(priority = 2, description = "Verify modal closes only when clicking X icon")
    public void testModalClosesOnXIconClick() {
        // Verify modal is displayed
        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(), "Modal should be displayed");
        CommonUtils.takeScreenshot(driver,"modal_displayed_before_close_click");

        // Click the X (close) button
        rangeArchitecturePage.clickCloseButton();

        // Verify modal is closed
        Assert.assertTrue(rangeArchitecturePage.isModalClosed(), "Modal should be closed after clicking X icon");
        CommonUtils.takeScreenshot(driver,"modal_closed_after_x_click");
    }

    @Test(priority = 3, description = "Verify Select Cluster mandatory label with asterisk")
    public void testSelectClusterMandatoryLabel() {
        // Verify Select Cluster label is displayed
        Assert.assertTrue(rangeArchitecturePage.isSelectClusterLabelDisplayed(), "Select Cluster label should be displayed");

        // Verify asterisk is displayed indicating mandatory field
        Assert.assertTrue(rangeArchitecturePage.isSelectClusterAsteriskDisplayed(), "Asterisk should be displayed for mandatory field");

        // Verify label text contains "Select Cluster"
        String labelText = rangeArchitecturePage.getSelectClusterLabelText();
        Assert.assertTrue(labelText.contains("Select Cluster"), "Label should contain 'Select Cluster' text");

        CommonUtils.takeScreenshot(driver,"select_cluster_mandatory_label_verified");
    }

    @Test(priority = 4, description = "Verify presence of Select Cluster dropdown field")
    public void testSelectClusterDropdownPresence() {
        // Verify dropdown field is displayed
        Assert.assertTrue(rangeArchitecturePage.isSelectClusterDropdownDisplayed(), "Select Cluster dropdown should be displayed");

        // Verify dropdown has correct placeholder text
        String placeholderText = rangeArchitecturePage.getDropdownPlaceholderText();
        Assert.assertEquals(placeholderText, "Search Clusters", "Dropdown should have 'Search Clusters' placeholder");

        // Verify dropdown arrow is displayed
        Assert.assertTrue(rangeArchitecturePage.isDropdownArrowDisplayed(), "Dropdown arrow should be displayed");

        CommonUtils.takeScreenshot(driver,"select_cluster_dropdown_presence_verified");
    }

    @Test(priority = 5, description = "Verify Select Cluster dropdown is clickable")
    public void testSelectClusterDropdownClickable() {
        // Click on the dropdown field
        rangeArchitecturePage.clickSelectClusterDropdown();

        // Verify dropdown list is displayed
        Assert.assertTrue(rangeArchitecturePage.isDropdownListDisplayed(), "Dropdown list should be displayed after clicking");

        // Verify cluster names are displayed in the list
        List<WebElement> options = rangeArchitecturePage.getClusterOptions();
        Assert.assertTrue(options.size() > 0, "Cluster options should be displayed in dropdown");

        CommonUtils.takeScreenshot(driver,"select_cluster_dropdown_opened");
    }

    @Test(priority = 6, description = "Verify dropdown arrow clickability")
    public void testDropdownArrowClickable() {
        // Click on the dropdown arrow
        rangeArchitecturePage.clickDropdownArrow();

        // Verify dropdown list is displayed
        Assert.assertTrue(rangeArchitecturePage.isDropdownListDisplayed(), "Dropdown list should be displayed after clicking arrow");

        CommonUtils.takeScreenshot(driver,"dropdown_arrow_clicked_list_displayed");
    }

    @Test(priority = 7, description = "Verify functionality of cluster name list")
    public void testClusterNameListFunctionality() {
        // Open dropdown
        rangeArchitecturePage.clickSelectClusterDropdown();

        // Verify scroll bar is present
        Assert.assertTrue(rangeArchitecturePage.isScrollBarDisplayed(), "Scroll bar should be displayed in dropdown list");

        // Verify search bar is present
        Assert.assertTrue(rangeArchitecturePage.isSearchBarDisplayed(), "Search bar should be displayed in dropdown");

        // Test search functionality
        rangeArchitecturePage.searchCluster("Ahmed");
        CommonUtils.takeScreenshot(driver,"search_cluster_ahmed");

        // Clear search and test selection
        rangeArchitecturePage.searchCluster("");

        // Select a cluster option
        rangeArchitecturePage.selectClusterOption("Bengaluru");

        // Verify selected cluster appears in the field
        String selectedValue = rangeArchitecturePage.getSelectedClusterValue();
        Assert.assertTrue(selectedValue.contains("Bengaluru"), "Selected cluster should appear in the field");

        CommonUtils.takeScreenshot(driver,"cluster_selected_bengaluru");
    }

    @Test(priority = 8, description = "Verify search functionality in cluster dropdown")
    public void testClusterSearchFunctionality() {
        // Open dropdown
        rangeArchitecturePage.clickSelectClusterDropdown();

        // Search for specific cluster
        rangeArchitecturePage.searchCluster("Jaipur");

        // Verify search results
        List<WebElement> filteredOptions = rangeArchitecturePage.getClusterOptions();
        Assert.assertTrue(filteredOptions.size() > 0, "Search should return matching results");

        CommonUtils.takeScreenshot(driver,"search_results_jaipur");

        // Test search with partial text
        rangeArchitecturePage.searchCluster("Bang");
        CommonUtils.takeScreenshot(driver,"search_results_bang");
    }

    @Test(priority = 9, description = "Verify cluster selection updates field")
    public void testClusterSelectionUpdatesField() {
        // Open dropdown and select different clusters
        String[] clusters = {"Ahmedabad", "Bengaluru", "Bangladesh", "Jaipur"};

        for (String cluster : clusters) {
            rangeArchitecturePage.clickSelectClusterDropdown();
            rangeArchitecturePage.selectClusterOption(cluster);

            String selectedValue = rangeArchitecturePage.getSelectedClusterValue();
            Assert.assertTrue(selectedValue.contains(cluster),
                    "Field should display selected cluster: " + cluster);

            CommonUtils.takeScreenshot(driver,"cluster_selected_" + cluster.toLowerCase());
        }
    }

    @Test(priority = 10, description = "Verify Upload RA mandatory label with asterisk")
    public void testUploadRAMandatoryLabel() {
        // Verify Upload RA label is displayed
        Assert.assertTrue(rangeArchitecturePage.isUploadRALabelDisplayed(), "Upload RA label should be displayed");

        // Verify asterisk is displayed indicating mandatory field
        Assert.assertTrue(rangeArchitecturePage.isUploadRAAsteriskDisplayed(), "Asterisk should be displayed for Upload RA mandatory field");

        // Verify label text contains "Upload RA"
        String labelText = rangeArchitecturePage.getUploadRALabelText();
        Assert.assertTrue(labelText.contains("Upload RA"), "Label should contain 'Upload RA' text");

        CommonUtils.takeScreenshot(driver,"upload_ra_mandatory_label_verified");
    }

    @Test(priority = 11, description = "Verify all mandatory fields have asterisk marks")
    public void testAllMandatoryFieldsHaveAsterisk() {
        // Verify Select Cluster has asterisk
        Assert.assertTrue(rangeArchitecturePage.isSelectClusterAsteriskDisplayed(),
                "Select Cluster should have asterisk mark");

        // Verify Upload RA has asterisk
        Assert.assertTrue(rangeArchitecturePage.isUploadRAAsteriskDisplayed(),
                "Upload RA should have asterisk mark");

        CommonUtils.takeScreenshot(driver,"all_mandatory_fields_asterisk_verified");
    }

    @Test(description = "Verify Upload Global RA Functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testUploadClusterRA() throws IOException {

            // Open upload modal
            rangeArchitecturePage.clickUploadClusterRA();
            Assert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(), "Upload modal should be displayed");
            // Upload file using Robot and path
            // Upload the file
            String filePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
            System.out.println(filePath);
            rangeArchitecturePage.uploadRAFile(filePath);
            System.out.println(filePath);

            Assert.assertTrue(rangeArchitecturePage.isFileUploadSheetNameDisplayed(), "Upload file sheet name should be displayed");
            // Upload RA file
            //   String raFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
            //  rangeArchitecturePage.uploadRAFile(raFilePath);
            //    Assert.assertTrue(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(),"Upload successfull should be displayed");
//        // Upload MRP file
//        String mrpFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/test-data/sample-mrp.xlsx";
//        rangeArchitecturePage.uploadMRPFile(mrpFilePath);

            // Complete upload
            rangeArchitecturePage.clickContinue();
            Assert.assertTrue(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(), "Upload successfull should be displayed");


            // Get UI table rows
            List<WebElement> uiRows = driver.findElements(By.xpath("//table//tbody/tr"));
            List<Map<String, String>> uiData = CommonUtils.extractTableData(uiRows);

            // Read Excel data
            String excelPath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
            List<Map<String, String>> excelData = ExcelUtils.readExcelData(excelPath, "RAFile");

            // Validate data
            //   boolean isValid = CommonUtils.validateExcelAndUIData(excelData, uiRows);
            //   Assert.assertTrue(isValid, "Excel data should match UI data");
            //    List<Map<String, String>> uiData = CommonUtils.extractTableData(uiRows);
            // ExtentTest test = extent.createTest("Excel vs UI Validation");
            System.out.println("Excel Data: " + excelData);
            System.out.println("UI Data: " + uiData);
            boolean isValid = CommonUtils.validateExcelAndUIData(excelData, uiData);
//        Assert.assertTrue(isValid, "Excel data should match UI data");

            // Use soft assertions to show all mismatches
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertTrue(isValid, "Excel data should match UI data");

            // If you want to see all failures before the test stops
            softAssert.assertAll();
        }

    @Test(description = "Verify timeout error 'Upload failed' is displayed when upload takes too long", priority = 3)
    public void testUploadTimeoutError() {
        try {
            // Trigger file upload with timeout expectation
            rangeArchitecturePage.triggerFileUploadDialog();

            // Use a large file or simulate slow upload
            String fileToUpload = new File(LARGE_TEST_FILE_PATH).exists() ? LARGE_TEST_FILE_PATH : RA_FILE_PATH;
            rangeArchitecturePage.uploadFileWithTimeoutHandling(fileToUpload, true);

            // Wait for upload to start
            CommonUtils.waitForSeconds(5);

            // Check if upload is in progress
            boolean uploadStarted = rangeArchitecturePage.isUploadInProgress() ||
                    rangeArchitecturePage.isSelectedFileDisplayed();

            if (uploadStarted) {
                // Wait for timeout to occur (should be around 30 seconds)
                boolean uploadCompleted = rangeArchitecturePage.waitForUploadCompletion(35);

                if (!uploadCompleted) {
                    // Verify timeout error message is displayed
                    Assert.assertTrue(rangeArchitecturePage.isUploadFailureMessageDisplayed(),
                            "Upload failure message should be displayed when upload times out");

                    String errorMessage = rangeArchitecturePage.getUploadFailureMessage();
                    Assert.assertTrue(errorMessage.toLowerCase().contains("upload failed") ||
                                    errorMessage.toLowerCase().contains("timeout") ||
                                    errorMessage.toLowerCase().contains("failed"),
                            "Error message should contain 'Upload failed' or 'timeout'. Actual: " + errorMessage);

                    CommonUtils.takeScreenshot(driver, "upload_timeout_error");
                } else {
                    // If upload completed successfully, this might indicate the timeout is too long
                    System.out.println("Upload completed successfully - timeout scenario not triggered");
                    CommonUtils.takeScreenshot(driver, "upload_completed_unexpectedly");
                }
            } else {
                Assert.fail("Upload did not start properly");
            }

        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "upload_timeout_test_error");
            Assert.fail("Upload timeout test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2, description = "Verify that multiple file selection is prevented during upload")
    public void testMultipleFileUploadPrevention() {
        SoftAssert softAssert = new SoftAssert();

        try {
            ExtentTestManager.getTest().log(Status.INFO,
                    "Testing multiple file upload prevention");

            // Step 1: Click upload button to open file dialog
            rangeArchitecturePage.clickUploadGlobalRA();
            softAssert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(),
                    "Upload modal should be displayed");

            // Step 2: Verify file input element has single file restriction
            WebElement fileInput = rangeArchitecturePage.getFileInputElement();
            String multipleAttribute = fileInput.getAttribute("multiple");

            softAssert.assertNull(multipleAttribute,
                    "File input should not have 'multiple' attribute");

            // Alternative check - if multiple attribute exists, it should be false
            if (multipleAttribute != null) {
                softAssert.assertFalse(Boolean.parseBoolean(multipleAttribute),
                        "Multiple attribute should be false if present");
            }

            // Step 3: Verify accept attribute restricts to xlsx and csv only
            String acceptAttribute = fileInput.getAttribute("accept");
            if (acceptAttribute != null) {
                softAssert.assertTrue(
                        acceptAttribute.contains(".xlsx") || acceptAttribute.contains(".csv") ||
                                acceptAttribute.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                                acceptAttribute.contains("text/csv"),
                        "Accept attribute should restrict to xlsx and csv files only. Actual: " + acceptAttribute
                );
            }

            // Step 4: Test JavaScript validation for multiple files
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Boolean supportsMultiple = (Boolean) js.executeScript(
                    "var input = arguments[0];" +
                            "return input.multiple === true;", fileInput);

            softAssert.assertFalse(supportsMultiple,
                    "File input should not support multiple file selection");

            // Step 5: Attempt to upload first file
            String firstFile = TEST_FILES_PATH + "valid_test.xlsx";
            rangeArchitecturePage.uploadRAFile(firstFile);
            CommonUtils.waitForSeconds(2);

            // Step 6: Verify only one file is selected
            softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "First file should be selected");

            String selectedFileName = rangeArchitecturePage.getSelectedFileName();
            softAssert.assertTrue(selectedFileName.contains("valid_test.xlsx"),
                    "Selected file should be the uploaded xlsx file");

            // Step 7: Verify file count is 1
            int fileCount = rangeArchitecturePage.getSelectedFileCount();
            softAssert.assertEquals(fileCount, 1,
                    "Only one file should be selectable at a time");

            // Step 8: Test browser-level multiple file prevention
            // This verifies that even if user tries to select multiple files,
            // only one will be accepted
            Boolean canSelectMultiple = (Boolean) js.executeScript(
                    "var input = arguments[0];" +
                            "var files = input.files;" +
                            "return files.length > 1;", fileInput);

            softAssert.assertFalse(canSelectMultiple,
                    "Browser should prevent multiple file selection");

            ExtentTestManager.getTest().log(Status.PASS,
                    "Multiple file upload prevention working correctly - only single file selection allowed");
            CommonUtils.takeScreenshot(driver, "single_file_upload_validation");

        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL,
                    "Failed to validate multiple file upload prevention: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "multiple_file_prevention_error");
            softAssert.fail("Test failed: " + e.getMessage());
        }

        softAssert.assertAll();
    }

    @Test(description = "Verify successful upload flow without timeout", priority = 4)
    public void testSuccessfulUpload() {
        try {
            // Upload file normally
            rangeArchitecturePage.triggerFileUploadDialog();
            rangeArchitecturePage.uploadFileWithTimeoutHandling(RA_FILE_PATH, false);

            // Verify file is selected
            Assert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "File should be selected and displayed");

            // Continue with upload process
            rangeArchitecturePage.clickContinue();

            // Wait for upload completion
            boolean uploadCompleted = rangeArchitecturePage.waitForUploadCompletion(30);
            Assert.assertTrue(uploadCompleted, "Upload should complete successfully within 30 seconds");

            // Verify success message
            Assert.assertTrue(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(),
                    "Upload success message should be displayed");

            CommonUtils.takeScreenshot(driver, "successful_upload");

        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "successful_upload_error");
            Assert.fail("Successful upload test failed: " + e.getMessage());
        }
    }

    @Test(priority = 1, description = "Verify valid file formats (.xlsx and .csv) are accepted",
            dataProvider = "validFileFormats")
    public void testValidFileFormatUpload(String filePath, String fileExtension, String fileDescription) {
        SoftAssert softAssert = new SoftAssert();

        try {
            ExtentTestManager.getTest().log(Status.INFO, "Testing upload with valid " + fileDescription + " format");

            // Navigate to upload section
            rangeArchitecturePage.clickUploadGlobalRA();
            softAssert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(),
                    "Upload modal should be displayed");

            // Get file info before upload
            File file = new File(filePath);
            String fileName = file.getName();
            long fileSize = file.length();

            ExtentTestManager.getTest().log(Status.INFO,
                    String.format("Uploading file: %s (Size: %d bytes)", fileName, fileSize));

            // Upload the file
            rangeArchitecturePage.uploadRAFile(filePath);

            // Verify loading indicator appears with filename and size
            softAssert.assertTrue(rangeArchitecturePage.isUploadInProgress(),
                    "Loading indicator should be displayed during upload");

            // Verify selected file is displayed
            CommonUtils.waitForSeconds(2);
            softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "Selected file should be displayed");

            String selectedFileName = rangeArchitecturePage.getSelectedFileName();
            softAssert.assertTrue(selectedFileName.contains(fileName) || selectedFileName.contains(fileExtension),
                    "Selected file name should contain the uploaded file name or extension");

            // Verify no error message is displayed
            softAssert.assertFalse(rangeArchitecturePage.isUploadFailureMessageDisplayed(),
                    "No error message should be displayed for valid file format");

            // Complete the upload
            rangeArchitecturePage.clickContinue();

            // Verify successful upload
            CommonUtils.waitForSeconds(3);
            softAssert.assertTrue(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(),
                    "Upload success message should be displayed");

            ExtentTestManager.getTest().log(Status.PASS,
                    fileDescription + " uploaded successfully");
            CommonUtils.takeScreenshot(driver, "valid_" + fileExtension + "_upload_success");

        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL,
                    "Failed to upload " + fileDescription + ": " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "valid_" + fileExtension + "_upload_error");
            softAssert.fail("Test failed: " + e.getMessage());
        } finally {
            // Close modal if open
            try {
                if (rangeArchitecturePage.isCancelButtonDisplayed()) {
                    rangeArchitecturePage.clickCancelButton();
                }
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }

        softAssert.assertAll();
    }

    @Test(priority = 2, description = "Verify invalid file formats show 'File Format not supported' error",
            dataProvider = "invalidFileFormats")
    public void testInvalidFileFormatUpload(String filePath, String fileExtension, String fileDescription) {
        SoftAssert softAssert = new SoftAssert();

        try {
            ExtentTestManager.getTest().log(Status.INFO,
                    "Testing upload with invalid " + fileDescription + " format");

            // Navigate to upload section
            rangeArchitecturePage.clickUploadGlobalRA();
            softAssert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(),
                    "Upload modal should be displayed");

            // Get file info
            File file = new File(filePath);
            String fileName = file.getName();
            long fileSize = file.length();

            ExtentTestManager.getTest().log(Status.INFO,
                    String.format("Attempting to upload invalid file: %s (Size: %d bytes)", fileName, fileSize));

            // Attempt to upload the invalid file
            rangeArchitecturePage.uploadRAFile(filePath);

            // Wait for error processing
            CommonUtils.waitForSeconds(3);

            // Verify error message is displayed
            softAssert.assertTrue(rangeArchitecturePage.isUploadFailureMessageDisplayed(),
                    "Error message should be displayed for invalid file format");

            // Verify the specific error message
            String errorMessage = rangeArchitecturePage.getUploadFailureMessage();
            softAssert.assertTrue(errorMessage.contains("File Format not supported") ||
                            errorMessage.contains("Invalid format") ||
                            errorMessage.contains("Unsupported format"),
                    "Error message should indicate file format is not supported. Actual message: " + errorMessage);

            // Verify error icon is displayed
            softAssert.assertTrue(rangeArchitecturePage.isErrorIconDisplayed(),
                    "Error icon should be displayed");

            // Verify upload was not successful
            softAssert.assertFalse(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(),
                    "Upload success message should not be displayed for invalid format");

            ExtentTestManager.getTest().log(Status.PASS,
                    "Invalid " + fileDescription + " correctly rejected with error: " + errorMessage);
            CommonUtils.takeScreenshot(driver, "invalid_" + fileExtension + "_upload_error");

        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL,
                    "Failed to test invalid " + fileDescription + ": " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "invalid_" + fileExtension + "_test_error");
            softAssert.fail("Test failed: " + e.getMessage());
        } finally {
            // Close modal if open
            try {
                if (rangeArchitecturePage.isCancelButtonDisplayed()) {
                    rangeArchitecturePage.clickCancelButton();
                }
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }

        softAssert.assertAll();
    }

    @Test(description = "Verify Sample File Downloads")
    public void testSampleFileDownloads() {
            //   uploadPage = new UploadGlobalRAPage(driver);

            // Open upload modal
            rangeArchitecturePage.clickUploadClusterRA();

            // Download sample files
            rangeArchitecturePage.downloadSampleRA();
            rangeArchitecturePage.downloadSampleMultiplier();

            // Verify downloads (you'll need to implement file verification)
            // Close modal
            rangeArchitecturePage.clickCancel();
        }

    @Test(description = "Verify Upload Validation")
    public void testUploadValidation() {
            // uploadPage = new UploadGlobalRAPage(driver);

            // Test with invalid file types
            rangeArchitecturePage.clickUploadClusterRA();
            String invalidFilePath = "src/test/java/com/impetus/data/invalid.txt";
            rangeArchitecturePage.uploadRAFile(invalidFilePath);

            // Add assertions for error messages

            rangeArchitecturePage.clickCancel();
        }

    @Test(priority = 1, description = "P1 - Verify Download Sample button functionality and Excel format")
    public void testDownloadSampleButtonFunctionality() throws Exception {
        ExtentTest test = ExtentTestManager.getTest();

        try {
            // Step 1: Verify Download Sample button is displayed and clickable
            test.log(Status.INFO, "Verifying Download Sample button is displayed and clickable");
            Assert.assertTrue(rangeArchitecturePage.downloadSampleRALink.isDisplayed(),
                    "Download Sample button should be displayed");
            Assert.assertTrue(rangeArchitecturePage.downloadSampleRALink.isEnabled(),
                    "Download Sample button should be clickable");
            CommonUtils.takeScreenshot(driver, "Download_Sample_Button_Displayed");

            // Step 2: Click Download Sample button and get file path
            test.log(Status.INFO, "Clicking Download Sample button and retrieving file path");
            String downloadedFile = rangeArchitecturePage.triggerDownloadSampleRAAndGetFilePath();
            Assert.assertNotNull(downloadedFile, "Downloaded file path should not be null");
            test.log(Status.PASS, "Downloaded file at: " + downloadedFile);

            // Step 3: Verify file format is Excel
            test.log(Status.INFO, "Verifying downloaded file is in Excel format");
            Assert.assertTrue(ExcelUtils.isValidExcelFile(downloadedFile),
                    "Downloaded file should be a valid Excel file");
            Assert.assertTrue(downloadedFile.endsWith(".xlsx"),
                    "Downloaded file should have .xlsx extension");
            CommonUtils.takeScreenshot(driver, "Excel_File_Format_Verified");

            // Step 4: Verify file naming convention
            test.log(Status.INFO, "Verifying file naming convention");
            String fileName = new File(downloadedFile).getName();
            Assert.assertTrue(ExcelUtils.validateDownloadSampleFileNaming(fileName),
                    "File naming should follow expected convention for sample files");

            test.log(Status.PASS, "Download Sample button functionality verified successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "Download_Sample_Test_Failed");
            throw e;
        }
    }

    @Test(description = "Verify Excel Headers Match with UI Headers",dependsOnMethods = "testUploadClusterRA")
    public void testHeadersMatch() {
            //  uploadPage = new UploadGlobalRAPage(driver);

            // Path to test data excel file
            String raFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";

            // Get headers from Excel
            //    List<String> excelHeaders = ExcelUtils.readExcelData(raFilePath,RAFile);

            // Upload the file
            rangeArchitecturePage.clickUploadClusterRA();
            rangeArchitecturePage.uploadRAFile(raFilePath);
            // rangeArchitecturePage.uploadRAFile(raFilePath);

            // Verify headers match
            //    Assert.assertTrue(rangeArchitecturePage.verifyHeadersMatch(excelHeaders),
//                "Excel headers should match with UI table headers");

            // Upload MRP file and verify its headers
            //String mrpFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/MRP.xlsx";
            //   List<String> mrpExcelHeaders = ExcelUtils.getExcelHeaders(mrpFilePath);
            //   rangeArchitecturePage.uploadMRPFile(mrpFilePath);
            //   Assert.assertTrue(rangeArchitecturePage.verifyHeadersMatch(mrpExcelHeaders),
            //    "MRP Excel headers should match with UI table headers");

            //  rangeArchitecturePage.clickContinue();
        }

    @Test(priority = 1, description = "Verify presence of cluster field next to filters")
    public void testClusterFieldPresence() {
        // Verify cluster field is present
        Assert.assertTrue(rangeArchitecturePage.isClusterFieldPresent(),
                "Cluster field should be present on the listing page");

        // Verify cluster field is positioned next to filters
        Assert.assertTrue(rangeArchitecturePage.isClusterFieldNextToFilters(),
                "Cluster field should be positioned next to filters");

        // Take screenshot
        CommonUtils.takeScreenshot(driver,"cluster_field_presence");

        System.out.println("✓ Cluster field is present and positioned correctly");
    }

    @Test(priority = 2, description = "Verify default cluster name and dropdown arrow")
    public void testDefaultClusterAndDropdownArrow() {
        // Verify default cluster text is displayed
        Assert.assertTrue(rangeArchitecturePage.isDefaultClusterDisplayed(),
                "Default cluster name or 'Select cluster' should be displayed");

        // Verify dropdown arrow is present
        Assert.assertTrue(rangeArchitecturePage.isDropdownArrowPresent(),
                "Dropdown arrow should be present");

        // Verify dropdown arrow is clickable
        Assert.assertTrue(rangeArchitecturePage.isDropdownArrowClickable(),
                "Dropdown arrow should be clickable");

        // Get and log default text
        String defaultText = rangeArchitecturePage.getClusterFieldText();
        System.out.println("Default cluster text: " + defaultText);

        // Take screenshot
        CommonUtils.takeScreenshot(driver,"default_cluster_and_arrow");

        System.out.println("✓ Default cluster and dropdown arrow verified");
    }

    @Test(priority = 3, description = "Verify cluster dropdown is clickable and opens")
    public void testClusterDropdownClickable() {
        // Click dropdown arrow
        rangeArchitecturePage.clickDropdownArrow();

        // Verify dropdown menu opens
        Assert.assertTrue(rangeArchitecturePage.isDropdownMenuOpen(),
                "Dropdown menu should open when arrow is clicked");

        // Verify cluster names are displayed
        Assert.assertTrue(rangeArchitecturePage.areClusterNamesDisplayed(),
                "Cluster names should be displayed in dropdown");

        // Get and log cluster options
        List<String> clusterNames = rangeArchitecturePage.getClusterOptionTexts();
        System.out.println("Available clusters: " + clusterNames);

        // Take screenshot
        CommonUtils.takeScreenshot(driver,"dropdown_opened_with_clusters");

        System.out.println("✓ Dropdown is clickable and displays cluster names");
    }

    @Test(priority = 4, description = "Verify cluster dropdown functionality - scroll bar and search")
    public void testClusterDropdownFunctionality() {
        // Open dropdown
        rangeArchitecturePage.clickDropdownArrow();

        // Verify scroll bar is present (if needed)
        boolean hasScrollBar = rangeArchitecturePage.isScrollBarPresent();
        System.out.println("Scroll bar present: " + hasScrollBar);

        // Verify search box is present
        Assert.assertTrue(rangeArchitecturePage.isSearchBoxPresent(),
                "Search box should be present in dropdown");

        // Test search functionality
        rangeArchitecturePage.enterSearchText("Beng");

        // Verify filtered results
        List<String> filteredClusters = rangeArchitecturePage.getClusterOptionTexts();
        boolean hasFilteredResults = filteredClusters.stream()
                .anyMatch(cluster -> cluster.toLowerCase().contains("beng"));
        Assert.assertTrue(hasFilteredResults,
                "Search should filter cluster names");

        // Take screenshot
        CommonUtils.takeScreenshot(driver,"dropdown_search_functionality");

        System.out.println("✓ Dropdown functionality verified - search and scroll");
    }

    @Test(priority = 5, description = "Verify cluster selection and field update")
    public void testClusterSelection() {
        // Open dropdown
        rangeArchitecturePage.clickDropdownArrow();

        // Get available clusters
        List<String> clusterNames = rangeArchitecturePage.getClusterOptionTexts();
        Assert.assertFalse(clusterNames.isEmpty(), "Should have cluster options available");

        // Select first cluster
        String selectedCluster = clusterNames.get(0);
        rangeArchitecturePage.selectClusterByName(selectedCluster);

        // Verify cluster appears in field
        Assert.assertTrue(rangeArchitecturePage.isClusterSelected(selectedCluster),
                "Selected cluster should appear in the field");

        // Take screenshot
        CommonUtils.takeScreenshot(driver,"cluster_selected");

        System.out.println("✓ Cluster selection verified: " + selectedCluster);
    }

    @Test(priority = 6, description = "Verify field is editable with clear functionality")
    public void testFieldEditableAndClear() {
        // Select a cluster first
        rangeArchitecturePage.clickDropdownArrow();
        List<String> clusterNames = rangeArchitecturePage.getClusterOptionTexts();
        rangeArchitecturePage.selectClusterByName(clusterNames.get(0));

        // Verify field is editable
        Assert.assertTrue(rangeArchitecturePage.isFieldEditable(),
                "Cluster field should be editable");

        // Verify clear button is present
        Assert.assertTrue(rangeArchitecturePage.isClearButtonPresent(),
                "Clear button should be present");

        // Click clear button
        rangeArchitecturePage.clickClearButton();

        // Verify field is cleared
        String fieldText = rangeArchitecturePage.getClusterFieldText();
        Assert.assertTrue(fieldText.isEmpty() || fieldText.contains("Select"),
                "Field should be cleared or show default text");

        // Take screenshot
        CommonUtils.takeScreenshot(driver,"field_cleared");

        System.out.println("✓ Field editable and clear functionality verified");
    }

    @Test(priority = 7, description = "Verify content updates based on cluster selection")
    public void testContentUpdateOnClusterSelection() {
        // Get initial content count
        int initialCount = rangeArchitecturePage.getListingItemsCount();

        // Select first cluster
        rangeArchitecturePage.clickDropdownArrow();
        List<String> clusterNames = rangeArchitecturePage.getClusterOptionTexts();
        String firstCluster = clusterNames.get(0);
        rangeArchitecturePage.selectClusterByName(firstCluster);

        // Wait for content to load
        rangeArchitecturePage.waitForContentToLoad();

        // Verify content is displayed or no data message
        boolean hasContent = rangeArchitecturePage.isContentDisplayed();
        boolean hasNoDataMessage = rangeArchitecturePage.isNoDataMessageDisplayed();

        Assert.assertTrue(hasContent || hasNoDataMessage,
                "Either content should be displayed or no data message should appear");

        // Take screenshot
        CommonUtils.takeScreenshot(driver,"content_after_first_cluster");

        // Test with different cluster if available
        if (clusterNames.size() > 1) {
            rangeArchitecturePage.clickDropdownArrow();
            String secondCluster = clusterNames.get(1);
            rangeArchitecturePage.selectClusterByName(secondCluster);

            rangeArchitecturePage.waitForContentToLoad();

            int newCount = rangeArchitecturePage.getListingItemsCount();
            boolean newHasContent = rangeArchitecturePage.isContentDisplayed();
            boolean newHasNoDataMessage = rangeArchitecturePage.isNoDataMessageDisplayed();

            Assert.assertTrue(newHasContent || newHasNoDataMessage,
                    "Content should update based on cluster selection");

            // Take screenshot
            CommonUtils.takeScreenshot(driver,"content_after_second_cluster");

            System.out.println("Content count changed from " + initialCount + " to " + newCount);
        }

        System.out.println("✓ Content updates verified based on cluster selection");
    }

    @Test(priority = 8, description = "Test complete cluster workflow")
    public void testCompleteClusterWorkflow() {
        // 1. Verify initial state
        Assert.assertTrue(rangeArchitecturePage.isClusterFieldPresent(), "Cluster field should be present");

        // 2. Open dropdown
        rangeArchitecturePage.clickDropdownArrow();
        Assert.assertTrue(rangeArchitecturePage.isDropdownMenuOpen(), "Dropdown should open");

        // 3. Search for cluster
        rangeArchitecturePage.enterSearchText("Bengaluru");

        // 4. Select cluster
        rangeArchitecturePage.selectClusterByName("Bengaluru");
        Assert.assertTrue(rangeArchitecturePage.isClusterSelected("Bengaluru"), "Bengaluru should be selected");

        // 5. Wait for content update
        rangeArchitecturePage.waitForContentToLoad();

        // 6. Verify content or no data message
        boolean hasContent = rangeArchitecturePage.isContentDisplayed();
        boolean hasNoDataMessage = rangeArchitecturePage.isNoDataMessageDisplayed();
        Assert.assertTrue(hasContent || hasNoDataMessage, "Should show content or no data message");

        // 7. Clear selection
        rangeArchitecturePage.clickClearButton();

        // Take final screenshot
        CommonUtils.takeScreenshot(driver,"complete_workflow_finished");

        System.out.println("✓ Complete cluster workflow verified successfully");
    }

}

