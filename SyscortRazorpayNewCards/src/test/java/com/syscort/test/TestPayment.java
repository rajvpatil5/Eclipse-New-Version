package com.syscort.test;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.syscort.execute.ExecutePaymentTest;
import com.syscort.execute.TestSetup;
import com.syscort.pages.EcomTestPage;
import com.syscort.util.Listen;
import com.syscort.util.ReadConfig;
import com.syscort.util.ReadExcel;

@Listeners(com.syscort.util.Listen.class)
public class TestPayment extends TestSetup {
	WebDriver driver;
	ReadConfig rc = new ReadConfig();
	public static ArrayList<Object[]> sessionUrl = new ArrayList<Object[]>();
	private static String lock = "lock";
	public static long start = 0;
	public static long end = 0;

	@BeforeMethod
	public void setupWebDriver() {
		try {
			setup();
			driver = getDriver();
			driver.manage().window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
//	public void m1() {
//		synchronized (lock) {
//			a = a + 1;
//			System.out.println(a + "---------");
//		}
//	}

	@Parameters({ "email", "phone", "amount", "cardNumbers", "cardExp", "cardHolderName", "cardCVV", "cardPin" })
	@Test
	public void paymentTest(String email, String phone, String amount, String cardNumbers, String cardExp,
			String cardHolderName, String cardCVV, String cardPin) {

		try {
			driver.get(rc.url);
			explicitWait(driver, EcomTestPage.textEmail);
			assertTrue(driver.findElement(EcomTestPage.textEmail).isDisplayed());
			driver.findElement(EcomTestPage.textEmail).sendKeys(email);

			assertTrue(driver.findElement(EcomTestPage.textPhone).isDisplayed());
			driver.findElement(EcomTestPage.textPhone).sendKeys(phone);

			assertTrue(driver.findElement(EcomTestPage.textAmount).isDisplayed());
			driver.findElement(EcomTestPage.textAmount).sendKeys(amount);

			assertTrue(driver.findElement(EcomTestPage.submitBtn).isDisplayed());
			driver.findElement(EcomTestPage.submitBtn).click();

			explicitWait(driver, EcomTestPage.checkoutFrame);
			driver.switchTo().frame(driver.findElement(EcomTestPage.checkoutFrame));

			Thread.sleep(3000);
			explicitWait(driver, EcomTestPage.cardBtn);
			assertTrue(driver.findElement(EcomTestPage.cardBtn).isDisplayed());
			driver.findElement(EcomTestPage.cardBtn).click();

			explicitWait(driver, EcomTestPage.cardNumber);
			assertTrue(driver.findElement(EcomTestPage.cardNumber).isDisplayed());
			driver.findElement(EcomTestPage.cardNumber).sendKeys(cardNumbers);

			assertTrue(driver.findElement(EcomTestPage.cardExpiry).isDisplayed());
			driver.findElement(EcomTestPage.cardExpiry).sendKeys(cardExp);

			assertTrue(driver.findElement(EcomTestPage.cardName).isDisplayed());
			driver.findElement(EcomTestPage.cardName).sendKeys(cardHolderName);

			assertTrue(driver.findElement(EcomTestPage.cardCvv).isDisplayed());
			driver.findElement(EcomTestPage.cardCvv).sendKeys(cardCVV);

			synchronized (lock) {

				String defaultWin = driver.getWindowHandle();
//				System.out.println(defaultWin);

				assertTrue(driver.findElement(EcomTestPage.payBtn).isDisplayed());
				driver.findElement(EcomTestPage.payBtn).click();

				Thread.sleep(3000);
				Set<String> windowHandles = driver.getWindowHandles();
//				System.out.println(windowHandles.size());

				Iterator<String> winIteraator = windowHandles.iterator();
				String child_window = null;
				while (winIteraator.hasNext()) {
					child_window = winIteraator.next();
				}
				driver.switchTo().window(child_window);
				Thread.sleep(3000);

				try {
					explicitWait(driver, EcomTestPage.ipin);
					driver.findElement(EcomTestPage.ipin).sendKeys(cardPin);
					driver.findElement(EcomTestPage.SubmitPayBtn).click();
					Thread.sleep(3000);
				} catch (Exception e) {
					System.out.println("Exception while making payment. Switching to default window.");
					driver.switchTo().window(defaultWin);
				}

				String transactionMessage = null;
				int win = 0;
				start = System.currentTimeMillis();
				end = start + 30 * 1000; // 60 seconds * 1000 ms/sec
				do {
					ExecutePaymentTest.then2 = LocalDateTime.now();
					win = driver.getWindowHandles().size();
					if (win == 1) {
						Thread.sleep(5000);
						driver.switchTo().window(defaultWin);

						if (driver.findElements(EcomTestPage.paymentID).size() == 1) {
							try {
								transactionMessage = driver.findElement(EcomTestPage.paymentID).getText();
								System.out.println(transactionMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							try {
								driver.switchTo().frame(driver.findElement(EcomTestPage.checkoutFrame));
								transactionMessage = driver.findElement(EcomTestPage.errorMessage).getText();
								System.out.println(transactionMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						System.out.println("Payment in Process...");
						Thread.sleep(2000);
					}
					System.out.println(System.currentTimeMillis() > end);
					System.out.println(end - System.currentTimeMillis());
					if (System.currentTimeMillis() > end) {
						transactionMessage = "Please check IPIN or invalid card...";
						System.out.println(transactionMessage);
						break;
					}
				} while (win > 1);

				// For skipping all future tests, If continue 5 failure occur all future
				// testcases get skip.
				System.out.println(transactionMessage.contains("error at bank"));
				if (transactionMessage.contains("error at bank")) {
					ExecutePaymentTest.skip = ExecutePaymentTest.skip + 1;
					System.out.println("Skip count " + ExecutePaymentTest.skip);
					if (ExecutePaymentTest.skip >= rc.skipAfterFailure) {
						Listen.hasFailures = true;
					}
				} else {
					ExecutePaymentTest.skip = 0;
				}
				sessionUrl.add(new String[] { email, phone, amount, cardNumbers, cardExp, cardHolderName, cardCVV,
						cardPin, transactionMessage });
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "cardNumbers" })
	@AfterMethod
	public void cleanup(String cardNumber) {

		driver.quit();
	}

	@AfterSuite
	public void createTransactionSheet() {
		try {
			ReadExcel.writeExcelData(sessionUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void explicitWait(WebDriver driver, By xpath) {
		WebDriverWait wait = new WebDriverWait(driver, rc.explicitWait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
	}
}
