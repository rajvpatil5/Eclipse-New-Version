package demo.test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.pages.EcomTestPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DemoTest {

	@Parameters({ "email", "phone", "amount", "cardNumbers", "cardExp", "cardHolderName", "cardCVV" })
	@Test
	public void paymentTest(String email, String phone, String amount, String cardNumbers, String cardExp,
			String cardHolderName, String cardCVV) {

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		try {

			driver.get("https://pages.razorpay.com/Ecomtest");
			driver.findElement(EcomTestPage.textEmail).sendKeys(email);
			driver.findElement(EcomTestPage.textPhone).sendKeys(phone);
			driver.findElement(EcomTestPage.textAmount).sendKeys(amount);
			driver.findElement(EcomTestPage.submitBtn).click();

			List<WebElement> allFrames = driver.findElements(By.tagName("iframe"));
			driver.switchTo().frame(0);
			explicitWait(driver, EcomTestPage.cardBtn);
			driver.findElement(EcomTestPage.cardBtn).click();

			explicitWait(driver, EcomTestPage.cardNumber);
			driver.findElement(EcomTestPage.cardNumber).sendKeys(cardNumbers);
			driver.findElement(EcomTestPage.cardExpiry).sendKeys(cardExp);
			driver.findElement(EcomTestPage.cardName).sendKeys(cardHolderName);
			driver.findElement(EcomTestPage.cardCvv).sendKeys(cardCVV);
			driver.findElement(EcomTestPage.payBtn).click();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void explicitWait(WebDriver driver, By xpath) {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
	}
}
