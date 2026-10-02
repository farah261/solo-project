package com.tutorialsninja.tests;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tutorialsninja.pages.RegisterPage;

public class RegisterTests extends BaseTest {
	private static final String PASSWORD = "Test@1234";
	private static final String PHONE = "0591234567";
	private final String newEmail = "user" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "@test.com";

	
	private static final String ACCOUNT_CREATED_TITLE = "Your Account Has Been Created!";
	private static final String LOGOUT_TITLE = "Account Logout";
	private static final String MY_ACCOUNT_TITLE = "My Account";

	
	private static final String EMAIL_EXISTS_MSG = "E-Mail Address is already registered!";
	private static final String FIRST_NAME_MSG = "First Name must be between 1 and 32 characters!";
	private static final String PRIVACY_MSG = "You must agree to the Privacy Policy!";
	private static final String PASSWORD_MISMATCH_MSG = "Password confirmation does not match password!";
	private static final String PASSWORD_LENGTH_MSG = "Password must be between 4 and 20 characters!";

	@Test(priority = 1)
	public void registerWithValidData() {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.register("Test", "User", newEmail, PHONE, PASSWORD, PASSWORD, true);

		Assert.assertTrue(registerPage.hasPageTitle(ACCOUNT_CREATED_TITLE));
		Reporter.log("pass", true);
	}

	@Test(priority = 2, dependsOnMethods = "registerWithValidData")
	public void logoutAfterRegister() {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.logout();

		Assert.assertTrue(registerPage.hasPageTitle(LOGOUT_TITLE));
		Reporter.log("pass", true);
	}

	@Test(priority = 3, dependsOnMethods = "logoutAfterRegister")
	public void loginWithNewAccount() {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.login(newEmail, PASSWORD);

		Assert.assertTrue(registerPage.hasPageTitle(MY_ACCOUNT_TITLE));
		Reporter.log("pass", true);
	}

	@Test(priority = 4, dependsOnMethods = "registerWithValidData")
	public void registerWithExistingEmail() {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.logout();
		registerPage.register("Test", "User", newEmail, PHONE, PASSWORD, PASSWORD, true);

		Assert.assertTrue(registerPage.isMessageShown(EMAIL_EXISTS_MSG));
		Reporter.log("pass", true);
	}

	@Test(priority = 5, dataProvider = "invalidRegisterData")
	public void registerWithInvalidData(String firstName, String password, String confirm,
			boolean acceptPrivacy, String expectedMessage) {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.register(firstName, "User", "x" + newEmail, PHONE, password, confirm, acceptPrivacy);

		Assert.assertTrue(registerPage.isMessageShown(expectedMessage));
		Reporter.log("pass", true);
	}

	@DataProvider
	public Object[][] invalidRegisterData() {
		return new Object[][] {
			{"",     PASSWORD, PASSWORD,        true,  FIRST_NAME_MSG},
			{"Test", PASSWORD, PASSWORD,        false, PRIVACY_MSG},
			{"Test", PASSWORD, "Different@123", true,  PASSWORD_MISMATCH_MSG},
			{"Test", "abc",    "abc",           true,  PASSWORD_LENGTH_MSG}
		};
	}
}