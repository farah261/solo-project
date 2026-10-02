package com.tutorialsninja.tests;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tutorialsninja.pages.ShoppingPage;


public class ShoppingTests extends BaseTest {
	private static final String VALID_EMAIL = "farahsheran2@gmail.com";
	private static final String VALID_PASSWORD = "farah123";
	private static final String PRODUCT = "iPhone";
	private static final String SORT_OPTION = "Price (High > Low)";
	private static final String OUT_OF_STOCK = "not available in the desired quantity or not in stock";

	
	
	@Test(priority = 1)
	public void loginWithValidData() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.login(VALID_EMAIL, VALID_PASSWORD);

		Assert.assertTrue(shoppingPage.hasPageTitle("My Account"));
		Reporter.log("pass", true);
	}

	@Test(priority = 2, dependsOnMethods = "loginWithValidData")
	public void searchAndSortProducts() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.searchFor(PRODUCT);
		shoppingPage.sortBy(SORT_OPTION);

		Assert.assertEquals(shoppingPage.getFirstProductName(), PRODUCT);
		Assert.assertEquals(shoppingPage.getSelectedSort(), SORT_OPTION);
		Reporter.log("pass", true);
	}

	@Test(priority = 3, dependsOnMethods = "searchAndSortProducts")
	public void addProductToCart() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.addFirstProductToCart();
		Assert.assertTrue(shoppingPage.getSuccessMessage().contains("You have added " + PRODUCT));

		shoppingPage.openCart();
		Assert.assertEquals(shoppingPage.getCartProductName(), PRODUCT);
		Reporter.log("pass", true);
	}

	

	@Test(priority = 4, dependsOnMethods = "addProductToCart")
	public void checkoutWithOutOfStockProduct() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.goToCheckout();

		Assert.assertTrue(shoppingPage.hasPageTitle("Shopping Cart"));
		Assert.assertTrue(shoppingPage.getWarningMessage().contains(OUT_OF_STOCK));
		Reporter.log("pass", true);
	}

	@Test(priority = 5, dependsOnMethods = "addProductToCart")
	public void updateQuantityToLargeNumber() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.openCart();
		shoppingPage.updateQuantity("9999999999");

		Assert.assertEquals(shoppingPage.getCartProductName(), PRODUCT);
		Assert.assertTrue(shoppingPage.getWarningMessage().contains(OUT_OF_STOCK));
		Reporter.log("pass", true);
	}

	@Test(priority = 6, dependsOnMethods = "loginWithValidData", dataProvider = "invalidQuantities")
	public void updateQuantityToZeroOrNegative(String quantity) {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.searchFor(PRODUCT);
		shoppingPage.addFirstProductToCart();
		shoppingPage.getSuccessMessage();
		shoppingPage.openCart();
		shoppingPage.updateQuantity(quantity);

		Assert.assertTrue(shoppingPage.isProductRemovedFromCart(PRODUCT));
		Reporter.log("pass", true);
	}

	@DataProvider
	public Object[][] invalidQuantities() {
		return new Object[][] { {"0"}, {"-5"} };
	}

	@Test(priority = 7)
	public void searchWithNoMatch() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.searchFor("zzzzzzzzzz");

		Assert.assertTrue(shoppingPage.isMessageShown("There is no product that matches the search criteria."));
		Reporter.log("pass", true);
	}

	@Test(priority = 8, dataProvider = "invalidLoginData")
	public void loginWithInvalidData(String email, String password) {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.logout();
		shoppingPage.login(email, password);

		Assert.assertTrue(shoppingPage.getWarningMessage().contains("No match for E-Mail Address and/or Password"));
		Reporter.log("pass", true);
	}

	@DataProvider
	public Object[][] invalidLoginData() {
		return new Object[][] {
			{VALID_EMAIL, "wrongPassword123"},
			{"notregistered999@test.com", "123456"},
			{"", ""}
		};
	}
}