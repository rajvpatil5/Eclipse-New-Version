package com.syscort.util;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReport implements IReporter {
	private ExtentReports extent;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
//		Date date = new Date();
//		String reportName = "Report_" + date.toString().replace(" ", "_").replace(":", "_") + ".html";
//		outputDirectory = System.getProperty("user.dir") + "/Report";
		extent = new ExtentReports(outputDirectory + File.separator + "ExtentReport.html", true,
				DisplayOrder.NEWEST_FIRST);

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();

				buildTestNodes(context.getPassedTests(), LogStatus.PASS);
				buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
				buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
				buildTestNodes(context.getSkippedTests(), LogStatus.INFO);
			}
		}

		extent.flush();
		extent.close();
	}

	private void buildTestNodes(IResultMap tests, LogStatus status) {
		ExtentTest test;

		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				test = extent.startTest(result.getMethod().getMethodName());
//				test.log(status, "Email", result.getParameters()[0].toString());
//				test.log(status, "Phone Number", result.getParameters()[1].toString());
//				test.log(status, "Amount", result.getParameters()[2].toString());
//				test.log(status, "Card Number", result.getParameters()[3].toString());
//				test.log(status, "Card Expiry", result.getParameters()[4].toString());
//				test.log(status, "Card Holder Name", result.getParameters()[5].toString());
//				test.log(status, "Card CVV", result.getParameters()[6].toString());

				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));

				for (String group : result.getMethod().getGroups())
					test.assignCategory(group);

				if (result.getThrowable() != null) {
					test.log(status, "Test Details", result.getThrowable());
				} else {
					test.log(status, "Test Details", "Test " + status.toString().toLowerCase() + "ed");
				}

				extent.endTest(test);
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
}
