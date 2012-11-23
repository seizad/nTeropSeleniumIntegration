package com.nterop.tests.selenium.actions;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;


public class ApplicationActions extends SeleniumActions {
	
	protected String baseUrl;
	static final int DELAY = 5 * 1000;
	
	public ApplicationActions(WebDriver d, String baseUrl) {
		super(d);
		this.baseUrl = baseUrl;
	}
	
	public static class ItemFromActions extends SeleniumActions {

		public ItemFromActions(WebDriver d) {
			super(d);
		}

		protected Select selectStatusList() {
			return new Select(
					driver.findElement(By 
							.xpath("//*[@id='gwt-debug-formfield-Status:']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/select")));
		}
		
		public void setResolution() {
			new Select(
					driver.findElement(By
							.xpath("//table[@id='gwt-debug-itemform-form']/tbody/tr[4]/td/div/table/tbody/tr/td/table/tbody/tr/td[2]/div/div[2]/select")))
			.selectByVisibleText("Ongoing");
		}
		
		public void setTypes(int optionsIndex) {
			driver.findElement(
					By.xpath("//*[@id='gwt-debug-formfield-Type:']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/select/option[" + optionsIndex + "]"))
					.click();
			driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
		}
		
		private void clickAddAttribute(String display) {
			//click add menu
			driver.findElement(By.id("gwt-debug-item-form-button-add")).click();
			//click on menu option
			driver.findElement(By.id("gwt-debug-dropdown-menu-item-" + display)).click();
		}
		
		/**
		 * 
		 * @param optionsIndex the index of the options starting form 1. i.e. first
		 * zone listed on the list will be 1.
		 */
		public void addZone(int optionsIndex) {
			clickAddAttribute("Zone");
			//select first element in list
			driver.findElement(By.xpath("//*[@id='gwt-debug-formfield-Zone']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/select/option[" + optionsIndex + "]")).click();
			//click right arrow button to add to selected list.
			driver.findElement(By.xpath("//*[@id='gwt-debug-formfield-Zone']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/table/tbody/tr/td[2]/table/tbody/tr[1]/td/button")).click();
		}
		
		/**
		 * 
		 * @param optionsIndex the index of the options starting form 1. i.e. first
		 * zone listed on the list will be 1.
		 */
		public void addDistrict(int optionsIndex) {
			clickAddAttribute("District");
			//select first element in list
			driver.findElement(By
					.xpath("//*[@id='gwt-debug-formfield-District']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/select/option["
							+ optionsIndex + "]")).click();
			// click right arrow button to add to selected list.
			driver.findElement(
					By.xpath("//*[@id='gwt-debug-formfield-District']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/table/tbody/tr/td[2]/table/tbody/tr[1]/td/button"))
					.click();
			
		}
		
		public void addSummary(String text) {
			clickAddAttribute("Executive-Summary");
			//select first element in list
			driver.findElement(By.xpath("//*[@id='gwt-debug-formfield-grouping-Executive-Summary']/tbody/tr[2]/td/div/div/div/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/input")).sendKeys(text);
		}

		public void setStaticTypes() {
			setTypes(6);
			setResolution();
		}
		
		public void setTitle(String title) {
			// Set Title
			driver.findElement(
					By.xpath("//*[@id='gwt-debug-itemform-form']/tbody/tr[2]/td/div/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/select/option[2]"))
					.click();
			driver.findElement(By.cssSelector("input.gwt-TextBox")).sendKeys(title);
		}
		
		public void setDescription(String description) {
			// Write in description
			String xpath = "//*[@id='gwt-debug-formfield-Description:']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/div/table/tbody/tr/td/span/table/tbody/tr[2]/td/iframe";
			driver.findElement(By.xpath(xpath)).click();
			driver.findElement(By.xpath(xpath)).sendKeys(description);
		}
		
		public void saveAndBack() {
			driver.findElement(By.id("gwt-debug-item-form-button-saveandclose")).click();
			sleep(DELAY);
		}
	}

	public static class CreatePage extends ItemFromActions {

		public CreatePage(WebDriver d) {
			super(d);
		}

	}
	
	public static class EditPage extends ItemFromActions{

		public EditPage(WebDriver d) {
			super(d);
		}

		public void setToPublished() {
			selectStatusList().selectByVisibleText("Published");
		}
	}
	
	public static class BrowsePage extends SeleniumActions {

		public BrowsePage(WebDriver d) {
			super(d);
		}

		public CreatePage openCreatePage() {
			
			// Navigate to My Item page
			driver.findElement(By.id("gwt-debug-nav-link-myitems")).click();
			
			// Choose New Item
			driver.findElement(By.id("gwt-debug-nav-compose-new")).click();
			driver.findElement(By.id("gwt-debug-nav-compose-new-item")).click();
			
			sleep(DELAY);
			return new CreatePage(driver);
		}
		
		/**
		 * 
		 * @param row the row of the celltable to select for edit.
		 * @return
		 */
		public EditPage openEditPage(int row) {
			driver.findElement(By.xpath("//table[@id='gwt-debug-browse-table']/tbody/tr/td[" + row + "]/div")).click();
			driver.findElement(By.id("gwt-debug-itembrowse-actionbar-edit-button")).click();
			sleep(DELAY);
			return new EditPage(driver);
		}
		
		public ReportBrowsePage openReportBrowsePage() {
			driver.findElement(By.id("gwt-debug-nav-link-allitemreports")).click();
			
			sleep(DELAY);
			// sort by updated time
			driver.findElement(By.xpath("//*[@id='gwt-debug-browse-table']/thead/tr/th[9]")).click();
			sleep(10000);
			driver.findElement(By.xpath("//*[@id='gwt-debug-browse-table']/thead/tr/th[9]")).click();
			sleep(10000);
			
			return new ReportBrowsePage(driver);
		}
	}
	
	public static class ReportFormActions extends SeleniumActions {

		public ReportFormActions(WebDriver d) {
			super(d);
		}

		public void setFromDate(Date date) {
			driver.findElement(By.xpath("//*[@id='gwt-debug-dar-form-date-from']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/input")).sendKeys(new SimpleDateFormat("MMM d, yyyy").format(date));
		}

		public void setToDate(Date date) {
			driver.findElement(By.xpath("//*[@id='gwt-debug-dar-form-date-to']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/input")).sendKeys(new SimpleDateFormat("MMM d, yyyy").format(date));
		}
		
		public void selectWestDistrict() {
			new Select(
					driver.findElement(By
							.xpath("//*[@id='gwt-debug-dar-form-district']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/select")))
			.selectByVisibleText("West");
		}
		
		public void saveAndBack() {
			driver.findElement(By.id("gwt-debug-dar-actionbar-saveandclose")).click();
			sleep(DELAY);
		}
		
		public void openAddActivityItem() {
			driver.findElement(By.id("gwt-debug-dar-analyst-actionbar-button-add")).click();
		}
		
		public void checkMarkItem(int i) {
			driver.findElement(By.xpath("//*[@id='gwt-debug-browse-table']/tbody[1]/tr[" + i + "]/td[1]/div/input")).click();
		}
		
		public void saveSelection() {
			driver.findElement(By.xpath("//button[contains(text(),'Save Selection')]")).click();
		}

		public void openAddPriorityItem() {
			driver.findElement(By.id("gwt-debug-dar-priority-actionbar-button-add")).click();
		}
	}
	
	public static class ReportCreatePage extends ReportFormActions {

		public ReportCreatePage(WebDriver d) {
			super(d);
		}
		
	}
	
	public static class ReportEditPage extends ReportFormActions {

		public ReportEditPage(WebDriver d) {
			super(d);
		}

		public void setStatusToActive() {
			new Select(
					driver.findElement(By 
							.xpath("//*[@id='gwt-debug-dar-form-status']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/select")))
			.selectByVisibleText("Active");
		}
	}
	
	public static class ReportBrowsePage extends SeleniumActions {

		public ReportBrowsePage(WebDriver d) {
			super(d);
		}
		
		public ReportCreatePage openCreatePage() {
			// sort by updated time
			// Choose New Item
			driver.findElement(By.id("gwt-debug-nav-compose-new")).click();
			driver.findElement(By.id("gwt-debug-nav-compose-new-dar")).click();
			
			sleep(DELAY);
			return new ReportCreatePage(driver);
		}
		
		/**
		 * 
		 * @param row the row of the celltable to select for edit.
		 * @return
		 */
		public ReportEditPage openEditPage(int row) {
			// select first row and edit
			sleep(DELAY);
			driver.findElement(By.xpath("//table[@id='gwt-debug-browse-table']/tbody/tr/td[" + row + "]/div")).click();
			driver.findElement(By.id("gwt-debug-reportbrowse-actionbar-edit-button")).click();
			sleep(DELAY);
			return new ReportEditPage(driver);
		}
	}
	
	
	
	public BrowsePage Login(String UN, String PW) {
		log("Login: " + UN + " pass: " + PW);
		driver.get(baseUrl + "/Login.html#");
		driver.findElement(By.name("j_username")).clear();
		driver.findElement(By.name("j_username")).sendKeys(UN);
		driver.findElement(By.name("j_password")).clear();
		driver.findElement(By.name("j_password")).sendKeys(PW);
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.id("gwt-debug-nav-link-myitems"));
		
		sleep(DELAY);

		// if there was a failure discard unsaved item
		if (driver.findElements( By.xpath("//a[contains(text(),'Discard')]") ).size() != 0)
			driver.findElement(By.xpath("//a[contains(text(),'Discard')]")).click();
		
		return new BrowsePage(driver);
	}

	public void Logout() {
		driver.findElement(By.linkText("Log Out")).click();
		sleep(DELAY);
	}
	
	
	public void close() {
		driver.close();
	}
	
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
	
	static void log(String msg) {
		System.out.println(msg);
	}
}
