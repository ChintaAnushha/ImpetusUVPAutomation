package com.impetus.pages;

import com.impetus.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.impetus.utils.CommonUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class RangeArchitecturePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String[] EXPECTED_MULTIPLIER_HEADERS = {
            "MRP Final", "RA Min MRP", "RA Max MRP", "Minimum Cost",
            "Maximum Cost", "Brick Code", "Enrichment"
    };

    @FindBy(xpath = "//span[text()='Range Architecture']")
    public WebElement rangeArchitectureLink;

    @FindBy(xpath = "//p[text()='Range Architecture']")
    public WebElement rangeArchitecturePage;

    @FindBy(xpath = "//p[contains(text(),'RA-')]")
    public WebElement raNumber;

    @FindBy(xpath = "//span[contains(text(), 'Current Global RA')]")
    public WebElement currentGlobalRATab;

    @FindBy(xpath = "//span[contains(text(), 'Current Cluster RA')]")
    public WebElement currentClusterRATab;

    @FindBy(xpath = "//span[contains(text(), 'Old Global RA')]")
    public WebElement oldGlobalRATab;

    @FindBy(xpath = "//span[contains(text(), 'Old Cluster RA')]")
    public WebElement oldClusterRATab;

    @FindBy(xpath = "//input[@type='text' and @placeholder]")
    public WebElement searchInput;

    @FindBy(xpath = "//table//tbody/tr")
    public List<WebElement> tableRows;

    @FindBy(xpath = "//button[@class='n-button ripple n-button-rounded n-button-secondary n-button-mid null']//div")
    // @FindBy(xpath = "//button[@data-testid='filter-button-toggle']")
    public WebElement filterButton;
    // Table elements
    @FindBy(xpath = "//span[text()='Family']")
    public WebElement familyHeader;

    @FindBy(xpath = "//span[text()='Class Name']")
    public WebElement classNameHeader;

    @FindBy(xpath = "//span[text()='Brick Name']")
    public WebElement brickNameHeader;

    @FindBy(xpath = "//span[text()='Top Brick']")
    public WebElement topBrickHeader;

    @FindBy(xpath = "//span[text()='Brick']")
    public WebElement brickHeader;

    @FindBy(xpath = "//span[text()='Enrichment']")
    public WebElement enrichmentHeader;

    @FindBy(xpath = "//span[text()='MRP Range']")
    public WebElement mrpRangeHeader;

    @FindBy(xpath = "//span[text()='Options']")
    public WebElement optionsHeader;

    @FindBy(xpath = "//th[@role='columnheader']//span[text()='ODM']")
    public WebElement odmHeader;

    @FindBy(xpath = "//th[@role='columnheader']//span[text()='OEM']")
    public WebElement oemHeader;

    @FindBy(xpath = "//span[text()='Total']")
    public WebElement totalHeader;

    @FindBy(xpath = "//span[text()='Fill Rate']")
    public WebElement fillRateHeader;

    @FindBy(xpath = "//span[text()='Action']")
    public WebElement actionHeader;

    @FindBy(xpath = "//div[contains(text(),'Upload Global RA')]")
    public WebElement uploadGlobalRAButton;

    @FindBy(xpath = "//div[contains(text(),'Upload Cluster RA')]")
    public WebElement uploadClusterRAButton;

    // Pagination elements
    @FindBy(xpath = "//span[contains(text(), '1-25 of')]")
    public WebElement paginationRange;

    @FindBy(xpath = "//table//tbody/tr[1]")
    public WebElement firstTableRow;

    @FindBy(xpath = "//div[@class='n-dropdown-input-arrow-wrapper']")
    public WebElement rowsPerPageDropdown;

    @FindBy(xpath = "//div[@data-testid='btnPrevious']")
    public WebElement previousPageButton;

    @FindBy(xpath = "//div[@data-testid='btnNext']")
    public WebElement nextPageButton;

    @FindBy(xpath = "//span[@class='n-pagination__count']")
    public WebElement totalPagesInfo;

    @FindBy(xpath = "//input[@type='text' and @aria-label='Go to page']")
    public WebElement pageNumberInput;

    @FindBy(xpath = "//div[contains(@class, 'pagination')]//span[contains(text(), ' of ')]")
    public WebElement paginationInfo;

//    @FindBy(xpath = "//button[text()='Upload Global RA']")
//    private WebElement uploadGlobalRAButton;

    @FindBy(xpath = "//span[contains(text(),'Upload Global RA')]")
    public WebElement uploadModalTitle;

    // Upload RA Section
    @FindBy(xpath = "//p[contains(text(),'Upload RA')]/following-sibling::div")
    public WebElement uploadRAInput;

    @FindBy(xpath = "//p[contains(text(),'Upload RA')]/following-sibling::div//div[@class='n-button-content' and contains(text(),'Upload File')]")
    public WebElement uploadRAButton;

    @FindBy(xpath = "//p[contains(text(),'MRP Multiplier File')]/following-sibling::div//div[@class='n-button-content' and contains(text(),'Upload File')]")
    public WebElement uploadMRPMultiplierFileButton;

    @FindBy(xpath = "//div[@class='n-button-content' and contains(text(),'Download Sample RA')]")
    public WebElement downloadSampleRALink;

    // MRP Multiplier Section
    @FindBy(xpath = "//p[text()='MRP Multiplier File']/following-sibling::div")
    public WebElement mrpMultiplierInput;

    @FindBy(xpath = "//p[text()='MRP Multiplier File']/following-sibling::div//div[@class='n-button-content' and contains(text(),'Upload File')] ")
    public WebElement uploadMRPButton;

    @FindBy(xpath = "//span[contains(text(),'Download')]")
    public WebElement downloadButton;

    @FindBy(xpath = "//div[contains(text(),'Download Successful')]")
    public WebElement downloadSuccessful;

    @FindBy(xpath = "//div[@class='n-button-content' and contains(text(),'Download Sample Multiplier')]")
    public WebElement downloadSampleMultiplierLink;

    // Action Buttons
    @FindBy(xpath = "//div[contains(text(),'Cancel')]")
    public WebElement cancelButton;

    @FindBy(xpath = "//div[contains(text(),'Continue')]")
    public WebElement continueButton;

    @FindBy(xpath = "//thead//tr[@role='row']")
    public List<WebElement> tableHeaders;

    @FindBy(xpath = "//p[contains(text(),'.xlsx')]")
    public WebElement uploadFileSheetName;

    @FindBy(xpath = "//span[contains(text(),'MRP Multiplier')]")
    public WebElement mrpMultiplierSideNavigationButton;

    @FindBy(xpath = "(//div[contains(text(),'MRP Multiplier')])[1]")
    public WebElement mrpMutliplierHeader;

    @FindBy(xpath = "//div[contains(text(),'Upload Successful')]")
    public WebElement uploadSuccessfullMessage;

    // Filter Elements
//    @FindBy(xpath = "//button[contains(@class, 'filter-button') or contains(text(), 'Filter')]")
//    private WebElement filterButton;

    @FindBy(xpath = "(//div[@class='n-input-container n-border-focused  ']//input[@type='search' or contains(@placeholder, 'Search')])[2]")
    public WebElement filtersearchBar;

    @FindBy(xpath = "//p[contains(text(), 'Clear All')]")
    public WebElement clearAllButton;

    // Filter Dropdown Headers
    @FindBy(xpath = "//div[contains(text(), 'Family')]")
    public WebElement familyFilter;

    @FindBy(xpath = "//div[contains(text(), 'Class Name')]")
    public WebElement classNameFilter;

    @FindBy(xpath = "//div[contains(text(), 'Brick Name')]")
    public WebElement brickNameFilter;

    @FindBy(xpath = "//div[contains(text(), 'Top Brick')]")
    public WebElement topBrickFilter;

    @FindBy(xpath = "(//div[contains(text(), 'Brick')])[3]")
    public WebElement brickFilter;

    @FindBy(xpath = "//div[contains(text(), 'Enrichment')]")
    public WebElement enrichmentFilter;

    // Filter dropdown elements
   // @FindBy(xpath = "//div[contains(@class, 'filter-dropdown-content')]")
    @FindBy(xpath = "//div[contains(text(), 'Family')]")
    public WebElement filterDropdownContent;

//    @FindBy(xpath = "//div[contains(@class, 'filter-dropdown-content')]//input[@type='search']")
//    private WebElement filterSearchBox;

    @FindBy(xpath = "//div[contains(text(), 'Clear')]")
    public WebElement filterClearButton;

    // @FindBy(xpath = "//div[contains(@class, 'filter-dropdown-content')]//div[contains(@class, 'scrollable-list')]")
    @FindBy(xpath = "//div[@class='sc-kbdlSk hlCwks']")
    public WebElement scrollableList;

    // @FindBy(xpath = "//div[contains(@class, 'filter-dropdown-content')]//input[@type='checkbox']")
    @FindBy(xpath = "//label[contains(@class, 'n-checkbox-container')]//input[@type='checkbox']")
    public List<WebElement> filterCheckboxes;

   // @FindBy(xpath = "//div[contains(@class, 'filter-dropdown-content')]//label")
    @FindBy(xpath = "//label[contains(@class, 'n-checkbox-container')]")
    public List<WebElement> filterOptions;

    // Table headers that act as filters
    //  @FindBy(xpath = "//th[contains(@class, 'filterable-header')]")
    @FindBy(xpath = "//div[@data-testid='filter-container']")
    public List<WebElement> filterableHeaders;

    // Sort buttons
    @FindBy(xpath = "//button[contains(@class, 'sort-button')]")
    public List<WebElement> sortButtons;

    @FindBy(xpath = "//span[contains(text(),'ODM')]")
    public WebElement odmTab;

    @FindBy(xpath = "//p[contains(text(),'Back') or contains(@class,'back-btn')]")
    public WebElement backButton;

    @FindBy(xpath = "//p[contains(text(),'RA-')]")
    public WebElement raidDisplayElement;

    @FindBy(xpath = "//input[contains(@placeholder,'Search') or contains(@class,'search')]")
    public WebElement searchBox;

    @FindBy(xpath = "//div[contains(text(),'Filter') or contains(@class,'n-button-content')]")
    public WebElement filterButtonInOdmTab;

    @FindBy(xpath = "//div[@aria-label='Scrollable Tab Container']")
    public WebElement tabsContainer;

    @FindBy(xpath = "//li[contains(@class,'n-tab-item n-tab-active')]")
    public WebElement activeOdmTab;

    @FindBy(xpath = "//table[contains(@role,'table')]")
    public WebElement tableInOdmTab;

    @FindBy(xpath = "//table//tbody//tr")
    public List<WebElement> raidRows;

   // @FindBy(xpath = "//th[contains(text(),'Action')]")
    @FindBy(xpath = "//table//th//span[normalize-space(text())='Action']")
    public WebElement actionColumnHeader;

   // @FindBy(xpath = "//a[contains(text(),'View') or contains(@class,'view-btn')]")
    @FindBy(xpath = "//span[contains(text(),'View') or contains(@class,'view-btn')]")
    public List<WebElement> viewButtons;

    // Row data verification
    public String rowDataXPath = "//tr[contains(@role, 'row') and contains(@style, 'opacity: 1')]";

    // Cluster field elements
    @FindBy(xpath = "//div[contains(@class,'cluster-field') or contains(@class,'cluster-dropdown')]")
    public WebElement clusterField;

    @FindBy(xpath = "//select[contains(@name,'cluster') or contains(@id,'cluster')] | //div[contains(@class,'cluster-select')]")
    public WebElement clusterDropdown;

    @FindBy(xpath = "//div[contains(@class,'cluster')]//span[contains(@class,'arrow') or contains(@class,'dropdown-arrow')]")
    public WebElement dropdownArrow;

    @FindBy(xpath = "//div[contains(@class,'cluster')]//input[contains(@placeholder,'Select') or contains(@placeholder,'cluster')]")
    public WebElement clusterInput;

    @FindBy(xpath = "//div[contains(@class,'cluster-options') or contains(@class,'dropdown-menu')]")
    public WebElement clusterDropdownMenu;

    @FindBy(xpath = "//div[contains(@class,'cluster-options')]//div[contains(@class,'option')] | //ul[contains(@class,'cluster-list')]//li")
    public List<WebElement> clusterOptions;

    @FindBy(xpath = "//input[contains(@placeholder,'Search cluster') or contains(@class,'cluster-search')]")
    public WebElement clusterSearchBox;

    @FindBy(xpath = "//div[contains(@class,'cluster')]//button[contains(@class,'clear') or contains(@class,'close')] | //span[contains(@class,'clear-icon')]")
    public WebElement clearButton;

    @FindBy(xpath = "//div[contains(@class,'cluster-dropdown')]//div[contains(@class,'scrollbar')] | //div[contains(@class,'cluster-options')][contains(@style,'overflow')]")
    public WebElement scrollBar;

    // Filter section
    @FindBy(xpath = "//div[contains(@class,'filters') or contains(@class,'filter-section')]")
    public WebElement filtersSection;

    // Listing content
    @FindBy(xpath = "//table//tbody//tr | //div[contains(@class,'listing-content')]//div[contains(@class,'item')]")
    public List<WebElement> listingItems;

    @FindBy(xpath = "//div[contains(@class,'no-data') or contains(text(),'No data found') or contains(text(),'No results')]")
    public WebElement noDataMessage;

    @FindBy(xpath = "//div[contains(@class,'loading') or contains(@class,'spinner')]")
    public WebElement loadingIndicator;

    // Modal elements
    @FindBy(xpath = "//div[contains(@class, 'modal') or contains(@class, 'dialog')]")
    public WebElement modal;

    @FindBy(xpath = "//button[contains(@class, 'close') or @aria-label='Close' or text()='×']")
    public WebElement closeButton;

    @FindBy(xpath = "//h1[text()='Upload Cluster RA'] | //h2[text()='Upload Cluster RA'] | //div[text()='Upload Cluster RA']")
    public WebElement modalTitle;

    // Select Cluster elements
    @FindBy(xpath = "//label[contains(text(), 'Select Cluster')]")
    public WebElement selectClusterLabel;

    @FindBy(xpath = "//label[contains(text(), 'Select Cluster')]//*[text()='*'] | //label[contains(text(), 'Select Cluster*')]")
    public WebElement selectClusterAsterisk;

    @FindBy(xpath = "//input[@placeholder='Search Clusters'] | //div[contains(@class, 'dropdown') and contains(., 'Search Clusters')]")
    public WebElement selectClusterDropdown;

//    @FindBy(xpath = "//div[contains(@class, 'dropdown-arrow') or contains(@class, 'arrow')] | //svg[contains(@class, 'arrow')]")
//    private WebElement dropdownArrow;

    @FindBy(xpath = "//input[@placeholder='Search Clusters']")
    public WebElement searchClustersInput;

    @FindBy(xpath = "//div[contains(@class, 'dropdown-list') or contains(@class, 'options')] | //ul[contains(@class, 'dropdown')]")
    public WebElement dropdownList;

//    @FindBy(xpath = "//div[contains(@class, 'scroll') or contains(@class, 'scrollbar')]")
//    private WebElement scrollBar;

    @FindBy(xpath = "//div[text()='Ahmedabad'] | //li[text()='Ahmedabad'] | //option[text()='Ahmedabad']")
    public WebElement ahmedabadOption;

    @FindBy(xpath = "//div[text()='Bengaluru'] | //li[text()='Bengaluru'] | //option[text()='Bengaluru']")
    public WebElement bengaluruOption;

    @FindBy(xpath = "//div[text()='Bangladesh'] | //li[text()='Bangladesh'] | //option[text()='Bangladesh']")
    public WebElement bangladeshOption;

    @FindBy(xpath = "//div[text()='Jaipur'] | //li[text()='Jaipur'] | //option[text()='Jaipur']")
    public WebElement jaipurOption;

    // Upload RA elements
    @FindBy(xpath = "//label[contains(text(), 'Upload RA')]")
    public WebElement uploadRALabel;

    @FindBy(xpath = "//label[contains(text(), 'Upload RA')]//*[text()='*'] | //label[contains(text(), 'Upload RA*')]")
    public WebElement uploadRAAsterisk;

    // Add failure message element
    @FindBy(xpath = "//div[contains(text(),'Download Failed') or contains(text(),'Error') or contains(text(),'Failed')]")
    public WebElement downloadFailureMessage;

    @FindBy(xpath = "//div[contains(@class,'success') or contains(@class,'tick') or contains(@class,'check')]")
    public WebElement successIcon;

    @FindBy(xpath = "//div[contains(@class,'error') or contains(@class,'cross') or contains(@class,'x')]")
    public WebElement errorIcon;

    // Action buttons
//    @FindBy(xpath = "//button[text()='Cancel']")
//    private WebElement cancelButton;

//    @FindBy(xpath = "//button[text()='Continue']")
//    private WebElement continueButton;

    // Add new elements for upload timeout handling
    @FindBy(xpath = "//div[contains(text(),'Upload failed') or contains(text(),'Upload Failed') or contains(text(),'Timeout') or contains(text(),'Error uploading')]")
    public WebElement uploadFailureMessage;

    @FindBy(xpath = "//input[@type='file' or @accept or contains(@class,'file-input')]")
    public WebElement fileInput;

    @FindBy(xpath = "//div[contains(@class,'file-name') or contains(@class,'selected-file') or contains(text(),'Selected file:')]")
    public WebElement selectedFileDisplay;

    @FindBy(xpath = "//div[contains(@class,'loading') or contains(@class,'spinner') or contains(text(),'Uploading')]")
    public WebElement uploadLoadingIndicator;


    public RangeArchitecturePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

//    public void clickRangeArchitecture() {
//        CommonUtils.waitForElementToBeClickable(driver, rangeArchitectureLink);
//        rangeArchitectureLink.click();
//    }

    public boolean isRANumberDisplayed() {
        return raNumber.isDisplayed() && raNumber.getText().contains("RA-");
    }

    public void switchToCurrentGlobalRA() {
        currentGlobalRATab.click();
        CommonUtils.waitForPageLoad(driver);
    }

    public void switchToCurrentClusterRA() {
        currentClusterRATab.click();
        CommonUtils.waitForPageLoad(driver);
    }

    public void switchToOldGlobalRA() {
        oldGlobalRATab.click();
        CommonUtils.waitForPageLoad(driver);
    }

    public void switchToOldClusterRA() {
        oldClusterRATab.click();
        CommonUtils.waitForPageLoad(driver);
    }

    public boolean verifyTableHeaders() {
        return familyHeader.isDisplayed() &&
                classNameHeader.isDisplayed() &&
                brickNameHeader.isDisplayed() && topBrickHeader.isDisplayed() && brickHeader.isDisplayed() &&
                enrichmentHeader.isDisplayed() && mrpRangeHeader.isDisplayed() && optionsHeader.isDisplayed() &&
                odmHeader.isDisplayed() && oemHeader.isDisplayed() && totalHeader.isDisplayed() &&
                fillRateHeader.isDisplayed() && actionHeader.isDisplayed();
    }

    public boolean verifyRowData(String family, String className, String brickName, String topBrick,
                                 String brick, String enrichment, String mrpRange, String options,
                                 String odmQuantity, String oemQuantity, String totalQty, String fillRate, String action) {

        return driver.findElement(By.xpath(String.format(rowDataXPath, family))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, className))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, brickName))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, topBrick))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, brick))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, enrichment))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, mrpRange))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, options))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, odmQuantity))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, oemQuantity))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, totalQty))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, fillRate))).isDisplayed() &&
                driver.findElement(By.xpath(String.format(rowDataXPath, action))).isDisplayed();
    }

    public boolean validateAllHeaders() {
        Map<String, String> headerValidations = new HashMap<>();
        headerValidations.put("Family", "^[A-Za-z\\s&-]+$");
        headerValidations.put("Class Name", "^[A-Za-z\\s&-]+$");
        headerValidations.put("Brick Name", "^[A-Za-z0-9\\s&-]+$");
        headerValidations.put("Top Brick", "^[A-Za-z0-9\\s&-]+$");
        headerValidations.put("Brick", "^[A-Za-z0-9\\s&-]+$");
        headerValidations.put("Enrichment", "^[A-Za-z\\s&-]+$");
        // Updated pattern to match actual UI format: "300 - 400" without ₹
        headerValidations.put("MRP Range", "^\\d+\\s*-\\s*\\d+$");
        headerValidations.put("Options", "^\\d+$");
        headerValidations.put("ODM Quantity", "^\\d+$");
        headerValidations.put("OEM Quantity", "^\\d+$");
        headerValidations.put("Total Qty", "^\\d+$");
        // Updated pattern to match actual UI format: "0 %" with space
        headerValidations.put("Fill Rate", "^\\d+\\s*%$");
        headerValidations.put("Action", "^[A-Za-z\\s]+$");

        // Rest of the validation logic remains the same
        boolean isValid = true;
        List<WebElement> rows = driver.findElements(By.xpath("//table//tbody/tr"));

        for (WebElement row : rows) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put("Family", row.findElement(By.xpath(".//td[1]")).getText().trim());
            rowData.put("Class Name", row.findElement(By.xpath(".//td[2]")).getText().trim());
            rowData.put("Brick Name", row.findElement(By.xpath(".//td[3]")).getText().trim());
            rowData.put("Top Brick", row.findElement(By.xpath(".//td[4]")).getText().trim());
            rowData.put("Brick", row.findElement(By.xpath(".//td[5]")).getText().trim());
            rowData.put("Enrichment", row.findElement(By.xpath(".//td[6]")).getText().trim());
            rowData.put("MRP Range", row.findElement(By.xpath(".//td[7]")).getText().trim());
            rowData.put("Options", row.findElement(By.xpath(".//td[8]")).getText().trim());
            rowData.put("ODM Quantity", row.findElement(By.xpath(".//td[9]")).getText().trim());
            rowData.put("OEM Quantity", row.findElement(By.xpath(".//td[10]")).getText().trim());
            rowData.put("Total Qty", row.findElement(By.xpath(".//td[11]")).getText().trim());
            rowData.put("Fill Rate", row.findElement(By.xpath(".//td[12]")).getText().trim());
            rowData.put("Action", row.findElement(By.xpath(".//td[13]")).getText().trim());

            for (Map.Entry<String, String> entry : headerValidations.entrySet()) {
                String headerName = entry.getKey();
                String pattern = entry.getValue();
                String value = rowData.get(headerName);

                if (value.isEmpty()) {
                    System.out.println("❌ Empty value found for " + headerName);
                    isValid = false;
                    continue;
                }

                if (!value.matches(pattern)) {
                    System.out.println("❌ Invalid format for " + headerName + ": '" + value + "'");
                    isValid = false;
                }
            }
        }

        if (isValid) {
            System.out.println("✅ All headers and their data formats are valid");
        }

        return isValid;
    }

    public boolean verifySearchBarFunctionality() {
        try {
            // Wait for search bar to be visible
            CommonUtils.waitForVisibility(driver, searchInput, 20);

            // Verify if search bar is displayed
            if (!searchInput.isDisplayed()) {
                System.out.println("❌ Search bar is not displayed");
                return false;
            }

            // Verify if search bar is enabled
            if (!searchInput.isEnabled()) {
                System.out.println("❌ Search bar is not enabled");
                return false;
            }

            // Try to click the search bar
            searchInput.click();

            // Verify if search bar is focused after clicking
            WebElement focusedElement = driver.switchTo().activeElement();
            boolean isSearchBarFocused = focusedElement.equals(searchInput);

            if (!isSearchBarFocused) {
                System.out.println("❌ Search bar is not focusable");
                return false;
            }

            System.out.println("✅ Search bar is clickable and functional");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error verifying search bar: " + e.getMessage());
            return false;
        }
    }

    public boolean verifySearchBarPresence() {
        try {
            CommonUtils.waitForVisibility(driver, searchInput, 20);
            return searchInput.isDisplayed() && searchInput.isEnabled();
        } catch (Exception e) {
            System.out.println("❌ Search bar verification failed: " + e.getMessage());
            return false;
        }
    }

    public boolean enterSearchText(String searchText) {
        try {
            // Wait for search bar to be interactive
            CommonUtils.waitForVisibility(driver, searchInput, 20);
            CommonUtils.waitForElementToBeClickable(driver, searchInput);
            // Clear existing text and enter new text
            searchInput.clear();
            searchInput.sendKeys(searchText);
            // Verify if text was entered correctly
            String actualText = searchInput.getAttribute("value");
            assert actualText != null;
            if (!actualText.equals(searchText)) {
                System.out.println("❌ Search text mismatch - Expected: '" + searchText + "', Actual: '" + actualText + "'");
                return false;
            }

            // Add small delay to allow search to process
            Thread.sleep(500);

            System.out.println("✅ Successfully entered search text: " + searchText);
            return true;
        } catch (ElementNotInteractableException e) {
            System.out.println("❌ Search bar is not interactable: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("❌ Error entering search text: " + e.getMessage());
            return false;
        }
    }

    public boolean verifySearchResults(String searchText) {
        try {
            Thread.sleep(1000); // Wait for search results
            boolean foundMatch = false;

            for (WebElement row : tableRows) {
                if (!row.isDisplayed()) continue;

                List<WebElement> cells = row.findElements(By.tagName("td"));
                boolean rowContainsText = false;

                for (WebElement cell : cells) {
                    if (cell.getText().toLowerCase().contains(searchText.toLowerCase())) {
                        rowContainsText = true;
                        CommonUtils.highlightElement(driver, row, "#FFEB3B");
                        break;
                    }
                }

                if (rowContainsText) {
                    foundMatch = true;
                }
            }

            return foundMatch;
        } catch (Exception e) {
            System.out.println("❌ Search results verification failed: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyPartialSearch(String partialText) {
        try {
            enterSearchText(partialText);
            Thread.sleep(1000);

            int matchCount = 0;
            for (WebElement row : tableRows) {
                if (!row.isDisplayed()) continue;

                if (row.getText().toLowerCase().contains(partialText.toLowerCase())) {
                    matchCount++;
                    CommonUtils.highlightElement(driver, row, "#FFEB3B");
                }
            }

            System.out.println("✅ Found " + matchCount + " matches for partial search: " + partialText);
            return matchCount > 0;
        } catch (Exception e) {
            System.out.println("❌ Partial search verification failed: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyCategories(String category) {
        return driver.findElement(By.xpath("//div[contains(text(),'" + category + "']")).isDisplayed();
    }

    public boolean isPageLoaded() {
        CommonUtils.waitForPageLoad(driver);
        return rangeArchitectureLink.isDisplayed() && verifyTableHeaders();
    }


    public boolean isRangeArchitectureDisplayed() {
        CommonUtils.waitForPageLoad(driver);
        return rangeArchitecturePage.isDisplayed();
    }

    public boolean isCurrentClusterRADisplayed() {
        CommonUtils.waitForVisibility(driver, currentClusterRATab, 5);
        return currentClusterRATab.isDisplayed();
    }

    public boolean isCurrentGlobalRADisplayed() {
        CommonUtils.waitForVisibility(driver, currentGlobalRATab, 5);
        return currentGlobalRATab.isDisplayed();
    }


    public boolean isOldClusterRADisplayed() {
        CommonUtils.waitForVisibility(driver, oldClusterRATab, 5);
        return oldClusterRATab.isDisplayed();
    }

    public boolean isOldGlobalRADisplayed() {
        CommonUtils.waitForVisibility(driver, oldClusterRATab, 5);
        return oldGlobalRATab.isDisplayed();
    }

    public boolean isSearchInputTextBoxDisplayed() {
        CommonUtils.waitForVisibility(driver, searchInput, 5);
        return searchInput.isDisplayed();
    }

    public boolean isFilterButtonDisplayed() {
        CommonUtils.waitForVisibility(driver, filterButton, 5);
        return filterButton.isDisplayed();
    }

    public boolean isDownloadButtonDisplayed() {
        CommonUtils.waitForVisibility(driver, downloadButton, 5);
        return downloadButton.isDisplayed();
    }

    public boolean isDownloadSuccessMessageDisplayed() {
        CommonUtils.waitForVisibility(driver, downloadSuccessful, 5);
        return downloadSuccessful.isDisplayed();
    }

    public void selectRowsPerPage(String value) {
        Select select = new Select(rowsPerPageDropdown);
        select.selectByValue(value);
        CommonUtils.waitForPageLoad(driver);
    }

    public void goToNextPage() {
        if (nextPageButton.isEnabled()) {
            nextPageButton.click();
            CommonUtils.waitForPageLoad(driver);
        }
    }

    public void goToPreviousPage() {
        if (previousPageButton.isEnabled()) {
            previousPageButton.click();
            CommonUtils.waitForPageLoad(driver);
        }
    }

    public void goToPage(String pageNumber) {
        pageNumberInput.clear();
        pageNumberInput.sendKeys(pageNumber + Keys.ENTER);
        CommonUtils.waitForPageLoad(driver);
    }

    public String getCurrentPageInfo() {
        return totalPagesInfo.getText();
    }

    public boolean isNextPageEnabled() {
        return nextPageButton.isEnabled();
    }

    public boolean isPreviousPageEnabled() {
        return previousPageButton.isEnabled();
    }

    public boolean verifyPaginationRange() {
        try {
            CommonUtils.waitForVisibility(driver, paginationRange, 10);
            String paginationText = paginationInfo.getText(); // e.g., "1-25 of 309"
            String[] parts = paginationText.split(" of ");
            String[] range = parts[0].split("-");
            int startRow = Integer.parseInt(range[0]);
            int endRow = Integer.parseInt(range[1]);
            int totalRows = Integer.parseInt(parts[1]);

            boolean isValid = startRow == 1 && endRow <= 25 && totalRows > 0;
            System.out.println("Pagination range verified: " + paginationText);
            return isValid;
        } catch (Exception e) {
            System.out.println("❌ Error verifying pagination range: " + e.getMessage());
            return false;
        }
//            if (!isValid) {
//                System.out.println("❌ Invalid pagination range: " + range);
//                return false;
//            }
//
//            System.out.println("✅ Pagination range verified: " + range);
//            return true;
//        } catch (Exception e) {
//            System.out.println("❌ Error verifying pagination range: " + e.getMessage());
//            return false;
//        }
    }

    public boolean verifyNextPageNavigation() {
        try {
            CommonUtils.waitForVisibility(driver, paginationInfo, 20);
            String beforeNavigation = paginationInfo.getText();

            if (!nextPageButton.isEnabled()) {
                System.out.println("❌ Next button is not enabled");
                return false;
            }
            //   String initialRange = paginationRange.getText();
            CommonUtils.waitForVisibility(driver, nextPageButton, 10);
            CommonUtils.waitForElementToBeClickable(driver, nextPageButton);
            nextPageButton.click();
            CommonUtils.waitForPageLoad(driver);
            Thread.sleep(1000);

            // Wait for pagination info to update
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBe(
                            By.xpath("//div[contains(@class, 'pagination')]//span[contains(text(), ' of ')]"),
                            beforeNavigation
                    )
            ));
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error during next page navigation: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, String>> getTableDataForPage() {
        try {
            CommonUtils.waitForVisibility(driver, tableRows.get(0), 20);
            return CommonUtils.extractTableData(tableRows);
        } catch (Exception e) {
            System.out.println("❌ Error extracting table data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Map<String, String>> getAllPagesData() {
        List<Map<String, String>> allData = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        int maxWaitTime = 120; // Increased timeout to 2 minutes
        long startTime = System.currentTimeMillis();
        int currentPage = 1;

        try {
            // Get total number of pages from the pagination text
            WebElement paginationElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[contains(text(), ' of ')]")));
            String totalRecords = paginationElement.getText().split(" of ")[1].trim();
            int recordsPerPage = 25; // Based on your dropdown showing 25
            int totalPages = (int) Math.ceil(Double.parseDouble(totalRecords) / recordsPerPage);

            System.out.println("📊 Total pages to process: " + totalPages);

            while (currentPage <= totalPages) {
                // Check for timeout
                if ((System.currentTimeMillis() - startTime) / 1000 > maxWaitTime) {
                    System.out.println("⚠️ Timeout reached after processing " + currentPage + " pages");
                    break;
                }

                // Get current page range
                String currentRange = driver.findElement(By.xpath("//span[contains(text(), ' of ')]"))
                        .getText().split(" of ")[0].trim();
                System.out.println("📄 Processing records " + currentRange + " of " + totalRecords);

                // Extract current page data
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//tbody/tr")));
                List<WebElement> rows = driver.findElements(By.xpath("//table//tbody/tr"));
                List<Map<String, String>> pageData = getTableDataForPage();
                allData.addAll(pageData);

                if (currentPage == totalPages) {
                    System.out.println("✅ Reached final page " + totalPages + ". Total records collected: " + allData.size());
                    break;
                }

                // Try to click next button
                WebElement nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@data-testid='btnNext']")));

                if (nextButton.getAttribute("class").contains("disabled")) {
                    System.out.println("✅ Next button disabled at page " + currentPage + ". Collection complete.");
                    break;
                }

                js.executeScript("arguments[0].click();", nextButton);
                CommonUtils.waitForPageLoad(driver);
                Thread.sleep(1000);
                currentPage++;
            }

        } catch (Exception e) {
            System.out.println("❌ Error collecting data at page " + currentPage + ": " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("📊 Final collection summary: " + allData.size() + " records from " + currentPage + " pages");
        return allData;
    }

    public boolean verifyPageNumberUpdate() {
        try {
            // Get initial page info (e.g., "1-25 of 309")
            CommonUtils.waitForVisibility(driver, paginationInfo, 10);
            String initialPageInfo = paginationInfo.getText();
            int initialPage = Integer.parseInt(initialPageInfo.split("-")[0]);

            // Click next page
            CommonUtils.waitForElementToBeClickable(driver, nextPageButton);
            nextPageButton.click();
            CommonUtils.waitForPageLoad(driver);

            // Wait for pagination info to update
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.not(
                    ExpectedConditions.textToBe(
                            By.xpath("//div[contains(@class, 'pagination')]//span[contains(text(), ' of ')]"),
                            initialPageInfo
                    )
            ));

            // Get new page info
            String newPageInfo = paginationInfo.getText();
            int newPage = Integer.parseInt(newPageInfo.split("-")[0]);

            boolean pageUpdated = newPage == initialPage + 25; // Assuming 25 rows per page

            if (!pageUpdated) {
                System.out.println("❌ Page number did not update correctly. Initial: " + initialPage + ", New: " + newPage);
                return false;
            }

            System.out.println("✅ Page number updated correctly to: " + newPage);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error verifying page number update: " + e.getMessage());
            return false;
        }
    }


    public boolean verifyFirstPagePreviousButton() {
        try {
            // Wait for the button to be present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@data-testid='btnPrevious']")));

            // Check if button is disabled using multiple attributes
            boolean isDisabled = previousPageButton.getAttribute("class").contains("disabled") ||
                    previousPageButton.getAttribute("disabled") != null ||
                    !previousPageButton.isEnabled();

            if (!isDisabled) {
                System.out.println("❌ Previous button not disabled on first page");
                return false;
            }

            System.out.println("✅ Previous button correctly disabled on first page");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error verifying first page previous button: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyLastPageNextButton() {
        try {
            // Wait for the button to be present and visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(nextPageButton));

            // Get current pagination info for logging
            String paginationText = driver.findElement(By.xpath("//span[contains(text(), ' of ')]")).getText();
            System.out.println("📄 Current pagination: " + paginationText);

            // Check button state using multiple approaches
            String buttonClass = nextPageButton.getAttribute("class");
            String ariaDisabled = nextPageButton.getAttribute("aria-disabled");
            boolean isEnabled = nextPageButton.isEnabled();

            System.out.println("🔍 Button state - Class: " + buttonClass +
                    ", Aria-disabled: " + ariaDisabled +
                    ", Enabled: " + isEnabled);

            // Verify disabled state
            boolean isDisabled = buttonClass.contains("disabled") ||
                    buttonClass.contains("pagination-diabled") ||
                    "true".equals(ariaDisabled) ||
                    !isEnabled;

            if (!isDisabled) {
                System.out.println("❌ Next button not disabled on last page");
                return false;
            }

            System.out.println("✅ Next button correctly disabled on last page");
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error verifying last page next button: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public WebElement getFirstTableRow() {
        try {
            CommonUtils.waitForVisibility(driver, firstTableRow, 20);
            return firstTableRow;
        } catch (Exception e) {
            System.out.println("❌ Error getting first table row: " + e.getMessage());
            return null;
        }
    }

    public List<WebElement> getTableRows() {
        try {
            CommonUtils.waitForVisibility(driver, firstTableRow, 20);
            return tableRows;
        } catch (Exception e) {
            System.out.println("❌ Error getting table rows: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean isNextButtonEnabled() {
        try {
            CommonUtils.waitForVisibility(driver, nextPageButton, 10);
            return nextPageButton.isEnabled() && !nextPageButton.getAttribute("class").contains("disabled");
        } catch (Exception e) {
            return false;
        }
    }

    public void goToNextPageIfEnabled() {
        if (isNextButtonEnabled()) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextPageButton);
                Thread.sleep(300);
                nextPageButton.click();
                System.out.println("➡️ Clicked next page");
            } catch (Exception e) {
                System.out.println("⚠️ Failed to click Next button: " + e.getMessage());
            }
        } else {
            System.out.println("🚫 Skipping next page click – button is disabled");
        }
    }

    public void navigateToFirstPage() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            while (true) {
                // Wait for button state to be determined
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@data-testid='btnPrevious']")));

                // Check if already on first page
                if (previousPageButton.getAttribute("class").contains("disabled") ||
                        previousPageButton.getAttribute("disabled") != null) {
                    break;
                }

                // Use JavaScript click instead of regular click
                js.executeScript("arguments[0].click();", previousPageButton);

                // Wait for page load
                CommonUtils.waitForPageLoad(driver);
                Thread.sleep(1000); // Small delay between clicks
            }

            System.out.println("✅ Successfully navigated to first page");
        } catch (Exception e) {
            System.out.println("❌ Error navigating to first page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean hasMultiplePages() {
        try {
            CommonUtils.waitForVisibility(driver, paginationInfo, 10);
            String totalPages = paginationInfo.getText().split(" of ")[1].trim();
            return Integer.parseInt(totalPages) > 1;
        } catch (Exception e) {
            return false;
        }
    }

    public void navigateToLastPage() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            while (true) {
                // Wait for button state to be determined
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@data-testid='btnNext']")));

                // Check if already on last page
                if (nextPageButton.getAttribute("class").contains("disabled") ||
                        nextPageButton.getAttribute("disabled") != null) {
                    break;
                }

//                // Use JavaScript click instead of regular click
//                js.executeScript("arguments[0].click();", nextPageButton);

                // Wait for page load
                CommonUtils.waitForPageLoad(driver);
                Thread.sleep(1000); // Small delay between clicks
            }

            System.out.println("✅ Successfully navigated to last page");
        } catch (Exception e) {
            System.out.println("❌ Error navigating to last page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isDetailsPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(backButton));
            return backButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBackButtonDisplayed() {
        try {
            return backButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickBackButton() {
        wait.until(ExpectedConditions.elementToBeClickable(backButton));
        backButton.click();
    }

    public boolean isRaidDisplayedTopLeft() {
        try {
            wait.until(ExpectedConditions.visibilityOf(raidDisplayElement));
            return raidDisplayElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getRaidDisplayText() {
        wait.until(ExpectedConditions.visibilityOf(raidDisplayElement));
        return raidDisplayElement.getText().trim();
    }

    public boolean areTabsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(tabsContainer));
            return tabsContainer.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOdmTabActive() {
        try {
            wait.until(ExpectedConditions.visibilityOf(activeOdmTab));
            return activeOdmTab.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTableDisplayedInOdmTab() {
        try {
            wait.until(ExpectedConditions.visibilityOf(tableInOdmTab));
            return tableInOdmTab.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchBoxDisplayed() {
        try {
            return searchBox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

//    public boolean isFilterButtonDisplayed() {
//        try {
//            return filterButton.isDisplayed();
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isBackButtonClickable() {
        try {
            return backButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isListingPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(actionColumnHeader));
            return actionColumnHeader.isDisplayed();
        } catch (Exception e) {
            System.err.println("Action column header not visible: " + e.getMessage());
            CommonUtils.takeScreenshot(driver,"RAID_Listing_Page_Failure");
            return false;
        }
    }

    public List<WebElement> getAllViewButtons() {
        wait.until(ExpectedConditions.visibilityOfAllElements(viewButtons));
        return viewButtons;
    }

    public WebElement getViewButtonForRow(int rowIndex) {
      //  String xpath = String.format("(//table//tbody//tr)[%d]//a[contains(text(),'View') or contains(@class,'view-btn')]", rowIndex + 1);
        String xpath = String.format("(//table//tbody//tr)//span[contains(text(),'View') or contains(@class,'view-btn')]",rowIndex + 1);
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }

    public void clickViewButtonForRow(int rowIndex) {
        WebElement viewButton = getViewButtonForRow(rowIndex);
        viewButton.click();
    }

    public void clickFirstViewButton() {
        if (!viewButtons.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(viewButtons.get(0)));
            viewButtons.get(0).click();
        }
    }

    public void clickCurrentClusterRAButton() {
        if (!viewButtons.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(viewButtons.get(0)));
            viewButtons.get(0).click();
        }
    }

    public boolean areViewButtonsClickable() {
        try {
            for (WebElement viewButton : viewButtons) {
                if (!viewButton.isEnabled()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getViewButtonsCount() {
        return viewButtons.size();
    }

    public String getRaidIdFromRow(int rowIndex) {
      //  String xpath = String.format("(//table//tbody//tr)[%d]//td[1]", rowIndex + 1);
        String xpath = String.format("//table//tbody//tr[1]",rowIndex + 1);
        WebElement raidIdElement = driver.findElement(By.xpath(xpath));
        return raidIdElement.getText().trim();
    }


    public void clickUploadGlobalRA() {
        CommonUtils.waitForElementToBeClickable(driver, uploadGlobalRAButton);
        uploadGlobalRAButton.click();
    }

    public void clickUploadClusterRA() {
        CommonUtils.waitForElementToBeClickable(driver, uploadClusterRAButton);
        uploadClusterRAButton.click();
    }

    public void clickDownloadRA() {
        CommonUtils.waitForElementToBeClickable(driver, downloadButton);
        downloadButton.click();
    }

    public String triggerDownloadAndGetFilePath() throws Exception {
        // 1. Click the Download button
        //  WebElement downloadButton = driver.findElement(By.xpath("//button[contains(., 'Download')]"));
        downloadButton.click();

        // 2. Define download directory and expected file prefix
        //   String downloadDir = "/Users/chinta.anusha/Downloads"; // ✅ Update path as per your system
        String downloadDir = System.getProperty("user.home") + "/Downloads"; // ✅ Update path as per your system
        String expectedPrefix = "RA_RA-";

        // 3. Wait for the file to appear (use helper class/method)
        File downloadedFile = CommonUtils.waitForFileDownload(downloadDir, expectedPrefix, 20);

        if (downloadedFile == null || !downloadedFile.exists()) {
            throw new FileNotFoundException("Downloaded file not found in expected location");
        }

        return downloadedFile.getAbsolutePath();
    }

    public boolean isUploadModalDisplayed() {
        return uploadModalTitle.isDisplayed();
    }

    public void uploadRAFile(String filePath) {
        CommonUtils.waitForElementToBeClickable(driver, uploadRAButton);
        uploadRAButton.click();
        try {
            // Wait if needed
            Thread.sleep(2000);
            // Trigger file picker interaction using Robot
            CommonUtils.uploadFileUsingRobot(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload RA file using Robot", e);
        }
        // uploadRAButton.sendKeys(filePath);
//        CommonUtils.waitForElementToBeClickable(driver, uploadRAButton);
//        uploadRAButton.click();
    }

    public void uploadMRPMultiplierFile(String filePath) {
        CommonUtils.waitForElementToBeClickable(driver, uploadMRPMultiplierFileButton);
        uploadMRPMultiplierFileButton.click();
        try {
            // Wait if needed
            Thread.sleep(2000);
            // Trigger file picker interaction using Robot
            CommonUtils.uploadFileUsingRobot(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload RA file using Robot", e);
        }
        // uploadRAButton.sendKeys(filePath);
//        CommonUtils.waitForElementToBeClickable(driver, uploadRAButton);
//        uploadRAButton.click();
    }

    public WebElement getFileInputElement() {
        return fileInput;
    }

    /**
     * Get count of selected files
     */
    public int getSelectedFileCount() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Long fileCount = (Long) js.executeScript(
                    "var input = document.querySelector('input[type=\"file\"]');" +
                            "return input.files ? input.files.length : 0;");
            return fileCount.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getFileInputAcceptAttribute() {
        try {
            return fileInput.getAttribute("accept");
        } catch (Exception e) {
            return "";
        }
    }

    public void uploadMRPFile(String filePath) {
        mrpMultiplierInput.sendKeys(filePath);
        CommonUtils.waitForElementToBeClickable(driver, uploadMRPButton);
        uploadMRPButton.click();
    }

    public List<WebElement> getAllTableRows() {
        return driver.findElements(By.xpath("//table//tbody/tr"));
    }

    public String getCellValueFromRow(WebElement row, String columnName) {
        Map<String, Integer> columnIndexMap = new HashMap<>();
        columnIndexMap.put("Family", 1);
        columnIndexMap.put("Class Name", 2);
        columnIndexMap.put("Brick Name", 3);
        columnIndexMap.put("Top Brick", 4);
        columnIndexMap.put("Brick", 5);
        columnIndexMap.put("Enrichment", 6);
        // add more if needed

        int columnIndex = columnIndexMap.get(columnName); // assuming headers are fixed
        return row.findElement(By.xpath("./td[" + columnIndex + "]")).getText().trim();
    }

    public String triggerDownloadSampleMultiplierAndGetFilePath() throws Exception {
        String downloadDir = System.getProperty("user.home") + "/Downloads";

        // Get files before download
        File[] filesBefore = new File(downloadDir).listFiles((dir, name) ->
                name.toLowerCase().contains("multiplier") && name.toLowerCase().endsWith(".xlsx"));
        int filesCountBefore = filesBefore != null ? filesBefore.length : 0;

        // Click download button
        downloadSampleMultiplier();

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

        throw new Exception("Download Sample Multiplier file not found");
    }

    public boolean validateMultiplierHeaders(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                System.out.println("❌ Header row is null");
                return false;
            }

            if (headerRow.getLastCellNum() != EXPECTED_MULTIPLIER_HEADERS.length) {
                System.out.println("❌ Header column count mismatch: found " +
                        headerRow.getLastCellNum() + ", expected " + EXPECTED_MULTIPLIER_HEADERS.length);
                return false;
            }

            for (int i = 0; i < EXPECTED_MULTIPLIER_HEADERS.length; i++) {
                String actualHeader = ExcelUtils.getCellValueAsString(headerRow.getCell(i)).trim();
                if (!EXPECTED_MULTIPLIER_HEADERS[i].trim().equalsIgnoreCase(actualHeader.trim())) {
                    System.out.println("❌ Header mismatch at index " + i +
                            ": expected '" + EXPECTED_MULTIPLIER_HEADERS[i] + "', found '" + actualHeader + "'");
                    return false;
                }
            }

            System.out.println("✅ All Multiplier headers matched successfully");
            return true;
        }
    }

    /**
     * Check if download success message is displayed
     */
//    public boolean isDownloadSuccessMessageDisplayed() {
//        try {
//            CommonUtils.waitForVisibility(driver, downloadSuccessful, 10);
//            return downloadSuccessful.isDisplayed();
//        } catch (Exception e) {
//            return false;
//        }
//    }


    public void downloadSampleRA() {
        downloadSampleRALink.click();
    }

    public void downloadSampleMultiplier() {
        downloadSampleMultiplierLink.click();
    }

    public boolean isFileUploadSheetNameDisplayed() {
        CommonUtils.waitForVisibility(driver, uploadFileSheetName, 10);
        return uploadFileSheetName.isDisplayed();
    }

    public boolean isUploadSuccessfullMessageDisplayed() {
        //  CommonUtils.waitForPageLoad(driver);
        CommonUtils.waitForVisibility(driver, uploadSuccessfullMessage, 10);
        return uploadSuccessfullMessage.isDisplayed();
    }

    public boolean isMRPMultiplierHeaderMessageDisplayed() {
        //  CommonUtils.waitForPageLoad(driver);
        CommonUtils.waitForVisibility(driver, mrpMutliplierHeader, 10);
        return mrpMutliplierHeader.isDisplayed();
    }

    public void clickCancel() {
        cancelButton.click();
    }

    public void clickContinue() {
        CommonUtils.waitForElementToBeClickable(driver, continueButton);
        continueButton.click();
        CommonUtils.waitForPageLoad(driver);
    }

    public List<String> getTableHeaders() {
        List<String> headers = new ArrayList<>();
        for (WebElement header : tableHeaders) {
            headers.add(header.getText().trim());
        }
        return headers;
    }

    public void clickOnMRPMultiplierNavigationButton() {
        CommonUtils.waitForElementToBeClickable(driver, mrpMultiplierSideNavigationButton);
        mrpMultiplierSideNavigationButton.click();
        CommonUtils.waitForPageLoad(driver);
    }

    public boolean verifyHeadersMatch(List<String> excelHeaders) {
        List<String> uiHeaders = getTableHeaders();
        return uiHeaders.containsAll(excelHeaders) && excelHeaders.containsAll(uiHeaders);
    }

    public boolean isFilterButtonNextToSearchBar() {
        try {
            Point searchBarLocation = searchInput.getLocation();
            Point filterButtonLocation = filterButton.getLocation();

            // Check if filter button is positioned to the right of search bar
            return filterButtonLocation.getX() > searchBarLocation.getX();
        } catch (Exception e) {
            return false;
        }
    }

//    public String getFilterIconColor() {
//        try {
//            return filterButton.getCssValue("color");
//        } catch (Exception e) {
//            System.err.println("Failed to get SVG icon color: " + e.getMessage());
//            return "";
//        }
//    }

    public String getFilterIconColor() {
        WebElement filterIcon = driver.findElement(By.xpath("//button[@class='n-button ripple n-button-rounded n-button-secondary n-button-mid null']//div"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String color = (String) js.executeScript(
                "return window.getComputedStyle(arguments[0]).getPropertyValue('color');", filterIcon);
        return color;
    }

    public boolean isFilterButtonBlue() {
        String color = getFilterIconColor();
        System.out.println("Filter Icon Color: " + color);
        // Check for blue color variations (RGB values)
        return  color.contains("rgb(53, 53, 243)") ||
                color.contains("rgb(0, 123, 255)") ||
                color.contains("rgb(0, 86, 179)") ||
                color.contains("blue") ||
                color.contains("#007bff");
    }

    // Filter Button Functionality Methods
    public void clickFilterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(filterButton)).click();
    }

    public void closeDropdown() {
        Actions actions = new Actions(driver);
        try {
            actions.sendKeys(Keys.ESCAPE).perform();
        } catch (Exception e) {
            actions.moveByOffset(0, 0).click().perform();
        }
    }

    public boolean areFilterHeadersDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(familyFilter));
            return familyFilter.isDisplayed() &&
                    classNameFilter.isDisplayed() &&
                    brickNameFilter.isDisplayed() &&
                    topBrickFilter.isDisplayed() &&
                    brickFilter.isDisplayed() &&
                    enrichmentFilter.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isClearAllButtonDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(clearAllButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isClearAllButtonDarkText() {
        String color = clearAllButton.getCssValue("color");
        System.out.println("Clear All text color: " + color);

        return color.contains("rgba(10, 10, 10, 1)") ||  // Actual color seen
                color.contains("rgb(10, 10, 10)") ||
                color.contains("#0a0a0a"); // Hex equivalent
    }

    public boolean isClearAllButtonBlue() {
        String color = clearAllButton.getCssValue("color");
//        return  color.contains("rgb(53, 53, 243)") ||
//
//                color.contains("rgb(0, 123, 255)") ||
//                color.contains("rgb(0, 86, 179)") ||
//                color.contains("blue") ||
//                color.contains("#007bff");
        // Add known blue tones including the deep navy you see
        return color.contains("rgb(0, 0, 153)") ||  // Deep blue
                color.contains("rgb(0, 86, 179)") ||
                color.contains("rgb(0, 123, 255)") ||
                color.contains("#000099") ||        // hex for rgb(0,0,153)
                color.toLowerCase().contains("blue");
    }

    public List<String> getFilterHeadersOrder() {
        List<String> headers = new ArrayList<>();
        try {
            headers.add(familyFilter.getText());
            headers.add(classNameFilter.getText());
            headers.add(brickNameFilter.getText());
            headers.add(topBrickFilter.getText());
            headers.add(brickFilter.getText());
            headers.add(enrichmentFilter.getText());
        } catch (Exception e) {
            // Handle exception
        }
        return headers;
    }

    // Filter Header Dropdown Methods
    public void clickFilterHeader(String headerName) {
        WebElement header = getFilterHeaderByName(headerName);
        if (header != null) {
            wait.until(ExpectedConditions.elementToBeClickable(header)).click();
        }
    }

    private WebElement getFilterHeaderByName(String headerName) {
        switch (headerName.toLowerCase()) {
            case "family":
                return familyFilter;
            case "class name":
                return classNameFilter;
            case "brick name":
                return brickNameFilter;
            case "top brick":
                return topBrickFilter;
            case "brick":
                return brickFilter;
            case "enrichment":
                return enrichmentFilter;
            default:
                return null;
        }
    }

    public boolean isFilterDropdownDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(filterDropdownContent)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFilterSearchBoxDisplayed() {
        try {
            return filtersearchBar.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isScrollableListDisplayed() {
        try {
            return scrollableList.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFilterClearButtonDisplayed() {
        try {
            return filterClearButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Filter Options Selection Methods
    public void selectFilterOption(String optionText) {
        for (WebElement option : filterOptions) {
            if (option.getText().equals(optionText)) {
                option.click();
                break;
            }
        }
    }

    public void selectMultipleFilterOptions(List<String> optionTexts) {
        for (String optionText : optionTexts) {
            selectFilterOption(optionText);
        }
    }

    public void unselectFilterOption(String optionText) {
        for (WebElement option : filterOptions) {
            if (option.getText().equals(optionText)) {
                WebElement checkbox = option.findElement(By.xpath(".//input[@type='checkbox']"));
                if (checkbox.isSelected()) {
                    option.click();
                }
                break;
            }
        }
    }

    public void clickFilterClearButton() {
        wait.until(ExpectedConditions.elementToBeClickable(filterClearButton)).click();
    }

    public boolean areAllOptionsUnselected() {
        for (WebElement checkbox : filterCheckboxes) {
            if (checkbox.isSelected()) {
                return false;
            }
        }
        return true;
    }

    // Filter Search Methods
    public void searchInFilter(String searchText) {
        wait.until(ExpectedConditions.visibilityOf(filtersearchBar));
        filtersearchBar.clear();
        filtersearchBar.sendKeys(searchText);
    }

    public boolean isFilterSearchBoxEditable() {
        try {
            filtersearchBar.clear();
            filtersearchBar.sendKeys("test");
            String value = filtersearchBar.getAttribute("value");
            filtersearchBar.clear();
            return "test".equals(value);
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getFilteredOptions() {
        List<String> options = new ArrayList<>();
        for (WebElement option : filterOptions) {
            if (option.isDisplayed()) {
                options.add(option.getText());
            }
        }
        return options;
    }

    public void clickClearAllButton() {
        wait.until(ExpectedConditions.elementToBeClickable(clearAllButton)).click();
    }

    // Verify filter list items and checkboxes
//    public boolean verifyFilterListItemsWithCheckboxes(String filterName) {
//        try {
//            WebElement filterDropdown = getFilterDropdown(filterName);
//            filterDropdown.click();
//
//            List<WebElement> listItems = driver.findElements(
//                    By.xpath("//label[@class='n-checkbox-container']"));
//
//            for (WebElement item : listItems) {
//                WebElement checkbox = item.findElement(By.xpath(".//input[@type='checkbox']"));
//                if (!checkbox.isDisplayed()) {
//                    return false;
//                }
//            }
//            return !listItems.isEmpty();
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public int verifyFilterListItemsWithCheckboxes(String filterName) {

        // 1) Open
            WebElement header = getFilterDropdown(filterName);
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(header)).click();

            // 2) Scope to panel
        //    By panelLocator = By.xpath("//div[contains(@class,'sc-fI') and .//label[@class='n-checkbox-container']]");
            By panelLocator = By.xpath(
                    "//div[contains(@style,'display: flex') and contains(@style,'flex-direction: column')]");

            WebElement panel = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(panelLocator));
            // 3) Now find *all* checkbox labels inside that panel
            List<WebElement> labels = panel.findElements(
                    By.cssSelector("label.n-checkbox-container"));

            // Log it so you can see in console
            System.out.println("🔢 Checkbox labels under '" + filterName + "': " + labels.size());

            return labels.size();
        }
//            // 3) Grab checkboxes
//            List<WebElement> labels = panel.findElements(By.cssSelector("label.n-checkbox-container"));
//            System.out.println("🔢 Found checkboxes: " + labels.size());
//
//            if (labels.isEmpty()) {
//                System.err.println("❌ No checkbox labels found under '" + filterName + "'");
//                return false;
//            }
//
//            // 4) Assert each label is visible/enabled
//            for (WebElement label : labels) {
//                if (!label.isDisplayed() || !label.isEnabled()) {
//                    System.err.println("❌ Filter label not interactable for: "
//                            + label.getAttribute("for"));
//                    return false;
//                }
//            }
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
 //   }

    public boolean validateMultiplierDataTypes(String filePath) throws IOException {
        List<Map<String, String>> data = ExcelUtils.readMultiplierData(filePath);

        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);

            // Validate numeric fields (MRP Final, RA Min MRP, RA Max MRP, Minimum Cost, Maximum Cost)
            if (!isValidNumeric(row.get("MRP Final")) ||
                    !isValidNumeric(row.get("RA Min MRP")) ||
                    !isValidNumeric(row.get("RA Max MRP")) ||
                    !isValidNumeric(row.get("Minimum Cost")) ||
                    !isValidNumeric(row.get("Maximum Cost"))) {
                System.out.println("❌ Invalid numeric data in row " + (i + 2));
                return false;
            }

            // Validate Brick Code (should be numeric/integer)
            if (!isValidInteger(row.get("Brick Code"))) {
                System.out.println("❌ Invalid Brick Code data type in row " + (i + 2));
                return false;
            }

            // Validate Enrichment (should be string)
            if (!isValidString(row.get("Enrichment"))) {
                System.out.println("❌ Invalid Enrichment data in row " + (i + 2));
                return false;
            }
        }

        return true;
    }

    private boolean isValidNumeric(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidInteger(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            Integer.parseInt(value.replace(".0", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Verify search bar presence in filter dropdown
    public boolean verifySearchBarInFilter(String filterName) {
        try {
            WebElement filterDropdown = getFilterDropdown(filterName);
            filterDropdown.click();
            CommonUtils.waitForSeconds(5);
            WebElement searchBar = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//input[contains(@class, 'search-bar') or @placeholder='Search'])[2]")));

            return searchBar.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Test search functionality in filter dropdown
    public boolean testSearchFunctionality(String filterName, String searchTerm) {
        try {
            WebElement filterDropdown = getFilterDropdown(filterName);
            filterDropdown.click();
            CommonUtils.waitForSeconds(5);
            WebElement searchBar = driver.findElement(
                    By.xpath("(//input[contains(@class, 'search-bar') or @placeholder='Search'])[2]"));

            // Get initial count of items
            List<WebElement> initialItems = driver.findElements(
                    By.xpath("//div[contains(@class, 'filter-dropdown')]//div[contains(@class, 'filter-item')]"));
            int initialCount = initialItems.size();

            // Type in search bar
            searchBar.clear();
            searchBar.sendKeys(searchTerm);

            Thread.sleep(1000); // Wait for search results

            // Get filtered items
            List<WebElement> filteredItems = driver.findElements(
                    By.xpath("//div[contains(@class, 'filter-dropdown')]//div[contains(@class, 'filter-item')]"));

            // Verify that filtered items contain the search term
            for (WebElement item : filteredItems) {
                String itemText = item.getText().toLowerCase();
                if (!itemText.contains(searchTerm.toLowerCase())) {
                    return false;
                }
            }

            return filteredItems.size() <= initialCount;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyFilterIsMultiSelectable(WebElement filterElement) {
        try {
            // 1) Open the dropdown panel
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(filterElement))
                    .click();
            //filterElement.click();
         //   Thread.sleep(1000); // add explicit wait in real use

            // 2) Wait for the panel container to appear
            //    Adjust this locator to match your panel’s unique wrapper
            By panelLocator = By.xpath("//div[contains(@style,'display: flex') and contains(@style,'flex-direction: column')]");
            WebElement panel = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(panelLocator));

            // 3) Find all the <input type="checkbox"> inside that panel
            List<WebElement> inputs = panel.findElements(
                    By.cssSelector("input.n-id-checkbox[type='checkbox']"));
            System.out.println("🔢 Found inputs: " + inputs.size());

            // Need at least two to test multi-select
            if (inputs.size() < 2) {
                System.err.println("❌ Only " + inputs.size() + " checkboxes found");
                return false;
            }

            // 4) Select the first two if not already selected
            WebElement firstInput  = inputs.get(0);
            WebElement secondInput = inputs.get(1);


            // 4) Click their associated labels (safer than clicking the input directly)
            panel.findElement(By.cssSelector("label[for='" + firstInput.getAttribute("id") + "']")).click();
            panel.findElement(By.cssSelector("label[for='" + secondInput.getAttribute("id") + "']")).click();

            // 5) Verify the real inputs are now selected
            boolean firstSelected  = firstInput.isSelected();
            boolean secondSelected = secondInput.isSelected();
            System.out.println("✅ Inputs selected? " + firstSelected + ", " + secondSelected);

            return firstSelected && secondSelected;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//            return first.isSelected() && second.isSelected();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean verifyAllFiltersAreMultiSelectable() {
        return verifyFilterIsMultiSelectable(familyFilter)
                && verifyFilterIsMultiSelectable(classNameFilter)
                && verifyFilterIsMultiSelectable(brickFilter);
    }

    // Verify multi-select functionality
    public boolean verifyMultiSelectFunctionality(String filterName, List<String> itemsToSelect) {
        try {
            // 1) Open the dropdown
            WebElement filterDropdown = getFilterDropdown(filterName);
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(filterDropdown))
                    .click();
            CommonUtils.waitForSeconds(10);
          //  filterDropdown.click();

            // 2) Wait for the panel container (the white box) to appear
            By panelLocator = By.xpath(
                    "//div[contains(@style,'display: flex') and contains(@style,'flex-direction: column')]");
            WebElement panel = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(panelLocator));
            CommonUtils.waitForSeconds(10);

            // 3) For each desired itemText, find its label and click it
            for (String itemText : itemsToSelect) {
                // Find the label whose text exactly matches the itemText
                WebElement label = panel.findElement(By.xpath(
                        ".//label[contains(@class,'n-checkbox-container') and normalize-space(text())='"
                                + itemText + "']"));

                // Scroll into view if needed
                ((JavascriptExecutor)driver)
                        .executeScript("arguments[0].scrollIntoView(true);", label);

                // Click the label (toggles the hidden <input>)
                label.click();
            }

            // 4) Verify that each underlying <input> is selected
            for (String itemText : itemsToSelect) {
                WebElement label = panel.findElement(By.xpath(
                        ".//label[contains(@class,'n-checkbox-container') and normalize-space(text())='"
                                + itemText + "']"));
                String inputId = label.getAttribute("for");
                WebElement input = panel.findElement(By.id(inputId));

                if (!input.isSelected()) {
                    System.err.println("❌ Failed to select: " + itemText);
                    return false;
                }
            }

            // 5) Optionally close the dropdown
            new Actions(driver).moveByOffset(0, 0).click().perform();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
//            // 2) Wait for the panel container (the white box) to appear
//            List<WebElement> selectedItems = new ArrayList<>();
//
//            for (String itemText : itemsToSelect) {
//                WebElement item = driver.findElement(
//                        By.xpath("//div[contains(@class, 'filter-item')][contains(text(), '" + itemText + "')]"));
//                WebElement checkbox = item.findElement(By.xpath(".//input[@type='checkbox']"));
//
//                if (!checkbox.isSelected()) {
//                    checkbox.click();
//                }
//                selectedItems.add(item);
//            }

//            // Verify all selected items are checked
//            for (WebElement item : selectedItems) {
//                WebElement checkbox = item.findElement(By.xpath(".//input[@type='checkbox']"));
//                if (!checkbox.isSelected()) {
//                    return false;
//                }
//            }
//
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    // Apply filter and verify table content changes
    public boolean applyFilterAndVerifyResults(String filterName, String filterValue) {
        try {
            // Get initial row count
            List<WebElement> initialRows = driver.findElements(By.xpath("//table//tbody//tr"));
            int initialCount = initialRows.size();

            // Apply filter
            WebElement filterDropdown = getFilterDropdown(filterName);
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(filterDropdown))
                    .click();

            // 2) Wait for the panel container
            By panelLocator = By.xpath(
                    "//div[contains(@style,'display: flex') and contains(@style,'flex-direction: column')]");
            WebElement panel = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(panelLocator));

            // 3) Find the label whose text matches filterValue
            //    We use normalize-space() to trim extra whitespace
            WebElement valueLabel = panel.findElement(
                    By.xpath(".//label[@class='n-checkbox-container' and normalize-space(text())='"+ filterValue +"']"));
//            WebElement checkbox = valueLabel.findElement(By.xpath(".//input[@type='checkbox']"));
//            checkbox.click();

            // Scroll into view just in case
            ((JavascriptExecutor)driver)
                    .executeScript("arguments[0].scrollIntoView(true);", valueLabel);

            // 4) Click the label (toggles the checkbox)
            valueLabel.click();
            CommonUtils.waitForSeconds(10);

            // 5) Verify underlying input is now selected
            String inputId = valueLabel.getAttribute("for");
            WebElement input = panel.findElement(By.id(inputId));
            boolean selected = input.isSelected();


            // 6) Close the dropdown by clicking outside
            new Actions(driver).moveByOffset(0, 0).click().perform();

            System.out.println("Checkbox for '" + filterValue + "' selected? " + selected);
          //  return selected;

            // 7) Wait for the table to refresh
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> d.findElements(By.xpath("//table//tbody//tr")).size() <= initialCount);

            // 8) Get filtered row count
            List<WebElement> filteredRows = driver.findElements(By.xpath("//table//tbody//tr"));
            int filteredCount = filteredRows.size();
            System.out.println("🔍 Filtered row count: " + filteredCount);

            // 9) Verify that applying the filter did not increase the row count
            return filteredCount <= initialCount;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Test sort functionality after filtering
    public boolean testSortAfterFiltering(String filterName, String filterValue, String columnToSort) {
        try {
            // First apply filter
            applyFilterAndVerifyResults(filterName, filterValue);

            // 1) Locate the <th> for your column, then click its second <span> (the sort icon)
            String headerXPath = "//th[.//*[text()='" + columnToSort + "']]//span[2]";
            WebElement sortIcon = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath(headerXPath)
                    ));

            // 2) Figure out this column’s index (1-based)
            WebElement headerTh = sortIcon.findElement(By.xpath("ancestor::th"));
            List<WebElement> allHeaders = driver.findElements(By.xpath("//table//thead//th"));
            int columnIndex = -1;
            for (int i = 0; i < allHeaders.size(); i++) {
                if (allHeaders.get(i).equals(headerTh)) {
                    columnIndex = i + 1;
                    break;
                }
            }
            if (columnIndex < 1) {
                throw new NoSuchElementException("Could not find column index for " + columnToSort);
            }

            // 3) Grab the cell texts before sorting
            List<WebElement> beforeCells = driver.findElements(
                    By.xpath("//table//tbody//tr/td[" + columnIndex + "]")
            );
            List<String> beforeTexts = beforeCells.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            // 4) Click the sort icon
            sortIcon.click();
            // 5) Wait for the table to update (simple sleep or better wait)
            CommonUtils.waitForSeconds(10);
            Thread.sleep(1500);

            // 6) Grab the cell texts after sorting
            List<WebElement> afterCells = driver.findElements(
                    By.xpath("//table//tbody//tr/td[" + columnIndex + "]")
            );
            List<String> afterTexts = afterCells.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            // 7) Return true if the list order changed
            return !beforeTexts.equals(afterTexts);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to get filter dropdown by name
    private WebElement getFilterDropdown(String filterName) {
        return driver.findElement(
             //   By.xpath("//th[contains(text(), '" + filterName + "')] | //div[contains(@class, 'sc-ciQpPG')][contains(text(), '" + filterName + "')]"));
                By.xpath("//div[normalize-space(text())='" + filterName + "']"));

    }

    // Verify Clear All functionality
    public boolean verifyClearAllFunctionality() {
        try {
            clearAllButton.click();
            Thread.sleep(1000);

            // Check if all checkboxes are unchecked
            List<WebElement> checkboxes = driver.findElements(
                    By.xpath("//div[contains(@class, 'filter-dropdown')]//input[@type='checkbox']"));

            for (WebElement checkbox : checkboxes) {
                if (checkbox.isSelected()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get all available filter headers
    public List<String> getAllFilterHeaders() {
        List<String> headers = new ArrayList<>();
        for (WebElement header : filterableHeaders) {
            headers.add(header.getText());
        }
        return headers;
    }

    // Verify filter dropdown order
    public boolean verifyFilterHeaderOrder(List<String> expectedOrder) {
        List<String> actualOrder = getAllFilterHeaders();
        return actualOrder.equals(expectedOrder);
    }

    // Cluster field presence verification
    public boolean isClusterFieldPresent() {
        try {
            return clusterField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isClusterFieldNextToFilters() {
        try {
            WebElement filters = filtersSection;
            WebElement cluster = clusterField;

            int filtersX = filters.getLocation().getX();
            int clusterX = cluster.getLocation().getX();

            // Check if cluster field is positioned near filters (within reasonable distance)
            return Math.abs(filtersX - clusterX) < 500;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDefaultClusterDisplayed() {
        try {
            String clusterText = getClusterFieldText();
            return clusterText.contains("Select cluster") || clusterText.contains("Bengaluru") || !clusterText.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getClusterFieldText() {
        try {
            if (clusterInput.isDisplayed()) {
                return clusterInput.getAttribute("value");
            } else {
                return clusterField.getText();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isDropdownArrowPresent() {
        try {
            return dropdownArrow.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDropdownArrowClickable() {
        try {
            return dropdownArrow.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Dropdown functionality
    public void clickDropdownArrow() {
        wait.until(ExpectedConditions.elementToBeClickable(dropdownArrow));
        dropdownArrow.click();
    }

    public void clickClusterField() {
        wait.until(ExpectedConditions.elementToBeClickable(clusterField));
        clusterField.click();
    }

    public boolean isDropdownMenuOpen() {
        try {
            wait.until(ExpectedConditions.visibilityOf(clusterDropdownMenu));
            return clusterDropdownMenu.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<WebElement> getClusterOptions() {
        wait.until(ExpectedConditions.visibilityOfAllElements(clusterOptions));
        return clusterOptions;
    }

    public List<String> getClusterOptionTexts() {
        List<WebElement> options = getClusterOptions();
        return options.stream().map(WebElement::getText).toList();
    }

    public boolean areClusterNamesDisplayed() {
        try {
            List<WebElement> options = getClusterOptions();
            return !options.isEmpty() && options.stream().anyMatch(option -> !option.getText().trim().isEmpty());
        } catch (Exception e) {
            return false;
        }
    }

    // Scroll bar verification
    public boolean isScrollBarPresent() {
        try {
            return scrollBar.isDisplayed();
        } catch (Exception e) {
            // Alternative check using JavaScript
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                Object result = js.executeScript(
                        "var element = arguments[0]; return element.scrollHeight > element.clientHeight;",
                        clusterDropdownMenu
                );
                return (Boolean) result;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    // Search functionality
    public boolean isSearchBoxPresent() {
        try {
            return clusterSearchBox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterClusterSearchText(String searchText) {
        wait.until(ExpectedConditions.visibilityOf(clusterSearchBox));
        clusterSearchBox.clear();
        clusterSearchBox.sendKeys(searchText);
    }

    public void selectClusterByName(String clusterName) {
        List<WebElement> options = getClusterOptions();
        for (WebElement option : options) {
            if (option.getText().trim().equals(clusterName)) {
                option.click();
                break;
            }
        }
    }

    public boolean isClusterSelected(String clusterName) {
        String selectedText = getClusterFieldText();
        return selectedText.contains(clusterName);
    }

    // Clear functionality
    public boolean isClearButtonPresent() {
        try {
            return clearButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickClearButton() {
        wait.until(ExpectedConditions.elementToBeClickable(clearButton));
        clearButton.click();
    }

    public boolean isFieldEditable() {
        try {
            return clusterInput.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Content verification
    public void waitForContentToLoad() {
        try {
            wait.until(ExpectedConditions.invisibilityOf(loadingIndicator));
        } catch (Exception e) {
            // Loading indicator might not be present
        }

        // Wait a bit for content to stabilize
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isContentDisplayed() {
        try {
            return !listingItems.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoDataMessageDisplayed() {
        try {
            return noDataMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getListingItemsCount() {
        try {
            return listingItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isLoadingIndicatorDisplayed() {
        try {
            return loadingIndicator.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    // Modal interaction methods
    public boolean isModalDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(modal)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickOutsideModal() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.click();");
    }

    public void clickCloseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
    }

    public boolean isModalClosed() {
        try {
            wait.until(ExpectedConditions.invisibilityOf(modal));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Select Cluster field methods
    public boolean isSelectClusterLabelDisplayed() {
        try {
            return selectClusterLabel.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSelectClusterAsteriskDisplayed() {
        try {
            return selectClusterAsterisk.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSelectClusterLabelText() {
        return selectClusterLabel.getText();
    }

    public boolean isSelectClusterDropdownDisplayed() {
        try {
            return selectClusterDropdown.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDropdownPlaceholderText() {
        return selectClusterDropdown.getAttribute("placeholder");
    }

    public boolean isDropdownArrowDisplayed() {
        try {
            return dropdownArrow.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickSelectClusterDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(selectClusterDropdown)).click();
    }
//
//    public void clickDropdownArrow() {
//        wait.until(ExpectedConditions.elementToBeClickable(dropdownArrow)).click();
//    }

    public boolean isDropdownListDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(dropdownList)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isScrollBarDisplayed() {
        try {
            return scrollBar.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchBarDisplayed() {
        try {
            return searchClustersInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void searchCluster(String clusterName) {
        wait.until(ExpectedConditions.visibilityOf(searchClustersInput));
        searchClustersInput.clear();
        searchClustersInput.sendKeys(clusterName);
    }

    public void selectClusterOption(String clusterName) {
        WebElement option = driver.findElement(By.xpath("//div[text()='" + clusterName + "'] | //li[text()='" + clusterName + "'] | //option[text()='" + clusterName + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    public String getSelectedClusterValue() {
        return selectClusterDropdown.getAttribute("value");
    }

//    public List<WebElement> getClusterOptions() {
//        return driver.findElements(By.xpath("//div[contains(@class, 'option')] | //li[contains(@class, 'option')] | //option"));
//    }

    // Upload RA field methods
    public boolean isUploadRALabelDisplayed() {
        try {
            return uploadRALabel.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUploadRAAsteriskDisplayed() {
        try {
            return uploadRAAsterisk.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getUploadRALabelText() {
        return uploadRALabel.getText();
    }

    /**
     * Trigger Download Sample RA and get file path
     */
    public String triggerDownloadSampleRAAndGetFilePath() throws Exception {
        // Click the Download Sample RA button
        downloadSampleRALink.click();

        // Define download directory and expected file prefix
        String downloadDir = System.getProperty("user.home") + "/Downloads";
        String filePrefix = "Sample"; // Adjust based on actual file naming

        // Wait for download to complete and find the file
        Thread.sleep(3000); // Wait for download

        File downloadFolder = new File(downloadDir);
        File[] files = downloadFolder.listFiles((dir, name) ->
                name.toLowerCase().contains("sample") && name.endsWith(".xlsx"));

        if (files != null && files.length > 0) {
            // Get the most recently downloaded file
            File latestFile = Arrays.stream(files)
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElse(null);
            return latestFile != null ? latestFile.getAbsolutePath() : null;
        }

        throw new Exception("Download Sample file not found in: " + downloadDir);
    }

//    private String triggerDownloadSampleMultiplierAndGetFilePath() throws Exception {
//
//        downloadSampleMultiplierLink.click();
//        // Clear downloads folder before download
//        String downloadDir = System.getProperty("user.home") + "/Downloads";
//        String filePrefix = "Multiplier"; // Adjust based on actual file naming
//
//        // Wait for download to complete and find the file
//        Thread.sleep(3000); // Wait for download
//
//        // Get files before download
//        File[] filesBefore = new File(downloadDir).listFiles((dir, name) ->
//                name.toLowerCase().contains("multiplier") && name.toLowerCase().endsWith(".xlsx"));
//        int filesCountBefore = filesBefore != null ? filesBefore.length : 0;
//
//        // Trigger download
//     //   rangeArchitecturePage.downloadSampleMultiplier();
//
//        // Wait for download to complete
//        Thread.sleep(5000);
//
//        // Get files after download
//        File[] filesAfter = new File(downloadDir).listFiles((dir, name) ->
//                name.toLowerCase().contains("multiplier") && name.toLowerCase().endsWith(".xlsx"));
//
//        if (filesAfter != null && filesAfter.length > filesCountBefore) {
//            // Return the most recently downloaded file
//            Arrays.sort(filesAfter, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
//            return filesAfter[0].getAbsolutePath();
//        }
//
//        throw new Exception("Download Sample Multiplier file not found in downloads folder");
//    }

    /**
     * Check if download failure message is displayed
     */
    public boolean isDownloadFailureMessageDisplayed() {
        try {
            CommonUtils.waitForVisibility(driver, downloadFailureMessage, 5);
            return downloadFailureMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if success icon (green tick) is displayed
     */
    public boolean isSuccessIconDisplayed() {
        try {
            CommonUtils.waitForVisibility(driver, successIcon, 5);
            return successIcon.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if error icon (cross mark) is displayed
     */
    public boolean isErrorIconDisplayed() {
        try {
            CommonUtils.waitForVisibility(driver, errorIcon, 5);
            return errorIcon.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCancelButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
            cancelButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click cancel button: " + e.getMessage());
        }
    }

    public boolean isCancelButtonDisplayed() {
        try {
            return cancelButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCancelButtonEnabled() {
        try {
            return cancelButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCloseButtonDisplayed() {
        try {
            return closeButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCloseButtonEnabled() {
        try {
            return closeButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForModalToAppear() {
        try {
            wait.until(ExpectedConditions.visibilityOf(modal));
        } catch (Exception e) {
            throw new RuntimeException("Modal did not appear within timeout: " + e.getMessage());
        }
    }

    public void waitForModalToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOf(modal));
        } catch (Exception e) {
            throw new RuntimeException("Modal did not disappear within timeout: " + e.getMessage());
        }
    }

    // Method to trigger popup/modal (customize based on your application)
    public void triggerPopup() {
        // This should be customized based on how the popup is triggered in your application
        // For example, clicking an upload button, edit button, etc.
        try {
            // Example: clicking upload button to trigger modal
            wait.until(ExpectedConditions.elementToBeClickable(uploadRAButton));
            uploadRAButton.click();
            waitForModalToAppear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to trigger popup: " + e.getMessage());
        }
    }
    /**
     * Simulate download failure scenario (for testing failure cases)
     */
    public void simulateDownloadFailure() {
        // This method can be used to test failure scenarios
        // Implementation depends on how failures can be simulated in your application
        // For example: network disconnection, server error simulation, etc.
    }

    /**
     * Triggers file upload dialog by clicking upload button
     */
    public void triggerFileUploadDialog() {
        CommonUtils.waitForElementToBeClickable(driver, uploadRAButton);
        uploadRAButton.click();
        CommonUtils.waitForSeconds(2); // Wait for dialog to appear
    }

    /**
     * Uploads file and handles potential timeout scenarios
     * @param filePath Path to the file to upload
     * @param expectTimeout Whether to expect a timeout error
     */
    public void uploadFileWithTimeoutHandling(String filePath, boolean expectTimeout) {
        try {
            triggerFileUploadDialog();

            if (expectTimeout) {
                // Simulate slow upload by using a large file or network delay
                CommonUtils.uploadFileUsingRobot(filePath);
                // Wait longer than expected timeout to trigger error
                CommonUtils.waitForSeconds(35);
            } else {
                CommonUtils.uploadFileUsingRobot(filePath);
                CommonUtils.waitForSeconds(3);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if selected file is displayed after selection
     */
    public boolean isSelectedFileDisplayed() {
        try {
            CommonUtils.waitForVisibility(driver, selectedFileDisplay, 10);
            return selectedFileDisplay.isDisplayed() && !selectedFileDisplay.getText().trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the name of the selected file
     */
    public String getSelectedFileName() {
        if (isSelectedFileDisplayed()) {
            return selectedFileDisplay.getText().trim();
        }
        return "";
    }

    /**
     * Checks if upload failure message is displayed
     */
    public boolean isUploadFailureMessageDisplayed() {
        try {
            CommonUtils.waitForVisibility(driver, uploadFailureMessage, 15);
            return uploadFailureMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the upload failure message text
     */
    public String getUploadFailureMessage() {
        if (isUploadFailureMessageDisplayed()) {
            return uploadFailureMessage.getText().trim();
        }
        return "";
    }

    /**
     * Checks if upload is in progress (loading indicator visible)
     */
    public boolean isUploadInProgress() {
        try {
            return uploadLoadingIndicator.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for upload to complete or timeout
     * @param timeoutSeconds Maximum time to wait
     * @return true if upload completed successfully, false if timed out
     */
    public boolean waitForUploadCompletion(int timeoutSeconds) {
        int waited = 0;
        while (waited < timeoutSeconds) {
            if (!isUploadInProgress()) {
                return isUploadSuccessfullMessageDisplayed();
            }
            CommonUtils.waitForSeconds(1);
            waited++;
        }
        return false; // Timed out
    }

    /**
     * Enhanced file upload method with format validation
     */
    public void uploadRAFileWithValidation(String filePath) {
        try {
            // Check file extension before upload
            String fileExtension = getFileExtension(filePath);
            if (!isValidFileFormat(fileExtension)) {
                throw new IllegalArgumentException("Invalid file format: " + fileExtension);
            }

            // Trigger file upload
            CommonUtils.waitForElementToBeClickable(driver, uploadRAButton);
            uploadRAButton.click();

            // Upload using robot
            CommonUtils.uploadFileUsingRobot(filePath);

            // Wait for processing
            CommonUtils.waitForSeconds(2);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    /**
     * Check if file format is valid (.xlsx or .csv)
     */
    public boolean isValidFileFormat(String fileExtension) {
        return fileExtension.equalsIgnoreCase("xlsx") ||
                fileExtension.equalsIgnoreCase("csv");
    }

    /**
     * Get file extension from file path
     */
    public String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * Check if file format error message is displayed
     */
    public boolean isFileFormatErrorDisplayed() {
        try {
            WebElement errorElement = driver.findElement(
                    By.xpath("//div[contains(text(),'File Format not supported') or " +
                            "contains(text(),'Invalid format') or " +
                            "contains(text(),'Unsupported format')]"));
            return errorElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get file size information from upload area
     */
    public String getUploadedFileSize() {
        try {
            WebElement fileSizeElement = driver.findElement(
                    By.xpath("//div[contains(@class,'file-size') or contains(text(),'KB') or contains(text(),'MB')]"));
            return fileSizeElement.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Verify loading indicator shows file details
     */
    public boolean isLoadingIndicatorShowingFileDetails(String expectedFileName) {
        try {
            if (!isUploadInProgress()) {
                return false;
            }

            // Check if filename is visible in loading area
            WebElement loadingArea = driver.findElement(
                    By.xpath("//div[contains(@class,'loading') or contains(@class,'uploading')]"));
            String loadingText = loadingArea.getText();

            return loadingText.contains(expectedFileName) ||
                    loadingText.contains(expectedFileName.substring(0, expectedFileName.lastIndexOf('.')));
        } catch (Exception e) {
            return false;
        }
    }
}





