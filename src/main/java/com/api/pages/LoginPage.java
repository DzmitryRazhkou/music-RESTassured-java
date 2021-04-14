package com.api.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import com.api.base.BasePage;

public class LoginPage extends BasePage {
	
	// FORM ELEMENTS
	private final By EMAIL_OR_USERNAME_FIELD = By.id("login-username");
	private final By PASSWORD_FIELD = By.id("login-password");
	private final By REMEMBER_CHECKBOX = By.xpath("//div[@class='checkbox']");
	private final By LOGIN_BUTTON = By.id("login-button");
	
	// PAGE INITIALIZATION
	public LoginPage(ChromeDriver chrome) {
		super(chrome);
	}
	
	// FILL AND SUBMIT FORM
	public AuthorizePage login(String emailOrUsername, String password) {
		enterText(EMAIL_OR_USERNAME_FIELD, emailOrUsername, TIMEOUT);
		enterText(PASSWORD_FIELD, password);
		click(REMEMBER_CHECKBOX);
		click(LOGIN_BUTTON);
		return new AuthorizePage(chrome);
	}
}