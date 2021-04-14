package com.api.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import com.api.base.BasePage;

public class AuthorizePage extends BasePage {
	
	// WEB ELEMENTS
	private final By AGREE_BUTTON = By.id("auth-accept");
	
	// PAGE INITIALIZATION
	public AuthorizePage(ChromeDriver chrome) {
		super(chrome);
	}
	
	// APPROVE APP ACCESS TO DATA AND FEATURES
	public void approveApp() {
		click(AGREE_BUTTON, TIMEOUT);
	}
}