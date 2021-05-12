package com.src.util;

import static org.testng.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.src.execute.TestRunProperties;
import com.src.pages.CreateMeetingPage;

public class AddCanvas {
	static JavascriptExecutor js;

	public static void addCanvas(WebDriver driver) throws InterruptedException {
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_AddCanvas));
		Thread.sleep(1000);
		assertTrue(driver.findElement(CreateMeetingPage.img_CanvasPresent).isDisplayed());
		Thread.sleep(2000);
	}

	public static void AddAnnotations_Circle(WebDriver driver) throws InterruptedException {
		Actions action = new Actions(driver);
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Color));
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_Purple));
		Thread.sleep(1000);

		WebElement wb = driver.findElement(CreateMeetingPage.img_Annotations);
		if (wb.isSelected() == false) {
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Annotations));
		}
		WebElement Circle = driver.findElement(CreateMeetingPage.img_Circle);
		js.executeScript("arguments[0].click();", Circle);
		Thread.sleep(1000);
		WebElement Canvas = driver.findElement(CreateMeetingPage.LOC_Canvas);
		action.moveToElement(Canvas, 200, 110);

		org.openqa.selenium.Point point = Canvas.getLocation();

		int xcord = point.getX() - 10;
		int ycord = point.getY() - 5;
		System.out.println("X coordinates:" + xcord);
		System.out.println("Y coordinates:" + ycord);
		int x = xcord - 20;
		int y = ycord + 45;

		Thread.sleep(3000);
		Circle.click();

		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", Circle);

		Action series = action.moveToElement(Canvas, xcord, ycord).clickAndHold().moveByOffset(x, y).build();
		series.perform();
		action.release().perform();

	}

	public static void AddAnnotations_Rectangle(WebDriver driver) throws InterruptedException {
		Actions action = new Actions(driver);
		Thread.sleep(2000);
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Color));
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_Green));
		Thread.sleep(1000);
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Annotations));
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Rectangle));
		Thread.sleep(1000);

		WebElement Canvas = driver.findElement(CreateMeetingPage.LOC_Canvas);
		action.moveToElement(Canvas, 110, 70);

		org.openqa.selenium.Point point1 = Canvas.getLocation();
		Thread.sleep(1000);

		int xcord1 = point1.getX() + 20;
		int ycord1 = point1.getY() - 10;
		System.out.println("X coordinates:" + xcord1);
		System.out.println("Y coordinates:" + ycord1);
		int x = xcord1 - 5;
		int y = ycord1 - 15;

		// js.executeScript("arguments[0].click();",
		// driver.findElement(CreateMeetingPage.img_Rectangle));
		Action series = action.moveToElement(Canvas, x, y).clickAndHold().moveByOffset(xcord1, ycord1).build();
		series.perform();

		action.release().perform();
	}

	public static void AddAnnotations_Line(WebDriver driver) throws InterruptedException {
		Actions action = new Actions(driver);
		Thread.sleep(2000);
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Color));
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_Red));
		Thread.sleep(1000);
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Annotations));
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Line));
		Thread.sleep(1000);

		WebElement Canvas = driver.findElement(CreateMeetingPage.LOC_Canvas);
		action.moveToElement(Canvas, 100, 75);

		org.openqa.selenium.Point point2 = Canvas.getLocation();
		Thread.sleep(1000);

		int xcord2 = point2.getX() + 75;
		int ycord2 = point2.getY() - 35;
		System.out.println("X coordinates:" + xcord2);
		System.out.println("Y coordinates:" + ycord2);

		int x = xcord2 + 15;
		int y = ycord2 - 35;

		driver.findElement(CreateMeetingPage.img_Line).click();
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Line));
		Action series = action.moveToElement(Canvas, x, y).clickAndHold().moveByOffset(xcord2 + 25, ycord2 - 10)
				.build();
		series.perform();

		action.release().perform();

	}

	public static void AddAnnotations_Draw(WebDriver driver) throws InterruptedException {
		Actions action = new Actions(driver);
		Thread.sleep(2000);
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Color));
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.btn_Yellow));
		Thread.sleep(1000);
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Annotations));
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Draw));
		Thread.sleep(1000);

		WebElement Canvas = driver.findElement(CreateMeetingPage.LOC_Canvas);
		action.moveToElement(Canvas, 170, 125);

		org.openqa.selenium.Point point3 = Canvas.getLocation();
		Thread.sleep(1000);

		int xcord3 = point3.getX() + 15;
		int ycord3 = point3.getY() - 15;
		System.out.println("X coordinates:" + xcord3);
		System.out.println("Y coordinates:" + ycord3);
		int x = xcord3 + 120;
		int y = ycord3 - 10;

		//js = (JavascriptExecutor) driver;
		//js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Draw));
		Action series = action.moveToElement(Canvas, x, y).clickAndHold().moveByOffset(xcord3 - 50, ycord3 - 40)
				.moveByOffset(xcord3 + 20, ycord3 + 20).build();
		series.perform();
		action.release().perform();
	}

	public static void downloadCanvas(WebDriver driver) throws InterruptedException {
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Download));
		Thread.sleep(5000);
		System.out.println("Canvas downloaded successfully");
	}

	public static void clearAndCloseCanvas(WebDriver driver) throws InterruptedException {
		WebElement wb = driver.findElement(CreateMeetingPage.img_Annotations);
		if (wb.isSelected() == false) {
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Annotations));
		}
		WebElement text = driver.findElement(CreateMeetingPage.canvas_Erase);
		js.executeScript("arguments[0].click();", text);
		Thread.sleep(1000);
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.canvas_Clear));
		Thread.sleep(1000);
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.Canvas_Close));
		Thread.sleep(2000);
		TestRunProperties.explicitWait(driver, CreateMeetingPage.leave_Session_Accept);
		WebElement modalContainer = driver.findElement(CreateMeetingPage.accept_modal);
		WebElement modalContent = modalContainer.findElement(CreateMeetingPage.leave_Session_Accept);
		modalContent.click();
		Thread.sleep(1000);
		System.out.println("Successully cleared the canvas");
	}

	public static void textDecoration_canvas(WebDriver driver) throws InterruptedException, AWTException {
		js = (JavascriptExecutor) driver;
		WebElement wb = driver.findElement(CreateMeetingPage.img_Annotations);
		if (wb.isSelected() == false) {
			js.executeScript("arguments[0].click();", driver.findElement(CreateMeetingPage.img_Annotations));
		}
		WebElement text = driver.findElement(CreateMeetingPage.img_Text);
		js.executeScript("arguments[0].click();", text);
		Thread.sleep(1000);
		if(UtilityClass.lang.equalsIgnoreCase("english"))
		{
		Select fontStyle = new Select(driver.findElement(CreateMeetingPage.fontStyle_drpDown));
		fontStyle.selectByValue("Pacifico");
		Thread.sleep(1000);
		Select fontSize = new Select(driver.findElement(CreateMeetingPage.fontSize_drpDown));
		fontSize.selectByValue("60");
		} else if(UtilityClass.lang.equalsIgnoreCase("english"))
		{
			Thread.sleep(1000);
			Select fontSize = new Select(driver.findElement(CreateMeetingPage.fontSize_drpDownJap));
			fontSize.selectByValue("60");
		}
		
		Actions builder = new Actions(driver);
		WebElement Canvas = driver.findElement(CreateMeetingPage.LOC_Canvas);
		builder.moveToElement(Canvas, 200, 250).click();
		builder.sendKeys(Canvas, "Automating Text Decoration feature of canvas and writing on a new canvas.");
		builder.perform();

	}
}
