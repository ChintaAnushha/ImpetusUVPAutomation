package com.impetus.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.impetus.pages.LoginPage;
import com.impetus.pages.DashboardPage;
import com.impetus.utils.DriverManager;
import com.impetus.config.ConfigurationManager;

public class LoginTest  {
    private ConfigurationManager config = ConfigurationManager.getInstance();
    protected static LoginPage loginPage;
    protected static DashboardPage dashboardPage;

//    public LoginTest() {
//        super(DriverManager.getDriver());
//    }

    @Test
    public void loginToApplication() {
        // Navigate to login page
        DriverManager.navigateToUrl(config.getProperty("login.url"));

        // Perform login
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.login(
                config.getProperty("login.email"),
                config.getProperty("login.password")
        );

        // Verify dashboard is displayed
        dashboardPage = new DashboardPage(DriverManager.getDriver());
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed after login");
    }
}
