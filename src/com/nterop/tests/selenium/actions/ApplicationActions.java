package com.nterop.tests.selenium.actions;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;


public class ApplicationActions extends SeleniumActions {
	
	protected String baseUrl;
	
	public ApplicationActions(WebDriver d, String baseUrl) {
		super(d);
		this.baseUrl = baseUrl;
	}
	
	public static class ItemFromActions extends SeleniumActions {

		public ItemFromActions(WebDriver d) {
			super(d);
			log("Item Form");
		}

		protected Select selectStatusList() {
			log("Select Status List");
			return new Select(
					driver.findElement(By 
							.xpath("//*[@id='gwt-debug-formfield-Status']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/select")));
		}
		
		public void setResolution() {
			log("Select Resolution");
			new Select(
					driver.findElement(By
							.xpath("//table[@id='gwt-debug-itemform-form']/tbody/tr[4]/td/div/table/tbody/tr/td/table/tbody/tr/td[2]/div/div[2]/select")))
			.selectByVisibleText("Ongoing");
		}
		
		public void setTypes(int optionsIndex) {
			log("Set Types");
			driver.findElement(
					By.xpath("//*[@id='gwt-debug-formfield-Types']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/select/option[" + optionsIndex + "]"))
					.click();
			driver.findElement(By.xpath("(//button[@type='button'])[4]")).click();
		}
		
		private void clickAddAttribute(String display) {
			log("Click Add Attribute");
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
			log("Add Zone");
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
			log("Add District");
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
			log("Add Summary");
			clickAddAttribute("Executive-Summary");
			//select first element in list
			driver.findElement(By.xpath("//*[@id='gwt-debug-formfield-grouping-Executive-Summary']/tbody/tr[2]/td/div/div/div/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/input")).sendKeys(text);
		}

		public void setStaticTypes() {
			log("Set Static Types");
			setTypes(6);
			setResolution();
		}
		
		public void setTitle(String title) {
			log("Set Title");
			// Set Title
			driver.findElement(
					By.xpath("//*[@id='gwt-debug-itemform-form']/tbody/tr[2]/td/div/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/table/tbody/tr/td[1]/table/tbody/tr[2]/td/select/option[2]"))
					.click();
			driver.findElement(By.cssSelector("input.gwt-TextBox")).sendKeys(title);
		}
		
		public void setEffectiveFromDate(Date date) {
			log("Set Effective-From Date "  + date.toString());
			driver.findElement(By.xpath("//*[@id='gwt-debug-formfield-Effective-From']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/input")).sendKeys(new SimpleDateFormat("MMM d, yyyy").format(date));
		}
		
		public void setEffectiveToDate(Date date) {
			log("Set Effective-To Date "  + date.toString());
			driver.findElement(By.xpath("//*[@id='gwt-debug-formfield-Effective-To']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/input")).sendKeys(new SimpleDateFormat("MMM d, yyyy").format(date));
		}
		
		public void setDescription(String description) {
			log("Set Description");
			// Write in description
			String xpath = "//*[@id='gwt-debug-formfield-Activity']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/div/table/tbody/tr/td/span/table/tbody/tr[2]/td/iframe";
			driver.findElement(By.xpath(xpath)).click();
			driver.findElement(By.xpath(xpath)).sendKeys(description);
		}
		
		public void saveAndBack() {
			log("Save and Back");
			driver.findElement(By.id("gwt-debug-item-form-button-saveandclose")).click();
			sleep(Generator.DELAY);
		}
	}

	public static class CreatePage extends ItemFromActions {

		public CreatePage(WebDriver d) {
			super(d);
			log("Item Create Page");
		}

	}
	
	public static class EditPage extends ItemFromActions{

		public EditPage(WebDriver d) {
			super(d);
			log("Item Edit Page");
		}

		public void setToPublished() {
			log("Set Status To Published");
			selectStatusList().selectByVisibleText("Published");
		}
	}
	
	public static class BrowsePage extends SeleniumActions {

		public BrowsePage(WebDriver d) {
			super(d);
			log("Item Browse Page");
		}

		public CreatePage openCreatePage() {
			log("Open Create Page");
			
			// Navigate to My Item page
			driver.findElement(By.id("gwt-debug-nav-link-myitems")).click();
			
			// Choose New Item
			driver.findElement(By.id("gwt-debug-nav-compose-new")).click();
			driver.findElement(By.id("gwt-debug-nav-compose-new-item")).click();
			
			sleep(Generator.DELAY);
			return new CreatePage(driver);
		}
		
		/**
		 * 
		 * @param row the row of the celltable to select for edit.
		 * @return
		 */
		public EditPage openEditPage(int row) {
			log("Open Edit Page");
			driver.findElement(By.xpath("//table[@id='gwt-debug-browse-table']/tbody/tr/td[" + row + "]/div")).click();
			driver.findElement(By.id("gwt-debug-itembrowse-actionbar-edit-button")).click();
			sleep(Generator.DELAY);
			return new EditPage(driver);
		}
		
		public ReportBrowsePage openReportBrowsePage() {
			log("Open Report Browse Page");
			driver.findElement(By.id("gwt-debug-nav-link-allitemreports")).click();
			
			sleep(Generator.DELAY);
			// sort by updated time
			driver.findElement(By.xpath("//*[@id='gwt-debug-browse-table']/thead/tr/th[9]")).click();
			sleep(Generator.DELAY);
			driver.findElement(By.xpath("//*[@id='gwt-debug-browse-table']/thead/tr/th[9]")).click();
			sleep(Generator.DELAY);
			
			return new ReportBrowsePage(driver);
		}
		

		public ReportBrowsePage openMyReports() {
			log("Open My Reports");
			driver.findElement(By.id("gwt-debug-nav-link-allitemreports")).click();
			
			
			sleep(Generator.DELAY);
//			driver.findElement(By.id("gwt-debug-filter-discloure-header")).click();
			driver.findElement(By.xpath("//a[@id='gwt-debug-filter-discloure-header']/table/tbody/tr/td[2]")).click();


			// sort by updated time
			driver.findElement(By.xpath("//*[@id='gwt-debug-browse-table']/thead/tr/th[7]")).click();
			sleep(Generator.DELAY);
			
//			driver.findElement(By.id("gwt-debug-fitlter-field-label-Created-By-label")).click();
			driver.findElement(By.id("gwt-debug-fitlter-field-label-Created-By-input")).click();

			
			driver.findElement(By.xpath("//*[@id='gwt-debug-browse-table']/thead/tr/th[7]")).click();
			sleep(Generator.DELAY);
			
//			driver.findElement(By.linkText("Filter")).click();
			driver.findElement(By.cssSelector("button.gwt-Button")).click();

			sleep(Generator.DELAY);

			return new ReportBrowsePage(driver);
		}
		
		private void addParadeDistrict(String district) {
			log("Set Parade District");
			//select first element in list
			new Select(driver.findElement(By
					.xpath("//*[@id='gwt-debug-parade-detail-location']/tbody/tr/td[1]/table/tbody/tr[2]/td/select"))).selectByVisibleText(district);
			// click right arrow button to add to selected list.
			driver.findElement(
					By.xpath("//*[@id='gwt-debug-parade-detail-location']/tbody/tr/td[2]/table/tbody/tr[1]/td/button"))
					.click();
			
		}
		
		private void addParadeDistrict(int optionsIndex) {
			log("Set Parade District");
			//select first element in list
			driver.findElement(By
					.xpath("//*[@id='gwt-debug-parade-detail-location']/tbody/tr/td[1]/table/tbody/tr[2]/td/select/option["
							+ optionsIndex + "]")).click();
			// click right arrow button to add to selected list.
			driver.findElement(
					By.xpath("//*[@id='gwt-debug-parade-detail-location']/tbody/tr/td[2]/table/tbody/tr[1]/td/button"))
					.click();
			
		}
		
		public ParadeManagePage createParade(Date p_date, String usr_district) {
			log("Create Parade");
			// Choose New Parade
			driver.findElement(By.id("gwt-debug-nav-compose-new")).click();
			driver.findElement(By.id("gwt-debug-nav-compose-new-parade")).click();
			
			//Select date
			WebElement date_textfield = driver.findElement(By.id("gwt-debug-parade-detail-date"));
			date_textfield.clear();
			date_textfield.sendKeys(new SimpleDateFormat("MMM d, yyyy").format(p_date));
			
			new Select(driver.findElement(By.id("gwt-debug-parade-detail-shift"))).selectByVisibleText("Morning");
			
//			addParadeDistrict(3);
			addParadeDistrict(usr_district);
			
			driver.findElement(By.id("gwt-debug-parade-detail-ok")).click();
			
			sleep(Generator.DELAY);
			return new ParadeManagePage(driver);
		}
	}
	
	public static class ReportFormActions extends SeleniumActions {

		public ReportFormActions(WebDriver d) {
			super(d);
			log("Report Form");
		}

		public void setFromDate(Date date) {
			log("Set From Date "  + date.toString());
			driver.findElement(By.xpath("//*[@id='gwt-debug-dar-form-date-from']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/input")).sendKeys(new SimpleDateFormat("MMM d, yyyy").format(date));
		}

		public void setToDate(Date date) {
			log("Set To-Date " + date.toString());
			driver.findElement(By.xpath("//*[@id='gwt-debug-dar-form-date-to']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/input")).sendKeys(new SimpleDateFormat("MMM d, yyyy").format(date));
		}
		
		public void selectWestDistrict() {
			log("Select West District");
			new Select(
					driver.findElement(By
							.xpath("//*[@id='gwt-debug-dar-form-district']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/select")))
			.selectByVisibleText("West");
		}
		
		public void selectDistrict(int index) {
			log("Select West District");
			new Select(
					driver.findElement(By
							.xpath("//*[@id='gwt-debug-dar-form-district']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/select")))
			.selectByIndex(index);
		}

		public void selectDistrict(String district) {
			log("Select West District");
			new Select(
					driver.findElement(By
							.xpath("//*[@id='gwt-debug-dar-form-district']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/select")))
			.selectByVisibleText(district);
		}
		
		public void saveAndBack() {
			log("Save and Back");
			driver.findElement(By.id("gwt-debug-dar-actionbar-saveandclose")).click();
			sleep(Generator.DELAY);
		}
		
		public void openAddActivityItem() {
			log("Open Add Activity");
			driver.findElement(By.id("gwt-debug-dar-analyst-actionbar-button-add")).click();
		}
		
		public void checkMarkItem(int i) {
			log("Checkmark Item " + i);
			driver.findElement(By.xpath("//*[@id='gwt-debug-browse-table']/tbody[1]/tr[" + i + "]/td[1]/div/input")).click();
		}
		
		public void saveSelection() {
			log("Save Selection");
			driver.findElement(By.xpath("//button[contains(text(),'Save Selection')]")).click();
		}

		public void openAddPriorityItem() {
			log("Open Add Priority Items");
			driver.findElement(By.id("gwt-debug-dar-priority-actionbar-button-add")).click();
		}
		
		public void filterItemOverlayByMyItems() {
			log("Filter Item Overlay By My Items");
			driver.findElement(By.xpath("//a[@id='gwt-debug-filter-discloure-header']/table/tbody/tr/td[2]")).click();
			sleep(Generator.DELAY);
			driver.findElement(By.xpath("//a[@id='gwt-debug-filter-group-By-User-Activity-header']/table/tbody/tr/td[2]")).click();
			driver.findElement(By.id("gwt-debug-fitlter-field-label-Created-By-input")).click();
			driver.findElement(By.id("gwt-debug-filter-button")).click();
			sleep(Generator.DELAY);
		}
	}
	
	public static class ReportCreatePage extends ReportFormActions {

		public ReportCreatePage(WebDriver d) {
			super(d);
			log("Report Create Page");
		}
		
	}
	
	public static class ReportEditPage extends ReportFormActions {

		public ReportEditPage(WebDriver d) {
			super(d);
			log("Report Edit Page");
		}

		public void setStatusToActive() {
			log("Set Status Active");
			new Select(
					driver.findElement(By 
							.xpath("//*[@id='gwt-debug-dar-form-status']/table/tbody/tr/td[1]/table/tbody/tr/td[2]/div/div[2]/select")))
			.selectByVisibleText("Active");
		}
	}
	
	public static class ReportBrowsePage extends SeleniumActions {

		public ReportBrowsePage(WebDriver d) {
			super(d);
			log("Report Browse Page");
		}
		
		public ReportCreatePage openCreatePage() {
			log("Open Report Create Page");
			// sort by updated time
			// Choose New Item
			driver.findElement(By.id("gwt-debug-nav-compose-new")).click();
			driver.findElement(By.id("gwt-debug-nav-compose-new-dar")).click();
			
			sleep(Generator.DELAY);
			return new ReportCreatePage(driver);
		}
		
		/**
		 * 
		 * @param row the row of the celltable to select for edit.
		 * @return
		 */
		public ReportEditPage openEditPage(int row) {
			log("Open Edit Page");
			// select first row and edit
			sleep(Generator.DELAY);
			driver.findElement(By.xpath("//table[@id='gwt-debug-browse-table']/tbody/tr/td[" + row + "]/div")).click();
			driver.findElement(By.id("gwt-debug-reportbrowse-actionbar-edit-button")).click();
			sleep(Generator.DELAY);
			return new ReportEditPage(driver);
		}
		
		public BrowsePage openMyItems() {
			log("Open My Items");
			// Navigate to My Item page
			driver.findElement(By.id("gwt-debug-nav-link-myitems")).click();
			return new BrowsePage(driver);
		}
	}
	
	public static class ParadeBrowsePage extends SeleniumActions {
		
		public ParadeBrowsePage(WebDriver d) {
			super(d);
			log("Parade Browse Page");
		}
		
		
	}

	public static class ParadeManagePage extends SeleniumActions {
		
		public ParadeManagePage(WebDriver d) {
			super(d);
			log("Parade Browse Page");
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
		
		sleep(Generator.DELAY);

		// if there was a failure discard unsaved item
		if (driver.findElements( By.xpath("//a[contains(text(),'Discard')]") ).size() != 0)
			driver.findElement(By.xpath("//a[contains(text(),'Discard')]")).click();
		
		return new BrowsePage(driver);
	}
	
	public void Logout() {
		driver.findElement(By.linkText("Log Out")).click();
		sleep(Generator.DELAY);
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
