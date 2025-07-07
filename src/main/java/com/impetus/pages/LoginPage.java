package com.impetus.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.impetus.utils.CommonUtils;

public class LoginPage {
    private WebDriver driver;

    @FindBy(xpath = "//input[@placeholder='abc@example.com/9045637465']")
    public WebElement emailInput;

    @FindBy(xpath = "//input[@type='password']")
    public WebElement passwordInput;

    @FindBy(xpath = "//button[@role='login-btn' and @data-testid='login-CTA']//div")
    public WebElement loginButton;

    @FindBy(xpath = "//p[contains(text(),'Forgot password?')]")
    public WebElement forgotPasswordLink;

    @FindBy(xpath = "//button[contains(text(),'Login here')]")
    public WebElement employeeLoginLink;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterEmail(String email) {
        CommonUtils.waitForPageLoad(driver);
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.click();
        CommonUtils.waitForPageLoad(driver);
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }
}
