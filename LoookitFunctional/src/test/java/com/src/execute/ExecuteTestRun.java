package com.src.execute;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;
import org.testng.xml.XmlTest;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;
import com.src.pojo.JsonFileReader;
import com.src.util.ExcelData;

public class ExecuteTestRun {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static int totalNumberofRunningMeetings = 0;
	static int numberOfUserPerMeeting;
	HashMap<String, Integer> tempUserNodeMap = new HashMap<String, Integer>();
	static ColoredPrinter cp;
	public static String callType = "";
	public static JsonFileReader jsonFileReader;
	static HashMap<String, String> testClasses = new HashMap<String, String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		cp = new ColoredPrinter.Builder(1, false).foreground(FColor.RED).background(BColor.BLUE) // setting
																									// format
				.build();

		// printing according to that format
		// cp.setAttribute(Attribute.REVERSE);
		cp.clear();
		cp.print("Autoscale set functional test", Attribute.BOLD, FColor.YELLOW, BColor.GREEN);
		cp.clear();
		cp.print(cp.getDateFormatted(), Attribute.NONE, FColor.CYAN, BColor.BLACK);
		cp.debugPrintln(" Started");
		cp.clear();
		jsonFileReader = new JsonFileReader();
		jsonFileReader.readJson();
		TestRunProperties.userNodeMap = new HashMap<>();
		startMeeting();
		cp.clear(); // don't forget to clear the terminal's format before
		// exiting

	}

	private static void startMeeting() {
		int numberofMeeeting;
		int numberofuser;
		testClasses.put("1", "com.src.testCases.TestCreateMeeting");
		testClasses.put("2", "com.src.testCases.TestVideo");
		testClasses.put("3", "com.src.testCases.TestSnapshot");
		testClasses.put("4", "com.src.testCases.TestCanvas");
		testClasses.put("5", "com.src.testCases.TestShareLocation");
		testClasses.put("6", "com.src.testCases.TestAudio");
		try {

			try {
				String cmd = ExecuteTestRun.jsonFileReader.userValues.getMeetingPrefix();
				System.out.print("\nMeeting Prefix.: " + cmd + "\n");
				if (cmd.trim().isEmpty() == false) {
					TestRunProperties.meetingPrefix = cmd;
				} else {
					TestRunProperties.meetingPrefix = "Meeting" + (int) (Math.random() * 50 + 1);
				}
				numberofMeeeting = ExecuteTestRun.jsonFileReader.userValues.getTestType().length;
				System.out.print("\nNumber of meetings to start : " + numberofMeeeting + "\n");
				numberofuser = Integer.parseInt(ExecuteTestRun.jsonFileReader.userValues.getUsersPerMeeting());
				System.out.print("\nNumber of user in each meeting: " + numberofuser + "\n");
				numberOfUserPerMeeting = numberofMeeeting;
				int totalUsers = numberofuser * numberofMeeeting;
				int noOfNodes = TestRunProperties.GetAvailableNodeList().size();
				int actualInstances = totalUsers / noOfNodes;
				if (actualInstances > TestRunProperties.maxInstance) {
					System.out.print(
							"Your number of browser instance per node is more then defined in config.properties fille, Please try again with less number of meetings");
					System.out.print("\n--------------------------------------------------------------------");
					startMeeting();
				} else {
					ExecuteTestRun Et = new ExecuteTestRun();
					// Et.tempUserNodeMap = TestRunProperties.userNodeMap;
					Et.tempUserNodeMap = ExecuteTestRun.deepClone(TestRunProperties.userNodeMap);
					// Et.hostMeeting(numberofMeeeting, numberofuser);
					HashMap<String, String> meetingToHost = Et.getMeetingsNameWithHostNode(numberofMeeeting);
					List<XmlSuite> suites = new ArrayList<XmlSuite>();
					int i = 0;
					for (HashMap.Entry<String, String> entry : meetingToHost.entrySet()) {
						XmlSuite XmlSuite1 = null;
						XmlSuite XmlSuite2 = null;
						for (Entry<String, String> entry1 : testClasses.entrySet()) {
							if (jsonFileReader.userValues.getTestType()[i].equals(entry1.getKey())) {
								XmlSuite1 = Et.getSinglehostMeetingXmlSuite(entry.getKey(), entry.getValue(),
										entry1.getValue());
								XmlSuite2 = Et.getUserSingleMeetingXmlSuite(entry.getKey(), numberofuser,
										entry1.getValue());
							}
						}

						suites.add(XmlSuite1);
						// System.out.print(XmlSuite2.toXml());
						suites.add(XmlSuite2);
						// System.out.print(suiteUser.toXml());
						i++;
					}
					executeTest(suites);
				}

			} catch (Exception e) {
				System.out.println("Please enter a valid input\n" + e);
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	private static void executeTest(List<XmlSuite> suites) {
		TestNG testNG = new TestNG();
		testNG.setXmlSuites(suites);
		for (int i = 0; i < suites.size(); i++) {
			System.out.println(suites.get(i).toXml());
			System.out.println("==================");
		}
		testNG.run();
	}

	private XmlSuite getSinglehostMeetingXmlSuite(String meetingName, String node, String className) {

		XmlSuite suite = createSuite("StartMeetings", ParallelMode.FALSE, 1);

		XmlTest test = getJoinUserXmlTest(
				ExecuteTestRun.jsonFileReader.userValues.getUsers().get(0).getUserName() + meetingName, node,
				meetingName, ExecuteTestRun.jsonFileReader.userValues.getUsers().get(0).getUserBrowser(),
				ExecuteTestRun.jsonFileReader.userValues.getUsers().get(0).getUserLanguage(),
				ExecuteTestRun.jsonFileReader.userValues.getUsers().get(0).getUserPlatform(),
				className);

		test.setXmlSuite(suite);
		suite.addTest(test);

		// System.out.print("d......."+suite.toXml());
		return suite;
	}

	private XmlSuite createSuite(String suiteName, ParallelMode parallelMode, int threadCount) {
		XmlSuite suite = new XmlSuite();
		suite.setName(suiteName);
		suite.setParallel(parallelMode);
		suite.setThreadCount(threadCount);
		return suite;
	}

	private XmlTest getJoinUserXmlTest(String userName, String nodeIp, String meetingName, String browser,
			String language, String platform, String className) {
		List<XmlClass> classes = new ArrayList<XmlClass>();

		classes.add(new XmlClass(className));

		XmlTest test = new XmlTest();
		test.setName(userName);
		test.setGroupByInstances(true);
		test.setXmlClasses(classes);
		test.addParameter("node", nodeIp);
		test.addParameter("meetingName", meetingName);
		test.addParameter("FakeCame", TestRunProperties.fakCamera);
		test.addParameter("userName", userName);
		test.addParameter("browser", browser);
		test.addParameter("language", language);
		test.addParameter("platform", platform);
		return test;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deepClone(T o) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(o);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
			return (T) o.getClass().cast(in.readObject());

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	private HashMap<String, String> getMeetingsNameWithHostNode(int numberofMeeeting) {
		HashMap<String, String> meetingsWithHost = new HashMap<String, String>();

		int nodeIndex = 0;
		int capacityFullInstance = 0;
		for (int i = 1; i <= numberofMeeeting; i++) {
			int userCount = 0;
			String nodeIp = "";
			do {
				if (nodeIndex == TestRunProperties.nodeList.size()
						|| nodeIndex > TestRunProperties.nodeList.size() - 1) {
					nodeIndex = 0;
				}
				nodeIp = TestRunProperties.nodeList.get(nodeIndex);
				userCount = 0;
				if (tempUserNodeMap.containsKey(nodeIp) == true) {
					userCount = tempUserNodeMap.get(nodeIp.toString());
				}

				if (userCount <= TestRunProperties.maxInstance) {
					nodeIndex++;
					capacityFullInstance = 0;
					break;
				} else {
					capacityFullInstance += 1;
				}
				nodeIndex++;
			} while (capacityFullInstance != TestRunProperties.nodeList.size());

			if (capacityFullInstance != TestRunProperties.nodeList.size()) {
				if (tempUserNodeMap.containsKey(nodeIp) == true) {
					userCount = tempUserNodeMap.get(nodeIp.toString());
				}

				if (userCount <= TestRunProperties.maxInstance) {

					meetingsWithHost.put(TestRunProperties.meetingPrefix + (totalNumberofRunningMeetings + i), nodeIp);

					userCount += 1;
					tempUserNodeMap.put(nodeIp.toString(), userCount);
				}
			} else {
				setErrorBoxPrinter();
				System.out.print("\n No more meeting can be hosted as max limit reached");
				setResponsePrinter();
				break;
			}

		}
		totalNumberofRunningMeetings += numberofMeeeting;
		return meetingsWithHost;
	}

	private XmlSuite getUserSingleMeetingXmlSuite(String meetingName, int numberofuser, String className) {
		// XmlSuite for Join Meeting
		XmlSuite suiteUser = createSuite("Join Meetings", ParallelMode.TESTS, numberofuser);

		int userNodeIndex = 0;
		// Loop for total number of user
		String userNodeIp = "";
		int userCount = 0;
		int capacityFullInstance = 0;
		int LastUserIndex = 0;
		if (TestRunProperties.userMeetingMap.containsKey(meetingName)) {
			LastUserIndex = TestRunProperties.userMeetingMap.get(meetingName).intValue();
		}
		System.out.println(meetingName + " " + LastUserIndex);
		for (int userindex = 1 + LastUserIndex; userindex <= (numberofuser + LastUserIndex) - 1; userindex++) {

			do {
				if (userNodeIndex == TestRunProperties.nodeList.size()
						|| userNodeIndex > TestRunProperties.nodeList.size() - 1) {
					userNodeIndex = 0;
				}
				userNodeIp = TestRunProperties.nodeList.get(userNodeIndex);
				userCount = 0;
				if (tempUserNodeMap.containsKey(userNodeIp) == true) {
					userCount = tempUserNodeMap.get(userNodeIp.toString());

				}

				if (userCount <= TestRunProperties.maxInstance) {
					userNodeIndex++;
					capacityFullInstance = 0;
					break;
				} else {
					capacityFullInstance += 1;
				}
				userNodeIndex++;
			} while (capacityFullInstance != TestRunProperties.nodeList.size());

			if (capacityFullInstance != TestRunProperties.nodeList.size()) {

				if (tempUserNodeMap.containsKey(userNodeIp) == true) {
					userCount = tempUserNodeMap.get(userNodeIp.toString());

				}

				if (userCount <= TestRunProperties.maxInstance) {

					XmlTest testUser = getJoinUserXmlTest(
							ExecuteTestRun.jsonFileReader.userValues.getUsers().get(userindex).getUserName() + userindex
									+ meetingName,
							userNodeIp, meetingName,
							ExecuteTestRun.jsonFileReader.userValues.getUsers().get(userindex).getUserBrowser(),
							ExecuteTestRun.jsonFileReader.userValues.getUsers().get(userindex).getUserLanguage(),
							ExecuteTestRun.jsonFileReader.userValues.getUsers().get(userindex).getUserPlatform(),
							className);

					testUser.setXmlSuite(suiteUser);
					suiteUser.addTest(testUser);
					userCount += 1;
					tempUserNodeMap.put(userNodeIp.toString(), userCount);
				}
			} else {
				setErrorBoxPrinter();
				System.out.print("\n No more user can join meeting as max limit reached");
				setResponsePrinter();
				break;
			}

		}

		return suiteUser;

	}

	public static void setCommandPrinter() {
		cp.print("", Attribute.BOLD, FColor.RED, BColor.BLACK);
	}

	public static void setInputPrinter() {
		cp.print("", Attribute.BOLD, FColor.GREEN, BColor.BLACK);
	}

	public static void setResponsePrinter() {
		cp.print("", Attribute.BOLD, FColor.YELLOW, BColor.BLACK);
	}

	public static void setBoxPrinter() {
		cp.print("", Attribute.BOLD, FColor.WHITE, BColor.YELLOW);
	}

	public static void setErrorBoxPrinter() {
		cp.print("", Attribute.BOLD, FColor.WHITE, BColor.RED);
	}
}
