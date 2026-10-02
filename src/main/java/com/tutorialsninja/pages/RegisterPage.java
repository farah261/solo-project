package com.tutorialsninja.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class RegisterPage extends BasePage {
	private static final String REGISTER_URL = BASE_URL + "index.php?route=account/register";

	
	private final By firstNameField = By.id("input-firstname");
	private final By lastNameField = By.id("input-lastname");
	private final By emailField = By.id("input-email");
	private final By telephoneField = By.id("input-telephone");
	private final By passwordField = By.id("input-password");
	private final By confirmField = By.id("input-confirm");
	private final By privacyCheckbox = By.name("agree");
	private final By continueButton = By.cssSelector("input[value='Continue']");
	private final By registerArea = By.id("account-register");
	
	private final By loginButton = By.cssSelector("input[value='Login']");

	public RegisterPage(WebDriver driver) {
		super(driver);
	}

	public void register(String firstName, String lastName, String email, String telephone,
			String password, String confirm, boolean acceptPrivacy) {
		driver.get(REGISTER_URL);
		type(firstNameField, firstName);
		type(lastNameField, lastName);
		type(emailField, email);
		type(telephoneField, telephone);
		type(passwordField, password);
		type(confirmField, confirm);
		if (acceptPrivacy) {
			click(privacyCheckbox);
		}
		click(continueButton);
	}

	public boolean isMessageShown(String message) {
		return isTextShown(registerArea, message);
	}

	public void login(String email, String password) {
		driver.get(LOGIN_URL);
		type(emailField, email);
		type(passwordField, password);
		click(loginButton);
	}
}