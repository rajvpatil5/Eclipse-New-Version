package com.src.util;

import org.openqa.selenium.WebDriver;

public class userInfo {

	private String meetingName;
	private String nodeIp;
	private WebDriver driver;
	private String userName;
	private boolean isHost;

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public String getMeetingName() {
		return meetingName;
	}

	public String getNodeIp() {
		return nodeIp;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public String getUserName() {
		return userName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
