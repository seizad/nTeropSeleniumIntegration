package com.nterop.tests.selenium.actions;
import org.openqa.selenium.WebDriver;


public abstract class SeleniumActions {
	
	static final int DELAY =  5000;
	static final int WAIT_DELAY = 10 * 1000;

	WebDriver driver;
	public SeleniumActions(WebDriver d) {
		driver = d;
	}
}
