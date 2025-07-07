package com.impetus.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import com.impetus.config.ConfigurationManager;
import com.impetus.constants.ApplicationConstants;

import java.io.File;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static ConfigurationManager config = ConfigurationManager.getInstance();
    private static final String BASE_DIR = "/Users/chinta.anusha/Desktop/UVPAutomation";

    private static void createReportDirectories() {
        try {
            File extentReportDir = new File(BASE_DIR + "/test-output/ExtentReport");
            File screenshotsDir = new File(BASE_DIR + "/test-output/Screenshots");

            if (!extentReportDir.exists() && !extentReportDir.mkdirs()) {
                throw new RuntimeException("Failed to create ExtentReport directory");
            }
            if (!screenshotsDir.exists() && !screenshotsDir.mkdirs()) {
                throw new RuntimeException("Failed to create Screenshots directory");
            }
        } catch (SecurityException e) {
            throw new RuntimeException("Permission denied while creating directories: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create directories: " + e.getMessage());
        }
    }

    public static WebDriver getDriver() {
        try {
            if (driver.get() == null) {
            //    createReportDirectories();
                initializeDriver();
            }
            return driver.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage());
        }
    }

    private static void initializeDriver() {

        createReportDirectories();

        String browserType = config.getProperty(ApplicationConstants.BROWSER_TYPE);
        boolean isHeadless = Boolean.parseBoolean(config.getProperty(ApplicationConstants.HEADLESS_MODE));

        switch (browserType.toLowerCase()) {
            case ApplicationConstants.CHROME_BROWSER:
                ChromeOptions chromeOptions = new ChromeOptions();
                if (isHeadless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                driver.set(new ChromeDriver(chromeOptions));
                break;

            case ApplicationConstants.FIREFOX_BROWSER:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) {
                    firefoxOptions.addArguments("--headless");
                }
                driver.set(new FirefoxDriver(firefoxOptions));
                break;

            case ApplicationConstants.EDGE_BROWSER:
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) {
                    edgeOptions.addArguments("--headless");
                }
                driver.set(new EdgeDriver(edgeOptions));
                break;

            default:
                throw new RuntimeException("Unsupported browser type: " + browserType);
        }

        setupDriver();
    }

    private static void setupDriver() {
        WebDriver webDriver = driver.get();
        webDriver.manage().window().maximize();
        webDriver.manage().deleteAllCookies();
        webDriver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(
                Integer.parseInt(config.getProperty("implicit.wait",
                        String.valueOf(ApplicationConstants.DEFAULT_IMPLICIT_WAIT)))));
        webDriver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(
                Integer.parseInt(config.getProperty("page.load.timeout",
                        String.valueOf(ApplicationConstants.PAGE_LOAD_TIMEOUT)))));
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    public static void navigateToUrl(String url) {
        getDriver().get(url);
        CommonUtils.waitForPageLoad(getDriver());
    }

    public static void refreshPage() {
        getDriver().navigate().refresh();
        CommonUtils.waitForPageLoad(getDriver());
    }
}
