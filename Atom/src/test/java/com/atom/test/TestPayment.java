package com.atom.test;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.atom.execute.ExecutePaymentTest;
import com.atom.execute.TestSetup;
import com.atom.pages.AtomTestPage;
import com.atom.util.ReadConfig;
import com.atom.util.ReadExcel;

@Listeners(com.atom.util.Listen.class)
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

	@Parameters({ "name", "email", "phone", "amount", "cardNumbers", "cardExpMonth", "cardExpYear", "cardHolderName",
			"cardCVV", "cardPin", "cardBankName" })
	@Test
	public void paymentTest(String name, String email, String phone, String amount, String cardNumbers,
			String cardExpMonth, String cardExpYear, String cardHolderName, String cardCVV, String cardPin,
			String cardBankName) {
		String cardExpYear1 = "20" + cardExpYear;
		try {

			driver.get(rc.url);

			explicitWait(driver, AtomTestPage.textName);
			assertTrue(driver.findElement(AtomTestPage.textName).isDisplayed());
			driver.findElement(AtomTestPage.textName).sendKeys(name);

			assertTrue(driver.findElement(AtomTestPage.textEmail).isDisplayed());
			driver.findElement(AtomTestPage.textEmail).sendKeys(email);

			assertTrue(driver.findElement(AtomTestPage.textPhone).isDisplayed());
			driver.findElement(AtomTestPage.textPhone).sendKeys(phone);

			assertTrue(driver.findElement(AtomTestPage.textAmount).isDisplayed());
			driver.findElement(AtomTestPage.textAmount).sendKeys(amount);

			assertTrue(driver.findElement(AtomTestPage.submitBtn).isDisplayed());
			driver.findElement(AtomTestPage.submitBtn).click();

			explicitWait(driver, AtomTestPage.debitCard);
			driver.findElement(AtomTestPage.debitCard).click();

			explicitWait(driver, AtomTestPage.cardNumber);
			assertTrue(driver.findElement(AtomTestPage.cardNumber).isDisplayed());
			driver.findElement(AtomTestPage.cardNumber).sendKeys(cardNumbers);

			assertTrue(driver.findElement(AtomTestPage.cardExpiryMonth).isDisplayed());
			new Select(driver.findElement(AtomTestPage.cardExpiryMonth)).selectByValue(cardExpMonth);

			assertTrue(driver.findElement(AtomTestPage.cardExpiryYear).isDisplayed());
			new Select(driver.findElement(AtomTestPage.cardExpiryYear)).selectByValue(cardExpYear1);

			assertTrue(driver.findElement(AtomTestPage.cardCvv).isDisplayed());
			driver.findElement(AtomTestPage.cardCvv).sendKeys(cardCVV);

			assertTrue(driver.findElement(AtomTestPage.cardName).isDisplayed());
			driver.findElement(AtomTestPage.cardName).sendKeys(cardHolderName);

			explicitWait(driver, AtomTestPage.saveCards);
			if (driver.findElement(AtomTestPage.saveCards).isSelected()) {
				driver.findElement(AtomTestPage.saveCards).click();
			}

//			assertTrue(driver.findElement(AtomTestPage.cardBankName).isDisplayed());
//			driver.findElement(AtomTestPage.cardBankName).clear();
//			driver.findElement(AtomTestPage.cardBankName).sendKeys(cardBankName);

			Thread.sleep(2000);
			assertTrue(driver.findElement(AtomTestPage.payBtn).isDisplayed());
			driver.findElement(AtomTestPage.payBtn).click();

			try {
				explicitWait(driver, AtomTestPage.atmPinSpan);
			} catch (Exception e) {
				try {
					System.out.println("Again click on Pay button");
					driver.findElement(AtomTestPage.payBtn).click();
				} catch (Exception e2) {
					System.out.println("Unable to find pay button.");
				}
			}

			try {
				explicitWait(driver, AtomTestPage.atmPinSpan);
				assertTrue(driver.findElement(AtomTestPage.atmPinSpan).isDisplayed());
				driver.findElement(AtomTestPage.atmPinSpan).click();

				assertTrue(driver.findElement(AtomTestPage.atmPinExpiry).isDisplayed());
				String val = cardExpMonth + "20" + cardExpYear;
				for (int i = 0; i < val.length(); i++) {
					char c = val.charAt(i);
					String s = new StringBuilder().append(c).toString();
					driver.findElement(AtomTestPage.atmPinExpiry).sendKeys(s);
				}

				assertTrue(driver.findElement(AtomTestPage.atmPin).isDisplayed());
				driver.findElement(AtomTestPage.atmPin).sendKeys(cardPin);

				assertTrue(driver.findElement(AtomTestPage.submitPaymentBtn).isDisplayed());
				driver.findElement(AtomTestPage.submitPaymentBtn).click();
			} catch (Exception e) {
				System.out.println("ATM payment page did not display. Assume reason - Invalid card details.");
			}

			synchronized (lock) {
				try {
					explicitWait(driver, AtomTestPage.transactionStatus);
					ExecutePaymentTest.transactionStatus = driver.findElement(AtomTestPage.transactionStatus).getText();
					ExecutePaymentTest.merchantTxnId = driver.findElement(AtomTestPage.merchantTxnId).getText();
					ExecutePaymentTest.atomTxnID = driver.findElement(AtomTestPage.atomTxnID).getText();
					ExecutePaymentTest.bankRefNo = driver.findElement(AtomTestPage.bankRefNo).getText();
					ExecutePaymentTest.clientCode = driver.findElement(AtomTestPage.clientCode).getText();
					ExecutePaymentTest.dateTime = driver.findElement(AtomTestPage.dateTime).getText();

					System.out.println(ExecutePaymentTest.transactionStatus);
					System.out.println(ExecutePaymentTest.merchantTxnId);
					System.out.println(ExecutePaymentTest.atomTxnID);
					System.out.println(ExecutePaymentTest.bankRefNo);
					System.out.println(ExecutePaymentTest.clientCode);
					System.out.println(ExecutePaymentTest.dateTime);

					sessionUrl.add(new String[] { name, email, phone, amount, cardNumbers,
							cardExpMonth + "/" + cardExpYear, cardHolderName, cardCVV, cardPin,
							ExecutePaymentTest.transactionStatus, ExecutePaymentTest.merchantTxnId,
							ExecutePaymentTest.atomTxnID, ExecutePaymentTest.bankRefNo, ExecutePaymentTest.clientCode,
							ExecutePaymentTest.dateTime });
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Exception while fetching transaction details.");
					sessionUrl.add(
							new String[] { name, email, phone, amount, cardNumbers, cardExpMonth + "/" + cardExpYear,
									cardHolderName, cardCVV, cardPin, "null", "null", "null", "null", "null", "null" });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Parameters({ "cardNumbers" })
	@AfterMethod
	public void cleanup(String cardNumber) {
		try {
			ReadExcel.writeExcelData(sessionUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.quit();
	}

	@AfterSuite
	public void createExcel(String cardNumber) {

	}

	public void explicitWait(WebDriver driver, By xpath) {
		WebDriverWait wait = new WebDriverWait(driver, rc.explicitWait);
		wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
	}
}
