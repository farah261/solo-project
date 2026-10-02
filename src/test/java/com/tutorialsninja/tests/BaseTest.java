package com.tutorialsninja.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.tutorialsninja.pages.BasePage;

public class BaseTest {
	protected WebDriver driver;

	@BeforeClass
	public void setUp() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(BasePage.BASE_URL);
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}
}