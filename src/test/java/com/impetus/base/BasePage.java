package com.impetus.base;

import com.aventstack.extentreports.ExtentReports;
import com.impetus.config.ConfigurationManager;
import com.impetus.pages.LoginPage;
import com.impetus.utils.ExtentManager;
import com.impetus.utils.ExtentTestManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor js;
    public static ExtentReports extent;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }


    @BeforeMethod
    public void beforeMethod(Method method) {
        ExtentTestManager.startTest(method.getName());
    }

    public void loginToDashboard() {
        LoginPage loginPage = new LoginPage(driver);
      //  DashboardPage dashboardPage = new DashboardPage(driver);

        loginPage.login(
                ConfigurationManager.getInstance().getProperty("login.email"),
                ConfigurationManager.getInstance().getProperty("login.password")
        );
    }

//    public void navigateToDashboard() {
//        DashboardPage dashboardPage = new DashboardPage(driver);
//        dashboardPage.navigateToDashboard();
//    }


    // Click Methods
    protected void click(WebElement element) {
        waitForElementClickable(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    protected void clickWithJS(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    // Wait Methods
    protected void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForElementInvisible(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    // Input Methods
    protected void sendKeys(WebElement element, String text) {
        waitForElementVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    protected void clearAndSendKeys(WebElement element, String text) {
        waitForElementVisible(element);
        element.clear();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }

    // Dropdown Methods
    protected void selectByVisibleText(WebElement element, String text) {
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    protected void selectByValue(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByValue(value);
    }

    // Mouse Actions
    protected void mouseHover(WebElement element) {
        actions.moveToElement(element).perform();
    }

    protected void dragAndDrop(WebElement source, WebElement target) {
        actions.dragAndDrop(source, target).perform();
    }

    // Verification Methods
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Text Methods
    protected String getText(WebElement element) {
        waitForElementVisible(element);
        return element.getText();
    }

    protected String getAttributeValue(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    // Scroll Methods
    protected void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void scrollToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    protected void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0)");
    }

    // Frame Methods
    protected void switchToFrame(WebElement frame) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
    }

    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    // Window Methods
    protected void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    // Alert Methods
    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    protected void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    // List Methods
    protected List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    // Navigation Methods
    protected void navigateBack() {
        driver.navigate().back();
    }

    protected void refreshPage() {
        driver.navigate().refresh();
    }

    // Browser Methods
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownExtentReport() {
        ExtentManager.getInstance().flush(); // Only once
    }

}
