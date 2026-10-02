package com.tutorialsninja.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;


public class ShoppingPage extends BasePage {
	private static final String CART_URL = BASE_URL + "index.php?route=checkout/cart";
	private static final String CHECKOUT_URL = BASE_URL + "index.php?route=checkout/checkout";
	private static final String SORT_URL_PART = "sort=";
	private static final String SEARCH_TITLE = "Search";

	
	private final By emailField = By.id("input-email");
	private final By passwordField = By.id("input-password");
	private final By loginButton = By.cssSelector("input[value='Login']");
	private final By warningMessage = By.cssSelector(".alert-danger");

	
	private final By searchBox = By.name("search");
	private final By productNames = By.cssSelector(".product-thumb h4 a");
	private final By sortDropdown = By.id("input-sort");
	private final By addToCartButton = By.cssSelector("button[onclick*='cart.add']");
	private final By successMessage = By.cssSelector(".alert-success");

	
	private final By content = By.id("content");
	private final By cartProductName = By.cssSelector("#content td.text-left a");
	private final By quantityField = By.cssSelector("input[name^='quantity']");
	private final By updateButton = By.cssSelector("#content button[type='submit']");

	public ShoppingPage(WebDriver driver) {
		super(driver);
	}

	public void login(String email, String password) {
		driver.get(LOGIN_URL);
		type(emailField, email);
		type(passwordField, password);
		click(loginButton);
	}

	public String getWarningMessage() {
		return getText(warningMessage);
	}

	public void searchFor(String keyword) {
		type(searchBox, keyword + Keys.ENTER);
		wait.until(ExpectedConditions.titleContains(SEARCH_TITLE));
	}

	public String getFirstProductName() {
		return getText(productNames);
	}

	public boolean isMessageShown(String message) {
		return isTextShown(content, message);
	}

	public void sortBy(String option) {
		WebElement dropdown = driver.findElement(sortDropdown);
		Select objSelect = new Select(dropdown);
		objSelect.selectByVisibleText(option);
		wait.until(ExpectedConditions.urlContains(SORT_URL_PART));
	}

	public String getSelectedSort() {
		WebElement dropdown = driver.findElement(sortDropdown);
		Select objSelect = new Select(dropdown);
		return objSelect.getFirstSelectedOption().getText();
	}

	public void addFirstProductToCart() {
		click(addToCartButton);
	}

	public String getSuccessMessage() {
		return getText(successMessage);
	}

	public void openCart() {
		driver.get(CART_URL);
	}

	public String getCartProductName() {
		return getText(cartProductName);
	}

	public void updateQuantity(String quantity) {
		type(quantityField, quantity);
		click(updateButton);
	}

	public boolean isProductRemovedFromCart(String productName) {
		return wait.until(ExpectedConditions.invisibilityOfElementWithText(cartProductName, productName));
	}

	public void goToCheckout() {
		driver.get(CHECKOUT_URL);
	}
}