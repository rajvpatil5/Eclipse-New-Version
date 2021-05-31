package com.atom.execute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;

import com.atom.pages.AtomTestPage;
import com.atom.util.ReadConfig;
import com.atom.util.ReadExcel;

import org.testng.xml.XmlTest;

public class ExecutePaymentTest {
	ReadExcel readExcel = new ReadExcel();
	ReadConfig readConfig = new ReadConfig();
	public static int skip = 0;
	public static String transactionStatus;
	public static String merchantTxnId;
	public static String atomTxnID;
	public static String bankRefNo;
	public static String clientCode;
	public static String dateTime;

	private void executeTest(List<XmlSuite> suites) {
		TestNG testNG = new TestNG();
		testNG.setXmlSuites(suites);
		for (int i = 0; i < suites.size(); i++) {
			System.out.println(suites.get(i).toXml());
			System.out.println("==================");
		}
		testNG.run();
	}

	private void startMeeting() {
		try {
			int numberofuser = readConfig.numberOfUsers;
			int threadCount = readConfig.threads;
			List<XmlSuite> suites = new ArrayList<XmlSuite>();
			XmlSuite XmlSuite1 = null;
			XmlSuite1 = getUserSingleMeetingXmlSuite(numberofuser, threadCount);
			suites.add(XmlSuite1);
			executeTest(suites);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private XmlTest getJoinUserXmlTest(String userName, String name, String email, String phone, String amount,
			String cardNumbers, String cardExp, String cardHolderName, String cardCVV, String cardPin,
			String cardBankName) {
		List<XmlClass> classes = new ArrayList<XmlClass>();

		classes.add(new XmlClass("com.atom.test.TestPayment"));

		XmlTest test = new XmlTest();
		test.setName(userName);
		test.setGroupByInstances(true);
		test.setXmlClasses(classes);
		test.addParameter("name", name);
		test.addParameter("email", email);
		test.addParameter("phone", phone);
		test.addParameter("amount", amount);
		test.addParameter("cardNumbers", cardNumbers);
		test.addParameter("cardExpMonth", cardExp.split("/")[0]);
		test.addParameter("cardExpYear", cardExp.split("/")[1]);
		test.addParameter("cardHolderName", cardHolderName);
		test.addParameter("cardCVV", cardCVV);
		test.addParameter("cardPin", cardPin);
		test.addParameter("cardBankName", cardBankName);
		return test;
	}

	private XmlSuite getUserSingleMeetingXmlSuite(int numberofuser, int threadCount) {
		// XmlSuite for Join Meeting
		XmlSuite suiteUser = createSuite("Join Meetings", ParallelMode.TESTS, threadCount);
		String meetingName = "Test";
		int LastUserIndex = 0;

		for (int userindex = 0 + LastUserIndex; userindex <= (numberofuser + LastUserIndex) - 1; userindex++) {

			XmlTest testUser = getJoinUserXmlTest(meetingName + userindex, readExcel.name.get(userindex),
					readExcel.email.get(userindex), readExcel.phone.get(userindex), readExcel.amount.get(userindex),
					readExcel.cardNumbers.get(userindex), readExcel.cardExp.get(userindex),
					readExcel.cardHolderName.get(userindex), readExcel.cardCVV.get(userindex),
					readExcel.cardPin.get(userindex), readExcel.cardBankName.get(userindex));
			testUser.setXmlSuite(suiteUser);
			suiteUser.addTest(testUser);
		}
		return suiteUser;
	}

	private XmlSuite createSuite(String suiteName, ParallelMode parallelMode, int threadCount) {
		XmlSuite suite = new XmlSuite();
		suite.setName(suiteName);
		suite.setParallel(parallelMode);
		suite.setThreadCount(threadCount);
		List<String> listeners = Lists.newArrayList();
		listeners.add("com.atom.util.Listen");
		suite.setListeners(listeners);
		return suite;
	}

	public static void main(String[] args) {

		ExecutePaymentTest det = new ExecutePaymentTest();
		det.startMeeting();
	}

}
