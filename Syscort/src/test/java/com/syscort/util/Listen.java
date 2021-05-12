package com.syscort.util;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import com.syscort.test.TestPayment;

public class Listen implements IInvokedMethodListener {

	public static boolean hasFailures = false;

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		synchronized (this) {
			if (hasFailures) {
				try {
					ReadExcel.writeExcelData(TestPayment.sessionUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
				throw new SkipException("Skipping this test");
			}
		}

	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub

	}

}
