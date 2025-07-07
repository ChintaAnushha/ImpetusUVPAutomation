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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CurrentGlobalRATest extends BasePage {

    private WebDriver driver;
    private DashboardPage dashboardPage;
    private RangeArchitecturePage rangeArchitecturePage;
    //  private final ExtentTest test;


    private LoginHelper loginHelper;
    private NavigationHelper navHelper;

    //  private static final String RA_FILE_PATH = "src/test/java/com/impetus/data/RA.xlsx";
    String RA_FILE_PATH = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
    private static final String LARGE_TEST_FILE_PATH = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/LargeRA.xlsx";
    private String downloadPath = System.getProperty("user.home") + "/Downloads";
    private static final String TEST_FILES_PATH = "src/test/resources/testfiles/";
    private static final String[] EXPECTED_MULTIPLIER_HEADERS = {
            "MRP Final", "RA Min MRP", "RA Max MRP", "Minimum Cost",
            "Maximum Cost", "Brick Code", "Enrichment"
    };


    // Test data file paths
    private static final String VALID_XLSX_FILE = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";
    private static final String VALID_CSV_FILE = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/test_data.csv";
    private static final String INVALID_TXT_FILE = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/invalid_file.txt";
    private static final String INVALID_PDF_FILE = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/invalid_file.pdf";
    private static final String INVALID_DOC_FILE = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/invalid_file.doc";

    public CurrentGlobalRATest() {
        super(DriverManager.getDriver());
    }

//    @BeforeMethod
//    public void setup(Method method) {
//        ExtentTestManager.startTest(method.getName());
//    }

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
//
//        // Create test data files if they don't exist
//        createTestDataFiles();
//    }


    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        driver = DriverManager.getDriver();
        loginHelper = new LoginHelper(driver);
        navHelper = new NavigationHelper(driver);
        rangeArchitecturePage = new RangeArchitecturePage(driver); // Safe

    }

    /**
     * Create test data files for different scenarios
     */
    private void createTestDataFiles() {
        try {
            // Create CSV test file
            createCSVTestFile();

            // Create invalid format files
            createInvalidFormatFiles();

        } catch (IOException e) {
            System.err.println("Failed to create test data files: " + e.getMessage());
        }
    }

    private void createCSVTestFile() throws IOException {
        String csvContent = "Family,Class Name,Brick Name,Top Brick,Brick,Enrichment,MRP Range,Options,ODM,OEM,Total,Fill Rate\n" +
                "Test Family,Test Class,Test Brick,Test Top Brick,Test Brick Value,Test Enrichment,100-200,Test Options,10,20,30,85.5\n" +
                "Sample Family,Sample Class,Sample Brick,Sample Top Brick,Sample Brick Value,Sample Enrichment,200-300,Sample Options,15,25,40,90.2";

        java.nio.file.Path csvPath = Paths.get(VALID_CSV_FILE);
        Files.createDirectories(csvPath.getParent());
        Files.write(csvPath, csvContent.getBytes());
    }

    private void createInvalidFormatFiles() throws IOException {
        // Create TXT file
        java.nio.file.Path txtPath = Paths.get(INVALID_TXT_FILE);
        Files.createDirectories(txtPath.getParent());
        Files.write(txtPath, "This is a text file content".getBytes());

        // Create PDF file (dummy content)
        java.nio.file.Path pdfPath = Paths.get(INVALID_PDF_FILE);
        Files.write(pdfPath, "Dummy PDF content".getBytes());

        // Create DOC file (dummy content)
        Path docPath = Paths.get(INVALID_DOC_FILE);
        Files.write(docPath, "Dummy DOC content".getBytes());
    }

    @Test(description = "Verify Range Architecture Navigation and Sections")
    public void testRangeArchitectureNavigation() {
        try {
            ExtentTest test = ExtentTestManager.getTest();
            loginHelper.loginToApplication();
            navHelper.navigateToRangeArchitecture();
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
    public void tableContentsInCurrentGlobalRATab() {
        ExtentTest test = ExtentTestManager.getTest();
        //  loginHelper.loginToApplication();
        // navHelper.navigateToRangeArchitecture();
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the table headers and content");
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


//        // Verify specific categories
//        Assert.assertTrue(rangeArchitecturePage.verifyCategories("Women"), "Women category should be present");
//        Assert.assertTrue(rangeArchitecturePage.verifyCategories("Western Wear"), "Western Wear should be present");
//        Assert.assertTrue(rangeArchitecturePage.verifyCategories("Leggings"), "Leggings should be present");

        // Verify RA tabs functionality
        //   rangeArchitecturePage.switchToCurrentGlobalRA();
//        Assert.assertTrue(rangeArchitecturePage.isCurrentGlobalRADisplayed(), "Current Global RA should be displayed");
//
//        //   rangeArchitecturePage.switchToCurrentClusterRA();
//        Assert.assertTrue(rangeArchitecturePage.isCurrentClusterRADisplayed(), "Current Cluster RA should be displayed");
    }

    @Test(description = "Verify Search Bar Functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void verifySearchBar() {
        ExtentTest test = ExtentTestManager.getTest();
        ExtentTestManager.getTest().log(Status.INFO, "Verifying search bar functionality in Range Architecture page");
        CommonUtils.takeScreenshot(driver, "verifySearchBar");
        Assert.assertTrue(rangeArchitecturePage.verifySearchBarFunctionality(),
                "Search bar should be clickable and functional");
        CommonUtils.takeScreenshot(driver, "Search bar is clickable and functional");
        ExtentTestManager.getTest().log(Status.INFO, "Search bar is clickable and functional");

    }

    @Test(description = "Verify search bar presence and functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void verifySearchBarBasics() {
        ExtentTest test = ExtentTestManager.getTest();
        ExtentTestManager.getTest().log(Status.INFO, "Verifying search bar functionality in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.verifySearchBarPresence(),
                "Search bar should be present and enabled");
        CommonUtils.takeScreenshot(driver, "Presence of search bar");
        ExtentTestManager.getTest().log(Status.INFO, "Verifying search bar functionality in Range Architecture page");
    }

    @Test(description = "Verify exact search functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void verifyExactSearch() {
        ExtentTest test = ExtentTestManager.getTest();
        ExtentTestManager.getTest().log(Status.INFO, "Verifying search results in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.enterSearchText("Western Wear"),
                "Should be able to enter western wear in search bar");
        CommonUtils.takeScreenshot(driver, "Entered western wear in search bar");
        Assert.assertTrue(rangeArchitecturePage.verifySearchResults("Western Wear"),
                "Should find exact matches");
        CommonUtils.takeScreenshot(driver, "Results should show exact matches");
    }

    @Test(description = "Verify partial search functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void verifyPartialSearch() {
        ExtentTest test = ExtentTestManager.getTest();
        ExtentTestManager.getTest().log(Status.INFO, "Verifying partial search functionality in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.verifyPartialSearch("West"),
                "Should find partial matches");
        CommonUtils.takeScreenshot(driver, "Partial search text");
    }

    @Test(description = "Verify pagination functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void verifyPagination() {
        try {
            ExtentTest test = ExtentTestManager.getTest();
            // First verify the table is loaded
            CommonUtils.waitForVisibility(driver, rangeArchitecturePage.getFirstTableRow(), 20);

            // Step 1: Verify pagination range display
            ExtentTestManager.getTest().log(Status.INFO, "Verifying pagination functionality in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.verifyPaginationRange(),
                    "Pagination range should be correctly displayed");

            // Step 2: Verify first page data
            ExtentTestManager.getTest().log(Status.INFO, "Verifying first page data in Range Architecture page");
            List<Map<String, String>> firstPageData = rangeArchitecturePage.getTableDataForPage();
            Assert.assertFalse(firstPageData.isEmpty(), "First page should contain data");
            Assert.assertTrue(firstPageData.size() <= 25, "First page should have maximum 25 rows");
            CommonUtils.takeScreenshot(driver, "First page data");


            // Step 3: Verify next page navigation
            ExtentTestManager.getTest().log(Status.INFO, "Verifying next page navigation in Range Architecture page");
            if (rangeArchitecturePage.isNextButtonEnabled()) {
                Assert.assertTrue(rangeArchitecturePage.verifyNextPageNavigation(),
                        "Next page navigation should work correctly");
                CommonUtils.takeScreenshot(driver, "Next page navigation");

                // Step 4: Verify page number updates after navigation
                ExtentTestManager.getTest().log(Status.INFO, "Verifying page Number update in Range Architecture page");
                Assert.assertTrue(rangeArchitecturePage.verifyPageNumberUpdate(),
                        "Page number should update correctly");
                CommonUtils.takeScreenshot(driver, "Page Number Update");
            }

            // Step 5: Verify previous button on first page
            ExtentTestManager.getTest().log(Status.INFO, "Verifying navigating to first page and previous button in Range Architecture page");
            rangeArchitecturePage.navigateToFirstPage();
            Assert.assertTrue(rangeArchitecturePage.verifyFirstPagePreviousButton(),
                    "Previous button should be disabled on first page");

            // Step 6: Verify data consistency across pages
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the all pages data in Range Architecture page");
            List<Map<String, String>> allPagesData = rangeArchitecturePage.getAllPagesData();
            Assert.assertFalse(allPagesData.isEmpty(),
                    "Should have data across all pages");
            CommonUtils.takeScreenshot(driver, "All pages data");

            // Step 7: Verify next button on last page
            ExtentTestManager.getTest().log(Status.INFO, "Verifying last page next button in Range Architecture page");
            Assert.assertTrue(rangeArchitecturePage.verifyLastPageNextButton(),
                    "Next button should be disabled on last page");
            CommonUtils.takeScreenshot(driver, "Last page verification");

        } catch (Exception e) {
            Assert.fail("Pagination test failed: " + e.getMessage());
        }
    }

    @Test(description = "Verify sort functionality of the headers", dependsOnMethods = "testRangeArchitectureNavigation")
//,dependsOnMethods = "verifyPagination")
    public void verifySortingFunctionality() {
        ExtentTest test = ExtentTestManager.getTest();
        CommonUtils.waitForVisibility(driver, rangeArchitecturePage.getFirstTableRow(), 20);
        // Verify column order
        List<String> expectedColumns = Arrays.asList(
                "Family", "Class Name", "Brick Name", "Top Brick",
                "Brick", "Enrichment", "MRP Range", "Options",
                "ODM", "OEM", "Total", "Fill Rate", "Action"
        );
        Assert.assertTrue(CommonUtils.verifyColumnOrder(driver, expectedColumns));

        // Verify sort symbols present
        List<String> sortableColumns = Arrays.asList(
                "Family", "Class Name", "Brick Name", "Top Brick",
                "Brick", "Enrichment", "Fill Rate"
        );
        Assert.assertTrue(CommonUtils.verifySortingSymbolsPresent(driver, sortableColumns));

        // Get table rows
        List<WebElement> rows = driver.findElements(By.xpath("//table//tbody/tr"));

        // Verify sorting for each column
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the columns data sorting for table data in Range Architecture page");
        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Family", rows, 1));
        CommonUtils.takeScreenshot(driver, "Family header");
        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Class Name", rows, 2));
        CommonUtils.takeScreenshot(driver, "ClassName header");
        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Brick Name", rows, 3));
        CommonUtils.takeScreenshot(driver, "Brick Name header");
        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Top Brick", rows, 4));
        CommonUtils.takeScreenshot(driver, "TopBrick header");
        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Brick", rows, 5));
        CommonUtils.takeScreenshot(driver, "Brick header");
        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Enrichment", rows, 6));
        CommonUtils.takeScreenshot(driver, "Enrichment header");
        Assert.assertTrue(CommonUtils.verifyColumnSorting(driver, "Fill Rate", rows, 12));
        CommonUtils.takeScreenshot(driver, "Fill Rate header");
    }

    @Test(priority = 1, description = "Verify View button redirects to detailed page", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testViewButtonRedirectsToDetailedPage() {
        ExtentTest test = ExtentTestManager.getTest();
        // Verify listing page is displayed
        ExtentTestManager.getTest().log(Status.INFO, "Verifying list page display in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
                "RAID listing page should be displayed");

        // Take screenshot of listing page
        CommonUtils.takeScreenshot(driver, "listing_page_before_click");

        // Get current URL and RAID ID
        String listingPageUrl = rangeArchitecturePage.getCurrentUrl();
        String raidId = rangeArchitecturePage.getRaidIdFromRow(0);

        // Click on first View button
        rangeArchitecturePage.clickFirstViewButton();

        // Verify navigation to details page
        Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
                "Should navigate to RAID details page");
        CommonUtils.waitForPageLoad(driver);

        // Verify URL has changed
        String detailsPageUrl = rangeArchitecturePage.getCurrentUrl();
        Assert.assertNotEquals(listingPageUrl, detailsPageUrl,
                "URL should change after clicking View button");

        // Take screenshot of details page
        CommonUtils.takeScreenshot(driver, "details_page_after_click");

        System.out.println("✓ View button successfully redirects to detailed page");
    }

    @Test(priority = 2, description = "Verify back button is present on redirected page", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testBackButtonPresenceOnDetailedPage() {
        ExtentTest test = ExtentTestManager.getTest();
        // Navigate to details page
        rangeArchitecturePage.clickFirstViewButton();

        // Verify back button is displayed
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the back button in ODM tab in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.isBackButtonDisplayed(),
                "Back button should be displayed on details page");

        // Verify back button is clickable
        Assert.assertTrue(rangeArchitecturePage.isBackButtonClickable(),
                "Back button should be clickable");

        // Take screenshot
        CommonUtils.takeScreenshot(driver, "back_button_verification");

        System.out.println("✓ Back button is present and clickable on details page");
    }

    @Test(priority = 3, description = "Verify RAID is displayed on top left next to back button", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testRaidDisplayedTopLeft() {
        ExtentTest test = ExtentTestManager.getTest();

        // Navigate to details page
        //  String expectedRaidId = rangeArchitecturePage.getRaidIdFromRow(0);
        rangeArchitecturePage.clickFirstViewButton();
        CommonUtils.waitForPageLoad(driver);

        // System.out.println("Expected RAID ID: " + expectedRaidId);

        // Verify RAID is displayed on top left
        ExtentTestManager.getTest().log(Status.INFO, "Verifying is raid data displayed on top left in ODM tab in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.isRaidDisplayedTopLeft(),
                "RAID should be displayed on top left next to back button");
        CommonUtils.takeScreenshot(driver, "RAID_Top_Left_Displayed");

        // Verify RAID ID matches
        //  String displayedRaidId = rangeArchitecturePage.getRaidDisplayText();
        // System.out.println("Displayed RAID ID: " + displayedRaidId);
        // Assert.assertTrue(displayedRaidId.contains(expectedRaidId),
        //       "Displayed RAID ID should match the selected RAID");

        // Take screenshot
        //  CommonUtils.takeScreenshot(driver, "raid_display_top_left");

        System.out.println("✓ RAID is correctly displayed on top left next to back button");
    }

    @Test(priority = 4, description = "Verify page lands with two tabs, default ODM tab with table, search, filter")
    public void testDefaultOdmTabWithFeatures() {
        ExtentTest test = ExtentTestManager.getTest();
        // Navigate to details page
        rangeArchitecturePage.clickFirstViewButton();

        // Verify tabs are displayed
        ExtentTestManager.getTest().log(Status.INFO, "Verifying tabs are displaying in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.areTabsDisplayed(),
                "Tabs should be displayed on details page");

        // Verify ODM tab is active by default
        ExtentTestManager.getTest().log(Status.INFO, "Verifying is odm tab active in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.isOdmTabActive(),
                "ODM tab should be active by default");

        // Verify table is displayed in ODM tab
        ExtentTestManager.getTest().log(Status.INFO, "Verifying first page data in Range Architecture page");
        Assert.assertTrue(rangeArchitecturePage.isTableDisplayedInOdmTab(),
                "Table should be displayed in ODM tab");

        // Verify search box is displayed
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the search box display");
        Assert.assertTrue(rangeArchitecturePage.isSearchBoxDisplayed(),
                "Search box should be displayed");

        // Verify filter button is displayed
        ExtentTestManager.getTest().log(Status.INFO, "Filter button display");
        Assert.assertTrue(rangeArchitecturePage.isFilterButtonDisplayed(),
                "Filter button should be displayed");

        // Take screenshot
        CommonUtils.takeScreenshot(driver, "odm_tab_with_features");

        System.out.println("✓ Page correctly lands with ODM tab active and all features present");
    }

    @Test(priority = 5, description = "Verify back button navigates to listing page", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testBackButtonNavigatesToListingPage() {
        ExtentTest test = ExtentTestManager.getTest();
        // Get listing page URL
        String originalListingUrl = rangeArchitecturePage.getCurrentUrl();

        // Navigate to details page
        rangeArchitecturePage.clickFirstViewButton();

        // Verify we're on details page
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the details page");
        Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
                "Should be on details page");

        // Take screenshot before clicking back
        CommonUtils.takeScreenshot(driver, "before_clicking_back_button");

        // Click back button
        rangeArchitecturePage.clickBackButton();
        CommonUtils.waitForPageLoad(driver);

        // Verify we're back on listing page
        Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
                "Should navigate back to listing page");

        // Verify URL is back to listing page
        String currentUrl = rangeArchitecturePage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("range-architecture") || currentUrl.equals(originalListingUrl),
                "Should be back on the main listing page");

        // Take screenshot after navigation
        CommonUtils.takeScreenshot(driver, "after_clicking_back_button");

        System.out.println("✓ Back button successfully navigates to listing page");
    }

    @Test(priority = 6, description = "Verify View buttons are clickable for all RAIDs", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testAllViewButtonsClickable() {
        // Verify all view buttons are clickable
        ExtentTestManager.getTest().log(Status.INFO, "Verifying all the view buttons");
        Assert.assertTrue(rangeArchitecturePage.areViewButtonsClickable(),
                "All View buttons should be clickable");

        // Get count of view buttons
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the of view buttons");
        int viewButtonCount = rangeArchitecturePage.getViewButtonsCount();
        Assert.assertTrue(viewButtonCount > 0,
                "There should be at least one View button");

        // Take screenshot
        CommonUtils.takeScreenshot(driver, "all_view_buttons_verification");

        System.out.println("✓ All " + viewButtonCount + " View buttons are clickable");
    }

    @Test(priority = 7, description = "Test complete navigation flow for multiple RAIDs", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testCompleteNavigationFlowMultipleRaids() {
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the first three view buttons ");
        int viewButtonCount = Math.min(rangeArchitecturePage.getViewButtonsCount(), 3); // Test first 3 RAIDs

        for (int i = 0; i < viewButtonCount; i++) {
            // Get RAID ID
            String raidId = rangeArchitecturePage.getRaidIdFromRow(i);

            // Click View button for current row
            rangeArchitecturePage.clickViewButtonForRow(i);
            CommonUtils.waitForPageLoad(driver);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Verify details page
            Assert.assertTrue(rangeArchitecturePage.isDetailsPageDisplayed(),
                    "Details page should be displayed for RAID: " + raidId);

            // Take screenshot
            CommonUtils.takeScreenshot(driver, "raid_" + raidId + "_details_page");

            // Click back button
            rangeArchitecturePage.clickBackButton();
            CommonUtils.waitForPageLoad(driver);

            // Verify back on listing page
            Assert.assertTrue(rangeArchitecturePage.isListingPageDisplayed(),
                    "Should be back on listing page after viewing RAID: " + raidId);
            CommonUtils.takeScreenshot(driver, "Verifying the listing page is visible");

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

    @Test(description = "Verify Download button functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testCompleteDownloadFunctionality() throws Exception {
        //Verify the download button
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the Download button visible");
        Assert.assertTrue(rangeArchitecturePage.isDownloadButtonDisplayed(), "Download button should be displayed");
        CommonUtils.takeScreenshot(driver, "Download button visible");

        // 1. Trigger download
        ExtentTestManager.getTest().log(Status.INFO, "Verifying triggering the download button");
        String downloadedFile = rangeArchitecturePage.triggerDownloadAndGetFilePath();
        CommonUtils.takeScreenshot(driver, "Filepath");
        System.out.println("Downloaded file at: " + downloadedFile);


        // ✅ Validate success message
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the download success message");
        Assert.assertTrue(rangeArchitecturePage.isDownloadSuccessMessageDisplayed(), "Download Successful message should be displayed");
        CommonUtils.takeScreenshot(driver, "Download success message is displayed");

        // 2. Validate file exists and naming convention
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the file exists and naming convention");
        Assert.assertTrue(ExcelUtils.isValidExcelFile(downloadedFile));
        CommonUtils.takeScreenshot(driver, "Excel file validation");
        Assert.assertTrue(ExcelUtils.validateFileNamingConvention(
                new File(downloadedFile).getName()));
        CommonUtils.takeScreenshot(driver, "File Naming convention");

        // 3. Validate headers
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the Excel headers");
        Assert.assertTrue(ExcelUtils.validateExcelHeaders(downloadedFile, "RA-250605-0001"));
        CommonUtils.takeScreenshot(driver, "Headers are verified in excel");

        // 4. Read and validate data
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the data");
        List<Map<String, String>> data = ExcelUtils.readExcelData(downloadedFile, "RA-250605-0001");
        Assert.assertTrue(ExcelUtils.validateDataTypes(data));
        CommonUtils.takeScreenshot(driver, "Data types are valid");
        Assert.assertTrue(ExcelUtils.validateTotalQtyCalculation(data));
        CommonUtils.takeScreenshot(driver, "Validates the Quality calculation");
        Assert.assertTrue(ExcelUtils.validateFillPercentageCalculation(data));
        CommonUtils.takeScreenshot(driver, "Validated the percentage calculation");
    }

    @Test(priority = 1, description = "Verify Filter button visibility", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testFilterButtonVisibility() {
        // Verify if the Filter button is present right next to the Search bar in blue color
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter button display");
        Assert.assertTrue(rangeArchitecturePage.isFilterButtonDisplayed(),
                "Filter button should be visible");
        CommonUtils.takeScreenshot(driver, "Filter button is displayed");

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the search bar next to Filter button");
        Assert.assertTrue(rangeArchitecturePage.isFilterButtonNextToSearchBar(),
                "Filter button should be next to search bar");
        CommonUtils.takeScreenshot(driver, "Filter button is next to the search bar");

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the color of filter button");
        Assert.assertTrue(rangeArchitecturePage.isFilterButtonBlue(),
                "Filter button should be blue in color");
        CommonUtils.takeScreenshot(driver, "filter_button_visibility and in blue color");
    }

    @Test(priority = 2, description = "Verify Filter button functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testFilterButtonFunctionality() {
        // Verify that when the Filter button is clicked, all table headers appear as dropdown fields
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter button");
        rangeArchitecturePage.clickFilterButton();

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter headers");
        Assert.assertTrue(rangeArchitecturePage.areFilterHeadersDisplayed(),
                "All filter headers should be displayed after clicking filter button");
        CommonUtils.takeScreenshot(driver, "Filter headers are displayed");

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the ClearAll button display");
        Assert.assertTrue(rangeArchitecturePage.isClearAllButtonDisplayed(),
                "Clear All button should be displayed");
        CommonUtils.takeScreenshot(driver, "ClearAll is displayed");

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the ClearAll button color");
        Assert.assertTrue(rangeArchitecturePage.isClearAllButtonDarkText(),
                "Clear All button should be grey in color");
        CommonUtils.takeScreenshot(driver, "ClearAll_button_color is dark black");
    }

    @Test(priority = 3, description = "Verify headers on clicking Filter button", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testFilterHeaders() {

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter button");
        rangeArchitecturePage.clickFilterButton();

        // Verify all table headers are displayed in correct order
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the table headers display");
        List<String> expectedHeaders = Arrays.asList(
                "Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment"
        );

        List<String> actualHeaders = rangeArchitecturePage.getFilterHeadersOrder();
        CommonUtils.takeScreenshot(driver, "Table headers are in order");

        Assert.assertEquals(actualHeaders, expectedHeaders,
                "Filter headers should appear in the correct order");
        CommonUtils.takeScreenshot(driver, "Filter headers are in order");

        Assert.assertTrue(rangeArchitecturePage.isClearAllButtonDisplayed(),
                "Clear All button should be displayed next to headers");
        CommonUtils.takeScreenshot(driver, "ClearAll button is displayed");
    }

    @Test(priority = 4, description = "Verify functionality of each filter header", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testFilterHeaderFunctionality() {

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter button");
        rangeArchitecturePage.clickFilterButton();

        String[] headers = {"Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment"};

        for (String header : headers) {
            // Click on filter header arrow
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the family header clicking");
            rangeArchitecturePage.clickFilterHeader(header);
            CommonUtils.takeScreenshot(driver, "Family header clicked");

            // Verify dropdown content is displayed
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the filter dropdown display");
            Assert.assertTrue(rangeArchitecturePage.isFilterDropdownDisplayed(),
                    header + " filter dropdown should be displayed");
            CommonUtils.takeScreenshot(driver, "Filter dropdown is displayed");

            // Verify search bar is present
            ExtentTestManager.getTest().log(Status.INFO, "Verify filter search box display");
            Assert.assertTrue(rangeArchitecturePage.isFilterSearchBoxDisplayed(),
                    header + " filter should have search bar");
            CommonUtils.takeScreenshot(driver, "Filter search box is displayed");

//            // Verify scrollable list is present
//            Assert.assertTrue(rangeArchitecturePage.isScrollableListDisplayed(),
//                    header + " filter should have scrollable list");

            // Verify clear button is present
            ExtentTestManager.getTest().log(Status.INFO, "Verify Clear button display");
            Assert.assertTrue(rangeArchitecturePage.isFilterClearButtonDisplayed(),
                    header + " filter should have clear button");
            CommonUtils.takeScreenshot(driver, "Family Clear button is displayed");

            CommonUtils.takeScreenshot(driver, "filter_" + header.toLowerCase().replace(" ", "_") + "_dropdown");

            // Close dropdown by clicking elsewhere or pressing escape
            rangeArchitecturePage.closeDropdown(); // Close current dropdown
        }
    }

    @Test(priority = 5, description = "Verify select/unselect dropdown values & clear", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testFilterSelectionAndClear()  {

        ExtentTestManager.getTest().log(Status.INFO, "Opening the filter panel and applying Family filter");

        // Step 1: Apply single filter "Family = Women"
        rangeArchitecturePage.clickFilterButton();
        rangeArchitecturePage.clickFilterHeader("Family");
        rangeArchitecturePage.selectFilterOption("Women");
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.waitForSeconds(20);
        CommonUtils.takeScreenshot(driver, "filter_family_women_selected");

        int pageCount = 1;
        while (true) {
            List<WebElement> rows = rangeArchitecturePage.getAllTableRows();
            Assert.assertTrue(rows.size() > 0, "Table rows should not be empty on page " + pageCount);

            for (WebElement row : rows) {
                String family = rangeArchitecturePage.getCellValueFromRow(row, "Family");
                Assert.assertEquals(family, "Women", "Family column should be 'Women'");
            }

            CommonUtils.takeScreenshot(driver, "family_filter_page_" + pageCount);

//            if (rangeArchitecturePage.isNextButtonEnabled()) {
////                ExtentTestManager.getTest().log(Status.INFO, "Verifying the table data in all the pages in Range Architecture page");
////                List<Map<String, String>> allPagesData = rangeArchitecturePage.getAllPagesData();
////                Assert.assertFalse(allPagesData.isEmpty(),
////                        "Should have data across all pages");
////                CommonUtils.takeScreenshot(driver,"Data should be available");
//                rangeArchitecturePage.goToNextPageIfEnabled();
//                CommonUtils.waitForPageLoad(driver);
//                pageCount++;
////            } else {
////                break;
////            }
////        } while (true);
//            } else {
//                //   ExtentTestManager.getTest().log(Status.INFO, "✅ Reached last page of pagination.");
//                break;
//            }
//        }
            // Check if next is disabled by class
            if (!rangeArchitecturePage.isNextButtonEnabled()) {
                System.out.println("✅ Reached last page. Total pages: " + pageCount);
                break;
            }

            // Now safely click next
            try {
                rangeArchitecturePage.goToNextPage(); // scrolls and clicks using JS safely
                CommonUtils.waitForPageLoad(driver);
                pageCount++;
            } catch (ElementClickInterceptedException e) {
                System.out.println("⚠️ Click intercepted. Likely already on last page. Stopping pagination.");
                break;
            } catch (Exception e) {
                System.out.println("⚠️ Unexpected error on page " + pageCount + ": " + e.getMessage());
                break;
            }
        }

        ExtentTestManager.getTest().log(Status.PASS, "✅ Verified all pages for Family = Women");

        // Step 2: Apply multiple selection on Class Name
        ExtentTestManager.getTest().log(Status.INFO, "Applying multi-select filter for Class Name");
        rangeArchitecturePage.clickFilterHeader("Class Name");
        List<String> multipleOptions = Arrays.asList("Inner Wear", "Western Wear");
        rangeArchitecturePage.selectMultipleFilterOptions(multipleOptions);
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.waitForSeconds(20);
        CommonUtils.takeScreenshot(driver, "class_name_multiselect_applied");

        // ✅ Validate Class Name values across all pages
        pageCount = 1;
        while (true) {
            List<WebElement> rows = rangeArchitecturePage.getAllTableRows();
            Assert.assertTrue(rows.size() > 0, "Table rows should not be empty on page " + pageCount);

            for (WebElement row : rows) {
                String className = rangeArchitecturePage.getCellValueFromRow(row, "Class Name");
                Assert.assertTrue(
                        className.equals("Inner Wear") || className.equals("Western Wear"),
                        "Class Name should be 'Inner Wear' or 'Western Wear'. Found: " + className
                );
            }

            CommonUtils.takeScreenshot(driver, "class_name_page_" + pageCount);

//            if (rangeArchitecturePage.isNextButtonEnabled()) {
//                rangeArchitecturePage.goToNextPageIfEnabled();
//                CommonUtils.waitForPageLoad(driver);
//                pageCount++;
//            } else {
//                ExtentTestManager.getTest().log(Status.INFO, "✅ Reached last page of pagination.");
//                break;
//            }
//        } //while (true);

            // Check if next is disabled by class
            if (!rangeArchitecturePage.isNextButtonEnabled()) {
                System.out.println("✅ Reached last page. Total pages: " + pageCount);
                break;
            }

            // Now safely click next
            try {
                rangeArchitecturePage.goToNextPage(); // scrolls and clicks using JS safely
                CommonUtils.waitForPageLoad(driver);
                pageCount++;
            } catch (ElementClickInterceptedException e) {
                System.out.println("⚠️ Click intercepted. Likely already on last page. Stopping pagination.");
                break;
            } catch (Exception e) {
                System.out.println("⚠️ Unexpected error on page " + pageCount + ": " + e.getMessage());
                break;
            }
        }

        ExtentTestManager.getTest().log(Status.PASS, "✅ Verified all pages for Class Name multi-select");

        // Step 3: Unselect "Western Wear"
        ExtentTestManager.getTest().log(Status.INFO, "Unselecting 'Western Wear' from Class Name");
        rangeArchitecturePage.clickFilterHeader("Class Name");
        rangeArchitecturePage.unselectFilterOption("Western Wear");
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.waitForSeconds(10);
        CommonUtils.takeScreenshot(driver, "western_wear_unselected");

        // Step 4: Click Clear All button
        ExtentTestManager.getTest().log(Status.INFO, "Clicking Clear All button");
        rangeArchitecturePage.clickFilterClearButton();
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.waitForSeconds(10);
        Assert.assertTrue(rangeArchitecturePage.areAllOptionsUnselected(), "Filters should be cleared");
        CommonUtils.takeScreenshot(driver, "filters_cleared");

        // Step 5: Validate table is still showing records after clear
        List<WebElement> rowsAfterClear = rangeArchitecturePage.getAllTableRows();
        Assert.assertTrue(rowsAfterClear.size() > 0, "Table should still show data after clearing filters");

        for (WebElement row : rowsAfterClear) {
            String family = rangeArchitecturePage.getCellValueFromRow(row, "Family");
            Assert.assertNotNull(family, "Family column value should not be null");
        }

        ExtentTestManager.getTest().log(Status.PASS, "✅ Verified data is visible after Clear All");
    }

    @Test(priority = 6, description = "Verify search bar under filter dropdown",dependsOnMethods = "testRangeArchitectureNavigation")
    public void testFilterSearchBar() {
        ExtentTestManager.getTest().log(Status.INFO,"Verifying the filter button and headers");
        rangeArchitecturePage.clickFilterButton();
        rangeArchitecturePage.clickFilterHeader("Family");

        // Verify search bar is editable
        ExtentTestManager.getTest().log(Status.INFO,"Verifying the search bar is editable");
        Assert.assertTrue(rangeArchitecturePage.isFilterSearchBoxEditable(),
                "Filter search bar should be editable");
        CommonUtils.takeScreenshot(driver,"search box is editable");

        // Test complete value search
        ExtentTestManager.getTest().log(Status.INFO,"Verifying the filter options and complete search");
        rangeArchitecturePage.searchInFilter("Women");
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.waitForSeconds(10);
        List<String> completeSearchResults = rangeArchitecturePage.getFilteredOptions();
        CommonUtils.waitForSeconds(10);
        Assert.assertTrue(completeSearchResults.contains("Women"),
                "Complete search should return exact match");
        CommonUtils.takeScreenshot(driver, "complete_search_results");

        // Test partial value search
        ExtentTestManager.getTest().log(Status.INFO,"Verifying the filter options and partial search");
        rangeArchitecturePage.searchInFilter("Wo");
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.waitForSeconds(10);
        Assert.assertTrue(rangeArchitecturePage.verifyPartialSearch("Wo"),
                "Partial search should return matching results");
        CommonUtils.takeScreenshot(driver, "partial_search_results");

        // Clear search
        rangeArchitecturePage.searchInFilter("");
        CommonUtils.waitForSeconds(30);
        CommonUtils.takeScreenshot(driver, "search_cleared");
    }

    @Test(priority = 1, description = "Verify that each filter has all list items with checkboxes",dependsOnMethods = "testRangeArchitectureNavigation")
    public void testFilterListItemsWithCheckboxes() {
        try {
            ExtentTestManager.getTest().log(Status.INFO,"Verifying the list items for checkboxes");
            rangeArchitecturePage.clickFilterButton();
            List<String> filterNames = Arrays.asList("Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment");

            for (String filterName : filterNames) {
                int count = rangeArchitecturePage.verifyFilterListItemsWithCheckboxes(filterName);
                CommonUtils.waitForSeconds(10);
                CommonUtils.takeScreenshot(driver,"checkboxes are listed");
                Assert.assertTrue(count > 0, "Filter '" + filterName + "' should have visible, clickable checkbox labels for all list items");
            }
            CommonUtils.takeScreenshot(driver, "filter_list_items_with_checkboxes");
        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "filter_list_items_error");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 2, description = "Verify search bar presence in each table filter",dependsOnMethods = "testRangeArchitectureNavigation")
    public void testSearchBarPresenceInFilters() {
        try {
            ExtentTestManager.getTest().log(Status.INFO,"Verifying the filter button");
            rangeArchitecturePage.clickFilterButton();
            List<String> filterNames = Arrays.asList("Family", "Class Name", "Brick Name", "Top Brick", "Brick", "Enrichment");

            for (String filterName : filterNames) {
                boolean hasSearchBar = rangeArchitecturePage.verifySearchBarInFilter(filterName);
                CommonUtils.takeScreenshot(driver,"Searchbar is visible");
                Assert.assertTrue(hasSearchBar, "Filter '" + filterName + "' should have a search bar");
            }
            CommonUtils.takeScreenshot(driver, "search_bar_presence_verification");
        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "search_bar_presence_error");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 3, description = "Verify search functionality in filter dropdowns")
    public void testSearchFunctionalityInFilters() {
        try {
             ExtentTestManager.getTest().log(Status.INFO,"Verifying the filter button");
             rangeArchitecturePage.clickFilterButton();
            // Test partial search in different filters
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
            ExtentTestManager.getTest().log(Status.INFO,"Verifying the filter button and multi select function");
            List<String> itemsToSelect = Arrays.asList("Item1", "Item2", "Item3");
            boolean multiSelect = rangeArchitecturePage.verifyMultiSelectFunctionality("Family", itemsToSelect);
            Assert.assertTrue(multiSelect, "Filter should support multi-select functionality");

            CommonUtils.takeScreenshot(driver, "multi_select_functionality");
        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "multi_select_error");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(description = "Verify that all table header filters support multi-select",dependsOnMethods = "testRangeArchitectureNavigation")
    public void verifyMultiSelectOnAllTableHeaderFilters() {
        ExtentTestManager.getTest().log(Status.INFO, "Validating multi-select capability of table header filters");
        rangeArchitecturePage.clickFilterButton();
        boolean result = rangeArchitecturePage.verifyAllFiltersAreMultiSelectable();
        CommonUtils.takeScreenshot(driver,"Multi checkboxes and filters selected");

        Assert.assertTrue(result, "Each filter dropdown should support multi-select functionality");
        CommonUtils.takeScreenshot(driver,"Dropdown should selected multi filters and checkboxes");
    }

    @Test(priority = 5, description = "Verify sort functionality after filtering",dependsOnMethods = "testRangeArchitectureNavigation")
    public void testSortAfterFiltering() {
        try {
            // Test sorting on different columns after applying filter
            ExtentTestManager.getTest().log(Status.INFO, "Verifying the Family header results based on sorting filter");
            rangeArchitecturePage.clickFilterButton();
            boolean brickSort = rangeArchitecturePage.testSortAfterFiltering("Family", "Women", "Brick Name");
            Assert.assertTrue(brickSort, "Sort should work on Brick Name column after filtering");
            CommonUtils.takeScreenshot(driver,"Family header table results sort_after_filtering");

            ExtentTestManager.getTest().log(Status.INFO, "Verifying the Class Name header results based on sorting filter");
            boolean attributeSort = rangeArchitecturePage.testSortAfterFiltering("Class Name", "Lingerie & Innerwear", "Brick Name");
            Assert.assertTrue(attributeSort, "Sort should work on Attributes column after filtering");
            CommonUtils.takeScreenshot(driver,"Class Name header table results sort_after_filtering");

            ExtentTestManager.getTest().log(Status.INFO, "Verifying the Top Brick header results based on sorting filter");
            boolean costSort = rangeArchitecturePage.testSortAfterFiltering("Top Brick", "Innerwear", "Brick Name");
            Assert.assertTrue(costSort, "Sort should work on Cost Range column after filtering");
            CommonUtils.takeScreenshot(driver,"Top Brick header table results sort_after_filtering");
        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "sort_after_filtering_error");
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test(priority = 6, description = "Verify Clear All functionality",dependsOnMethods = "testRangeArchitectureNavigation")
    public void testClearAllFunctionality() {
        try {
            // First select some filters
         //   List<String> toSelect = Arrays.asList("Men", "Women", "Activewear");
            rangeArchitecturePage.clickFilterButton();
            ExtentTestManager.getTest().log(Status.INFO,"Verifying the multi select functionalituy for filter headers");
            rangeArchitecturePage.verifyMultiSelectFunctionality("Family", Arrays.asList("Men", "Women"));


            // Then clear all
            boolean clearAll = rangeArchitecturePage.verifyClearAllFunctionality();

            Assert.assertTrue(clearAll, "Clear All should unselect all filter items");
            CommonUtils.takeScreenshot(driver, "clear_all_functionality");
        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "clear_all_error");
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

    @Test(description = "Verify Upload Global RA Functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testUploadGlobalRA() throws IOException {

        // Open upload modal
        rangeArchitecturePage.clickUploadGlobalRA();
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


    @Test(description = "Verify Upload Global RA Functionality", dependsOnMethods = "testRangeArchitectureNavigation")
    public void testUploadGlobalMRPMultiplierFile() throws IOException {

        // Open upload modal
        rangeArchitecturePage.clickUploadGlobalRA();
        Assert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(), "Upload modal should be displayed");
        // Upload file using Robot and path
        // Upload the file
        String filePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/MultiplierFile.xlsx";
        System.out.println(filePath);
        rangeArchitecturePage.uploadMRPMultiplierFile(filePath);
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

        //Navigate to MRP Multiplier tab
        rangeArchitecturePage.clickOnMRPMultiplierNavigationButton();
        Assert.assertTrue(rangeArchitecturePage.isMRPMultiplierHeaderMessageDisplayed(), "MRP Multiplier Header should be displayed");


        // Get UI table rows
        List<WebElement> uiRows = driver.findElements(By.xpath("//table//tbody/tr"));
        List<Map<String, String>> uiData = CommonUtils.extractTableDataForMultiplier(uiRows);

        // Read Excel data
        String excelPath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/MultiplierFile.xlsx";
        List<Map<String, String>> excelData = ExcelUtils.readMRPMultiplierExcelData(excelPath, "MultiplierData");

        // Validate data
        //   boolean isValid = CommonUtils.validateExcelAndUIData(excelData, uiRows);
        //   Assert.assertTrue(isValid, "Excel data should match UI data");
        //    List<Map<String, String>> uiData = CommonUtils.extractTableData(uiRows);
        // ExtentTest test = extent.createTest("Excel vs UI Validation");
        System.out.println("Excel Data: " + excelData);
        System.out.println("UI Data: " + uiData);
        boolean isValid = CommonUtils.validateMultiplierExcelAndUIData(excelData, uiData);
//        Assert.assertTrue(isValid, "Excel data should match UI data");

        // Use soft assertions to show all mismatches
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(isValid, "Excel data should match UI data");

        // If you want to see all failures before the test stops
        softAssert.assertAll();
    }


    @Test(description = "Verify Sample File Downloads")
    public void testSampleFileDownloads() {
        //   uploadPage = new UploadGlobalRAPage(driver);

        // Open upload modal
        rangeArchitecturePage.clickUploadGlobalRA();

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
        rangeArchitecturePage.clickUploadGlobalRA();
        String invalidFilePath = "src/test/java/com/impetus/data/invalid.txt";
        rangeArchitecturePage.uploadRAFile(invalidFilePath);

        // Add assertions for error messages

        rangeArchitecturePage.clickCancel();
    }

    @Test(description = "Verify Excel Headers Match with UI Headers", dependsOnMethods = "testUploadGlobalRA")
    public void testHeadersMatch() {
        //  uploadPage = new UploadGlobalRAPage(driver);

        // Path to test data excel file
        String raFilePath = "/Users/chinta.anusha/Desktop/UVPAutomation/src/test/java/com/impetus/data/RA.xlsx";

        // Get headers from Excel
        //    List<String> excelHeaders = ExcelUtils.readExcelData(raFilePath,RAFile);

        // Upload the file
        rangeArchitecturePage.clickUploadGlobalRA();
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

    @Test(priority = 2, description = "P1 - Verify Download Sample Excel content and column headers")
    public void testDownloadSampleExcelContent() throws Exception {
        ExtentTest test = ExtentTestManager.getTest();

        try {
            // Download the sample file
            test.log(Status.INFO, "Downloading sample file for content verification");
            String downloadedFile = rangeArchitecturePage.triggerDownloadSampleRAAndGetFilePath();

            // Step 1: Verify column headers
            test.log(Status.INFO, "Verifying Excel column headers match expected format");
            Assert.assertTrue(ExcelUtils.validateDownloadSampleHeaders(downloadedFile),
                    "Excel headers should match expected format: Family,ClassName,BrickName,TopBrick,Final brick,Brick,Brick Code,Enrichment,MRPMAx,MRPMin,OptionsCount,OTBNumber");
            CommonUtils.takeScreenshot(driver, "Excel_Headers_Verified");

            // Step 2: Verify data types are appropriate
            test.log(Status.INFO, "Verifying data types in Excel file");
            Assert.assertTrue(ExcelUtils.validateDownloadSampleDataTypes(downloadedFile),
                    "Data types should be appropriate as per product team specifications");
            CommonUtils.takeScreenshot(driver, "Excel_Data_Types_Verified");

            // Step 3: Verify data content is not empty (if file contains data)
            test.log(Status.INFO, "Verifying Excel file contains appropriate data");
            List<Map<String, String>> data = ExcelUtils.readDownloadSampleData(downloadedFile);
            // Note: Sample file might be empty or contain template data
            test.log(Status.INFO, "Excel file contains " + data.size() + " data rows");

            test.log(Status.PASS, "Download Sample Excel content verified successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "Excel_Content_Test_Failed");
            throw e;
        }
    }

    @Test(priority = 3, description = "P1 - Verify success message after Download Sample button click")
    public void testDownloadSampleSuccessMessage() throws Exception {
        ExtentTest test = ExtentTestManager.getTest();

        try {
            // Step 1: Click Download Sample button
            test.log(Status.INFO, "Clicking Download Sample button to verify success message");
            rangeArchitecturePage.downloadSampleRA();

            // Step 2: Verify success message is displayed
            test.log(Status.INFO, "Verifying success message pop-up is displayed");
            Assert.assertTrue(rangeArchitecturePage.isDownloadSuccessMessageDisplayed(),
                    "Success message should be displayed after successful download");
            CommonUtils.takeScreenshot(driver, "Success_Message_Displayed");

            // Step 3: Verify green tick/success icon is displayed
            test.log(Status.INFO, "Verifying green tick/success icon is displayed");
            Assert.assertTrue(rangeArchitecturePage.isSuccessIconDisplayed(),
                    "Green tick/success icon should be displayed with success message");
            CommonUtils.takeScreenshot(driver, "Success_Icon_Displayed");

            test.log(Status.PASS, "Download Sample success message verified successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "Success_Message_Test_Failed");
            throw e;
        }
    }

    @Test(priority = 4, description = "P1 - Verify failure message when Download Sample fails")
    public void testDownloadSampleFailureMessage() throws Exception {
        ExtentTest test = ExtentTestManager.getTest();

        try {
            // Step 1: Simulate download failure scenario
            test.log(Status.INFO, "Simulating download failure scenario");
            rangeArchitecturePage.simulateDownloadFailure();

            // Step 2: Attempt to download sample file
            test.log(Status.INFO, "Attempting to download sample file in failure scenario");
            rangeArchitecturePage.downloadSampleRA();

            // Step 3: Verify failure message is displayed
            test.log(Status.INFO, "Verifying failure message pop-up is displayed");
            Assert.assertTrue(rangeArchitecturePage.isDownloadFailureMessageDisplayed(),
                    "Failure message should be displayed when download fails");
            CommonUtils.takeScreenshot(driver, "Failure_Message_Displayed");

            // Step 4: Verify cross mark/error icon is displayed
            test.log(Status.INFO, "Verifying cross mark/error icon is displayed");
            Assert.assertTrue(rangeArchitecturePage.isErrorIconDisplayed(),
                    "Cross mark/error icon should be displayed with failure message");
            CommonUtils.takeScreenshot(driver, "Error_Icon_Displayed");

            test.log(Status.PASS, "Download Sample failure message verified successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "Failure_Message_Test_Failed");
            throw e;
        }
    }

    @Test(priority = 1, description = "Verify close button functionality - popup closes when clicking close (X) icon")
    // @Story("Close Button Functionality")
    // @Description("Verify that when the user clicks on the close (X) button, the popup is getting closed")
    public void testCloseButtonFunctionality() {
        ExtentTestManager.getTest().info("Starting test: Verify close button functionality");

        // Verify popup is displayed
        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(),
                "Popup should be displayed before clicking close button");
        ExtentTestManager.getTest().info("Popup is displayed successfully");

        // Verify close button is displayed and enabled
        Assert.assertTrue(rangeArchitecturePage.isCloseButtonDisplayed(),
                "Close button should be displayed");
        Assert.assertTrue(rangeArchitecturePage.isCloseButtonEnabled(),
                "Close button should be enabled");
        ExtentTestManager.getTest().info("Close button is displayed and enabled");

        // Click the close button
        rangeArchitecturePage.clickCloseButton();
        ExtentTestManager.getTest().info("Clicked on close button");

        // Verify popup is closed
        Assert.assertTrue(rangeArchitecturePage.isModalClosed(),
                "Popup should be closed after clicking close button");
        ExtentTestManager.getTest().pass("Popup closed successfully after clicking close button");

        CommonUtils.takeScreenshot(driver, "popup_closed_after_close_button_click");
    }

    @Test(priority = 2, description = "Verify cancel button functionality - popup closes when clicking cancel button")
    // @Story("Cancel Button Functionality")
    // @Description("Verify that when the user clicks on the cancel button, the popup is getting closed")
    public void testCancelButtonFunctionality() {
        ExtentTestManager.getTest().info("Starting test: Verify cancel button functionality");

        // Verify popup is displayed
        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(),
                "Popup should be displayed before clicking cancel button");
        ExtentTestManager.getTest().info("Popup is displayed successfully");

        // Verify cancel button is displayed and enabled
        Assert.assertTrue(rangeArchitecturePage.isCancelButtonDisplayed(),
                "Cancel button should be displayed");
        Assert.assertTrue(rangeArchitecturePage.isCancelButtonEnabled(),
                "Cancel button should be enabled");
        ExtentTestManager.getTest().info("Cancel button is displayed and enabled");

        // Click the cancel button
        rangeArchitecturePage.clickCancelButton();
        ExtentTestManager.getTest().info("Clicked on cancel button");

        // Verify popup is closed
        Assert.assertTrue(rangeArchitecturePage.isModalClosed(),
                "Popup should be closed after clicking cancel button");
        ExtentTestManager.getTest().pass("Popup closed successfully after clicking cancel button");

        CommonUtils.takeScreenshot(driver, "popup_closed_after_cancel_button_click");
    }

    @Test(priority = 3, description = "Verify close button visibility and accessibility")
    //  @Story("Close Button Accessibility")
    //  @Description("Verify that the close button is visible and accessible to users")
    public void testCloseButtonVisibilityAndAccessibility() {
        ExtentTestManager.getTest().info("Starting test: Verify close button visibility and accessibility");

        // Verify popup is displayed
        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(),
                "Popup should be displayed");

        // Verify close button is visible
        Assert.assertTrue(rangeArchitecturePage.isCloseButtonDisplayed(),
                "Close button should be visible");

        // Verify close button is clickable
        Assert.assertTrue(rangeArchitecturePage.isCloseButtonEnabled(),
                "Close button should be clickable");

        ExtentTestManager.getTest().pass("Close button is visible and accessible");
        CommonUtils.takeScreenshot(driver, "close_button_visibility_verified");
    }

    @Test(priority = 4, description = "Verify cancel button visibility and accessibility")
    //  @Story("Cancel Button Accessibility")
    //  @Description("Verify that the cancel button is visible and accessible to users")
    public void testCancelButtonVisibilityAndAccessibility() {
        ExtentTestManager.getTest().info("Starting test: Verify cancel button visibility and accessibility");

        // Verify popup is displayed
        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(),
                "Popup should be displayed");

        // Verify cancel button is visible
        Assert.assertTrue(rangeArchitecturePage.isCancelButtonDisplayed(),
                "Cancel button should be visible");

        // Verify cancel button is clickable
        Assert.assertTrue(rangeArchitecturePage.isCancelButtonEnabled(),
                "Cancel button should be clickable");

        ExtentTestManager.getTest().pass("Cancel button is visible and accessible");
        CommonUtils.takeScreenshot(driver, "cancel_button_visibility_verified");
    }

    @Test(priority = 5, description = "Verify popup remains open when clicking outside (negative test)")
    // @Story("Popup Behavior")
    // @Description("Verify that popup does not close when clicking outside the modal area")
    public void testPopupDoesNotCloseOnOutsideClick() {
        ExtentTestManager.getTest().info("Starting test: Verify popup does not close when clicking outside");

        // Verify popup is displayed
        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(),
                "Popup should be displayed before clicking outside");

        // Click outside the modal
        rangeArchitecturePage.clickOutsideModal();
        ExtentTestManager.getTest().info("Clicked outside the modal");

        // Wait a moment
        CommonUtils.waitForPageLoad(driver);

        // Verify popup is still displayed
        Assert.assertTrue(rangeArchitecturePage.isModalDisplayed(),
                "Popup should still be displayed after clicking outside");

        ExtentTestManager.getTest().pass("Popup correctly remains open when clicking outside");
        CommonUtils.takeScreenshot(driver, "popup_remains_open_after_outside_click");
    }

    @Test(description = "Verify user can select a file from system", priority = 1)
    public void testFileSelection() {
        try {
            // Verify file exists
            File testFile = new File(RA_FILE_PATH);
            Assert.assertTrue(testFile.exists(), "Test file should exist: " + RA_FILE_PATH);

            // Click upload button to trigger file dialog
            rangeArchitecturePage.triggerFileUploadDialog();

            // Upload file
            rangeArchitecturePage.uploadFileWithTimeoutHandling(RA_FILE_PATH, false);

            // Verify file selection is successful
            Assert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "Selected file should be displayed after file selection");

            String selectedFileName = rangeArchitecturePage.getSelectedFileName();
            Assert.assertTrue(selectedFileName.contains("RA.xlsx") || selectedFileName.contains("RA"),
                    "Selected file name should contain the uploaded file name. Actual: " + selectedFileName);

            CommonUtils.takeScreenshot(driver, "file_selection_success");

        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "file_selection_error");
            Assert.fail("File selection test failed: " + e.getMessage());
        }
    }

    @Test(description = "Verify selected file is displayed below upload button", priority = 2, dependsOnMethods = "testFileSelection")
    public void testSelectedFileDisplay() {
        try {
            // Upload file
            rangeArchitecturePage.triggerFileUploadDialog();
            rangeArchitecturePage.uploadFileWithTimeoutHandling(RA_FILE_PATH, false);

            // Verify file display
            Assert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                    "Selected file should be displayed below the upload button");

            String displayedFileName = rangeArchitecturePage.getSelectedFileName();
            Assert.assertFalse(displayedFileName.isEmpty(),
                    "Displayed file name should not be empty");

            // Verify the displayed name contains part of the actual file name
            Assert.assertTrue(displayedFileName.toLowerCase().contains("ra"),
                    "Displayed file name should contain 'ra'. Actual: " + displayedFileName);

            CommonUtils.takeScreenshot(driver, "file_display_verification");

        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "file_display_error");
            Assert.fail("File display test failed: " + e.getMessage());
        }
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

    @Test(description = "Verify upload button accessibility and visibility", priority = 5)
    public void testUploadButtonAccessibility() {
        try {
            // Verify upload button is visible and enabled
            Assert.assertTrue(rangeArchitecturePage.uploadRAButton.isDisplayed(),
                    "Upload button should be visible");
            Assert.assertTrue(rangeArchitecturePage.uploadRAButton.isEnabled(),
                    "Upload button should be enabled");

            // Verify button can be clicked
            rangeArchitecturePage.triggerFileUploadDialog();

            // Verify modal or file dialog appears (wait a moment for dialog)
            CommonUtils.waitForSeconds(2);

            CommonUtils.takeScreenshot(driver, "upload_button_accessibility");

        } catch (Exception e) {
            CommonUtils.takeScreenshot(driver, "upload_button_accessibility_error");
            Assert.fail("Upload button accessibility test failed: " + e.getMessage());
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

    @Test(priority = 3, description = "Verify loading indicator displays filename and file size during upload")
    public void testLoadingIndicatorWithFileDetails() {
        SoftAssert softAssert = new SoftAssert();

        try {
            ExtentTestManager.getTest().log(Status.INFO,
                    "Testing loading indicator with file details");

            // Navigate to upload section
            rangeArchitecturePage.clickUploadGlobalRA();
            softAssert.assertTrue(rangeArchitecturePage.isUploadModalDisplayed(),
                    "Upload modal should be displayed");

            // Get file information
            File file = new File(VALID_XLSX_FILE);
            String fileName = file.getName();
            long fileSize = file.length();
            String fileSizeFormatted = formatFileSize(fileSize);

            ExtentTestManager.getTest().log(Status.INFO,
                    String.format("File details - Name: %s, Size: %s", fileName, fileSizeFormatted));

            // Start file upload
            rangeArchitecturePage.triggerFileUploadDialog();

            // Upload file and immediately check loading indicator
            CommonUtils.uploadFileUsingRobot(VALID_XLSX_FILE);

            // Verify loading indicator appears
            CommonUtils.waitForSeconds(1);
            softAssert.assertTrue(rangeArchitecturePage.isUploadInProgress(),
                    "Loading indicator should be displayed during upload");

            // Check if filename is displayed in the loading area
            if (rangeArchitecturePage.isSelectedFileDisplayed()) {
                String displayedInfo = rangeArchitecturePage.getSelectedFileName();
                softAssert.assertTrue(displayedInfo.contains(fileName) ||
                                displayedInfo.contains(fileName.substring(0, fileName.lastIndexOf('.'))),
                        "Filename should be displayed during loading. Displayed: " + displayedInfo);
            }

            // Wait for upload to complete
            boolean uploadCompleted = rangeArchitecturePage.waitForUploadCompletion(30);
            softAssert.assertTrue(uploadCompleted, "Upload should complete within timeout");

            // Verify loading indicator disappears after upload
            softAssert.assertFalse(rangeArchitecturePage.isUploadInProgress(),
                    "Loading indicator should disappear after upload completion");

            ExtentTestManager.getTest().log(Status.PASS,
                    "Loading indicator correctly displayed file details during upload");
            CommonUtils.takeScreenshot(driver, "loading_indicator_file_details");

        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL,
                    "Failed to test loading indicator: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "loading_indicator_test_error");
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

    @Test(priority = 4, description = "Verify comprehensive file upload validation workflow")
    public void testComprehensiveFileUploadValidation() {
        SoftAssert softAssert = new SoftAssert();

        try {
            ExtentTestManager.getTest().log(Status.INFO,
                    "Testing comprehensive file upload validation workflow");

            // Test 1: Valid XLSX file
            testFileUploadScenario(VALID_XLSX_FILE, true, "Valid XLSX file", softAssert);

            // Test 2: Valid CSV file
            testFileUploadScenario(VALID_CSV_FILE, true, "Valid CSV file", softAssert);

            // Test 3: Invalid TXT file
            testFileUploadScenario(INVALID_TXT_FILE, false, "Invalid TXT file", softAssert);

            // Test 4: Invalid PDF file
            testFileUploadScenario(INVALID_PDF_FILE, false, "Invalid PDF file", softAssert);

            ExtentTestManager.getTest().log(Status.PASS,
                    "Comprehensive file upload validation completed successfully");

        } catch (Exception e) {
            ExtentTestManager.getTest().log(Status.FAIL,
                    "Comprehensive validation failed: " + e.getMessage());
            softAssert.fail("Test failed: " + e.getMessage());
        }

        softAssert.assertAll();
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

    @Test(priority = 1, description = "P1 - Verify Download Sample Multiplier Excel format and headers")
    public void testDownloadSampleMultiplierExcelFormat() throws Exception {
        ExtentTest test = ExtentTestManager.getTest();
        SoftAssert softAssert = new SoftAssert();

        try {
            // Step 1: Verify Download Sample Multiplier button is displayed and clickable
            test.log(Status.INFO, "Verifying Download Sample Multiplier button is displayed and clickable");
            //  softAssert.assertTrue(selectedFileName.contains("valid_test.xlsx"),
            softAssert.assertTrue(rangeArchitecturePage.downloadSampleMultiplierLink.isDisplayed(),
                    "Download Sample Multiplier button should be displayed");
            softAssert.assertTrue(rangeArchitecturePage.downloadSampleMultiplierLink.isEnabled(),
                    "Download Sample Multiplier button should be clickable");
            CommonUtils.takeScreenshot(driver, "Download_Sample_Multiplier_Button_Displayed");

            // Step 2: Click Download Sample Multiplier button and get file path
            test.log(Status.INFO, "Clicking Download Sample Multiplier button and retrieving file path");
            String downloadedFile = triggerDownloadSampleMultiplierAndGetFilePath();
            softAssert.assertNotNull(downloadedFile, "Downloaded file path should not be null");
            test.log(Status.PASS, "Downloaded file at: " + downloadedFile);

            // Step 3: Verify file format is Excel
            test.log(Status.INFO, "Verifying downloaded file is in Excel format");
            softAssert.assertTrue(ExcelUtils.isValidExcelFile(downloadedFile),
                    "Downloaded file should be a valid Excel file");
            softAssert.assertTrue(downloadedFile.toLowerCase().endsWith(".xlsx"),
                    "Downloaded file should have .xlsx extension");

            // Step 4: Verify file naming convention
            test.log(Status.INFO, "Verifying file naming convention");
            String fileName = new File(downloadedFile).getName();
            softAssert.assertTrue(fileName.toLowerCase().contains("multiplier") ||
                            fileName.toLowerCase().contains("sample"),
                    "File name should contain 'multiplier' or 'sample': " + fileName);

            // Step 5: Verify column headers
            test.log(Status.INFO, "Verifying Excel column headers");
            softAssert.assertTrue(rangeArchitecturePage.validateMultiplierHeaders(downloadedFile),
                    "Excel should contain correct column headers: MRP Final, RA Min MRP, RA Max MRP, Minimum Cost, Maximum Cost, Brick Code, Enrichment");

            // Step 6: Verify data types are appropriate
            test.log(Status.INFO, "Verifying data types in Excel file");
            softAssert.assertTrue(rangeArchitecturePage.validateMultiplierDataTypes(downloadedFile),
                    "Data types should be appropriate as per product team requirements");

            // Step 7: Verify file contains sample data
            test.log(Status.INFO, "Verifying file contains appropriate sample data");
            List<Map<String, String>> data = ExcelUtils.readMultiplierData(downloadedFile);
            softAssert.assertTrue(data.size() > 0, "Excel file should contain sample data rows");

            test.log(Status.PASS, "Download Sample Multiplier Excel format validation completed successfully");
            CommonUtils.takeScreenshot(driver, "Download_Sample_Multiplier_Validation_Success");

        } catch (Exception e) {
            test.log(Status.FAIL, "Download Sample Multiplier Excel format validation failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "Download_Sample_Multiplier_Validation_Failed");
            throw e;
        } finally {
            softAssert.assertAll();
        }
    }

    @Test(priority = 1, description = "P1 - Verify Download Sample Multiplier success notification")
    public void testDownloadSampleMultiplierSuccessNotification() throws Exception {
        ExtentTest test = ExtentTestManager.getTest();
        SoftAssert softAssert = new SoftAssert();


        try {
            // Step 1: Click Download Sample Multiplier button
            test.log(Status.INFO, "Clicking Download Sample Multiplier button to verify success notification");
            rangeArchitecturePage.downloadSampleMultiplier();

            // Step 2: Wait for and verify success notification
            test.log(Status.INFO, "Verifying success notification appears");
            CommonUtils.waitForSeconds(10);
            //  CommonUtils.waitForVisibility(driver, 10); // Wait for notification
            // CommonUtils.waitForVisibility(driver, uploadSuccessfullMessage, 10);

            // Verify success message pop-up is displayed
            softAssert.assertTrue(rangeArchitecturePage.isDownloadSuccessMessageDisplayed(),
                    "Success message pop-up should be displayed after successful download");

            // Verify green tick (success icon) is displayed
            test.log(Status.INFO, "Verifying green tick (success icon) is displayed");
            softAssert.assertTrue(rangeArchitecturePage.isSuccessIconDisplayed(),
                    "Green tick (success icon) should be displayed for successful download");

            // Verify no error icon is displayed
            softAssert.assertFalse(rangeArchitecturePage.isErrorIconDisplayed(),
                    "Error icon (cross mark) should not be displayed for successful download");

            test.log(Status.PASS, "Download Sample Multiplier success notification validation completed");
            CommonUtils.takeScreenshot(driver, "Download_Sample_Multiplier_Success_Notification");

        } catch (Exception e) {
            test.log(Status.FAIL, "Download Sample Multiplier success notification validation failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "Download_Sample_Multiplier_Success_Notification_Failed");
            throw e;
        } finally {
            softAssert.assertAll();
        }
    }

    @Test(priority = 1, description = "P1 - Verify Download Sample Multiplier failure notification")
    public void testDownloadSampleMultiplierFailureNotification() throws Exception {
        ExtentTest test = ExtentTestManager.getTest();
        SoftAssert softAssert = new SoftAssert();


        try {
            // Step 1: Simulate download failure scenario
            test.log(Status.INFO, "Simulating download failure scenario");
            rangeArchitecturePage.simulateDownloadFailure(); // This method should be implemented to simulate failure

            // Step 2: Attempt to download sample multiplier
            test.log(Status.INFO, "Attempting to download Sample Multiplier during failure scenario");
            rangeArchitecturePage.downloadSampleMultiplier();

            // Step 3: Verify failure notification appears
            test.log(Status.INFO, "Verifying failure notification appears");
            // CommonUtils.waitForElementVisibility(driver, 10);
            CommonUtils.waitForSeconds(10);

            // Verify failure message pop-up is displayed
            softAssert.assertTrue(rangeArchitecturePage.isDownloadFailureMessageDisplayed(),
                    "Download failed message pop-up should be displayed when download fails");

            // Verify error icon (cross mark) is displayed
            test.log(Status.INFO, "Verifying cross mark (error icon) is displayed");
            softAssert.assertTrue(rangeArchitecturePage.isErrorIconDisplayed(),
                    "Cross mark (error icon) should be displayed for failed download");

            // Verify no success icon is displayed
            softAssert.assertFalse(rangeArchitecturePage.isSuccessIconDisplayed(),
                    "Success icon (green tick) should not be displayed for failed download");

            test.log(Status.PASS, "Download Sample Multiplier failure notification validation completed");
            CommonUtils.takeScreenshot(driver, "Download_Sample_Multiplier_Failure_Notification");

        } catch (Exception e) {
            test.log(Status.FAIL, "Download Sample Multiplier failure notification validation failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "Download_Sample_Multiplier_Failure_Notification_Failed");
            throw e;
        } finally {
            softAssert.assertAll();
        }
    }

    @Test(priority = 2, description = "P1 - Comprehensive Download Sample Multiplier validation")
    public void testComprehensiveDownloadSampleMultiplierValidation() throws Exception {
        ExtentTest test = ExtentTestManager.getTest();
        SoftAssert softAssert = new SoftAssert();


        try {
            // Step 1: Verify button functionality
            test.log(Status.INFO, "Starting comprehensive Download Sample Multiplier validation");
            softAssert.assertTrue(rangeArchitecturePage.downloadSampleMultiplierLink.isDisplayed(),
                    "Download Sample Multiplier button should be displayed");

            // Step 2: Download and validate file
            String downloadedFile = triggerDownloadSampleMultiplierAndGetFilePath();

            // Step 3: Comprehensive Excel validation
            test.log(Status.INFO, "Performing comprehensive Excel file validation");

            // Validate file format
            softAssert.assertTrue(ExcelUtils.isValidExcelFile(downloadedFile),
                    "File should be valid Excel format");

            // Validate headers
            softAssert.assertTrue(rangeArchitecturePage.validateMultiplierHeaders(downloadedFile),
                    "Headers should match expected format");

            // Validate data types
            softAssert.assertTrue(rangeArchitecturePage.validateMultiplierDataTypes(downloadedFile),
                    "Data types should be appropriate");

            // Validate data content
            List<Map<String, String>> data = ExcelUtils.readMultiplierData(downloadedFile);
            softAssert.assertTrue(ExcelUtils.validateMultiplierDataContent(data),
                    "Data content should be appropriate as per product team requirements");

            test.log(Status.PASS, "Comprehensive Download Sample Multiplier validation completed successfully");
            CommonUtils.takeScreenshot(driver, "Comprehensive_Download_Sample_Multiplier_Validation_Success");

        } catch (Exception e) {
            test.log(Status.FAIL, "Comprehensive Download Sample Multiplier validation failed: " + e.getMessage());
            CommonUtils.takeScreenshot(driver, "Comprehensive_Download_Sample_Multiplier_Validation_Failed");
            throw e;
        } finally {
            softAssert.assertAll();
        }
    }

    /**
     * Test Case: Network Error During File Upload with Resumption
     * Priority: P1
     * Browsers: Chrome, Firefox, Safari
     * <p>
     * Scenario: Verify the process when there is a network problem during file upload
     * Expected: File upload should resume once network restores
     */
    @Test(priority = 1, description = "Verify file upload resumes after network error")
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
     * <p>
     * Scenario: Verify that when user presses cancel button, the file is not uploaded
     * Expected: File should not be uploaded when cancel is pressed
     */
    @Test(priority = 2, description = "Verify file upload cancellation functionality")
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
     * <p>
     * Scenario: Verify that when user hits refresh, uploaded file is not present before saving
     * Expected: Uploaded file should not be present when user hits refresh before saving
     */
    @Test(priority = 3, description = "Verify file is not present after page refresh before saving")
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
     * <p>
     * Additional scenario to test multiple network interruptions and recovery
     */
    @Test(priority = 4, description = "Verify upload recovery after multiple network interruptions")
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

    // Helper Methods

    private String triggerDownloadSampleMultiplierAndGetFilePath() throws Exception {
        // Clear downloads folder before download
        String downloadDir = System.getProperty("user.home") + "/Downloads";

        // Get files before download
        File[] filesBefore = new File(downloadDir).listFiles((dir, name) ->
                name.toLowerCase().contains("multiplier") && name.toLowerCase().endsWith(".xlsx"));
        int filesCountBefore = filesBefore != null ? filesBefore.length : 0;

        // Trigger download
        rangeArchitecturePage.downloadSampleMultiplier();

        // Wait for download to complete
        Thread.sleep(5000);

        // Get files after download
        File[] filesAfter = new File(downloadDir).listFiles((dir, name) ->
                name.toLowerCase().contains("multiplier") && name.toLowerCase().endsWith(".xlsx"));

        if (filesAfter != null && filesAfter.length > filesCountBefore) {
            // Return the most recently downloaded file
            Arrays.sort(filesAfter, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
            return filesAfter[0].getAbsolutePath();
        }

        throw new Exception("Download Sample Multiplier file not found in downloads folder");
    }


    private void testFileUploadScenario(String filePath, boolean shouldSucceed,
                                        String scenarioDescription, SoftAssert softAssert) {
        try {
            ExtentTestManager.getTest().log(Status.INFO, "Testing: " + scenarioDescription);

            // Navigate to upload section
            rangeArchitecturePage.clickUploadGlobalRA();
            CommonUtils.waitForSeconds(2);

            // Upload file
            rangeArchitecturePage.uploadRAFile(filePath);
            CommonUtils.waitForSeconds(3);

            if (shouldSucceed) {
                // Verify successful upload
                softAssert.assertTrue(rangeArchitecturePage.isSelectedFileDisplayed(),
                        scenarioDescription + ": Selected file should be displayed");
                softAssert.assertFalse(rangeArchitecturePage.isUploadFailureMessageDisplayed(),
                        scenarioDescription + ": No error message should be displayed");

                // Complete upload
                rangeArchitecturePage.clickContinue();
                CommonUtils.waitForSeconds(2);

            } else {
                // Verify error for invalid file
                softAssert.assertTrue(rangeArchitecturePage.isUploadFailureMessageDisplayed(),
                        scenarioDescription + ": Error message should be displayed");

                String errorMessage = rangeArchitecturePage.getUploadFailureMessage();
                softAssert.assertTrue(errorMessage.contains("File Format not supported") ||
                                errorMessage.contains("Invalid format"),
                        scenarioDescription + ": Should show format not supported error");
            }

            // Close modal
            if (rangeArchitecturePage.isCancelButtonDisplayed()) {
                rangeArchitecturePage.clickCancelButton();
            }
            CommonUtils.waitForSeconds(1);

        } catch (Exception e) {
            softAssert.fail(scenarioDescription + " failed: " + e.getMessage());
        }
    }

    /**
     * Helper method to format file size in human readable format
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    /**
     * Data provider for valid file formats
     */
    @DataProvider(name = "validFileFormats")
    public Object[][] validFileFormats() {
        return new Object[][]{
                {VALID_XLSX_FILE, "xlsx", "Excel file"},
                {VALID_CSV_FILE, "csv", "CSV file"}
        };
    }

    /**
     * Data provider for invalid file formats
     */
    @DataProvider(name = "invalidFileFormats")
    public Object[][] invalidFileFormats() {
        return new Object[][]{
                {INVALID_TXT_FILE, "txt", "Text file"},
                {INVALID_PDF_FILE, "pdf", "PDF file"},
                {INVALID_DOC_FILE, "doc", "Word document"}
        };
    }
}
