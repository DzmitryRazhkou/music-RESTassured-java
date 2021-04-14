package com.api.utils;

import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This class creates and returns unique Chrome drivers (browsers) in headless mode for each thread.
 * @author Muana Kimba
 */
public final class ChromeHeadlessFactory {

	// CONSTRUCTOR
	private ChromeHeadlessFactory() throws Exception {
		throw new Exception();
	}

	// ATTRIBUTES
	private static final ThreadLocal<ChromeDriver> CHROME_HEADLESS_DRIVERS = new ThreadLocal<>();

	// METHODS
	public static void set() {
		// suppress ChromeDriver logs
		System.setProperty("webdriver.chrome.silentOutput", "true");
		
		// suppress Selenium logs
		java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
		
		// set driver binary path 
		WebDriverManager.chromedriver().setup();

		// create driver instance with options
		CHROME_HEADLESS_DRIVERS.set(new ChromeDriver(new ChromeOptions()
				.addArguments("--headless")
				.addArguments("--disable-gpu")
				.addArguments("--no-sandbox")
				/*.addArguments("--start-maximized") script runs faster without it*/));
	}

	public static ChromeDriver get() {
		return CHROME_HEADLESS_DRIVERS.get();
	}

	public static void remove() {
		CHROME_HEADLESS_DRIVERS.remove();
	}
}