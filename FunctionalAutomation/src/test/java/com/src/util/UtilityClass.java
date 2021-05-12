package com.src.util;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.src.execute.TestRunProperties;
import com.src.pages.CreateMeetingPage;
import com.src.testCases.TestCreateMeeting;

public class UtilityClass {
	String mainWindow, newWindow;
	static JavascriptExecutor js;
	public static String CopiedURL = " ";
	public static String lang = " ";

	public void setLang(WebDriver driver, String language) {
		try {
			lang = language;
			if (language.equalsIgnoreCase("english")) {
				Thread.sleep(1000);
				Assert.assertTrue(driver.findElement(CreateMeetingPage.engLang).isDisplayed(),
						"English language radio button is missing on landing page.");
				driver.findElement(CreateMeetingPage.engLang).click();
			} else if (language.equalsIgnoreCase("japanese")) {
				Assert.assertTrue(driver.findElement(CreateMeetingPage.japnLang).isDisplayed(),
						"Japanese language radio button is missing on landing page.");
				driver.findElement(CreateMeetingPage.japnLang).click();
				System.out.println("Language set to " + lang + " successfully.");
			}
		} catch (Exception e) {
			System.out.println("Exception in setLang method.." + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void userlogIn(WebDriver driver) {
		try {
			Assert.assertTrue(driver.findElement(CreateMeetingPage.txt_Email).isDisplayed(),
					"Email input box is missing on login page.");
			Assert.assertTrue(driver.findElement(CreateMeetingPage.txt_password).isDisplayed(),
					"Password input box is missing on login page.");
			driver.findElement(CreateMeetingPage.txt_Email).clear();
			driver.findElement(CreateMeetingPage.txt_password).clear();
			driver.findElement(CreateMeetingPage.txt_Email).sendKeys(TestRunProperties.uid);
			driver.findElement(CreateMeetingPage.txt_password).sendKeys(TestRunProperties.password);
			js = (JavascriptExecutor) driver;
			Assert.assertTrue(driver.findElement(CreateMeetingPage.btn_signIn).isDisplayed(),
					"SignIn button is missing on login page.");
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_signIn));
			System.out.println("Successfully logged in with Agent : " + TestRunProperties.uid);
		} catch (Exception e) {
			System.out.println("Exception in UserLogIn method.." + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void invalidUserlogIn(WebDriver driver) {
		try {
			Assert.assertTrue(driver.findElement(CreateMeetingPage.txt_Email).isDisplayed(),
					"Email input box is missing on login page.");
			Assert.assertTrue(driver.findElement(CreateMeetingPage.txt_password).isDisplayed(),
					"Password input box is missing on login page.");
			driver.findElement(CreateMeetingPage.txt_Email).sendKeys(TestRunProperties.invalidUid);
			driver.findElement(CreateMeetingPage.txt_password).sendKeys(TestRunProperties.invalidPassword);
			js = (JavascriptExecutor) driver;
			Assert.assertTrue(driver.findElement(CreateMeetingPage.btn_signIn).isDisplayed(),
					"SignIn button is missing on login page.");
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_signIn));
			Assert.assertTrue(driver.findElement(CreateMeetingPage.errorMsg).isDisplayed(),
					"Invalid credentials message is missing.");
			String msg = driver.findElement(CreateMeetingPage.errorMsg).getText();
			if (UtilityClass.lang == "english") {
				Assert.assertEquals(msg, "Invalid Credentials", "Invalid credentials error message text mismatch.");

			} else if (UtilityClass.lang == "japanese") {
				Assert.assertEquals(msg, "無効な認証情報です", "Invalid credentials error message text mismatch.");

			}

			Thread.sleep(2000);
			System.out.println("Test passed successfully for invalid credentials.");
		} catch (Exception e) {
			System.out.println("Exception in invalidUserLogIn method.." + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void createSession(WebDriver driver, String meetingName) {
		try {
			// Thread.sleep(4000);
			TestRunProperties.explicitWait(driver, CreateMeetingPage.creatMeeting);
			Assert.assertTrue(driver.findElement(CreateMeetingPage.creatMeeting).isDisplayed(),
					"Create Meeting option is missing");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.creatMeeting);
			// driver.findElement(CreateMeetingPage.creatMeeting).click();
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.creatMeeting));
			Thread.sleep(2000);
			// driver.findElement(CreateMeetingPage.setup_New_Meeting).click();
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.setup_New_Meeting));
			Assert.assertTrue(driver.findElement(CreateMeetingPage.txt_SessionName).isDisplayed(),
					"Enter session name input box is missing");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.txt_SessionName);
			driver.findElement(CreateMeetingPage.txt_SessionName).sendKeys(meetingName);
			Assert.assertTrue(driver.findElement(CreateMeetingPage.btn_Proceed).isDisplayed(),
					"'Send Invite and Start' button is missing for create session.");
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_Proceed));
			System.out.println("Successfully created a session");
			TestCreateMeeting.sleep(2000);
		} catch (Exception e) {
			System.out.println("Exception in createSession method.." + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void audioCall(ArrayList<WebDriver> drivers, String meetingName, String userName, String node) {
		try {
			// Thread.sleep(7000);
			TestRunProperties.explicitWait(drivers.get(0), CreateMeetingPage.audio_icon);
			js.executeScript("arguments[0].click();", drivers.get(0).findElement(CreateMeetingPage.audio_icon));
			Thread.sleep(7000);
			TestRunProperties.explicitWait(drivers.get(1), CreateMeetingPage.btn_Confirm);

			WebElement modalContainer = drivers.get(1).findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			Assert.assertTrue(modalContent.isDisplayed());
			modalContent.click();
			TestRunProperties.explicitWait(drivers.get(1), CreateMeetingPage.assertAudioStrip);
			Assert.assertTrue(drivers.get(0).findElement(CreateMeetingPage.assertAudioStrip).isDisplayed(),
					"Audio strip is not visible");
			Thread.sleep(2000);
			System.out.println("Audio call established Successfully");
		} catch (Exception e) {
			TestRunProperties.sessionUrl.add(new String[] { meetingName, userName, node, "Null", "Fail" });
			System.out.println("Exception in audioCall call method.." + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void addParticipant(WebDriver driver, String meetingName, String userName, String node, String platform) {
		try {
			// Thread.sleep(6000);
			TestRunProperties.explicitWait(driver, CreateMeetingPage.img_AddInvitee);
			assertTrue(driver.findElement(CreateMeetingPage.img_AddInvitee).isDisplayed(),
					"Participant button is missing.");
			Thread.sleep(2000);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click()", driver.findElement(CreateMeetingPage.img_AddInvitee));
			// Thread.sleep(4000);
			TestRunProperties.explicitWait(driver, CreateMeetingPage.btn_CopySessionLink);
			assertTrue(driver.findElement(CreateMeetingPage.btn_CopySessionLink).isDisplayed(),
					"Copy Session link button is missing.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.btn_CopySessionLink);
			driver.findElement(CreateMeetingPage.btn_CopySessionLink).click();
			Thread.sleep(2000);
			assertTrue(driver.findElement(CreateMeetingPage.btn_ManualEntry).isDisplayed(),
					"Manual Entry modal is missing.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.btn_ManualEntry);
			driver.findElement(CreateMeetingPage.btn_ManualEntry).click();
			Thread.sleep(1000);
			assertTrue(driver.findElement(CreateMeetingPage.txt_CopyURL).isDisplayed(),
					"Enter email input box is missing to paste copied URL.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.txt_CopyURL);

			if (platform.equalsIgnoreCase("MAC")) {

				driver.findElement(CreateMeetingPage.txt_CopyURL).sendKeys(Keys.SHIFT, Keys.INSERT);
			} else {

				driver.findElement(CreateMeetingPage.txt_CopyURL).sendKeys(Keys.chord(Keys.CONTROL, "v"));
			}
			CopiedURL = driver.findElement(CreateMeetingPage.txt_CopyURL).getAttribute("value");
			System.out.println("Session Joining URL: " + CopiedURL);
			Thread.sleep(3000);
			assertTrue(driver.findElement(CreateMeetingPage.img_Close_manual).isDisplayed(),
					"Close icon is missing for manual entry modal.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.img_Close_manual);
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Close_manual));
			Thread.sleep(2000);
			assertTrue(driver.findElement(CreateMeetingPage.img_Close_participant).isDisplayed(),
					"Close icon is missing for Participant modal.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.img_Close_participant);
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Close_participant));
			System.out.println("Successfully Copied the session Joining URL");
			// TestRunProperties.sessionUrl.add(new
			// String[]{UtilityClass.CopiedURL,"Pass"});
			Thread.sleep(3000);

		} catch (Exception e) {
			System.out.println("Exception in addParticipant method.." + e);
			// TestRunProperties.sessionUrl.add(new String[] { meetingName, userName, node,
			// "Null", "Fail" });
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void videoCall(ArrayList<WebDriver> drivers, String meetingName, String userName, String node) {
		try {
			Thread.sleep(2000);
			TestRunProperties.explicitWait(drivers.get(0), CreateMeetingPage.img_Video);
			js.executeScript("arguments[0].click();", drivers.get(0).findElement(CreateMeetingPage.img_Video));
			// Accept video Request at User side
			Thread.sleep(6000);
			TestRunProperties.explicitWait(drivers.get(1), CreateMeetingPage.accept_modal);
			WebElement modalContainer = drivers.get(1).findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			modalContent.click();
			TestRunProperties.explicitWait(drivers.get(0), CreateMeetingPage.assertVideoStrip);
			Assert.assertTrue(drivers.get(0).findElement(CreateMeetingPage.assertVideoStrip).isDisplayed(),
					"VideoFilm strip is missing...");
			Thread.sleep(2000);
			System.out.println("Video call established Successfully");
		} catch (Exception e) {
			TestRunProperties.sessionUrl.add(new String[] { meetingName, userName, node, "Null", "Fail" });
			System.out.println("Exception in videoCall method.." + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void screenShare(ArrayList<WebDriver> drivers) throws InterruptedException {
		Thread.sleep(2000);
		try {
			TestRunProperties.explicitWait(drivers.get(0), CreateMeetingPage.screenShare_icon);
			Assert.assertTrue(drivers.get(0).findElement(CreateMeetingPage.screenShare_icon).isDisplayed(),
					"Screenshare icon is missing...");
			js.executeScript("arguments[0].click();", drivers.get(0).findElement(CreateMeetingPage.screenShare_icon));
			Thread.sleep(3000);
			Assert.assertTrue(drivers.get(0).findElement(CreateMeetingPage.screenshare_div).isDisplayed(),
					"Screenshare is not visible at Presentators side..");
			Thread.sleep(5000);
			drivers.get(1).findElement(CreateMeetingPage.screenshare_div);
			Assert.assertTrue(drivers.get(1).findElement(CreateMeetingPage.screenshare_div).isDisplayed(),
					"Screenshare is not visible at user side..");
			System.out.println("Screen Shared Successfully...");

		} catch (Exception e) {
			System.out.println("Exception in screenShare method.." + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void audioVideoAccept(WebDriver driver, String meetingName, String userName, String node) {
		try {
			Thread.sleep(6000);
			TestRunProperties.explicitWait(driver, CreateMeetingPage.btn_Confirm);
			System.out.println("CreateMeetingPage.btn_Confirm");
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			System.out.println("CreateMeetingPage.accept_modal");
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			System.out.println("CreateMeetingPage.btn_Confirm");
			modalContent.click();
			System.out.println("modalContent.click();");
			Thread.sleep(10000);
			System.out.println("Audio request accept successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void audioVideoDeny(WebDriver driver, String meetingName, String userName, String node) {
		try {
			Thread.sleep(8000);
			assertTrue(driver.findElement(CreateMeetingPage.btn_Deny).isDisplayed(),
					"Accept model deny button is missing for User.");
			TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Deny);
			System.out.println("CreateMeetingPage.btn_Deny");
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			System.out.println("CreateMeetingPage.accept_modal");
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Deny);
			System.out.println("CreateMeetingPage.btn_Deny");
			modalContent.click();
			System.out.println("modalContent.click();");
			Thread.sleep(10000);
			System.out.println("Audio request deny successfully");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void shareLocation(WebDriver driver, WebDriver driver1, String meetingName, String userName, String node) {
		try {
			// TestCreateMeeting.sleep(4000);
			TestRunProperties.explicitWait(driver1, CreateMeetingPage.img_Location);
			JavascriptExecutor js = (JavascriptExecutor) driver1;
			assertTrue(driver1.findElement(CreateMeetingPage.img_Location).isDisplayed(),
					"Location icon is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.img_Location));
			// driver.switchTo().window(tabs.get(1));
			UtilityClass.audioVideoAccept(driver, meetingName, userName, node);
			assertTrue(driver.findElement(CreateMeetingPage.location_div).isDisplayed(),
					"Shared Location is not visible for user.");
			// TestCreateMeeting.sleep(6000);
			TestRunProperties.explicitWait(driver1, CreateMeetingPage.location_div);
			assertTrue(driver1.findElement(CreateMeetingPage.location_div).isDisplayed(),
					"Shared Location is not visible for Agent.");
			assertTrue(driver1.findElement(CreateMeetingPage.btn_DnldToCanvas).isDisplayed(),
					"DOWNLOAD TO EDIT ON CANVAS button is missing for Agent.");
			TestCreateMeeting.sleep(2000);
			assertTrue(driver1.findElement(CreateMeetingPage.btn_ShareLiveLoc).isDisplayed(),
					"SHARE LIVE LOCATION button is missing for Agent.");
			driver1.findElement(CreateMeetingPage.btn_ShareLiveLoc).click();
			System.out.println("Sharing live location..");
			TestCreateMeeting.sleep(2000);
			assertTrue(driver1.findElement(CreateMeetingPage.btn_StopShareLiveLoc).isDisplayed(),
					"STOP SHARE LIVE LOCATION button is missing for Agent.");
			assertTrue(driver1.findElement(CreateMeetingPage.closeModal).isDisplayed(),
					"Close button is missing for share location modal.");
			// TestCreateMeeting.sleep(4000);
			TestRunProperties.explicitWait(driver1, CreateMeetingPage.closeModal);
			driver1.findElement(CreateMeetingPage.closeModal).click();
		} catch (Exception e) {
			// TestRunProperties.sessionUrl.add(new String[] { meetingName, userName, node,
			// "Null", "Fail" });
			System.out.println("Exception in shareLocation method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void snapShotWithoutAnnotation(WebDriver driver, WebDriver driver1, String meetingName, String userName,
			String node) {
		try {
			TestCreateMeeting.sleep(4000);
			JavascriptExecutor js = (JavascriptExecutor) driver1;
			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_icon).isDisplayed(),
					"SnapShot icon is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_icon));
			// UtilityClass.audioVideoAccept(driver, meetingName, userName, node);
			// assertTrue(driver.findElement(CreateMeetingPage.snapShot_div).isDisplayed(),
			// "Snapshot is not visible for user.");
			TestCreateMeeting.sleep(6000);
			// assertTrue(driver1.findElement(CreateMeetingPage.snapShot_div).isDisplayed(),
			// "Snapshot is not visible for Agent.");
			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_User1_icon).isDisplayed(),
					"Snapshot User1 icon is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_User1_icon));
			Thread.sleep(4000);
			try {
				TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Confirm);
				WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
				WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
				modalContent.click();
			} catch (Exception e) {
				System.out.println("Exception in snapshot - user request block");
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
			TestCreateMeeting.sleep(15000);
			assertTrue(driver1.findElement(CreateMeetingPage.takeSnapshot_Btn).isDisplayed(),
					"Take Snapshot button is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.takeSnapshot_Btn));
		} catch (Exception e) {
			System.out.println("Exception in snapShot() method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void logout(WebDriver driver, WebDriver driver1, String userName, String node) {
		try {
			TestCreateMeeting.sleep(4000);
			TestRunProperties.explicitWait(driver1, CreateMeetingPage.user_profile_btn);
			JavascriptExecutor js = (JavascriptExecutor) driver1;
			assertTrue(driver1.findElement(CreateMeetingPage.user_profile_btn).isDisplayed(),
					"User profile button is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.user_profile_btn));

			assertTrue(driver1.findElement(CreateMeetingPage.logout_btn).isDisplayed(),
					"Logout button is missing for Agent.");
			System.out.println("click on user profile button");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.logout_btn));
			System.out.println("click on logout button");

		} catch (Exception e) {
			System.out.println("Exception in logout() method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void chat(WebDriver driver, WebDriver driver1, String meetingName, String userName, String node) {
		try {
			TestCreateMeeting.sleep(4000);
			JavascriptExecutor jse = (JavascriptExecutor) driver1;
			jse.executeScript("scroll(0, -250);");
			Thread.sleep(6000);
			assertTrue(driver1.findElement(CreateMeetingPage.chat_icon).isDisplayed(),
					"Chat icon is missing for Agent.");
			driver1.findElement(CreateMeetingPage.chat_icon).click();
			assertTrue(driver1.findElement(CreateMeetingPage.chatTextarea).isDisplayed(),
					"Chat text area is missing for Agent.");
			driver1.findElement(CreateMeetingPage.chatTextarea).sendKeys("Hi i am agent");
			assertTrue(driver1.findElement(CreateMeetingPage.send_btn).isDisplayed(),
					"Send button is missing for Agent.");
			driver1.findElement(CreateMeetingPage.send_btn).click();

			driver1.findElement(CreateMeetingPage.chatTextarea).sendKeys("https://waagu.com/");
			driver1.findElement(CreateMeetingPage.send_btn).click();
			driver1.findElement(CreateMeetingPage.URLlink_chatbox).click();
			String parentwindowHandle = driver1.getWindowHandle();
			Set<String> windowHandles = driver1.getWindowHandles();
			System.out.println("Total number of windows - " + windowHandles.size());
			String subWindow = null;
			Iterator<String> iterator = windowHandles.iterator();
			while (iterator.hasNext()) {
				subWindow = iterator.next();
			}
			TestCreateMeeting.sleep(4000);
			driver1.switchTo().window(subWindow);
			System.out.println(driver1.getTitle());
			driver1.close();
			driver1.switchTo().window(parentwindowHandle);
			System.out.println(driver1.getTitle());
			Thread.sleep(5000);
			TestRunProperties.explicitWait(driver, CreateMeetingPage.chatWindow);
			assertTrue(driver.findElement(CreateMeetingPage.chatWindow).isDisplayed(),
					"Chat window is missing for " + userName);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.chatWindow);
			assertTrue(driver.findElement(CreateMeetingPage.chatTextarea).isDisplayed(),
					"Chat text area is missing for " + userName);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.chatTextarea);
			modalContent.sendKeys("Hi i am " + userName);
			assertTrue(driver.findElement(CreateMeetingPage.send_btn).isDisplayed(),
					"Send button is missing for " + userName);
			modalContent = modalContainer.findElement(CreateMeetingPage.send_btn);
			modalContent.click();

		} catch (Exception e) {
			System.out.println("Exception in chat() method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void chatAccept(WebDriver driver, String meetingName, String userName, String node) {
		try {
			Thread.sleep(3000);
			TestRunProperties.explicitWait(driver, CreateMeetingPage.chatWindow);
			assertTrue(driver.findElement(CreateMeetingPage.chatWindow).isDisplayed(),
					"Chat window is missing for " + userName);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.chatWindow);
			assertTrue(driver.findElement(CreateMeetingPage.chatTextarea).isDisplayed(),
					"Chat text area is missing for " + userName);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.chatTextarea);
			modalContent.sendKeys("Hi i am " + userName);
			assertTrue(driver.findElement(CreateMeetingPage.send_btn).isDisplayed(),
					"Send button is missing for " + userName);
			//modalContent = modalContainer.findElement(CreateMeetingPage.send_btn);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.send_btn));

			//modalContent.click();
		} catch (Exception e) {
			System.out.println("Exception in chatAccept() method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void downloadChat(WebDriver driver, WebDriver driver1, String meetingName, String userName, String node) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver1;
			TestCreateMeeting.sleep(4000);
			assertTrue(driver1.findElement(CreateMeetingPage.chat_download_icon).isDisplayed(),
					"Download chat icon is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.chat_download_icon));

			TestRunProperties.explicitWait(driver, CreateMeetingPage.chatWindow);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.chatWindow);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.chat_download_icon);
			modalContent.click();

		} catch (Exception e) {
			System.out.println("Exception in downloadChat() method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void downloadUsersChat(WebDriver driver, String meetingName, String userName, String node) {
		try {
			Thread.sleep(3000);
			TestRunProperties.explicitWait(driver, CreateMeetingPage.chatWindow);
		//	WebElement modalContainer = driver.findElement(CreateMeetingPage.chatWindow);
			//WebElement modalContent = modalContainer.findElement(CreateMeetingPage.chat_download_icon);
			//modalContent.click();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.chat_download_icon));
			
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("Exception in downloadUsersChat() method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void snapShot(WebDriver driver, WebDriver driver1, String meetingName, String userName, String node) {
		try {
			TestCreateMeeting.sleep(4000);
			JavascriptExecutor js = (JavascriptExecutor) driver1;
			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_icon).isDisplayed(),
					"SnapShot icon is missing for Agent.");
			System.out.println("pointer 0");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_icon));
		
			Thread.sleep(5000);

			

			JavascriptExecutor js1 = (JavascriptExecutor) driver;
			TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Confirm);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			// modalContent.click();
			js1.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_Confirm));

		


			TestCreateMeeting.sleep(5000);


			WebElement element = driver1.findElement(CreateMeetingPage.canvas);

			// where your canvas element is
			Random random = new Random();
			Actions act = new Actions(driver1);

			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < 1; j++) {



					int a = random.nextInt(200);
					int b = random.nextInt(200);
					act.clickAndHold(element).moveToElement(element, a, b).build().perform();
					act.moveToElement(element, (-a), (-b)).clickAndHold(element).build().perform();

					act.clickAndHold(element).moveToElement(element, a, (-b)).build().perform();
					act.moveToElement(element, a, (-b)).clickAndHold(element).build().perform();

					act.clickAndHold(element).moveToElement(element, (-a), b).build().perform();
					act.moveToElement(element, (-a), b).clickAndHold(element).release().build().perform();

				}
			}

			System.out.println("Line draw's on canvas.");
			Thread.sleep(3000);
			assertTrue(driver1.findElement(CreateMeetingPage.takeSnapshot_Btn).isDisplayed(),
					"Clear canvas button is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.clearCanvas_Btn));
			System.out.println("Canvas is clear");

			assertTrue(driver1.findElement(CreateMeetingPage.takeSnapshot_Btn).isDisplayed(),
					"Take Snapshot button is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.takeSnapshot_Btn));
			System.out.println("Successfully taken the Snapshot");
		} catch (Exception e) {
			System.out.println("Exception in snapShot() method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	public void snapShotMultiUser(WebDriver driver, WebDriver driver1, String meetingName, String userName, String node) {
		try {
			TestCreateMeeting.sleep(4000);
			JavascriptExecutor js = (JavascriptExecutor) driver1;
			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_icon).isDisplayed(),
					"SnapShot icon is missing for Agent.");
			System.out.println("pointer 0");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_icon));
			// UtilityClass.audioVideoAccept(driver, meetingName, userName, node);
			// assertTrue(driver.findElement(CreateMeetingPage.snapShot_div).isDisplayed(),
			// "Snapshot is not visible for user.");

			TestCreateMeeting.sleep(3000);

			//assertTrue(driver1.findElement(CreateMeetingPage.snapShot_div).isDisplayed(),
				//	"Snapshot is not visible for Agent.");
			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_User1_icon).isDisplayed(),
					"Snapshot User1 icon is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_User1_icon));
			

			Thread.sleep(3000);

			
			
//from
		/*	System.out.println("pointer 1");

			JavascriptExecutor js1 = (JavascriptExecutor) driver;
			TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Confirm);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			// modalContent.click();
			js1.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_Confirm));

			System.out.println("pointer 2");
//this  */
			
			 LinkedHashMap<String, RemoteWebDriver>ls = new LinkedHashMap<String, RemoteWebDriver>(TestRunProperties.driverMap);
				if (userName.contains("User1") == true && ls.size()>2){
					
					for(int i =1; i<ls.size();i++) {
						
						WebDriver drivertemp = ls.get(node+"User"+i+"Demo1");
						UtilityClass.multiUserAcceptSnapshot(drivertemp, meetingName, userName, node);
					}
				}

			TestCreateMeeting.sleep(3000);

			WebElement element = driver1.findElement(CreateMeetingPage.canvas);

			// where your canvas element is
			Random random = new Random();
			Actions act = new Actions(driver1);

			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < 1; j++) {
					int a = random.nextInt(200);
					int b = random.nextInt(200);
					act.clickAndHold(element).moveToElement(element, a, b).build().perform();
					act.moveToElement(element, (-a), (-b)).clickAndHold(element).build().perform();

					act.clickAndHold(element).moveToElement(element, a, (-b)).build().perform();
					act.moveToElement(element, a, (-b)).clickAndHold(element).build().perform();

					act.clickAndHold(element).moveToElement(element, (-a), b).build().perform();
					act.moveToElement(element, (-a), b).clickAndHold(element).release().build().perform();

				}
			}

			System.out.println("Line draw's on canvas.");
			Thread.sleep(2000);
			assertTrue(driver1.findElement(CreateMeetingPage.takeSnapshot_Btn).isDisplayed(),
					"Clear canvas button is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.clearCanvas_Btn));
			System.out.println("Canvas is clear");

			assertTrue(driver1.findElement(CreateMeetingPage.takeSnapshot_Btn).isDisplayed(),
					"Take Snapshot button is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.takeSnapshot_Btn));
			System.out.println("Successfully taken the Snapshot with multiusers");
		} catch (Exception e) {
			System.out.println("Exception in snapShotMulti user method... " + userName);
		
		}
	}
	
	public static void multiUserAcceptSnapshot(WebDriver driver, String meetingName, String userName, String node) {
		try {
			

			JavascriptExecutor js1 = (JavascriptExecutor) driver;
			TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Confirm);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			// modalContent.click();
			js1.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_Confirm));
			
		} catch (Exception e) {
			System.out.println("Exception with "+userName+" in multiuser Acceptsnapshot");
		}
	}

	public void denysnapShotMultiUser(WebDriver driver, WebDriver driver1, String meetingName, String userName, String node) {
		try {
			TestCreateMeeting.sleep(4000);
			JavascriptExecutor js = (JavascriptExecutor) driver1;
			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_icon).isDisplayed(),
					"SnapShot icon is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_icon));
			TestCreateMeeting.sleep(6000);
			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_User1_icon).isDisplayed(),
					"Snapshot User1 icon is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_User1_icon));
			Thread.sleep(8000);
/*
			try {
				assertTrue(driver.findElement(CreateMeetingPage.btn_Deny).isDisplayed(),
						"Accept model deny button is missing for User.");
				TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Deny);
				WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
				WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Deny);
				modalContent.click();
				System.out.println("Successfully denied the Snapshot request");
			} catch (Exception e) {
				System.out.println("Exception in multi user snapshot deny - user request block");
				
			}*/
			 LinkedHashMap<String, RemoteWebDriver>ls = new LinkedHashMap<String, RemoteWebDriver>(TestRunProperties.driverMap);
				if (userName.contains("User1") == true && ls.size()>2){
					
					for(int i =1; i<ls.size();i++) {
						
						WebDriver drivertemp = ls.get(node+"User"+i+"Demo1");
						UtilityClass.multiUserDenySnapshot(drivertemp, meetingName, userName, node);
					}
				}
			TestCreateMeeting.sleep(4000);

			assertTrue(driver1.findElement(CreateMeetingPage.close_snapShot_icon).isDisplayed(),
					"close Snapshot button is missing for Agent.");
			driver1.findElement(CreateMeetingPage.close_snapShot_icon).click();
			TestRunProperties.explicitWait(driver1, CreateMeetingPage.btn_Confirm);
			WebElement modalContainer1 = driver1.findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent1 = modalContainer1.findElement(CreateMeetingPage.btn_Confirm);
			modalContent1.click();

		} catch (Exception e) {
			System.out.println("Exception in denySnapShotMultiuser() method... " + e);
			
		}
	}
	public static void multiUserDenySnapshot(WebDriver driver, String meetingName, String userName, String node) {
		try {
			assertTrue(driver.findElement(CreateMeetingPage.btn_Deny).isDisplayed(),
					"Accept model deny button is missing for User.");
			TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Deny);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Deny);
			modalContent.click();
			System.out.println("Successfully denied the Snapshot request");
			
			
		} catch (Exception e) {
			System.out.println("Exception with "+userName+" in multiuser denysnapshot");
		}
	}
	
	public void denysnapShot(WebDriver driver, WebDriver driver1, String meetingName, String userName, String node) {
		try {
			TestCreateMeeting.sleep(4000);
			JavascriptExecutor js = (JavascriptExecutor) driver1;
			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_icon).isDisplayed(),
					"SnapShot icon is missing for Agent.");
			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_icon));
			System.out.println("click on snapshot icon");
//			TestCreateMeeting.sleep(6000);
//			assertTrue(driver1.findElement(CreateMeetingPage.snapShot_User1_icon).isDisplayed(),
//					"Snapshot User1 icon is missing for Agent.");
//			js.executeScript("arguments[0].click();", driver1.findElement(CreateMeetingPage.snapShot_User1_icon));
			System.out.println("click on select user icon");
			Thread.sleep(8000);
			try {
				assertTrue(driver.findElement(CreateMeetingPage.btn_Deny).isDisplayed(),
						"Accept model deny button is missing for User.");
				TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Deny);
				System.out.println("find button deny");
				WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
				WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Deny);
				System.out.println("click on deny");
				modalContent.click();
				System.out.println("Successfully denied the Snapshot request");
			} catch (Exception e) {
				System.out.println("Exception in snapshot deny - user request block");
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
//			TestCreateMeeting.sleep(4000);

//			assertTrue(driver1.findElement(CreateMeetingPage.close_snapShot_icon).isDisplayed(),
//					"close Snapshot button is missing for Agent.");
//			driver1.findElement(CreateMeetingPage.close_snapShot_icon).click();
//			TestRunProperties.explicitWait(driver1, CreateMeetingPage.btn_Confirm);
//			WebElement modalContainer1 = driver1.findElement(CreateMeetingPage.accept_modal);
//			WebElement modalContent1 = modalContainer1.findElement(CreateMeetingPage.btn_Confirm);
//			modalContent1.click();

		} catch (Exception e) {
			System.out.println("Exception in denySnapShot() method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void snapshotUserAccept(WebDriver driver, String meetingName, String userName, String node) {
		try {
			Thread.sleep(3000);
			TestRunProperties.SnapshotExplicitWait(driver, CreateMeetingPage.btn_Confirm);
			System.out.println("CreateMeetingPage.btn_Confirm");
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			System.out.println("CreateMeetingPage.accept_modal");
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			System.out.println("CreateMeetingPage.btn_Confirm");
			modalContent.click();
			System.out.println("modalContent.click();");
			Thread.sleep(10000);
			System.out.println("Audio request accept successfully");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void takeScreenShot(WebDriver driver, String meetingName, String userName) {
		try {
			System.out.print("\n Taking Screenshot...");
			Thread.sleep(2000);
			TakesScreenshot scrshot = ((TakesScreenshot) driver);
			File srcFile = scrshot.getScreenshotAs(OutputType.FILE);
			File DestFile = new File(
					System.getProperty("user.dir") + "//Screenshots//" + meetingName + "//" + userName + ".png");
			Thread.sleep(2000);
			FileUtils.copyFile(srcFile, DestFile);
			System.out.print("\nScreenshot taken Successfully...");
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("Exception in Screenshot method... " + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static boolean isElementPresent(WebDriver driver, By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public static void setOptions(String userName, String node, boolean fakeCame, String browser,
			DesiredCapabilities capa) {
		if (browser.equals("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			options.setPageLoadStrategy(PageLoadStrategy.NONE);
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			if (fakeCame == true) {
				// TBC
				System.out.println("Fake Camera is true for " + node);

				options.addArguments("--allow-file-access-from-files", "--allow-file-access", "--window-size=1366,768",
						"--allow-file-access-from-files", "--use-fake-ui-for-media-stream",
						"--use-file-for-fake-audio-capture=" + System.getProperty("user.dir") + "/Images/Example.wav",
						"--use-fake-device-for-media-stream", "--user-data-dir=/tmp/" + userName,
						"--ignore-certificate-errors");
			} else {
				// TBC
				System.out.println("Fake Camera is false for " + node);

				options.addArguments("--allow-file-access-from-files", "--allow-file-access", "--window-size=1366,768",
						"--allow-file-access-from-files", "--use-fake-ui-for-media-stream", "--allow-file-access",
						"--user-data-dir=/tmp/" + userName, "--ignore-certificate-errors");
			}
			options.addArguments("enable-usermedia-screen-capturing");
			options.addPreference("permissions.default.microphone", 1);
			options.addPreference("permissions.default.camera", 1);
			capa.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capa.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
		} else if (browser.equals("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.setPageLoadStrategy(PageLoadStrategy.NONE);
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
			if (fakeCame == true) {
				// TBC
				System.out.println("Fake Camera is true for " + node);
				options.addArguments("--allow-file-access-from-files", "--allow-file-access", "--window-size=1366,768",
						"--allow-file-access-from-files", "--use-fake-ui-for-media-stream",
						"--use-file-for-fake-audio-capture=" + System.getProperty("user.dir") + "/Images/Example.wav",
						"--use-fake-device-for-media-stream", "--user-data-dir=/tmp/" + userName,
						"--ignore-certificate-errors");
			} else {
				// TBC
				System.out.println("Fake Camera is false for " + node);

				options.addArguments("--allow-file-access-from-files", "--allow-file-access", "--window-size=1366,768",
						"--allow-file-access-from-files", "--use-fake-ui-for-media-stream", "--allow-file-access",
						"--user-data-dir=/tmp/" + userName, "--ignore-certificate-errors");
			}
			// options.addArguments("permissions.default.microphone");
			// options.addArguments("permissions.default.camera");
			capa.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capa.setCapability(ChromeOptions.CAPABILITY, options);

		} else if (browser.equals("iexplore")) {
			// InternetExplorerDriver options = new InternetExplorerDriver();
			/*
			 * WebDriverManager.iedriver().setup(); capa = new DesiredCapabilities();
			 * capa.setCapability(InternetExplorerDriver.
			 * INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			 * capa.setCapability(InternetExplorerDriver. IE_ENSURE_CLEAN_SESSION, true);
			 * capa.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			 * capa.setCapability(InternetExplorerDriver. ENABLE_ELEMENT_CACHE_CLEANUP,
			 * true); capa.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");
			 * System.out.println("IELocal"); //capa.setCapability(ChromeOptions.CAPABILITY,
			 * options); System.setProperty("webdriver.ie.driver",
			 * System.getProperty("user.dir") +
			 * "/src/test/resources/drivers/IEDriverServer.exe"); InternetExplorerOptions
			 * IEOptions = new InternetExplorerOptions();
			 * IEOptions.AddAdditionalCapability("ignoreProtectedModeSettings", true);
			 * IEOptions.AddAdditionalCapability("EnsureCleanSession", true); driver = new
			 * RemoteWebDriver( new Uri(hubUrl), IEOptions.ToCapabilities(), timeSpan );
			 */
			capa.setBrowserName("internet explorer");
			capa.setPlatform(Platform.WINDOWS);
			capa.setVersion("11");
			// capa.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			// capa.setCapability("acceptInsecureCerts", true);
			// capa.setCapability("acceptSslCerts",true);
			// capa.setAcceptInsecureCerts(true);
			capa.acceptInsecureCerts();

		}

	}

	public void stopAudioVideoCall(WebDriver driver, String calltype) {
		try {
			Thread.sleep(3000);
			assertTrue(driver.findElement(CreateMeetingPage.endCall_icon).isDisplayed(), "End call icon is missing.");
			driver.findElement(CreateMeetingPage.endCall_icon).click();
			Thread.sleep(3000);
			assertTrue(driver.findElement(CreateMeetingPage.btn_Confirm).isDisplayed(),
					"Yes button on confirmation Modal is missing.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.btn_Confirm);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			modalContent.click();
			TestRunProperties.explicitWaitInvisibility(driver, CreateMeetingPage.assertAudioEndBT);
			if (calltype.equalsIgnoreCase("audio")) {
				Assert.assertFalse(driver.findElement(CreateMeetingPage.audio_icon).isSelected());
			} else if (calltype.equalsIgnoreCase("video")) {
				Assert.assertFalse(driver.findElement(CreateMeetingPage.img_Video).isSelected());
			}
			Thread.sleep(3000);
			System.out.println("Audio/Video call Ended Successfully...");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void stopSS(WebDriver driver) throws InterruptedException {
		try {
			Thread.sleep(3000);
			assertTrue(driver.findElement(CreateMeetingPage.endSS_icon).isDisplayed(), "End call icon is missing.");

			// driver.findElement(CreateMeetingPage.endSS_icon).click();
			JavascriptExecutor jss = (JavascriptExecutor) driver;
			jss.executeScript("arguments[0].click()", driver.findElement(CreateMeetingPage.endSS_icon));
			Thread.sleep(5000);
			assertTrue(driver.findElement(CreateMeetingPage.btn_Confirm).isDisplayed(),
					"Yes button on confirmation Modal is missing.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.btn_Confirm);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.btn_Confirm);
			modalContent.click();
			Thread.sleep(3000);
			Assert.assertFalse(driver.findElement(CreateMeetingPage.screenShare_icon).isSelected());

			System.out.println("ScreenShare Ended Successfully...");
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	public void endSession(WebDriver driver) throws InterruptedException {
		try {
			Thread.sleep(3000);

			assertTrue(driver.findElement(CreateMeetingPage.leaveSessionBtn).isDisplayed(),
					"End Session button is missing.");
			driver.findElement(CreateMeetingPage.leaveSessionBtn).click();
			Thread.sleep(3000);
			assertTrue(driver.findElement(CreateMeetingPage.leave_Session_Accept).isDisplayed(),
					"Yes button on confirmation Modal is missing.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.leave_Session_Accept);
			WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
			WebElement modalContent = modalContainer.findElement(CreateMeetingPage.leave_Session_Accept);
			modalContent.click();
			Thread.sleep(2000);
			System.out.println("Session Ended Successfully...");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void admitUser(WebDriver driver) throws InterruptedException {
		try {
			// Thread.sleep(15000);
			TestRunProperties.explicitWait(driver, CreateMeetingPage.img_AddInvitee);
			assertTrue(driver.findElement(CreateMeetingPage.img_AddInvitee).isDisplayed(),
					"Participant button is missing.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.img_AddInvitee);
			Thread.sleep(2000);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click()", driver.findElement(CreateMeetingPage.img_AddInvitee));
			Thread.sleep(5000);
			boolean var = true;
			while (var) {
				boolean user = false;
				try {
					user = driver.findElement(CreateMeetingPage.waitingUsers).isDisplayed();
				} catch (Exception e) {
				}
				if (user == true) {
					Actions action = new Actions(driver);
					action.moveToElement(driver.findElement(CreateMeetingPage.waitingUsers)).perform();
					Thread.sleep(5000);
					assertTrue(driver.findElement(CreateMeetingPage.admit_btn).isDisplayed(),
							"Admit button is missing for User.");
					driver.findElement(CreateMeetingPage.admit_btn).click();
					var = true;
				} else {
					Thread.sleep(2000);
					assertTrue(driver.findElement(CreateMeetingPage.img_Close_participant).isDisplayed(),
							"Admit button is missing for User.");
					driver.findElement(CreateMeetingPage.img_Close_participant).click();
					var = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void dismissUser(WebDriver driver) throws InterruptedException {
		try {
			Thread.sleep(15000);
			assertTrue(driver.findElement(CreateMeetingPage.img_AddInvitee).isDisplayed(),
					"Participant button is missing.");
			TestRunProperties.explicitWait(driver, CreateMeetingPage.img_AddInvitee);
			Thread.sleep(4000);
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click()", driver.findElement(CreateMeetingPage.img_AddInvitee));
			Thread.sleep(10000);
			boolean var = true;
			while (var) {
				boolean user = false;
				try {
					user = driver.findElement(CreateMeetingPage.waitingUsers).isDisplayed();
				} catch (Exception e) {
				}
				if (user == true) {
					Actions action = new Actions(driver);
					action.moveToElement(driver.findElement(CreateMeetingPage.waitingUsers)).perform();
					Thread.sleep(5000);
					assertTrue(driver.findElement(CreateMeetingPage.admit_btn).isDisplayed(),
							"Admit button is missing for User.");
					driver.findElement(CreateMeetingPage.dismiss_btn).click();
					var = true;
				} else {
					Thread.sleep(2000);
					assertTrue(driver.findElement(CreateMeetingPage.img_Close_participant).isDisplayed(),
							"Admit button is missing for User.");
					driver.findElement(CreateMeetingPage.img_Close_participant).click();
					var = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public void makeUserPresenter(WebDriver driver1, By thumbType) throws InterruptedException {
		try {
			Thread.sleep(5000);

			assertTrue(driver1.findElement(CreateMeetingPage.img_AddInvitee).isDisplayed(),
					"Participant button is missing.");
			TestRunProperties.explicitWait(driver1, CreateMeetingPage.img_AddInvitee);
			JavascriptExecutor jse = (JavascriptExecutor) driver1;
			jse.executeScript("arguments[0].click()", driver1.findElement(CreateMeetingPage.img_AddInvitee));
			Thread.sleep(3000);
			assertTrue(driver1.findElement(thumbType).isDisplayed(),
					"User Thumbnail is missing Or user is not added..");
			TestRunProperties.fluentWait(driver1, thumbType);
			Actions action = new Actions(driver1);
			action.moveToElement(driver1.findElement(thumbType)).perform();
			Thread.sleep(2000);
			if (UtilityClass.lang.equalsIgnoreCase("english")) {
				assertTrue(driver1.findElement(CreateMeetingPage.makePresentorEng).isDisplayed(),
						"Make presentor link is missing.");
				// JavascriptExecutor jse = (JavascriptExecutor) driver1;
				jse.executeScript("arguments[0].click()", driver1.findElement(CreateMeetingPage.makePresentorEng));
			} else if (UtilityClass.lang.equalsIgnoreCase("japanese")) {
				assertTrue(driver1.findElement(CreateMeetingPage.makePresentorJap).isDisplayed(),
						"Make presentor link is missing.");
				jse.executeScript("arguments[0].click()", driver1.findElement(CreateMeetingPage.makePresentorJap));
			}
			assertTrue(driver1.findElement(CreateMeetingPage.img_Close_participant).isDisplayed(),
					"Admit button is missing for User.");
			//driver1.findElement(CreateMeetingPage.img_Close_participant).click();
			jse.executeScript("arguments[0].click()", driver1.findElement(CreateMeetingPage.img_Close_participant));

		} catch (Exception e) {
			System.out.println("Exception in makeUserPresenter method" + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());

		}
	}

	public void userPresentorSS(WebDriver driver, WebDriver driver1) throws InterruptedException {
		try {
			Thread.sleep(3000);

			TestRunProperties.explicitWait(driver, CreateMeetingPage.screenShare_icon);
			Thread.sleep(5000);
			Assert.assertTrue(driver.findElement(CreateMeetingPage.screenShare_icon).isDisplayed(),
					"Screenshare icon is missing...");
			Thread.sleep(3000);
			JavascriptExecutor js1 = (JavascriptExecutor) driver;
			// js1.executeScript("arguments[0].click()",
			// driver.findElement(CreateMeetingPage.screenShare_icon));
			driver.findElement(CreateMeetingPage.screenShare_icon).click();
			Thread.sleep(9000);
			Assert.assertTrue(driver.findElement(CreateMeetingPage.screenshare_div).isDisplayed(),
					"Screenshare is not visible at Presentators/Users side..");
			Thread.sleep(5000);
			Assert.assertTrue(driver1.findElement(CreateMeetingPage.screenshare_div).isDisplayed(),
					"Screenshare is not visible at Agents side..");
			Assert.assertTrue(driver1.findElement(CreateMeetingPage.maximizeScreenS).isDisplayed(),
					"Screenshare maximize icon is missing..");
			Thread.sleep(5000);
			JavascriptExecutor jse = (JavascriptExecutor) driver1;
			jse.executeScript("arguments[0].click()", driver1.findElement(CreateMeetingPage.maximizeScreenS));
			Thread.sleep(10000);
			Assert.assertTrue(driver1.findElement(CreateMeetingPage.downloadScreenS).isDisplayed(),
					"Screenshare download icon is missing..");
			Assert.assertTrue(driver1.findElement(CreateMeetingPage.downloadScreenS).isEnabled(),
					"Screenshare download icon is not enabled..");
			jse.executeScript("arguments[0].click()", driver1.findElement(CreateMeetingPage.downloadScreenS));
		} catch (Exception e) {
			System.out.println("Exception in userPresentorSS method" + e);
			e.printStackTrace();
			Assert.fail(e.getMessage());

		}
	}
}
