package com.impetus.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.impetus.config.ConfigurationManager;
import com.impetus.pages.DashboardPage;
import com.impetus.pages.LoginPage;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;


public class LoginHelper {

    private WebDriver driver;
    private ConfigurationManager config;

    // Constructor to initialize WebDriver and ConfigurationManager
    public LoginHelper(WebDriver driver) {
        this.driver = driver;
      //  this.loginPage = new LoginPage(driver);
        this.config = ConfigurationManager.getInstance();
    }


//    public void setup(Method method) {
//        ExtentTestManager.startTest(method.getName());
//    }

    // Method to perform login
    public void loginToApplication() {
     //   ExtentTest test = ExtentTestManager.getTest();
        ExtentTestManager.getTest().log(Status.INFO, "Attempting login");
      //  ExtentManager.
        // Navigate to login URL
        driver.get(config.getProperty("login.url"));
     //   CommonUtils.takeScreenshot(driver,"Login page should be displayed");

        // Wait for page load using CommonUtils
        CommonUtils.waitForPageLoad(driver);

        // Create login page object
        LoginPage loginPage = new LoginPage(driver);

        // Perform login using credentials from config
    //    ExtentTestManager.getTest().log(Status.INFO, "Verifying the user name and password are entered");
        loginPage.login(config.getProperty("login.email"), config.getProperty("login.password"));
       // CommonUtils.takeScreenshot(driver,"verify presence of Email and password");
        CommonUtils.takeScreenshot(driver, "Login completed");
        ExtentTestManager.getTest().log(Status.PASS, "Login successful");

        // ← NEW: select your role here
        DashboardPage dash = new DashboardPage(driver);
        dash.selectAccountRole("odm-buyer");

        // Wait for page load after login
        CommonUtils.waitForPageLoad(driver);
    }
}