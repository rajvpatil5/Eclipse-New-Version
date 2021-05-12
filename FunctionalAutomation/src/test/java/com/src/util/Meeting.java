package com.src.util;

import java.util.ArrayList;
import java.util.List;

public class Meeting {
	private String meetingName;
	private String nodeIp;
	private List<userInfo> users;

	public String getMeetingName() {
		return meetingName;
	}

	public String getNodeIp() {
		return nodeIp;
	}

	public List<userInfo> getUsers() {
		return users;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public void setUsers(List<userInfo> users) {
		this.users = users;
	}

	public Meeting() {
		super();
		users= new ArrayList<>();
	}

	public void addUser(userInfo userInfo) {
		this.users.add(userInfo);
	}

	public userInfo getHostUser() {
		for (userInfo userInfo : users) {
			if(userInfo.isHost())
				return userInfo;
		}
		return null;
	}
	
	public userInfo getUserByName(String userName) {
		for (userInfo userInfo : users) {
			if(userInfo.getUserName().equalsIgnoreCase(userName))
				return userInfo;
		}
		return null;
	}
}
