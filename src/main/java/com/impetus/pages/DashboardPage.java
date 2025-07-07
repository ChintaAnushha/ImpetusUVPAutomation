package com.impetus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.impetus.utils.CommonUtils;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import javax.swing.*;

public class DashboardPage {
    private WebDriver driver;

    // Dashboard elements

    @FindBy(xpath = "//img[@src='/ImpetusLogoNew.6a0324c4.png']")
    public WebElement impetusLogo;

    @FindBy(xpath = "//span[contains(text(),'Planning')]")
    public WebElement planningMenuDropdown;

    @FindBy(xpath = "//span[contains(text(),'QC')]")
    public WebElement qCMenuDropdown;

    @FindBy(xpath = "//span[contains(text(),'Notification')]")
    public WebElement notificationIcon;

    @FindBy(xpath = "//span[contains(text(),'Settings')]")
    public WebElement settingsIcon;

    @FindBy(xpath = "//span[contains(text(),'Support')]")
    public WebElement supportIcon;

    @FindBy(xpath = "//h1[contains(@class, 'dashboard-page')]")
    public WebElement dashboardContainer;

    @FindBy(xpath = "//h1[text()='Dashboard Page']")
    public WebElement dashboardTitle;

    // ODM Buyer elements
    @FindBy(xpath = "//p[text()='odm-buyer']")
    public WebElement odmBuyerLink;

    @FindBy(xpath = "//span[text()='Odm-Buyer']")
    public WebElement odmBuyerText;

    // Logout elements
    @FindBy(xpath = "//div[contains(@class, 'user-profile')]")
    public WebElement userProfileMenu;

    @FindBy(xpath = "//div[contains(text(),'Logout')]")
    public WebElement logoutButton;

    @FindBy(xpath = "//span[text()='UVP']")
    public WebElement UVPDropdown;

    @FindBy(xpath = "//span[text()='Range Architecture']")
    public WebElement rangeArchitectureLink;

    @FindBy(xpath = "//span[text()='ODM']")
    public WebElement odmLink;

    @FindBy(xpath = "//span[text()='OEM']")
    public WebElement oemLink;

    @FindBy(xpath = "//span[text()='MRP Multiplier']")
    public WebElement mrpMultiplierLink;

    @FindBy(xpath = "//span[text()='Size Set & Ratios']")
    public WebElement sizeSetRatiosLink;


//    @FindBy(xpath = "//p[text()='Range Architecture']")
//    public WebElement rangeArchitecturePage;


    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * After the user lands on the “Choose an account role” screen,
     * this method will click the role badge matching the given roleName.
     *
     * @param roleName the exact text of the role to pick, e.g. "vendor", "odm-buyer", "odm-cluster"
     */
    public void selectAccountRole(String roleName) {
        // 1) Build an XPath that finds the <div class="nitrozen-badge-truncate"> with exactly that role text
        String xpath = "//div[contains(@class,'nitrozen-badge-truncate') and normalize-space(text())='"
                + roleName + "']";

        // 2) Wait for it to be clickable
        WebElement badge = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

        // 3) Scroll into view in case it's offscreen
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", badge);

        // 4) Click it
        badge.click();

        // Now you should land on the dashboard page
    }

    public boolean isDashboardDisplayed() {
        CommonUtils.waitForPageLoad(driver);
        return dashboardTitle.isDisplayed();
    }

    public boolean isUvpMenuDropdownDisplayed(){
        CommonUtils.waitForVisibility(driver,UVPDropdown,5);
        return UVPDropdown.isDisplayed();
    }

    public boolean isPlanningMenuDropdownDisplayed(){
        CommonUtils.waitForVisibility(driver,planningMenuDropdown,5);
        return planningMenuDropdown.isDisplayed();
    }

    public boolean isQcMenuDropdownDisplayed(){
        CommonUtils.waitForVisibility(driver,qCMenuDropdown,5);
        return qCMenuDropdown.isDisplayed();
    }

    public boolean isNotificationsIconDisplayed(){
        CommonUtils.waitForVisibility(driver,notificationIcon,5);
        return notificationIcon.isDisplayed();
    }

    public boolean isSettingsIconDisplayed(){
        CommonUtils.waitForVisibility(driver,settingsIcon,5);
        return settingsIcon.isDisplayed();
    }

    public boolean isSupportIconDisplayed(){
        CommonUtils.waitForVisibility(driver,supportIcon,5);
        return supportIcon.isDisplayed();
    }

    public boolean isImpetusLogoDisplayed(){
        CommonUtils.waitForPageLoad(driver);
        return impetusLogo.isDisplayed();
    }

    public void clickOdmBuyer() {
        CommonUtils.waitForPageLoad(driver);
        odmBuyerLink.click();
    }

    // Existing UVP method
    public void clickUVP() {
        CommonUtils.waitForElementToBeClickable(driver, UVPDropdown);
        UVPDropdown.click();
    }

    // New RA methods
    public void clickRangeArchitecture() {
        CommonUtils.waitForElementToBeClickable(driver, rangeArchitectureLink);
      //  CommonUtils.highlightRangeArchitecture(driver, rangeArchitectureLink);
        // Move to the element before clicking
        Actions actions = new Actions(driver);
        actions.moveToElement(rangeArchitectureLink).click().perform();

       // rangeArchitectureLink.click();
        CommonUtils.highlightRangeArchitecture(driver, rangeArchitectureLink);
    }

    public void clickODM() {
        CommonUtils.waitForElementToBeClickable(driver, odmLink);
        odmLink.click();
    }

    public void clickOEM() {
        CommonUtils.waitForElementToBeClickable(driver, oemLink);
        oemLink.click();
    }

    public void clickMRPMultiplier() {
        CommonUtils.waitForElementToBeClickable(driver, mrpMultiplierLink);
        mrpMultiplierLink.click();
    }

    public void clickSizeSetRatios() {
        CommonUtils.waitForElementToBeClickable(driver, sizeSetRatiosLink);
        sizeSetRatiosLink.click();
    }

    public boolean isRangeArchitectureDisplayed() {
        return rangeArchitectureLink.isDisplayed();
    }

    public boolean isODMDisplayed() {
        CommonUtils.waitForVisibility(driver,odmLink,5);
        return rangeArchitectureLink.isDisplayed();
    }

    public boolean isOEMDisplayed() {
        CommonUtils.waitForVisibility(driver,odmLink,5);
        return rangeArchitectureLink.isDisplayed();
    }

    public boolean isMRPMultiplierDisplayed() {
        CommonUtils.waitForVisibility(driver,odmLink,5);
        return rangeArchitectureLink.isDisplayed();
    }

    public boolean isSizeSetRatiosDisplayed() {
        CommonUtils.waitForVisibility(driver,odmLink,5);
        return rangeArchitectureLink.isDisplayed();
    }


    public void logout() {
        logoutButton.click();
    }

//    public boolean isRangeArchitectureTitleDisplayed() {
//        return rangeArchitecturePage.isDisplayed();
//    }

    public boolean isOdmBuyerSelected() {
        return odmBuyerLink.isDisplayed();
    }
}
