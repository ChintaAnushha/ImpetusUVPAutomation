package com.impetus.constants;

public class ApplicationConstants {

    // Browser Constants
    public static final String CHROME_BROWSER = "chrome";
    public static final String FIREFOX_BROWSER = "firefox";
    public static final String EDGE_BROWSER = "edge";

    // Timeout Constants (in seconds)
    public static final int DEFAULT_EXPLICIT_WAIT = 20;
    public static final int DEFAULT_IMPLICIT_WAIT = 30;
    public static final int PAGE_LOAD_TIMEOUT = 30;

    // File Paths
    public static final String CONFIG_FILE_PATH = "/src/main/resources/config.properties";
    public static final String EXTENT_REPORT_PATH = "/test-output/ExtentReport";
    public static final String SCREENSHOT_PATH = "/test-output/Screenshots";

    // Common Messages
    public static final String ELEMENT_NOT_FOUND = "Element not found on the page";
    public static final String PAGE_LOAD_ERROR = "Page failed to load within specified timeout";
    public static final String CLICK_INTERCEPTED = "Element click was intercepted";

    // Test Data Keys
    public static final String TEST_DATA_USERNAME = "test.username";
    public static final String TEST_DATA_PASSWORD = "test.password";

    // WebDriver Properties
    public static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    public static final String WEBDRIVER_FIREFOX_DRIVER = "webdriver.gecko.driver";
    public static final String WEBDRIVER_EDGE_DRIVER = "webdriver.edge.driver";

    // Framework Properties
    public static final String BASE_URL = "base.url";
    public static final String BROWSER_TYPE = "browser";
    public static final String HEADLESS_MODE = "headless";

    // Report Constants
    public static final String EXTENT_REPORT_TITLE = "UVP Automation Test Report";
    public static final String EXTENT_REPORT_NAME = "UVP Test Execution Report";

    // Date Time Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd_HH-mm-ss";
}
