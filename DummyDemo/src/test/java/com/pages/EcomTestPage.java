package com.pages;

import org.openqa.selenium.By;

public class EcomTestPage {
	public static By textEmail = By.xpath("//input[@name='email']");
	public static By textPhone = By.xpath("//input[@name='phone']");
	public static By textAmount = By.xpath("//*[@id='form-section']/form/div[1]/div[3]/div/div[2]/div[1]/input");
	public static By submitBtn = By.xpath("//button[@type='submit']");

	// Card Modal
	public static By checkoutFrame = By.xpath("//iframe[@class='razorpay-checkout-frame']");

	public static By cardBtn = By.xpath("//*[@id='form-common']/div[1]/div/div/div[2]/div/div/button[1]");
	public static By cardNumber = By.xpath("//input[@id='card_number']");
	public static By cardExpiry = By.xpath("//input[@id='card_expiry']");
	public static By cardName = By.xpath("//input[@id='card_name']");
	public static By cardCvv = By.xpath("//input[@id='card_cvv']");
	public static By payBtn = By.xpath("//span[@id='footer-cta']");
}
