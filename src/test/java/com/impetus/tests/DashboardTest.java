package com.impetus.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.impetus.base.BasePage;
import com.impetus.pages.DashboardPage;
import com.impetus.pages.RangeArchitecturePage;
import com.impetus.utils.CommonUtils;
import com.impetus.utils.DriverManager;
import com.impetus.utils.ExtentTestManager;
import com.impetus.utils.LoginHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@Listeners(com.impetus.listeners.TestListener.class)
public class DashboardTest extends BasePage {

  //  private ConfigurationManager config = ConfigurationManager.getInstance();
  //  private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private RangeArchitecturePage rangeArchitecturePage;
    private LoginHelper loginHelper;

    public DashboardTest() {
        super(DriverManager.getDriver());
        loginHelper = new LoginHelper(driver);
    }

//    @BeforeMethod
//    public void setup(Method method) {
//        ExtentTestManager.startTest(method.getName());
//    }

    @BeforeMethod
    public void loginToApplication() {
//        DriverManager.navigateToUrl(config.getProperty("login.url"));
//        loginPage = new LoginPage(DriverManager.getDriver());
//        loginPage.login(
//                config.getProperty("login.email"),
//                config.getProperty("login.password")
//        );
        loginHelper.loginToApplication();
    }

    @Test
    public void testOdmBuyerDashboard() {
      //  ExtentTest test = ExtentTestManager.getTest();
      //  verifyOdmBuyerDashboard();
        uvpSubMenu();
    }

    // ✅ Public reusable method
    public void verifyOdmBuyerDashboard() {
        ExtentTest test = ExtentTestManager.getTest();
        dashboardPage = new DashboardPage(driver);
      //  rangeArchitecturePage = new RangeArchitecturePage(driver);
        ExtentTestManager.getTest().log(Status.INFO, "Impetus Logo should be displayed");
        Assert.assertTrue(dashboardPage.isImpetusLogoDisplayed(), "Impetus Logo should be displayed");
        CommonUtils.takeScreenshot(driver, "Verify presence of logo");
        ExtentTestManager.getTest().log(Status.INFO, "Dashboard should be displayed");
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed");
        CommonUtils.takeScreenshot(driver, "Verify presence of dashboard");
        ExtentTestManager.getTest().log(Status.INFO, "UVP dropdown should be displayed");
        Assert.assertTrue(dashboardPage.isUvpMenuDropdownDisplayed(), "UVP dropdown should be displayed");
        CommonUtils.takeScreenshot(driver, "Verify presence of UVP dropdown");
        ExtentTestManager.getTest().log(Status.INFO, "Planning dropdown should be displayed");
        Assert.assertTrue(dashboardPage.isPlanningMenuDropdownDisplayed(), "Planning dropdown should be displayed");
        CommonUtils.takeScreenshot(driver, "Verify presence of Planning dropdown");
        ExtentTestManager.getTest().log(Status.INFO, "QC dropdown should be displayed");
        Assert.assertTrue(dashboardPage.isQcMenuDropdownDisplayed(), "QC dropdown should be displayed");
        CommonUtils.takeScreenshot(driver, "Verify presence of QC dropdown");
        ExtentTestManager.getTest().log(Status.INFO, "Notification Icon should be displayed");
        Assert.assertTrue(dashboardPage.isNotificationsIconDisplayed(), "Notification Icon should be displayed");
        CommonUtils.takeScreenshot(driver, "Verify presence of Notification Icon");
        ExtentTestManager.getTest().log(Status.INFO, "Settings Icon should be displayed");
        Assert.assertTrue(dashboardPage.isSettingsIconDisplayed(), "Settings Icon should be displayed");
        CommonUtils.takeScreenshot(driver, "Verify presence of Settings Icon");
        ExtentTestManager.getTest().log(Status.INFO, "Support Icon should be displayed");
        Assert.assertTrue(dashboardPage.isSupportIconDisplayed(), "Support Icon should be displayed");
        CommonUtils.takeScreenshot(driver, "Verify presence of Support Icon");

    }

    @Test(priority = 1,dependsOnMethods = "testOdmBuyerDashboard")
    public void  uvpSubMenu() {
        ExtentTest test = ExtentTestManager.getTest();
        dashboardPage = new DashboardPage(driver);
        rangeArchitecturePage = new RangeArchitecturePage(driver);

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the UVP Menu icon is displayed");
        dashboardPage.clickUVP();
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.takeScreenshot(driver,"verify presence of UVP");

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the Range Architecture option should be visible ");
        Assert.assertTrue(dashboardPage.isRangeArchitectureDisplayed(), "Range Architecture option should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of Range Architecture option");
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the ODM option should be visible");
        Assert.assertTrue(dashboardPage.isODMDisplayed(),"ODM option should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of ODM Option");
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the OEM option should be visible");
        Assert.assertTrue(dashboardPage.isOEMDisplayed(),"OEM option should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of OEM option");
        ExtentTestManager.getTest().log(Status.INFO, "MRP Multiplier section should be visible");
        Assert.assertTrue(dashboardPage.isMRPMultiplierDisplayed(), "MRP Multiplier section should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of MRP Multiplier section");
        ExtentTestManager.getTest().log(Status.INFO, "Size Set & Ratios section should be visible");
        Assert.assertTrue(dashboardPage.isSizeSetRatiosDisplayed(), "Size Set & Ratios section should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of Size Set & Ratios section");

        ExtentTestManager.getTest().log(Status.INFO,"Verify the Range Architecture Menu is clickable");
        dashboardPage.clickRangeArchitecture();
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.takeScreenshot(driver,"Verify presence of RA page");
        ExtentTestManager.getTest().log(Status.INFO,"Verify the Range Architecture page loading");
        Assert.assertTrue(rangeArchitecturePage.isRangeArchitectureDisplayed(), "Range Architecture page should be loaded");
        CommonUtils.takeScreenshot(driver,"RA page loaded");
    }
}

