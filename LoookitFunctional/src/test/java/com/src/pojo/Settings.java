package com.src.pojo;

import java.util.List;

public class Settings {
	private String fakeCamera;
	private String url;
	private String uid;
	private String password;
	private String noOfMeeting;
	private String usersPerMeeting;
	private String maximumInstance;
	private String meetingPrefix;
	private List<MeetingUsers> users;
	private String testType[];
	private String meetingType;

	public String getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	public String[] getTestType() {
		return testType;
	}

	public void setTestType(String[] testType) {
		this.testType = testType;
	}

	public String getMeetingPrefix() {
		return meetingPrefix;
	}

	public void setMeetingPrefix(String meetingPrefix) {
		this.meetingPrefix = meetingPrefix;
	}

	public String getMaximumInstance() {
		return maximumInstance;
	}

	public void setMaximumInstance(String maximumInstance) {
		this.maximumInstance = maximumInstance;
	}

	public String getFakeCamera() {
		return fakeCamera;
	}

	public void setFakeCamera(String fakeCamera) {
		this.fakeCamera = fakeCamera;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNoOfMeeting() {
		return noOfMeeting;
	}

	public void setNoOfMeeting(String noOfMeeting) {
		this.noOfMeeting = noOfMeeting;
	}

	public String getUsersPerMeeting() {
		return usersPerMeeting;
	}

	public void setUsersPerMeeting(String usersPerMeeting) {
		this.usersPerMeeting = usersPerMeeting;
	}

	public List<MeetingUsers> getUsers() {
		return users;
	}

	public void setUsers(List<MeetingUsers> users) {
		this.users = users;
	}

}
