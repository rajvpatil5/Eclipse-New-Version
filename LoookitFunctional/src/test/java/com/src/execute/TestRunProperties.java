package com.src.execute;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.src.pages.CreateMeetingPage;
import com.src.testCases.TestCreateMeeting;
import com.src.util.Meeting;
import com.src.util.ReadFileData;
import com.src.util.UtilityClass;
import com.src.util.userInfo;

public class TestRunProperties {
	static UtilityClass action = new UtilityClass();
	public static ReadFileData rd = new ReadFileData();
	public static String strBaseUrl = ExecuteTestRun.jsonFileReader.userValues.getUrl();
	public static String baseUrl = ExecuteTestRun.jsonFileReader.userValues.getUrl();
	public static String uid = ExecuteTestRun.jsonFileReader.userValues.getUid();
	public static String password = ExecuteTestRun.jsonFileReader.userValues.getPassword();
	public static String invalidUid = randomCredentials()+"@gmail.com";
	public static String invalidPassword = randomCredentials();
	static List<String> nodeList = GetAvailableNodeList();
	public static HashMap<String, RemoteWebDriver> driverMap = new HashMap<String, RemoteWebDriver>();
	public static int maxInstance = Integer.parseInt(ExecuteTestRun.jsonFileReader.userValues.getMaximumInstance());
	public static String fakCamera = ExecuteTestRun.jsonFileReader.userValues.getFakeCamera();
	public static HashMap<String, Integer> userNodeMap = new HashMap<String, Integer>();
	public static HashMap<String, Integer> userMeetingMap = new HashMap<String, Integer>();
	public static HashMap<String, String> runningMeetings = new HashMap<String, String>();
	static String meetingPrefix = "Meeting";
	static HashMap<String, Meeting> runningMeeting = new HashMap<String, Meeting>();
	public static ArrayList<Object[]> sessionUrl = new ArrayList<Object[]>();

	public static List<String> GetAvailableNodeList() {
		List<String> tempNodeList = new ArrayList<String>();
		try {
			for (int i = 0; i < ExecuteTestRun.jsonFileReader.nodeValues.getCapabilities().size(); i++) {
				tempNodeList.add(ExecuteTestRun.jsonFileReader.nodeValues.getCapabilities().get(i).getNodeIP());
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("Error while fetching node list");

		}
		return tempNodeList;
	}

	public static boolean setupDriver(String node, String meetingName, boolean fakeCame, String userName,
			String browser) throws Exception {
		try {
			System.out.println("Joining class");
			Meeting meetingDetails = new Meeting();
			DesiredCapabilities capa = null;
			if (browser.equalsIgnoreCase("firefox")) {
				capa = DesiredCapabilities.firefox();
				capa.setBrowserName("firefox");
			} else if (browser.equalsIgnoreCase("chrome")) {
				capa = DesiredCapabilities.chrome();
				capa.setBrowserName("chrome");
			} else if (browser.equalsIgnoreCase("iexplore")) {
				capa = DesiredCapabilities.internetExplorer();
				capa.setBrowserName("iexplore");
			}
			// capa.setBrowserName("chrome");
			capa.setPlatform(Platform.ANY);

			UtilityClass.setOptions(userName, node, fakeCame, browser, capa);
			// TBC
			String Url = "http://" + node + "/wd/hub";
			System.out.println("Node value fetched in test run prop " + node);
			Thread.sleep(2000);
			RemoteWebDriver driver = null;
			try {
				driver = new RemoteWebDriver(new URL(Url), capa);
			} catch (Exception e) {
				System.out.print("Exception in Remote Webdriver " + e);
			}
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			// Sdriver.manage().window().setSize(new Dimension(1360, 700));
			driverMap.put(node + userName, driver);
			if (userName.contains("Host") == true) {

				// TBC
				System.out.println("Value is true: ");
				meetingDetails.setMeetingName(meetingName);
				// TBC
				System.out.println("Meeting name is set: " + meetingName);
				meetingDetails.setNodeIp(node);
				// TBC
				System.out.println("Meeting SETNodeIP  is set: " + node);

				TestRunProperties.runningMeeting.put(meetingName, meetingDetails);
				// TBC
				System.out.println("Meeting detailsand name is: " + meetingName + " Details " + meetingDetails);

			} else {
				JavascriptExecutor js = ((JavascriptExecutor) driver);
				driver.get(UtilityClass.CopiedURL);
				Thread.sleep(3000);
				action.setLang(driver, UtilityClass.lang);
				TestRunProperties.explicitWait(driver, CreateMeetingPage.txt_UserName);
				driver.findElement(CreateMeetingPage.txt_UserName).sendKeys(userName);
				js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_ProceedUser));
				// action.takeScreenShot(driver, meetingName, userName);
				TestCreateMeeting.participant = 2;
			}
			if (TestRunProperties.runningMeeting.containsKey(meetingName)) {
				meetingDetails = TestRunProperties.runningMeeting.get(meetingName);
				userInfo userInfo = new userInfo();
				userInfo.setMeetingName(meetingName);
				userInfo.setDriver(driver);
				userInfo.setNodeIp(node);
				userInfo.setUserName(userName);
				if (userName.contains("Host") == true) {
					userInfo.setHost(true);
				}
				meetingDetails.addUser(userInfo);
				TestRunProperties.runningMeeting.put(meetingName, meetingDetails);
			}
		} catch (Exception e) {
			TestRunProperties.sessionUrl.add(new String[] { meetingName, userName, node, "Null", "Fail" });
			System.out.println("Exception in driverSetup method. " + e);
		}
		return true;

	}

	public static void explicitWait(WebDriver driver, By xpath) {
		WebDriverWait wait = new WebDriverWait(driver, 70);
		wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
	}

	public static void explicitWaitInvisibility(WebDriver driver, By xpath) {
		WebDriverWait wait = new WebDriverWait(driver, 70);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(xpath));
	}

	public static void SnapshotExplicitWait(WebDriver driver, By xpath) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
	}

	public static String randomCredentials() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}

	public static void fluentWait(WebDriver driver, By xpath) {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
		wait.pollingEvery(60, TimeUnit.SECONDS);
		wait.withTimeout(10, TimeUnit.SECONDS);
		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				WebElement element = driver.findElement(xpath);
				return true;

			}
		};

		wait.until(function);

	}
}
