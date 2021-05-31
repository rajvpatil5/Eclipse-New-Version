package com.atom.pages;

import org.openqa.selenium.By;

public class AtomTestPage {
	// Customer info page
	public static By textName = By.xpath("//input[@name='Customer_Name_0']");
	public static By textEmail = By.xpath("//input[@name='Email_1']");
	public static By textPhone = By.xpath("//input[@name='Mobile_No_2']");
	public static By textAmount = By.xpath("//input[@name='amount']");
	public static By submitBtn = By.xpath("//input[@id='pay_checkPaymentDetails']");

	// payment page
	public static By debitCard = By.xpath("//a[@href='#debitcard']");
	public static By cardNumber = By.xpath("//input[@id='dcCardNo']");
	public static By cardName = By.xpath("//input[@id='dcCardName']");
	public static By cardExpiryMonth = By.xpath("//select[@name='dcMonth']");
	public static By cardExpiryYear = By.xpath("//select[@name='dcYear']");
	public static By cardCvv = By.xpath("//input[@id='dcCcv']");
	public static By cardBankName = By.xpath("//input[@id='dcbinBankName']");
	public static By saveCards = By.xpath("//input[@name='savecardcheckboxDC']");
	public static By payBtn = By.xpath("//input[@id='paymentProcess']");

	// Payment page
	public static By atmPinSpan = By.xpath("//span[text()='ATM PIN']");
	public static By atmPinExpiry = By.xpath("//input[@id='expDate']");
	public static By atmPin = By.xpath("//input[@id='pin']");
	public static By submitPaymentBtn = By.xpath("//button[@id='submitButtonIdForPin']");

	// Transaction Response page
	public static By transactionStatus = By.xpath(
			"/html/body/table/tbody/tr/td/table/tbody/tr[6]/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/table/tbody/tr[2]/td[3]");
	public static By merchantTxnId = By.xpath(
			"/html/body/table/tbody/tr/td/table/tbody/tr[6]/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/table/tbody/tr[4]/td[3]");
	public static By atomTxnID = By.xpath(
			"/html/body/table/tbody/tr/td/table/tbody/tr[6]/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/table/tbody/tr[6]/td[3]");
	public static By bankRefNo = By.xpath(
			"/html/body/table/tbody/tr/td/table/tbody/tr[6]/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/table/tbody/tr[10]/td[3]");
	public static By clientCode = By.xpath(
			"/html/body/table/tbody/tr/td/table/tbody/tr[6]/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/table/tbody/tr[12]/td[3]");
	public static By dateTime = By.xpath(
			"/html/body/table/tbody/tr/td/table/tbody/tr[6]/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/table/tbody/tr[14]/td[3]");
}
