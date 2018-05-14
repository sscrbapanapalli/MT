package com.cmacgm.cdrserver.model;

import java.util.Date;

public class UserHomeModel {
	
	private long id;
	private boolean activeIndicator;
	private String userId;
	private String userName; 
	private String createdBy;
	private Date createdDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isActiveIndicator() {
		return activeIndicator;
	}
	public void setActiveIndicator(boolean activeIndicator) {
		this.activeIndicator = activeIndicator;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	 

}
