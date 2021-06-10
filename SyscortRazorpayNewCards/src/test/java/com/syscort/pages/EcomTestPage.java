package com.syscort.pages;

import org.openqa.selenium.By;

public class EcomTestPage {
	public static By textEmail = By.xpath("//input[@name='email']");
	public static By textPhone = By.xpath("//input[@name='phone']");
	public static By textAmount = By.xpath("//*[@id='form-section']/form/div[1]/div[3]/div/div[2]/div[1]/input");
	public static By submitBtn = By.xpath("//button[@type='submit']");

	// Card Modal
	public static By checkoutFrame = By.xpath("//iframe[@class='razorpay-checkout-frame']");

	public static By cardBtn = By.xpath("//button[@method='card']");
	public static By cardNumber = By.xpath("//input[@id='card_number']");
	public static By cardExpiry = By.xpath("//input[@id='card_expiry']");
	public static By cardName = By.xpath("//input[@id='card_name']");
	public static By cardCvv = By.xpath("//input[@id='card_cvv']");
	public static By payBtn = By.xpath("//span[@id='footer-cta']");
	public static By ipin = By.xpath("//input[@id='txtipin']");
	public static By SubmitPayBtn = By.xpath("//input[@id='btnverify']");
	public static By errorMessage = By.xpath("//div[@id='fd-t']");
	public static By paymentID = By.xpath("	//*[@id='status-section-container']/div[3]/div[5]/div/div[1]");
}
