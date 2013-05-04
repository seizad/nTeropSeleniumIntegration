package com.nterop.tests.selenium.actions;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.nterop.tests.selenium.actions.ApplicationActions.BrowsePage;
import com.nterop.tests.selenium.actions.ApplicationActions.CreatePage;
import com.nterop.tests.selenium.actions.ApplicationActions.EditPage;
import com.nterop.tests.selenium.actions.ApplicationActions.ReportBrowsePage;
import com.nterop.tests.selenium.actions.ApplicationActions.ReportCreatePage;
import com.nterop.tests.selenium.actions.ApplicationActions.ReportEditPage;
import com.nterop.tests.selenium.actions.ApplicationActions.ReportFormActions;

import de.svenjacobs.loremipsum.LoremIpsum;

public class Generator {
	
	ApplicationActions app;
	LoremIpsum loip = new LoremIpsum();
	static int DELAY = 8 * 1000;
	
	WebDriver driver;
	String baseUrl;
	int TIMEOUT; //seconds
	
	public Generator(String baseUrl, int timeout, int delay) {
		TIMEOUT = timeout;
		this.baseUrl = baseUrl;
		DELAY = delay * 1000;
	}
	
	private void setup() {
		System.setProperty("webdriver.firefox.profile", "default");
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
	}
	
	
	public void addItems(String username, String password, int num) throws Exception {
		while (true) {
			setup();
			try {
				app = new ApplicationActions(driver, baseUrl);
				BrowsePage bp = app.Login(username, password);
				for (int i = 0; i < num; i++) {
					log("Item #" + i);
					createAndPublishItem(bp);
				}
				return;
			} catch (Exception e) { // if it fails for any reason restart
				log("Encountered problem. Closing the browser and retrying");
				cleanup();
			} 
		}
	}

	public void addReportsWithDistinctItems(String username, String password, int num) throws Exception {
		int r_count = 0;
		while (true) {
			setup();
			try {

				// Create a few items
				app = new ApplicationActions(driver, baseUrl);
				BrowsePage bp = app.Login(username, password);
				


				for (; r_count < num; r_count++) {
					int weeksBack = num - r_count;
					Calendar from = Calendar.getInstance();
					from.add(Calendar.WEEK_OF_YEAR, -weeksBack);

					Calendar to = Calendar.getInstance();
					to.add(Calendar.WEEK_OF_YEAR, -weeksBack+1);

					for (int j = 0; j < random(4,5); j++) {
						log("Item #" + j);
						
						createAndPublishItem(bp, from.getTime(), to.getTime());
					}
					
					ReportBrowsePage rbp = bp.openMyReports();
					
					log("Report #" + r_count);
					
//					Calendar cal = Calendar.getInstance();
//					cal.add(Calendar.WEEK_OF_YEAR, -weeksBack);
					
					createEmptyReport(rbp, from.getTime());
					editReportAddActivity(rbp);
					editReportAddPriority(rbp);
				}
				return;
			} catch (Exception e) { // if it fails for any reason restart
				log("Encountered problem. Closing the browser and retrying \n" + e.getMessage());
				cleanup();
			}
		}
	}
	
	public void addParadesWithItems(String username, String password, int num) throws Exception {
		// ratio of parades to reports is 6:1
		int report_num = (int)Math.ceil(num/6.0);
		int p_count = 0;
		int r_count = 0;
		while (true) {
			setup();
			try {
			
				// Create a few items
				for (;r_count < report_num; r_count++) {
					app = new ApplicationActions(driver, baseUrl);
					
					addReportsWithDistinctItems(username, password, 1);
					app.Logout();
					
					for (;p_count < num; p_count++) {
						
						BrowsePage bp = app.Login(username, password);
						
						bp.createParade();
						
						
						app.Logout();
					}
				}
				return;
				
			} catch (Exception e) { // if it fails for any reason restart
				log("Encountered problem. Closing the browser and retrying \n" + e.getMessage());
				cleanup();
			}
		}
	}
	
	
	
	
	public void addReports(String username, String password, int num) {
		while (true) {
			setup();
			try {

				app = new ApplicationActions(driver, baseUrl);
				BrowsePage bp = app.Login(username, password);
				ReportBrowsePage rbp = bp.openMyReports();

				for (int i = 0; i < num; i++) {
					log("Report #" + i);
					createEmptyReport(rbp, num - i);
					editReportAddActivity(rbp);
					editReportAddPriority(rbp);
				}
				return;
			} catch (Exception e) { // if it fails for any reason restart
				log("Encountered problem. Closing the browser and retrying");
				cleanup();
			}
		}
	}
	
	/*
	 * TODO: in the midst of taking active date as input and creating a report
	 * with appropriate active from and to dates.
	 */
	private void createEmptyReport(ReportBrowsePage rbp, Date activeDate) {
		ReportCreatePage rcp = rbp.openCreatePage();
		
		// active date
		Calendar cal = Calendar.getInstance();
		cal.setTime(activeDate);
		cal.add(Calendar.DATE, +7);
		
		rcp.setFromDate(activeDate);
		rcp.setToDate(cal.getTime());
		
		// inactive date 
//		rcp.selectWestDistrict();
		rcp.selectDistrict(random(1,5));
		
		rcp.saveAndBack();
	}
	
	private void createEmptyReport(ReportBrowsePage rbp, int weeksBack) {
		ReportCreatePage rcp = rbp.openCreatePage();
		
		// active date
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -weeksBack);
		rcp.setFromDate(cal.getTime());
		
		// inactive date 
		cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -weeksBack + 1);
		rcp.setToDate(cal.getTime());

//		rcp.selectWestDistrict();
		rcp.selectDistrict(random(1,5));
		
		rcp.saveAndBack();
	}
	
	private void editReportAddActivity(ReportBrowsePage rbp) {
		ReportEditPage rep = rbp.openEditPage(1);
		rep.setStatusToActive();
		addActivityItems(rep);
		rep.saveAndBack();
	}

	private void editReportAddPriority(ReportBrowsePage rbp) {
		ReportEditPage rep = rbp.openEditPage(1);
		rep.setStatusToActive();
		addPriorityItems(rep);
		rep.saveAndBack();
	}
	
	private void addActivityItems(ReportFormActions rcp) {
		rcp.openAddActivityItem();
		addItemsFromOverlay(rcp, random(3, 6));
	}

	private void addPriorityItems(ReportFormActions rep) {
		rep.openAddPriorityItem();
		addItemsFromOverlay(rep, random(2, 3));
	}
	
	private void addItemsFromOverlay(ReportFormActions rcp, int num) {
		try { //if not enough items don't fail
			rcp.filterItemOverlayByMyItems();
			for (int i =1; i < num + 1; i++) {
//				if(new Random().nextBoolean()) {
					rcp.checkMarkItem(i);
//				}
			}
		}catch (Exception e) {}
		rcp.saveSelection();
	}
	
	private BrowsePage createAndPublishItem(BrowsePage page, Date eff_from, Date eff_to) throws Exception {
		// Create new item
		CreatePage cp = page.openCreatePage();
		cp.setTypes(random(1, 30));
		cp.setResolution();
		cp.setTitle(text(5,15));

		if(eff_from != null){
			cp.setEffectiveFromDate(eff_from);
		}
		if(eff_to != null) {
			cp.setEffectiveToDate(eff_to);
		}
		
		cp.setDescription(paragraph(1, 3));
		cp.saveAndBack();
		
		// Publish the item
		EditPage ep = page.openEditPage(1);
		ep.setToPublished();
		ep.saveAndBack();
		
		// Edit the newly created item.
		maybeAddZone(page);
		maybeAddDistrict(page);
		maybeAddSummary(page);
		
		return page;
	}
	
	private BrowsePage createAndPublishItem(BrowsePage page) throws Exception {
		// Create new item
		return createAndPublishItem(page, null, null);
//		CreatePage cp = page.openCreatePage();
//		cp.setTypes(random(1, 30));
//		cp.setResolution();
//		cp.setTitle(text(5,15));
//		if(date != null){
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.WEEK_OF_YEAR, -weeksBack);
//			
//			cp.setEffectiveFromDate(cal.getTime());
//			cp.setEffectiveTpDate(cal);
//		}
//		cp.setDescription(paragraph(1, 3));
//		cp.saveAndBack();
//		
//		// Publish the item
//		EditPage ep = page.openEditPage(1);
//		ep.setToPublished();
//		ep.saveAndBack();
//		
//		// Edit the newly created item.
//		maybeAddZone(page);
//		maybeAddDistrict(page);
//		maybeAddSummary(page);
//		
//		return page;
	}
	
	private void maybeAddZone(BrowsePage page) {
		if(new Random().nextBoolean()) {
			EditPage ep = page.openEditPage(1);
			// add between 1 to 4 zones
			for (int i =0; i < random(1, 3);i++) {
				ep.addZone(random(3, 20));
			}
			ep.saveAndBack();
		}
	}
	

	void maybeAddDistrict(BrowsePage page) {
		if(new Random().nextBoolean()) {
			EditPage ep = page.openEditPage(1);
			// add between 1 to 4 zones
			for (int i =0; i < random(1, 2);i++) {
				ep.addDistrict(random(1, 5));
			}
			ep.saveAndBack();
		}
	}

	private void maybeAddSummary(BrowsePage page) {
		if(new Random().nextBoolean()) {
			EditPage ep = page.openEditPage(1);
			ep.addSummary(text(20, 30));
			ep.saveAndBack();
		}
	}
	
	private String text(int from, int to) {
		return loip.getWords(random(5, 15), random(1, 48));
	}
	
	private String paragraph(int from, int to) {
		return loip.getParagraphs(random(from, to));
	}
	
	private int random(int from, int to) {
		return (int)(Math.random() * (to - from)) + from;
	}
	
	public void cleanup() {
		if (app != null) app.close();
	}
	
	static void log(String msg) {
		System.out.println(msg);
	}
}
