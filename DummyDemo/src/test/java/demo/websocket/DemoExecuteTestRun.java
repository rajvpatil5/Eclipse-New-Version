package demo.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;

public class DemoExecuteTestRun {
	ReadExcel readExcel = new ReadExcel();

	private static void executeTest(List<XmlSuite> suites) {
		TestNG testNG = new TestNG();
		testNG.setXmlSuites(suites);
		for (int i = 0; i < suites.size(); i++) {
			System.out.println(suites.get(i).toXml());
			System.out.println("==================");
		}
		testNG.run();
	}

	private static void startMeeting() {
		try {
			int numberofuser = 1;
			int threadCount = 1;
			List<XmlSuite> suites = new ArrayList<XmlSuite>();
			DemoExecuteTestRun det = new DemoExecuteTestRun();
			XmlSuite XmlSuite1 = null;
			XmlSuite1 = det.getUserSingleMeetingXmlSuite(numberofuser, threadCount);
			suites.add(XmlSuite1);
			executeTest(suites);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private XmlTest getJoinUserXmlTest(String userName, String email, String phone, String amount, String cardNumbers,
			String cardExp, String cardHolderName, String cardCVV) {
		List<XmlClass> classes = new ArrayList<XmlClass>();

		classes.add(new XmlClass("demo.test.DemoTest"));

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
					readExcel.cardHolderName.get(userindex), readExcel.cardCVV.get(userindex));
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
		return suite;
	}

	public static void main(String[] args) {
		startMeeting();
	}

}
