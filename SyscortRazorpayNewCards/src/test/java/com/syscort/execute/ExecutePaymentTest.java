package com.syscort.execute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

import com.syscort.util.ReadConfig;
import com.syscort.util.ReadExcel;

public class ExecutePaymentTest {
	ReadExcel readExcel = new ReadExcel();
	ReadConfig readConfig = new ReadConfig();
	public static int skip = 0;
	public static LocalDateTime then2 = LocalDateTime.now();
	public static LocalDateTime then = LocalDateTime.now();

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

	private XmlTest getJoinUserXmlTest(String userName, String email, String phone, String amount, String cardNumbers,
			String cardExp, String cardHolderName, String cardCVV, String cardPin) {
		List<XmlClass> classes = new ArrayList<XmlClass>();

		classes.add(new XmlClass("com.syscort.test.TestPayment"));

		XmlTest test = new XmlTest();
		test.setName(userName);
		test.setGroupByInstances(true);
		test.setXmlClasses(classes);
		test.addParameter("email", email);
		test.addParameter("phone", phone);
		test.addParameter("amount", amount);
		test.addParameter("cardNumbers", cardNumbers);
		test.addParameter("cardExp", cardExp);
		test.addParameter("cardHolderName", cardHolderName);
		test.addParameter("cardCVV", cardCVV);
		test.addParameter("cardPin", cardPin);
		return test;
	}

	private XmlSuite getUserSingleMeetingXmlSuite(int numberofuser, int threadCount) {
		// XmlSuite for Join Meeting
		XmlSuite suiteUser = createSuite("Join Meetings", ParallelMode.TESTS, threadCount);
		String meetingName = "Test";
		int LastUserIndex = 0;

		for (int userindex = 0 + LastUserIndex; userindex <= (numberofuser + LastUserIndex) - 1; userindex++) {

			XmlTest testUser = getJoinUserXmlTest(meetingName + userindex, readExcel.email.get(userindex),
					readExcel.phone.get(userindex), readExcel.amount.get(userindex),
					readExcel.cardNumbers.get(userindex), readExcel.cardExp.get(userindex),
					readExcel.cardHolderName.get(userindex), readExcel.cardCVV.get(userindex),
					readExcel.cardPin.get(userindex));
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
		listeners.add("com.syscort.util.ExtentReport");
		listeners.add("com.syscort.util.Listen");
		suite.setListeners(listeners);
		return suite;
	}

	public static void main(String[] args) {

		ExecutePaymentTest det = new ExecutePaymentTest();
		det.startMeeting();
	}

}
