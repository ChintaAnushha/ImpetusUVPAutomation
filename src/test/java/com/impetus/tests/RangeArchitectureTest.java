//package com.impetus.tests;
//
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.Status;
//import com.impetus.base.BasePage;
//import com.impetus.pages.DashboardPage;
//import com.impetus.utils.*;
//import org.openqa.selenium.*;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//import com.impetus.pages.RangeArchitecturePage;
//import org.testng.annotations.BeforeClass;
//import org.testng.asserts.SoftAssert;
//
//import java.io.File;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//
//public class RangeArchitectureTest extends BasePage {
//    private WebDriver driver;
//    private DashboardPage dashboardPage;
//    private RangeArchitecturePage rangeArchitecturePage;
//    //  private final ExtentTest test;
//
//
//    //  private static final String RA_FILE_PATH = "src/test/java/com/impetus/data/RA.xlsx";
//    String RA_FILE_PATH = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
//    private String downloadPath = System.getProperty("user.home") + "/Downloads";
//
//    public RangeArchitectureTest() {
//        super(DriverManager.getDriver());
//    }
//
//    @BeforeClass
//    public void setup() {
//        driver = DriverManager.getDriver();
//
//        // Use LoginHelper to do login with config credentials
//        LoginHelper loginHelper = new LoginHelper(driver);
//        loginHelper.loginToApplication();
//
//        dashboardPage = new DashboardPage(driver);
//        rangeArchitecturePage = new RangeArchitecturePage(driver);
//
//        // ✅ Call the reusable method from DashboardTest to ensure navigation/setup
//        DashboardTest dashboardTest = new DashboardTest();
//        // dashboardTest.verifyOdmBuyerDashboard();
//        dashboardTest.uvpSubMenu();
//    }
//
//    @Test(description = "Verify Range Architecture Navigation and Sections")
//    public void testRangeArchitectureNavigation() {
//        try {
//            ExtentTest test = ExtentTestManager.getTest();
//            CommonUtils.waitForPageLoad(driver);
//            Thread.sleep(2000);
//
//            // Retry mechanism for page load verification
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//            boolean isLoaded = wait.until(driver -> {
//                try {
//                    return rangeArchitecturePage.isPageLoaded();
//                } catch (StaleElementReferenceException e) {
//                    return false;
//                }
//            });
//
//            ExtentTestManager.getTest().log(com.aventstack.extentreports.Status.INFO, "RA page should be loading");
//            Assert.assertTrue(isLoaded, "Range Architecture page should be loaded");
//        } catch (Exception e) {
//            Assert.fail("Failed to verify Range Architecture page load: " + e.getMessage());
//        }
//        Assert.assertTrue(rangeArchitecturePage.isPageLoaded(), "Range Architecture page should be loaded");
//        CommonUtils.takeScreenshot(driver, "Verify RA Page loaded");
//        ExtentTestManager.getTest().log(Status.INFO, "Current Global RA Tab id displayed");
//        Assert.assertTrue(rangeArchitecturePage.isCurrentGlobalRADisplayed(), "Current Global RA Tab id displayed");
//        CommonUtils.takeScreenshot(driver, "Verify Current Global RA tab");
//        ExtentTestManager.getTest().log(Status.INFO, "Current Cluster RA should be displayed");
//        Assert.assertTrue(rangeArchitecturePage.isCurrentClusterRADisplayed(), "Current Cluster RA should be displayed");
//        CommonUtils.takeScreenshot(driver, "Verify Current Cluster RA Tab");
//        ExtentTestManager.getTest().log(Status.INFO, "Old Global RA should be displayed");
//        Assert.assertTrue(rangeArchitecturePage.isOldGlobalRADisplayed(), "Old Global RA should be displayed");
//        CommonUtils.takeScreenshot(driver, "Verify Old Global RA Tab");
//        ExtentTestManager.getTest().log(Status.INFO, "Old Cluster RA should be displayed");
//        Assert.assertTrue(rangeArchitecturePage.isOldClusterRADisplayed(), "Old Cluster RA should be displayed");
//        CommonUtils.takeScreenshot(driver, "Verify Old Cluster RA Tab");
//
//    }
//
//    @Test(description = "Verify Range Architecture Content", dependsOnMethods = "testRangeArchitectureNavigation")
//    public void tableContentsInCurrentGlobalRATab() {
//
//        ExtentTestManager.getTest().log(Status.INFO, "Verifying the table headers and content");
//        // Verify table headers and content
//        ExtentTestManager.getTest().log(Status.INFO, "Search Input text box should be displayed");
//        Assert.assertTrue(rangeArchitecturePage.isSearchInputTextBoxDisplayed(), "Search Input text box should be displayed");
//        CommonUtils.takeScreenshot(driver, "Verify search input");
//        ExtentTestManager.getTest().log(Status.INFO, "Filter button should be displayed");
//        Assert.assertTrue(rangeArchitecturePage.isFilterButtonDisplayed(), "Filter button should be displayed");
//        CommonUtils.takeScreenshot(driver, "verifyFilter button");
//        ExtentTestManager.getTest().log(Status.INFO, "Table headers should be present");
//        Assert.assertTrue(rangeArchitecturePage.verifyTableHeaders(), "Table headers should be present");
//        CommonUtils.takeScreenshot(driver, "verifyTable headers");
//        ExtentTestManager.getTest().log(Status.INFO, "All headers and data should be present");
//        Assert.assertTrue(rangeArchitecturePage.validateAllHeaders(), "All headers and data should be present");
//        CommonUtils.takeScreenshot(driver, "verifyAll data headers");
//
//
////        // Verify specific categories
////        Assert.assertTrue(rangeArchitecturePage.verifyCategories("Women"), "Women category should be present");
////        Assert.assertTrue(rangeArchitecturePage.verifyCategories("Western Wear"), "Western Wear should be present");
////        Assert.assertTrue(rangeArchitecturePage.verifyCategories("Leggings"), "Leggings should be present");
//
//        // Verify RA tabs functionality
//        //   rangeArchitecturePage.switchToCurrentGlobalRA();
////        Assert.assertTrue(rangeArchitecturePage.isCurrentGlobalRADisplayed(), "Current Global RA should be displayed");
////
////        //   rangeArchitecturePage.switchToCurrentClusterRA();
////        Assert.assertTrue(rangeArchitecturePage.isCurrentClusterRADisplayed(), "Current Cluster RA should be displayed");
//    }
//
//    @Test(description = "Verify Search Bar Functionality", dependsOnMethods = "testRangeArchitectureNavigation")
//    public void verifySearchBar() {
//
//        ExtentTestManager.getTest().log(Status.INFO, "Verifying search bar functionality in Range Architecture page");
//        CommonUtils.takeScreenshot(driver, "verifySearchBar");
//        Assert.assertTrue(rangeArchitecturePage.verifySearchBarFunctionality(),
//                "Search bar should be clickable and functional");
//    }
//
//    @Test(description = "Verify search bar presence and functionality", dependsOnMethods = "testRangeArchitectureNavigation")
//    public void verifySearchBarBasics() {
//        Assert.assertTrue(rangeArchitecturePage.verifySearchBarPresence(),
//                "Search bar should be present and enabled");
//    }
//
//    @Test(description = "Verify exact search functionality", dependsOnMethods = "testRangeArchitectureNavigation")
//    public void verifyExactSearch() {
//        Assert.assertTrue(rangeArchitecturePage.enterSearchText("Western Wear"),
//                "Should be able to enter western wear in search bar");
//        Assert.assertTrue(rangeArchitecturePage.verifySearchResults("Western Wear"),
//                "Should find exact matches");
//    }
//
//    @Test(description = "Verify partial search functionality", dependsOnMethods = "testRangeArchitectureNavigation")
//    public void verifyPartialSearch() {
//        Assert.assertTrue(rangeArchitecturePage.verifyPartialSearch("West"),
//                "Should find partial matches");
//    }
//
//    @Test(description = "Verify pagination functionality", dependsOnMethods = "testRangeArchitectureNavigation")
//    public void verifyPagination() {
//        try {
//            // First verify the table is loaded
//            CommonUtils.waitForVisibility(driver, rangeArchitecturePage.getFirstTableRow(), 20);
//
//            // Step 1: Verify pagination range display
//            Assert.assertTrue(rangeArchitecturePage.verifyPaginationRange(),
//                    "Pagination range should be correctly displayed");
//
//            // Step 2: Verify first page data
//            List<Map<String, String>> firstPageData = rangeArchitecturePage.getTableDataForPage();
//            Assert.assertFalse(firstPageData.isEmpty(), "First page should contain data");
//            Assert.assertTrue(firstPageData.size() <= 25, "First page should have maximum 25 rows");
//
//            // Step 3: Verify next page navigation
//            if (rangeArchitecturePage.isNextButtonEnabled()) {
//                Assert.assertTrue(rangeArchitecturePage.verifyNextPageNavigation(),
//                        "Next page navigation should work correctly");
//
//                // Step 4: Verify page number updates after navigation
//                Assert.assertTrue(rangeArchitecturePage.verifyPageNumberUpdate(),
//                        "Page number should update correctly");
//            }
//
//            // Step 5: Verify previous button on first page
//            rangeArchitecturePage.navigateToFirstPage();
//            Assert.assertTrue(rangeArchitecturePage.verifyFirstPagePreviousButton(),
//                    "Previous button should be disabled on first page");
//
//            // Step 6: Verify data consistency across pages
//            List<Map<String, String>> allPagesData = rangeArchitecturePage.getAllPagesData();
//            Assert.assertFalse(allPagesData.isEmpty(),
//                    "Should have data across all pages");
//
//            // Step 7: Verify next button on last page
//            Assert.assertTrue(rangeArchitecturePage.verifyLastPageNextButton(),
//                    "Next button should be disabled on last page");
//
//
//        } catch (Exception e) {
//            Assert.fail("Pagination test failed: " + e.getMessage());
//        }
//    }
//
//    @Test(description = "Verify sort functionality of the headers")//,dependsOnMethods = "verifyPagination")
//    public void verifySortingFunctionality() {
//
//        CommonUtils.waitForVisibility(driver, rangeArchitecturePage.getFirstTableRow(), 20);
//        // Verify column order
//        List<String> expectedColumns = Arrays.asList(
//                "Family", "Class Name", "Brick Name", "Top Brick",
//                "Brick", "Enrichment", "MRP Range", "Options",
//                "ODM", "OEM", "Total", "Fill Rate", "Action"
//        );
//        Assert.assertTrue(CommonUtils.verifyColumnOrder(driver, expectedColumns));
//
//        // Verify sort symbols present
//        List<String> sortableColumns = Arrays.asList(
//                "Family", "Class Name", "Brick Name", "Top Brick",
//                "Brick", "Enrichment", "Fill Rate"
//        );
//        Assert.assertTrue(CommonUtils.verifySortingSymbolsPresent(driver, sortableColumns));
//
//        // Get table rows
//        List<WebElement> rows = driver.findElements(By.xpath("//table//tbody/tr"));
//
//        // Verify sorting for each column
//        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Family", rows, 1));
//        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Class Name", rows, 2));
//        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Brick Name", rows, 3));
//        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Top Brick", rows, 4));
//        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Brick", rows, 5));
//        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Enrichment", rows, 6));
//        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Fill Rate", rows, 12));
//    }
//
//    @Test(priority = 1, description = "Verify View button redirects to detailed page")
//    public void testViewButtonRedirectsToDetailedPage() {
//        // Verify listing page is displayed
//        Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
//                "RAID listing page should be displayed");
//
//        // Take screenshot of listing page
//        CommonUtils.takeScreenshot(driver,"listing_page_before_click");
//
//        // Get current URL and RAID ID
//        String listingPageUrl = rangeArchitecturePage.getCurrentUrl();
//        String raidId = rangeArchitecturePage.getRaidIdFromRow(0);
//
//        // Click on first View button
//        rangeArchitecturePage.clickFirstViewButton();
//
//        // Verify navigation to details page
//        Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
//                "Should navigate to RAID details page");
//
//        // Verify URL has changed
//        String detailsPageUrl = rangeArchitecturePage.getCurrentUrl();
//        Assert.assertNotEquals(listingPageUrl, detailsPageUrl,
//                "URL should change after clicking View button");
//
//        // Take screenshot of details page
//        CommonUtils.takeScreenshot(driver,"details_page_after_click");
//
//        System.out.println("✓ View button successfully redirects to detailed page");
//    }
//
//    @Test(priority = 2, description = "Verify back button is present on redirected page")
//    public void testBackButtonPresenceOnDetailedPage() {
//        // Navigate to details page
//        rangeArchitecturePage.clickFirstViewButton();
//
//        // Verify back button is displayed
//        Assert.assertTrue(rangeArchitecturePage.isBackButtonDisplayed(),
//                "Back button should be displayed on details page");
//
//        // Verify back button is clickable
//        Assert.assertTrue(rangeArchitecturePage.isBackButtonClickable(),
//                "Back button should be clickable");
//
//        // Take screenshot
//        CommonUtils.takeScreenshot(driver,"back_button_verification");
//
//        System.out.println("✓ Back button is present and clickable on details page");
//    }
//
//    @Test(priority = 3, description = "Verify RAID is displayed on top left next to back button")
//    public void testRaidDisplayedTopLeft() {
//        // Navigate to details page
//        String expectedRaidId = rangeArchitecturePage.getRaidIdFromRow(0);
//        rangeArchitecturePage.clickFirstViewButton();
//
//        // Verify RAID is displayed on top left
//        Assert.assertTrue(rangeArchitecturePage.isRaidDisplayedTopLeft(),
//                "RAID should be displayed on top left next to back button");
//
//        // Verify RAID ID matches
//        String displayedRaidId = rangeArchitecturePage.getRaidDisplayText();
//        Assert.assertTrue(displayedRaidId.contains(expectedRaidId),
//                "Displayed RAID ID should match the selected RAID");
//
//        // Take screenshot
//        CommonUtils.takeScreenshot(driver,"raid_display_top_left");
//
//        System.out.println("✓ RAID is correctly displayed on top left next to back button");
//    }
//
//    @Test(priority = 4, description = "Verify page lands with two tabs, default ODM tab with table, search, filter")
//    public void testDefaultOdmTabWithFeatures() {
//        // Navigate to details page
//        rangeArchitecturePage.clickFirstViewButton();
//
//        // Verify tabs are displayed
//        Assert.assertTrue(rangeArchitecturePage.areTabsDisplayed(),
//                "Tabs should be displayed on details page");
//
//        // Verify ODM tab is active by default
//        Assert.assertTrue(rangeArchitecturePage.isOdmTabActive(),
//                "ODM tab should be active by default");
//
//        // Verify table is displayed in ODM tab
//        Assert.assertTrue(rangeArchitecturePage.isTableDisplayedInOdmTab(),
//                "Table should be displayed in ODM tab");
//
//        // Verify search box is displayed
//        Assert.assertTrue(rangeArchitecturePage.isSearchBoxDisplayed(),
//                "Search box should be displayed");
//
//        // Verify filter button is displayed
//        Assert.assertTrue(rangeArchitecturePage.isFilterButtonDisplayed(),
//                "Filter button should be displayed");
//
//        // Take screenshot
//        CommonUtils.takeScreenshot(driver,"odm_tab_with_features");
//
//        System.out.println("✓ Page correctly lands with ODM tab active and all features present");
//    }
//
//    @Test(priority = 5, description = "Verify back button navigates to listing page")
//    public void testBackButtonNavigatesToListingPage() {
//        // Get listing page URL
//        String originalListingUrl = rangeArchitecturePage.getCurrentUrl();
//
//        // Navigate to details page
//        rangeArchitecturePage.clickFirstViewButton();
//
//        // Verify we're on details page
//        Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
//                "Should be on details page");
//
//        // Take screenshot before clicking back
//        CommonUtils.takeScreenshot(driver,"before_clicking_back_button");
//
//        // Click back button
//        rangeArchitecturePage.clickBackButton();
//
//        // Verify we're back on listing page
//        Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
//                "Should navigate back to listing page");
//
//        // Verify URL is back to listing page
//        String currentUrl = rangeArchitecturePage.getCurrentUrl();
//        Assert.assertTrue(currentUrl.contains("raids") || currentUrl.equals(originalListingUrl),
//                "Should be back on the main listing page");
//
//        // Take screenshot after navigation
//        CommonUtils.takeScreenshot(driver,"after_clicking_back_button");
//
//        System.out.println("✓ Back button successfully navigates to listing page");
//    }
//
//    @Test(priority = 6, description = "Verify View buttons are clickable for all RAIDs")
//    public void testAllViewButtonsClickable() {
//        // Verify all view buttons are clickable
//        Assert.assertTrue(rangeArchitecturePage.areViewButtonsClickable(),
//                "All View buttons should be clickable");
//
//        // Get count of view buttons
//        int viewButtonCount = rangeArchitecturePage.getViewButtonsCount();
//        Assert.assertTrue(viewButtonCount > 0,
//                "There should be at least one View button");
//
//        // Take screenshot
//        CommonUtils.takeScreenshot(driver,"all_view_buttons_verification");
//
//        System.out.println("✓ All " + viewButtonCount + " View buttons are clickable");
//    }
//
//    @Test(priority = 7, description = "Test complete navigation flow for multiple RAIDs")
//    public void testCompleteNavigationFlowMultipleRaids() {
//        int viewButtonCount = Math.min(rangeArchitecturePage.getViewButtonsCount(), 3); // Test first 3 RAIDs
//
//        for (int i = 0; i < viewButtonCount; i++) {
//            // Get RAID ID
//            String raidId = rangeArchitecturePage.getRaidIdFromRow(i);
//
//            // Click View button for current row
//            rangeArchitecturePage.clickViewButtonForRow(i);
//
//            // Verify details page
//            Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
//                    "Details page should be displayed for RAID: " + raidId);
//
//            // Take screenshot
//            CommonUtils.takeScreenshot(driver,"raid_" + raidId + "_details_page");
//
//            // Click back button
//            rangeArchitecturePage.clickBackButton();
//
//            // Verify back on listing page
//            Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
//                    "Should be back on listing page after viewing RAID: " + raidId);
//
//            System.out.println("✓ Navigation flow verified for RAID: " + raidId);
//        }
//
////    @Test(description = "Verify table data across pages",dependsOnMethods = "verifyPagination")
////    public void verifyTableDataAcrossPages() {
////        // Get data from first page
////        List<Map<String, String>> firstPageData = rangeArchitecturePage.getTableDataForPage();
////        Assert.assertFalse(firstPageData.isEmpty(), "First page should contain data");
////        Assert.assertTrue(firstPageData.size() <= 25, "First page should have maximum 25 rows");
////
////        // Get data from all pages
////        List<Map<String, String>> allPagesData = rangeArchitecturePage.getAllPagesData();
////        Assert.assertFalse(allPagesData.isEmpty(), "Should have data across all pages");
////    }
//
//    }
//
//        @Test(description = "Verify Download button functionality")
//        public void verifyDownloadFunctionality () {
//            // Click the Download button
//            Assert.assertTrue(rangeArchitecturePage.isDownloadButtonDisplayed(), "Download button should be displayed");
//            //Assert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(), "Upload modal should be displayed")
//            // isDisplayed() && downloadBtn.isEnabled(), "Download button not clickable");
//            //  downloadBtn.click();
//        }
//
//        @Test(description = "Verify Download button functionality")
//        public void testCompleteDownloadFunctionality () throws Exception {
//            // 1. Trigger download
//            String downloadedFile = rangeArchitecturePage.triggerDownloadAndGetFilePath();
//            System.out.println("Downloaded file at: " + downloadedFile);
//
//            // ✅ Validate success message
//            Assert.assertTrue(rangeArchitecturePage.isDownloadSuccessMessageDisplayed(), "Download Successful message should be displayed");
//
//            // 2. Validate file exists and naming convention
//            Assert.assertTrue(ExcelUtils.isValidExcelFile(downloadedFile));
//            Assert.assertTrue(ExcelUtils.validateFileNamingConvention(
//                    new File(downloadedFile).getName()));
//
//            // 3. Validate headers
//            Assert.assertTrue(ExcelUtils.validateExcelHeaders(downloadedFile, "RA-250605-0001"));
//
//            // 4. Read and validate data
//            List<Map<String, String>> data = ExcelUtils.readExcelData(downloadedFile, "RA-250605-0001");
//            Assert.assertTrue(ExcelUtils.validateDataTypes(data));
//            Assert.assertTrue(ExcelUtils.validateTotalQtyCalculation(data));
//            Assert.assertTrue(ExcelUtils.validateFillPercentageCalculation(data));
//        }
//
//        @Test(priority = 1, description = "Verify Filter button visibility")
//        public void testFilterButtonVisibility () {
//            // Verify if the Filter button is present right next to the Search bar in blue color
//            Assert.assertTrue(rangeArchitecturePage.isFilterButtonDisplayed(),
//                    "Filter button should be visible");
//
//            Assert.assertTrue(rangeArchitecturePage.isFilterButtonNextToSearchBar(),
//                    "Filter button should be next to search bar");
//
//            Assert.assertTrue(rangeArchitecturePage.isFilterButtonBlue(),
//                    "Filter button should be blue in color");
//
//            CommonUtils.takeScreenshot(driver, "filter_button_visibility");
//        }
//
//        @Test(priority = 2, description = "Verify Filter button functionality")
//        public void testFilterButtonFunctionality () {
//            // Verify that when the Filter button is clicked, all table headers appear as dropdown fields
//            rangeArchitecturePage.clickFilterButton();
//
//            Assert.assertTrue(rangeArchitecturePage.areFilterHeadersDisplayed(),
//                    "All filter headers should be displayed after clicking filter button");
//
//            Assert.assertTrue(rangeArchitecturePage.isClearAllButtonDisplayed(),
//                    "Clear All button should be displayed");
//
//            Assert.assertTrue(rangeArchitecturePage.isClearAllButtonBlue(),
//                    "Clear All button should be blue in color");
//
//            CommonUtils.takeScreenshot(driver, "filter_button_functionality");
//        }
//
//        @Test(priority = 3, description = "Verify headers on clicking Filter button")
//        public void testFilterHeaders () {
//            rangeArchitecturePage.clickFilterButton();
//
//            // Verify all table headers are displayed in correct order
//            List<String> expectedHeaders = Arrays.asList(
//                    "Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment"
//            );
//
//            List<String> actualHeaders = rangeArchitecturePage.getFilterHeadersOrder();
//
//            Assert.assertEquals(actualHeaders, expectedHeaders,
//                    "Filter headers should appear in the correct order");
//
//            Assert.assertTrue(rangeArchitecturePage.isClearAllButtonDisplayed(),
//                    "Clear All button should be displayed next to headers");
//
//            CommonUtils.takeScreenshot(driver, "filter_headers_display");
//        }
//
//        @Test(priority = 4, description = "Verify functionality of each filter header")
//        public void testFilterHeaderFunctionality () {
//            rangeArchitecturePage.clickFilterButton();
//
//            String[] headers = {"Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment"};
//
//            for (String header : headers) {
//                // Click on filter header arrow
//                rangeArchitecturePage.clickFilterHeader(header);
//
//                // Verify dropdown content is displayed
//                Assert.assertTrue(rangeArchitecturePage.isFilterDropdownDisplayed(),
//                        header + " filter dropdown should be displayed");
//
//                // Verify search bar is present
//                Assert.assertTrue(rangeArchitecturePage.isFilterSearchBoxDisplayed(),
//                        header + " filter should have search bar");
//
//                // Verify scrollable list is present
//                Assert.assertTrue(rangeArchitecturePage.isScrollableListDisplayed(),
//                        header + " filter should have scrollable list");
//
//                // Verify clear button is present
//                Assert.assertTrue(rangeArchitecturePage.isFilterClearButtonDisplayed(),
//                        header + " filter should have clear button");
//
//                CommonUtils.takeScreenshot(driver, "filter_" + header.toLowerCase().replace(" ", "_") + "_dropdown");
//
//                // Close dropdown by clicking elsewhere or pressing escape
//                rangeArchitecturePage.clickFilterButton(); // Close current dropdown
//            }
//        }
//
//        @Test(priority = 5, description = "Verify select/unselect dropdown values & clear")
//        public void testFilterSelectionAndClear () {
//            rangeArchitecturePage.clickFilterButton();
//            rangeArchitecturePage.clickFilterHeader("Family");
//
//            // Test single selection
//            rangeArchitecturePage.selectFilterOption("Women");
//            CommonUtils.takeScreenshot(driver, "single_option_selected");
//
//            // Test multiple selection
//            List<String> multipleOptions = Arrays.asList("Men", "Kids");
//            rangeArchitecturePage.selectMultipleFilterOptions(multipleOptions);
//            CommonUtils.takeScreenshot(driver, "multiple_options_selected");
//
//            // Test unselect
//            rangeArchitecturePage.unselectFilterOption("Women");
//            CommonUtils.takeScreenshot(driver, "option_unselected");
//
//            // Test clear button functionality
//            rangeArchitecturePage.clickFilterClearButton();
//            Assert.assertTrue(rangeArchitecturePage.areAllOptionsUnselected(),
//                    "All options should be unselected after clicking clear button");
//
//            CommonUtils.takeScreenshot(driver, "all_options_cleared");
//        }
//
//
//        @Test(priority = 6, description = "Verify search bar under filter dropdown")
//        public void testFilterSearchBar () {
//            rangeArchitecturePage.clickFilterButton();
//            rangeArchitecturePage.clickFilterHeader("Family");
//
//            // Verify search bar is editable
//            Assert.assertTrue(rangeArchitecturePage.isFilterSearchBoxEditable(),
//                    "Filter search bar should be editable");
//
//            // Test complete value search
//            rangeArchitecturePage.searchInFilter("Women");
//            List<String> completeSearchResults = rangeArchitecturePage.getFilteredOptions();
//            Assert.assertTrue(completeSearchResults.contains("Women"),
//                    "Complete search should return exact match");
//            CommonUtils.takeScreenshot(driver, "complete_search_results");
//
//            // Test partial value search
//            rangeArchitecturePage.searchInFilter("Wo");
//            Assert.assertTrue(rangeArchitecturePage.verifyPartialSearch("Wo"),
//                    "Partial search should return matching results");
//            CommonUtils.takeScreenshot(driver, "partial_search_results");
//
//            // Clear search
//            rangeArchitecturePage.searchInFilter("");
//            CommonUtils.takeScreenshot(driver, "search_cleared");
//        }
//
////    @Test(priority = 7, description = "Verify Clear All functionality")
////    public void testClearAllFunctionality() {
////        rangeArchitecturePage.clickFilterButton();
////
////        // Select some options from different filters
////        rangeArchitecturePage.clickFilterHeader("Family");
////        rangeArchitecturePage.selectFilterOption("Women");
////
////        rangeArchitecturePage.clickFilterHeader("Class Name");
////        rangeArchitecturePage.selectFilterOption("Western Wear");
////
////        CommonUtils.takeScreenshot(driver,"before_clear_all");
////
////        // Click Clear All
////        rangeArchitecturePage.clickClearAllButton();
////
////        CommonUtils.takeScreenshot(driver,"after_clear_all");
////
////        // Verify all filters are cleared (this would need additional verification logic)
////        // You might need to add methods to check if filters are applied
////    }
//
//    @Test(priority = 1, description = "Verify that each filter has all list items with checkboxes")
//    public void testFilterListItemsWithCheckboxes() {
//        try {
//            List<String> filterNames = Arrays.asList("Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment");
//
//            for (String filterName : filterNames) {
//                boolean hasCheckboxes = rangeArchitecturePage.verifyFilterListItemsWithCheckboxes(filterName);
//                Assert.assertTrue(hasCheckboxes, "Filter '" + filterName + "' should have checkboxes for all list items");
//            }
//
//            CommonUtils.takeScreenshot(driver,"filter_list_items_with_checkboxes");
//        } catch (Exception e) {
//            CommonUtils.takeScreenshot(driver,"filter_list_items_error");
//            Assert.fail("Test failed: " + e.getMessage());
//        }
//    }
//
//    @Test(priority = 2, description = "Verify search bar presence in each table filter")
//    public void testSearchBarPresenceInFilters() {
//        try {
//            List<String> filterNames = Arrays.asList("Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment");
//
//            for (String filterName : filterNames) {
//                boolean hasSearchBar = rangeArchitecturePage.verifySearchBarInFilter(filterName);
//                Assert.assertTrue(hasSearchBar, "Filter '" + filterName + "' should have a search bar");
//            }
//
//            CommonUtils.takeScreenshot(driver,"search_bar_presence_verification");
//        } catch (Exception e) {
//            CommonUtils.takeScreenshot(driver,"search_bar_presence_error");
//            Assert.fail("Test failed: " + e.getMessage());
//        }
//    }
//
//    @Test(priority = 3, description = "Verify search functionality in filter dropdowns")
//    public void testSearchFunctionalityInFilters() {
//        try {
//
//            // Test partial search in different filters
//            boolean attributeSearch = rangeArchitecturePage.testSearchFunctionality("Brick Name", "Short");
//            Assert.assertTrue(attributeSearch, "Search for 'Short' should filter relevant items");
//
//            boolean partialSearch = rangeArchitecturePage.testSearchFunctionality("Family", "Shirt");
//            Assert.assertTrue(partialSearch, "Partial search should work correctly");
//
//            CommonUtils.takeScreenshot(driver, "search_functionality_verification");
//        } catch (Exception e) {
//            CommonUtils.takeScreenshot(driver, "search_functionality_error");
//            Assert.fail("Test failed: " + e.getMessage());
//        }
//    }
//
//
//    @Test(priority = 4, description = "Verify multi-select functionality in filters")
//    public void testMultiSelectFunctionality() {
//        try {
//            List<String> itemsToSelect = Arrays.asList("Item1", "Item2", "Item3");
//            boolean multiSelect = rangeArchitecturePage.verifyMultiSelectFunctionality("Family", itemsToSelect);
//            Assert.assertTrue(multiSelect, "Filter should support multi-select functionality");
//
//            CommonUtils.takeScreenshot(driver, "multi_select_functionality");
//        } catch (Exception e) {
//            CommonUtils.takeScreenshot(driver, "multi_select_error");
//            Assert.fail("Test failed: " + e.getMessage());
//        }
//    }
//
//    @Test(description = "Verify that all table header filters support multi-select")
//    public void verifyMultiSelectOnAllTableHeaderFilters() {
//        ExtentTestManager.getTest().log(Status.INFO, "Validating multi-select capability of table header filters");
//
//        boolean result = rangeArchitecturePage.verifyAllFiltersAreMultiSelectable();
//
//        Assert.assertTrue(result, "Each filter dropdown should support multi-select functionality");
//    }
//
//    @Test(priority = 5, description = "Verify sort functionality after filtering")
//    public void testSortAfterFiltering() {
//        try {
//            // Test sorting on different columns after applying filter
//            boolean brickSort = rangeArchitecturePage.testSortAfterFiltering("Family", "TestFamily", "Brick Name");
//            Assert.assertTrue(brickSort, "Sort should work on Brick Name column after filtering");
//
//            boolean attributeSort = rangeArchitecturePage.testSortAfterFiltering("Class Name", "TestClass", "Attributes");
//            Assert.assertTrue(attributeSort, "Sort should work on Attributes column after filtering");
//
//            boolean costSort = rangeArchitecturePage.testSortAfterFiltering("Top Brick", "TestBrick", "Cost Range");
//            Assert.assertTrue(costSort, "Sort should work on Cost Range column after filtering");
//
//            CommonUtils.takeScreenshot(driver, "sort_after_filtering");
//        } catch (Exception e) {
//            CommonUtils.takeScreenshot(driver, "sort_after_filtering_error");
//            Assert.fail("Test failed: " + e.getMessage());
//        }
//    }
//
//    @Test(priority = 6, description = "Verify Clear All functionality")
//    public void testClearAllFunctionality() {
//        try {
//            // First select some filters
//            rangeArchitecturePage.verifyMultiSelectFunctionality("Family", Arrays.asList("Item1", "Item2"));
//
//            // Then clear all
//            boolean clearAll = rangeArchitecturePage.verifyClearAllFunctionality();
//            Assert.assertTrue(clearAll, "Clear All should unselect all filter items");
//
//            CommonUtils.takeScreenshot(driver,"clear_all_functionality");
//        } catch (Exception e) {
//            CommonUtils.takeScreenshot(driver,"clear_all_error");
//            Assert.fail("Test failed: " + e.getMessage());
//        }
//    }
//
//    @Test(priority = 7, description = "Verify filter header order and clickability")
//    public void testFilterHeaderOrderAndClickability() {
//        try {
//            List<String> expectedOrder = Arrays.asList("Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment");
//            boolean correctOrder = rangeArchitecturePage.verifyFilterHeaderOrder(expectedOrder);
//            Assert.assertTrue(correctOrder, "Filter headers should be in correct order");
//
//            CommonUtils.takeScreenshot(driver, "filter_header_order");
//        } catch (Exception e) {
//            CommonUtils.takeScreenshot(driver, "filter_header_order_error");
//            Assert.fail("Test failed: " + e.getMessage());
//        }
//    }
//
//    @Test(description = "Verify Upload Global RA Functionality", dependsOnMethods = "testRangeArchitectureNavigation")
//    public void testUploadGlobalRA() throws IOException {
//
//        // Open upload modal
//        rangeArchitecturePage.clickUploadGlobalRA();
//        Assert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(), "Upload modal should be displayed");
//        // Upload file using Robot and path
//        // Upload the file
//        String filePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
//        System.out.println(filePath);
//        rangeArchitecturePage.uploadRAFile(filePath);
//        System.out.println(filePath);
//
//        Assert.assertTrue(rangeArchitecturePage.isFileUploadSheetNameDisplayed(), "Upload file sheet name should be displayed");
//        // Upload RA file
//        //   String raFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
//        //  rangeArchitecturePage.uploadRAFile(raFilePath);
//        //    Assert.assertTrue(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(),"Upload successfull should be displayed");
////        // Upload MRP file
////        String mrpFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/test-data/sample-mrp.xlsx";
////        rangeArchitecturePage.uploadMRPFile(mrpFilePath);
//
//        // Complete upload
//        rangeArchitecturePage.clickContinue();
//        Assert.assertTrue(rangeArchitecturePage.isUploadSuccessfullMessageDisplayed(), "Upload successfull should be displayed");
//
//
//        // Get UI table rows
//        List<WebElement> uiRows = driver.findElements(By.xpath("//table//tbody/tr"));
//        List<Map<String, String>> uiData = CommonUtils.extractTableData(uiRows);
//
//        // Read Excel data
//        String excelPath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
//        List<Map<String, String>> excelData = ExcelUtils.readExcelData(excelPath, "RAFile");
//
//        // Validate data
//     //   boolean isValid = CommonUtils.validateExcelAndUIData(excelData, uiRows);
//     //   Assert.assertTrue(isValid, "Excel data should match UI data");
//    //    List<Map<String, String>> uiData = CommonUtils.extractTableData(uiRows);
//       // ExtentTest test = extent.createTest("Excel vs UI Validation");
//        System.out.println("Excel Data: " + excelData);
//        System.out.println("UI Data: " + uiData);
//        boolean isValid = CommonUtils.validateExcelAndUIData(excelData, uiData);
////        Assert.assertTrue(isValid, "Excel data should match UI data");
//
//       // Use soft assertions to show all mismatches
//        SoftAssert softAssert = new SoftAssert();
//        softAssert.assertTrue(isValid, "Excel data should match UI data");
//
//        // If you want to see all failures before the test stops
//        softAssert.assertAll();
//    }
//
//
//
//
//
//
//    @Test(description = "Verify Sample File Downloads")
//    public void testSampleFileDownloads() {
//     //   uploadPage = new UploadGlobalRAPage(driver);
//
//        // Open upload modal
//        rangeArchitecturePage.clickUploadGlobalRA();
//
//        // Download sample files
//        rangeArchitecturePage.downloadSampleRA();
//        rangeArchitecturePage.downloadSampleMultiplier();
//
//        // Verify downloads (you'll need to implement file verification)
//        // Close modal
//        rangeArchitecturePage.clickCancel();
//    }
//
//    @Test(description = "Verify Upload Validation")
//    public void testUploadValidation() {
//       // uploadPage = new UploadGlobalRAPage(driver);
//
//        // Test with invalid file types
//        rangeArchitecturePage.clickUploadGlobalRA();
//        String invalidFilePath = "src/test/java/com/impetus/data/invalid.txt";
//        rangeArchitecturePage.uploadRAFile(invalidFilePath);
//
//        // Add assertions for error messages
//
//        rangeArchitecturePage.clickCancel();
//    }
//
//    @Test(description = "Verify Excel Headers Match with UI Headers",dependsOnMethods = "testUploadGlobalRA")
//    public void testHeadersMatch() {
//      //  uploadPage = new UploadGlobalRAPage(driver);
//
//        // Path to test data excel file
//        String raFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
//
//        // Get headers from Excel
//    //    List<String> excelHeaders = ExcelUtils.readExcelData(raFilePath,RAFile);
//
//        // Upload the file
//        rangeArchitecturePage.clickUploadGlobalRA();
//        rangeArchitecturePage.uploadRAFile(raFilePath);
//       // rangeArchitecturePage.uploadRAFile(raFilePath);
//
//        // Verify headers match
//    //    Assert.assertTrue(rangeArchitecturePage.verifyHeadersMatch(excelHeaders),
////                "Excel headers should match with UI table headers");
//
//        // Upload MRP file and verify its headers
//        //String mrpFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/MRP.xlsx";
//     //   List<String> mrpExcelHeaders = ExcelUtils.getExcelHeaders(mrpFilePath);
//     //   rangeArchitecturePage.uploadMRPFile(mrpFilePath);
//     //   Assert.assertTrue(rangeArchitecturePage.verifyHeadersMatch(mrpExcelHeaders),
//            //    "MRP Excel headers should match with UI table headers");
//
//      //  rangeArchitecturePage.clickContinue();
//    }
//}
//
