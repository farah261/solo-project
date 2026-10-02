package com.tutorialsninja.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
	public static final String BASE_URL = "https://tutorialsninja.com/demo/";
	protected static final String LOGIN_URL = BASE_URL + "index.php?route=account/login";
	protected static final String LOGOUT_URL = BASE_URL + "index.php?route=account/logout";
	private static final String LOGOUT_TITLE = "Account Logout";

	protected WebDriver driver;
	protected WebDriverWait wait;

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	protected void click(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	protected void type(By locator, String text) {
		WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		field.clear();
		field.sendKeys(text);
	}

	protected String getText(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
	}

	protected boolean isTextShown(By locator, String text) {
		return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	public boolean hasPageTitle(String title) {
		return wait.until(ExpectedConditions.titleIs(title));
	}

	public void logout() {
		driver.get(LOGOUT_URL);
		hasPageTitle(LOGOUT_TITLE);
	}
}