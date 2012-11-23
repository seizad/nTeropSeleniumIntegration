package com.nterop.tests.selenium.actions;
import java.util.Calendar;
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
	
	
//	int num_reports_per_user = 30;
//	int num_items = 2;
//	int num_items_per_report = 6;
	String [][] users = new String [][] {
			{"aasenmm", "nterop_4dmin"}, 
			{"HoJM", "nterop_4dmin"},
			{"BiddisAM", "nterop_4dmin"},
			{"CasagrKM", "nterop_4dmin"},
			{"TopsheDE", "nterop_4dmin"},
			{"CharboJP", "nterop_4dmin"},
			{"LiKY", "nterop_4dmin"},
	};
	
	WebDriver driver;
	String baseUrl = "http://hercules";
	final int TIMEOUT = 40; //seconds
	
	private void setup() {
		System.setProperty("webdriver.firefox.profile", "default");
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
	}
	
//	@Test
	public void addItemAndReportsMixed() throws Exception {
//		System.setProperty("webdriver.firefox.profile", "default");
//		WebDriver driver = new FirefoxDriver();
////		WebDriver driver = new RemoteWebDriver(new URL("http://hercules:4444/wd/hub/"),
////				DesiredCapabilities.firefox());
//		String baseUrl = "http://hercules";
//		driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
		
//		setup();
//		
//		app = new ApplicationActions(driver, baseUrl);
//		
//		for(int u =0; u < users.length ; u++) {
//			BrowsePage bp = app.Login(users[u][0], users[u][1]);
//			// for every user create a bunch of reports
//			for (int i=0; i < num_reports_per_user; i++) {
//				log("Report #" + i);
//				// for every report create a bunch of items
//				for (int j=0; j < num_items_per_report; j++) {
//					log("Item #" + j);
//					createAndPublishItem(bp);
//				}
//				ReportBrowsePage rbp = bp.openReportBrowsePage();
//				createReport(rbp, num_reports_per_user - i + 1);
//				editReport(rbp);
//			}
//			app.Logout();
//		}
	}
	
//	@Test
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

	
//	@Test
	public void addReports(String username, String password, int num) {
		while (true) {
			setup();
			try {

				app = new ApplicationActions(driver, baseUrl);
				BrowsePage bp = app.Login(username, password);
				ReportBrowsePage rbp = bp.openReportBrowsePage();

				for (int i = 0; i < num; i++) {
					log("Report #" + i);
					createReport(rbp, num - i);
					editReport(rbp);
				}
			} catch (Exception e) { // if it fails for any reason restart
				log("Encountered problem. Closing the browser and retrying");
				cleanup();
			}
		}
	}
	
	private void createReport(ReportBrowsePage rbp, int weeksBack) {
		ReportCreatePage rcp = rbp.openCreatePage();
		
		// active date
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -weeksBack);
		rcp.setFromDate(cal.getTime());
		
		// inactive date 
		cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -weeksBack + 1);
		rcp.setToDate(cal.getTime());

		rcp.selectWestDistrict();
		
		addActivityItems(rcp);
		
		rcp.saveAndBack();
	}
	
	private void editReport(ReportBrowsePage rbp) {
		ReportEditPage rep = rbp.openEditPage(1);
		rep.setStatusToActive();
		addPriorityItems(rep);
		rep.saveAndBack();
	}
	
	private void addActivityItems(ReportFormActions rcp) {
		rcp.openAddActivityItem();
		addItemsFromOverlay(rcp);
	}

	private void addPriorityItems(ReportFormActions rep) {
		rep.openAddPriorityItem();
		addItemsFromOverlay(rep);
	}
	
	private void addItemsFromOverlay(ReportFormActions rcp) {
		try { //if not enough items don't fail
			for (int i =1; i < 11; i++) {
				if(new Random().nextBoolean()) {
					rcp.checkMarkItem(i);
				}
			}
		}catch (Exception e) {}
		rcp.saveSelection();
	}
	
	private void createAndPublishItem(BrowsePage page) throws Exception {
		// Create new item
		CreatePage cp = page.openCreatePage();
		cp.setTypes(random(1, 30));
		cp.setResolution();
		cp.setTitle(text(5,15));
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
				ep.addDistrict(random(1, 4));
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
//		return new Random(new Date().getTime()).nextInt(to - from) + from;
	}
	
//	@After
	public void cleanup() {
		if (app != null) app.close();
	}
	
	static void log(String msg) {
		System.out.println(msg);
	}
}
