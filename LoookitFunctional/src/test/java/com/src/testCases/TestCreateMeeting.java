package com.src.testCases;

import static org.testng.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.src.execute.ExecuteTestRun;
import com.src.execute.TestRunProperties;
import com.src.pages.CreateMeetingPage;
import com.src.util.AddCanvas;
import com.src.util.ExcelData;
import com.src.util.UtilityClass;

public class TestCreateMeeting {
	UtilityClass action = new UtilityClass();
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public static WebDriver driver1;
	public static int participant = 1;
	public static int browserCount = 4;
	static JavascriptExecutor js;

	@Parameters({ "node", "meetingName", "FakeCame", "userName", "browser", "language", "platform" })
	@BeforeTest
	public void beforeMethod(String node, String meetingName, String FakeCame, String userName, String browser,
			String language, String platform) {
		long id = Thread.currentThread().getId();
		int trycount = 0;
		// browserCount++;
		boolean fakeCamera = Boolean.parseBoolean(FakeCame);
		do {
			System.out.println(
					"Before test-joinMeeting try count:" + trycount + " " + meetingName + ". Thread id is: " + id);
			try {
				Assert.assertTrue(TestRunProperties.setupDriver(node, meetingName, fakeCamera, userName, browser),
						MessageFormat.format("Error while Driver setup for {0} Meeting name={1} User Name={2}", node,
								meetingName, userName));

				trycount += 2;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (trycount >= 2) {
					Assert.assertTrue(false,
							MessageFormat.format(
									"Error while Driver setup for {0} Meeting name={1} User Name={2} Exception={3}",
									node, meetingName, userName, e.getMessage()));
				}
				trycount++;
			}
		} while (trycount <= 1);
	}

	@Parameters({ "node", "meetingName", "userName" })
	@Test(priority = 0)
	public void startMeeting(String node, String meetingName, String userName) throws Exception {
		try {
			if (userName.contains("Host") == false) {
				System.out.println("Host is FALSE");
				System.out.println("Meeting name" + TestRunProperties.runningMeetings.containsKey(meetingName));
				Assert.assertTrue(TestRunProperties.runningMeetings.containsKey(meetingName),
						"Meeting not found. Unable to join " + userName);
			} else {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.get(TestRunProperties.baseUrl);
				assertEquals("Loookit", driver.getTitle());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName", "language" })
	@Test(priority = 1)
	public void setLang(String node, String userName, String language) throws Exception {
		try {
			if (userName.contains("Host") == true) {
				System.out.println("Host is TRUE");
				System.out.println("Meeting name TRUE" + TestRunProperties.driverMap.get(node + userName));
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				action.setLang(driver, language);
				// action.setLang(driver, "english");
				driver1 = driver;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName" })
	@Test(priority = 2)
	public void invalidUserLogin(String node, String userName) throws Exception {
		try {
			// ArrayList<String> data = ExcelData.readExcelData();
			if (userName.contains("Host") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				// Start functional automation
				action.invalidUserlogIn(driver);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName" })
	@Test(priority = 3)
	public void userLogin(String node, String userName) throws Exception {
		try {
			if (userName.contains("Host") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				// Start functional automation
				action.userlogIn(driver);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName", "platform" })
	@Test(priority = 4)
	public void createSessionAddParticipant(String node, String meetingName, String userName, String platform)
			throws Exception {
		try {
			if (userName.contains("Host") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				action.createSession(driver, meetingName);
				// Thread.sleep(2000);
				action.addParticipant(driver, meetingName, userName, node, platform);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName" })
	@Test(priority = 5)
	public void userPresentor(String node, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				action.admitUser(driver1);
				System.out.println("Host admit users");
				 action.makeUserPresenter(driver1, CreateMeetingPage.userThumb);
				 System.out.println("Host makes user1 presentator");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	 @Test(priority = 6)
	public void userPresentorSS(String node, String meetingName, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				action.userPresentorSS(driver, driver1);
				System.out.println("user1 presentator share screen");
				action.makeUserPresenter(driver1, CreateMeetingPage.hostThumb);
				System.out.println("host is now presentator");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	 @Test(priority = 7)
	public void shareLocation(String node, String meetingName, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				action.shareLocation(driver, driver1, meetingName, userName, node);
				System.out.println("location shared and stop by host");
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	@Test(priority = 8)
	public void startAndStopAudio(String node, String meetingName, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				ArrayList<WebDriver> drivers = new ArrayList<WebDriver>();
				drivers.add(driver1);
				drivers.add(driver);
				UtilityClass.audioCall(drivers, meetingName, userName, node);
				if(TestRunProperties.driverMap.size()==2) {
				Thread.sleep(10000);
				action.stopAudioVideoCall(driver1, "audio");
				}
			}
			LinkedHashMap<String, RemoteWebDriver>ls = new LinkedHashMap<String, RemoteWebDriver>(TestRunProperties.driverMap);
			if (userName.contains("User1") == true && ls.size()>2){

			for(int i =2; i<ls.size();i++) {

			WebDriver drivertemp = ls.get(node+"User"+i+"Demo1");
			UtilityClass.audioVideoAccept(drivertemp, meetingName, userName, node);
			}
			Thread.sleep(15000);
			action.stopAudioVideoCall(driver1, "audio");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	 //@Test(priority = 9)
	public void acceptAudioRequest(String node, String meetingName, String userName) throws Exception {
		try {
			System.out.println("checking if condition for accepting audio/video request");
		/*	if (!userName.contains("User1") && !userName.contains("Host")) {
				System.out.println("if condition true");
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(20000);
				UtilityClass.audioVideoAccept(driver, meetingName, userName, node);*/
			  
			LinkedHashMap<String, RemoteWebDriver>ls = new LinkedHashMap<String, RemoteWebDriver>(TestRunProperties.driverMap);
			if (userName.contains("User1") == true && ls.size()>2){

			for(int i =2; i<ls.size();i++) {

			WebDriver drivertemp = ls.get(node+"User"+i+"Demo1");
			UtilityClass.audioVideoAccept(drivertemp, meetingName, userName, node);
			}
			}
			else {
				System.out.println("if condition false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	@Test(priority = 10)
	public void audioCallSS(String node, String meetingName, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				ArrayList<WebDriver> drivers = new ArrayList<WebDriver>();
				// drivers = new ArrayList<WebDriver>();
				drivers.add(driver1);
				drivers.add(driver);

				action.screenShare(drivers);
				Thread.sleep(4000);
				UtilityClass.audioCall(drivers, meetingName, userName, node);
				Thread.sleep(4000);

			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	@Test(priority = 11)
	public void aceptAudioRequest(String node, String meetingName, String userName) throws Exception {
		try {
			System.out.println("checking if condition for accepting audio/video request");
			/*if (!userName.contains("User1") && !userName.contains("Host")) {
				System.out.println("if condition true");
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(25000);
				UtilityClass.audioVideoAccept(driver, meetingName, userName, node);
				*/
			LinkedHashMap<String, RemoteWebDriver>ls = new LinkedHashMap<String, RemoteWebDriver>(TestRunProperties.driverMap);
			if (userName.contains("User1") == true && ls.size()>2){

			for(int i =2; i<ls.size();i++) {

			WebDriver drivertemp = ls.get(node+"User"+i+"Demo1");
			UtilityClass.audioVideoAccept(drivertemp, meetingName, userName, node);
			}
			
			
			} else {
				System.out.println("if condition false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	@Test(priority = 12)
	public void startVideo(String node, String meetingName, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);

				ArrayList<WebDriver> drivers = new ArrayList<WebDriver>();
				drivers.add(driver1);
				drivers.add(driver);

				Thread.sleep(4000);
				UtilityClass.videoCall(drivers, meetingName, userName, node);
				Thread.sleep(4000);

				// action.stopAudioVideoCall(driver1,"video");

			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	@Test(priority = 13)
	public void acceptVideoRequest(String node, String meetingName, String userName) throws Exception {
		try {
			/*if (!userName.contains("User1") && !userName.contains("Host")) {
				WebDriver drivertemp = TestRunProperties.driverMap.get(node + userName);
				UtilityClass.audioVideoAccept(drivertemp, meetingName, userName, node);
			}*/
			
				 LinkedHashMap<String, RemoteWebDriver>ls = new LinkedHashMap<String, RemoteWebDriver>(TestRunProperties.driverMap);
					if (userName.contains("User1") == true && ls.size()>2){
						
						for(int i =2; i<ls.size();i++) {
							
							WebDriver drivertemp = ls.get(node+"User"+i+"Demo1");
							UtilityClass.audioVideoAccept(drivertemp, meetingName, userName, node);
						}
					}
			
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Parameters({ "node", "userName" })
	//@Test(priority = 14)
	public void addCanvas(String node, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebElement Canvas = driver1.findElement(CreateMeetingPage.LOC_Canvas);
				Canvas.click();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName" })
	//@Test(priority = 15)
	public void addCanvas_circle(String node, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				AddCanvas.AddAnnotations_Circle(driver1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName" })
	//@Test(priority = 16)
	public void addCanvas_rectangle(String node, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				AddCanvas.AddAnnotations_Rectangle(driver1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName" })
	//@Test(priority = 17)
	public void addCanvas_draw(String node, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				AddCanvas.AddAnnotations_Draw(driver1);
			}
		} catch (Exception e) {
			
		}
	}

	@Parameters({ "node", "userName" })
	//@Test(priority = 18)
	public void addCanvas_line(String node, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				// AddCanvas.AddAnnotations_Line(driver1);
			}
		} catch (Exception e) {
			
		}
	}

	@Parameters({ "node", "userName" })
	//@Test(priority = 19)
	public void addCanvas_textDecoration(String node, String userName) throws Exception {
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				AddCanvas.addCanvas(driver1);
				AddCanvas.AddAnnotations_Line(driver1);
				AddCanvas.textDecoration_canvas(driver1);
			}
		} catch (Exception e) {
			
		}
	}

	@Parameters({ "userName" })
	@Test(priority = 20)
	public void stopSS(String userName) throws Exception {

		try {
			if (participant == 2 && userName.contains("User1") == true) {
				action.stopSS(driver1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName" })
	//@Test(priority = 21)
	public void canvas_clearAndDelete(String node, String userName) throws Exception {

		try {
			if (participant == 2 && userName.contains("User1") == true) {
				AddCanvas.clearAndCloseCanvas(driver1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	@Parameters({ "userName" })
	@Test(priority = 22)
	public void endCall(String userName) throws Exception {

		try {
			if (participant == 2 && userName.contains("User1") == true) {
				action.stopAudioVideoCall(driver1, "video");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	@Parameters({ "node", "userName", "meetingName" })
	@Test(priority = 23)
	public void denySnapShot(String node, String userName, String meetingName) throws Exception {

		try {
			if (ExecuteTestRun.jsonFileReader.userValues.getUsersPerMeeting().equals("2")
					&& userName.contains("User1") == true) {
				System.out.println(participant);
				System.out.println(participant == 2);
				System.out.println(userName.contains("User1"));
				System.out.println(participant == 2 && userName.contains("User1"));
				System.out.println(userName + " - " + TestRunProperties.driverMap.get(node + userName));
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				action.denysnapShot(driver, driver1, meetingName, userName, node);
				Thread.sleep(4000);
			}
			String uusercount = ExecuteTestRun.jsonFileReader.userValues.getUsersPerMeeting();
			int usercountExtra = Integer.parseInt(uusercount);

			if (usercountExtra > 2 && userName.contains("User1") == true) {
				System.out.println("User count is > 2");

				System.out.println(userName + " - " + TestRunProperties.driverMap.get(node + userName));
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				action.denysnapShotMultiUser(driver, driver1, meetingName, userName, node);
				Thread.sleep(4000);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	// This method looks wrong
	@Parameters({ "node", "meetingName", "userName" })
	// @Test(priority = 24)
	public void denySnapshotRequest(String node, String meetingName, String userName) throws Exception {
		try {
			if (!userName.contains("User1") && !userName.contains("Host")) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);
				UtilityClass.audioVideoDeny(driver, meetingName, userName, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName", "meetingName" })
	@Test(priority = 25)
	public void snapShot(String node, String userName, String meetingName) throws Exception {

		try {
			if (ExecuteTestRun.jsonFileReader.userValues.getUsersPerMeeting().equals("2")
					&& userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				// ArrayList<WebDriver> drivers = new ArrayList<WebDriver>();
				action.snapShot(driver, driver1, meetingName, userName, node);
				Thread.sleep(2000);
			}

			String uusercount = ExecuteTestRun.jsonFileReader.userValues.getUsersPerMeeting();
			int usercountExtra = Integer.parseInt(uusercount);

			if (usercountExtra > 2 && userName.contains("User1")) {
				System.out.println("User count is > 2");

				System.out.println(userName + " - " + TestRunProperties.driverMap.get(node + userName));
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				action.snapShotMultiUser(driver, driver1, meetingName, userName, node);
				Thread.sleep(2000);
			}
			if (usercountExtra > 2 && !userName.contains("User1")) {
				Thread.sleep(20000);
			}

			String uusercount2 = ExecuteTestRun.jsonFileReader.userValues.getUsersPerMeeting();
			int usercountExtra2 = Integer.parseInt(uusercount2);

			if (usercountExtra2 > 2 && userName.contains("User1")) {
				System.out.println("User count is > 2");

				System.out.println(userName + " - " + TestRunProperties.driverMap.get(node + userName));
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				action.snapShotMultiUser(driver, driver1, meetingName, userName, node);
				Thread.sleep(4000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	@Parameters({ "node", "meetingName", "userName" })
	// @Test(priority = 26)
	public void acceptSnapshotRequest(String node, String meetingName, String userName) throws Exception {
		try {
			if (!userName.contains("User1") && !userName.contains("Host")) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);
				UtilityClass.snapshotUserAccept(driver, meetingName, userName, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName", "meetingName" })
	// @Test(priority = 27)
	public void snapShotwithoutAnnotation(String node, String userName, String meetingName) throws Exception {

		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);
				action.snapShotWithoutAnnotation(driver, driver1, meetingName, userName, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	// @Test(priority = 28)
	public void aceptSnapshotRequestwithoutAnnotation(String node, String meetingName, String userName)
			throws Exception {
		try {
			if (!userName.contains("User1") && !userName.contains("Host")) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);
				UtilityClass.snapshotUserAccept(driver, meetingName, userName, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName", "meetingName" })
	 @Test(priority = 29)
	public void chatViaAgent(String node, String userName, String meetingName) throws Exception {
		Thread.sleep(6000);
		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);
				action.chat(driver, driver1, meetingName, userName, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	 @Test(priority = 30)
	public void chatViaUsers(String node, String meetingName, String userName) throws Exception {
		try {
			/*
			if (!userName.contains("User1") && !userName.contains("Host")) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);
				UtilityClass.chatAccept(driver, meetingName, userName, node);
				*/
			LinkedHashMap<String, RemoteWebDriver>ls = new LinkedHashMap<String, RemoteWebDriver>(TestRunProperties.driverMap);
			if (userName.contains("User1") == true && ls.size()>2){

			for(int i =2; i<ls.size();i++) {

			WebDriver drivertemp = ls.get(node+"User"+i+"Demo1");
			UtilityClass.chatAccept(drivertemp, meetingName, userName, node);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName", "meetingName" })
	 @Test(priority = 31)
	public void downloadChatAgent(String node, String userName, String meetingName) throws Exception {

		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);
				action.downloadChat(driver, driver1, meetingName, userName, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "meetingName", "userName" })
	 @Test(priority = 32)
	public void downloadChatUser(String node, String meetingName, String userName) throws Exception {
		try {
			/*if (!userName.contains("User1") && !userName.contains("Host")) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);
				UtilityClass.downloadUsersChat(driver, meetingName, userName, node);*/
			LinkedHashMap<String, RemoteWebDriver>ls = new LinkedHashMap<String, RemoteWebDriver>(TestRunProperties.driverMap);
			if (userName.contains("User1") == true && ls.size()>2){

			for(int i =2; i<ls.size();i++) {

			WebDriver drivertemp = ls.get(node+"User"+i+"Demo1");
			UtilityClass.downloadUsersChat(drivertemp, meetingName, userName, node);
			}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName" })
	 @Test(priority = 33)
	public void endSession(String node, String userName) throws Exception {

		try {
			if (participant == 2 && userName.contains("User1") == true) {
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				ArrayList<WebDriver> drivers = new ArrayList<WebDriver>();
				drivers.add(driver1);
				drivers.add(driver);
				// action.screenShare(drivers);
				action.endSession(driver1);
				participant = 1;
				action.logout(driver, driver1, userName, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "node", "userName", "meetingName" })
	 @Test(priority = 34)
	public void agentLogout(String node, String userName, String meetingName) throws Exception {

		try {
			System.out.println("logout - checking if condition");
			if (participant == 1 && userName.contains("Host") == true) {
				System.out.println("logout - checking if condition - true");
				WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				Thread.sleep(4000);

			} else {
				System.out.println("logout - checking if condition - false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/*
	 * @Parameters({"node", "userName" })
	 * 
	 * //@Test(priority = 19) public void verifyUserSideSS(String node,String
	 * userName) throws Exception { try { if (!userName.contains("Host")) {
	 * WebDriver driver = TestRunProperties.driverMap.get(node + userName);
	 * Thread.sleep(7000);
	 * Assert.assertTrue(driver.findElement(CreateMeetingPage.screenshare_div).
	 * isDisplayed(), "Screenshare is not visible at Presentators/Users side.."); }
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */

	@Parameters({ "node", "meetingName", "FakeCame", "userName" })
	 @Test(priority = 35)
	public void closeMeeting(String node, String meetingName, String FakeCame, String userName) throws Exception {
		/*
		 * Assert.assertTrue(false, "Inside JoinMeeting " + node + " " + meetingName +
		 * " " + userName);
		 */
		try {
			if (userName.contains("Host") == true) {
				// WebDriver driver = TestRunProperties.driverMap.get(node + userName);
				// AddCanvas.clearAndCloseCanvas(driver);

				/*
				 * TestRunProperties.sessionUrl .add(new String[] { meetingName, userName, node,
				 * UtilityClass.CopiedURL, "Pass" }); TestCreateMeeting.sleep(3000);
				 * action.takeScreenShot(driver, meetingName, userName);
				 */
				if (userName.contains("Host") == true) {
					// ExecuteTestRun.totalNumberofRunningMeetings += 1;
					TestRunProperties.runningMeetings.put(meetingName, node);
				}
				int userCountPerMeeting = 0;
				if (TestRunProperties.userMeetingMap.containsKey(meetingName)) {
					userCountPerMeeting = TestRunProperties.userMeetingMap.get(meetingName).intValue();
				}
				userCountPerMeeting += 1;
				TestRunProperties.userMeetingMap.put(meetingName, userCountPerMeeting);

				synchronized (TestRunProperties.userNodeMap) {
					int userCount = 0;
					if (TestRunProperties.userNodeMap.containsKey(node.toString())) {
						userCount = TestRunProperties.userNodeMap.get(node.toString()).intValue();
						System.out.println(
								Thread.currentThread().getId() + "........" + node.toString() + " " + userCount);

					}
					userCount += 1;
					System.out.println(node.toString() + " " + userCount);
					TestRunProperties.userNodeMap.put(node.toString(), userCount);
				}
			}

		} catch (Exception e) {
			// TestRunProperties.sessionUrl.add(new String[]{"Null","Fail"});
			// TODO: handle exception
			e.printStackTrace();

		}
	}

	@Test
	@AfterSuite
	public void afterSuite() throws InterruptedException {
		// action.stopSS(driver1);

		// Write meeting details in excel sheet
		/*
		 * try { //ExcelData.writeExcelData(TestRunProperties.sessionUrl); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
	}

	public static void sleep(long milliSec) {
		try {
			Thread.sleep(milliSec);
		} catch (Exception e) {
		}
	}
}