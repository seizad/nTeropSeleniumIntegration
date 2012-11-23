package com.nterop.tests.selenium.actions;
import org.openqa.selenium.WebDriver;


public abstract class SeleniumActions {
	
	WebDriver driver;
	public SeleniumActions(WebDriver d) {
		driver = d;
	}
}
