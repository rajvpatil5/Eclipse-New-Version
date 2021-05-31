package com.atom.execute;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestSetup {
	protected static ThreadLocal<WebDriver> drivers = new ThreadLocal<WebDriver>();

	public WebDriver getDriver() {
		return drivers.get();
	}

	public void setup() {

		try {
			ChromeOptions options = new ChromeOptions();
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			options.setExperimentalOption("prefs", prefs);
			options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
			options.setPageLoadStrategy(PageLoadStrategy.NONE);
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			WebDriverManager.chromedriver().setup();
			drivers.set(new ChromeDriver(options));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
