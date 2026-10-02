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
	private static final String LARGE_QUANTITY = "9999999999";
	private static final String NO_MATCH_KEYWORD = "zzzzzzzzzz";

	
	private static final String MY_ACCOUNT_TITLE = "My Account";
	private static final String CART_TITLE = "Shopping Cart";

	
	private static final String ADDED_MSG = "You have added " + PRODUCT;
	private static final String OUT_OF_STOCK_MSG = "not available in the desired quantity or not in stock";
	private static final String NO_PRODUCT_MSG = "There is no product that matches the search criteria.";
	private static final String LOGIN_ERROR_MSG = "No match for E-Mail Address and/or Password";

	@Test(priority = 1)
	public void loginWithValidData() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.login(VALID_EMAIL, VALID_PASSWORD);

		Assert.assertTrue(shoppingPage.hasPageTitle(MY_ACCOUNT_TITLE));
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
		Assert.assertTrue(shoppingPage.getSuccessMessage().contains(ADDED_MSG));

		shoppingPage.openCart();
		Assert.assertEquals(shoppingPage.getCartProductName(), PRODUCT);
		Reporter.log("pass", true);
	}

	@Test(priority = 4, dependsOnMethods = "addProductToCart")
	public void checkoutWithOutOfStockProduct() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.goToCheckout();

		Assert.assertTrue(shoppingPage.hasPageTitle(CART_TITLE));
		Assert.assertTrue(shoppingPage.getWarningMessage().contains(OUT_OF_STOCK_MSG));
		Reporter.log("pass", true);
	}

	@Test(priority = 5, dependsOnMethods = "addProductToCart")
	public void updateQuantityToLargeNumber() {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.openCart();
		shoppingPage.updateQuantity(LARGE_QUANTITY);

		Assert.assertEquals(shoppingPage.getCartProductName(), PRODUCT);
		Assert.assertTrue(shoppingPage.getWarningMessage().contains(OUT_OF_STOCK_MSG));
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
		shoppingPage.searchFor(NO_MATCH_KEYWORD);

		Assert.assertTrue(shoppingPage.isMessageShown(NO_PRODUCT_MSG));
		Reporter.log("pass", true);
	}

	@Test(priority = 8, dataProvider = "invalidLoginData")
	public void loginWithInvalidData(String email, String password) {
		ShoppingPage shoppingPage = new ShoppingPage(driver);
		shoppingPage.logout();
		shoppingPage.login(email, password);

		Assert.assertTrue(shoppingPage.getWarningMessage().contains(LOGIN_ERROR_MSG));
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