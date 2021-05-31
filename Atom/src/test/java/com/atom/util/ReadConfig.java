package com.atom.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadConfig {
	public Properties prop;
	public FileInputStream in;
	public int numberOfUsers = Integer.parseInt(readConfigData("NumberOfUser"));
	public int threads = Integer.parseInt(readConfigData("ThreadCount"));
	public int explicitWait = Integer.parseInt(readConfigData("ExplicitWaitTime"));
	public int skipAfterFailure = Integer.parseInt(readConfigData("SkitAfterFailure"));
	public String url = readConfigData("URL");

	public String readConfigData(String testdata) {
		if (prop == null) {
			try {
				prop = new Properties();
				in = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
				prop.load(in);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop.getProperty(testdata);
	}
}
