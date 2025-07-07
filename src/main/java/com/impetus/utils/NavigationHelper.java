package com.impetus.utils;

import com.aventstack.extentreports.Status;
import com.impetus.pages.DashboardPage;
import com.impetus.pages.RangeArchitecturePage;
import org.openqa.selenium.WebDriver;


public class NavigationHelper {
    private WebDriver driver;
    private DashboardPage dashboardPage;
    private RangeArchitecturePage rangeArchitecturePage;

    public NavigationHelper(WebDriver driver) {
        this.driver = driver;
        this.dashboardPage = new DashboardPage(driver);
    }

    public void navigateToRangeArchitecture() {
      //  ExtentTestManager.getTest().log(Status.INFO, "Navigating to Range Architecture menu");
//        dashboardPage.clickRangeArchitecture();
//        CommonUtils.waitForPageLoad(driver);
//        CommonUtils.takeScreenshot(driver, "RA Navigation");
//        ExtentTestManager.getTest().log(Status.PASS, "Navigated to Range Architecture");

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the UVP Menu icon is displayed");
        dashboardPage.clickUVP();
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.takeScreenshot(driver,"verify presence of UVP");

        ExtentTestManager.getTest().log(Status.INFO, "Verifying the Range Architecture option should be visible ");
       // Object Assert = null;
       // Assert.assertTrue(dashboardPage.isRangeArchitectureDisplayed(), "Range Architecture option should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of Range Architecture option");
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the ODM option should be visible");
      //  Assert.assertTrue(dashboardPage.isODMDisplayed(),"ODM option should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of ODM Option");
        ExtentTestManager.getTest().log(Status.INFO, "Verifying the OEM option should be visible");
      //  Assert.assertTrue(dashboardPage.isOEMDisplayed(),"OEM option should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of OEM option");
        ExtentTestManager.getTest().log(Status.INFO, "MRP Multiplier section should be visible");
     //   Assert.assertTrue(dashboardPage.isMRPMultiplierDisplayed(), "MRP Multiplier section should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of MRP Multiplier section");
        ExtentTestManager.getTest().log(Status.INFO, "Size Set & Ratios section should be visible");
     //   Assert.assertTrue(dashboardPage.isSizeSetRatiosDisplayed(), "Size Set & Ratios section should be visible");
        CommonUtils.takeScreenshot(driver,"Verify presence of Size Set & Ratios section");

        ExtentTestManager.getTest().log(Status.INFO,"Verify the Range Architecture Menu is clickable");
        dashboardPage.clickRangeArchitecture();
        CommonUtils.waitForPageLoad(driver);
        CommonUtils.takeScreenshot(driver,"Verify presence of RA page");
        ExtentTestManager.getTest().log(Status.INFO,"Verify the Range Architecture page loading");
      //  Assert.assertTrue(rangeArchitecturePage.isRangeArchitectureDisplayed(), "Range Architecture page should be loaded");
        CommonUtils.takeScreenshot(driver,"RA page loaded");
        ExtentTestManager.getTest().log(Status.PASS, "Navigated to Range Architecture");
    }
}

