package com.api.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class is inherited by the page classes.
 * @author Muana Kimba
 */
public abstract class BasePage {

	//ATTRIBUTES
	protected static final long TIMEOUT = 5;
	protected ChromeDriver chrome;

	// CONSTRUCTOR
	protected BasePage(ChromeDriver chrome) {
		this.chrome = chrome;
	}

	// METHODS
	////////////////////////////////////////////////////////////////////////////////////////////////////
	//												CLICK
	protected void click(By locator) {
		chrome.findElement(locator).click();
	}
	protected void click(By locator, long timeout) {
		elementToBeClickable(locator, timeout).click();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//												ENTER TEXT
	protected void enterText(By locator, String text) {
		WebElement element = chrome.findElement(locator);
		element.clear();
		element.sendKeys(text);
	}
	protected void enterText(By locator, String text, long timeout) {
		WebElement element = visibilityOfElementLocated(locator, timeout);
		element.clear();
		element.sendKeys(text);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	//											WAIT FOR LOCATOR
	//	CLICKABLE
	protected WebElement elementToBeClickable(By locator, long timeout) {
		return new WebDriverWait(chrome, timeout).until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	//	VISIBILITY
	protected WebElement visibilityOfElementLocated(By locator, long timeout) {
		return new WebDriverWait(chrome, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
}