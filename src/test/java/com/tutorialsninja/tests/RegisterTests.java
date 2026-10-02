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

	@DataProvider
	public Object[][] invalidRegisterData() {
		return new Object[][] {
			{"",     PASSWORD, PASSWORD,        true,  "First Name must be between 1 and 32 characters!"},
			{"Test", PASSWORD, PASSWORD,        false, "You must agree to the Privacy Policy!"},
			{"Test", PASSWORD, "Different@123", true,  "Password confirmation does not match password!"},
			{"Test", "abc",    "abc",           true,  "Password must be between 4 and 20 characters!"}
		};
	}

	@Test(priority = 1)
	public void registerWithValidData() {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.register("Test", "User", newEmail, PHONE, PASSWORD, PASSWORD, true);

		Assert.assertTrue(registerPage.hasPageTitle("Your Account Has Been Created!"));
		Reporter.log("pass", true);
	}

	@Test(priority = 2, dependsOnMethods = "registerWithValidData")
	public void logoutAfterRegister() {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.logout();

		Assert.assertTrue(registerPage.hasPageTitle("Account Logout"));
		Reporter.log("pass", true);
	}

	@Test(priority = 3, dependsOnMethods = "logoutAfterRegister")
	public void loginWithNewAccount() {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.login(newEmail, PASSWORD);

		Assert.assertTrue(registerPage.hasPageTitle("My Account"));
		Reporter.log("pass", true);
	}

	

	@Test(priority = 4, dependsOnMethods = "registerWithValidData")
	public void registerWithExistingEmail() {
		RegisterPage registerPage = new RegisterPage(driver);
		registerPage.logout();
		registerPage.register("Test", "User", newEmail, PHONE, PASSWORD, PASSWORD, true);

		Assert.assertTrue(registerPage.isMessageShown("E-Mail Address is already registered!"));
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

	
}