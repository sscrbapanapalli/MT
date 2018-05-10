package com.cmacgm.cdrserver.model;

import java.util.List;

public class ActivityConfig {
	
	private Long id;
	private String userName;
	private List<String> activityMapping;
	private List<ActivitySettings> activityTracker;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<String> getActivityMapping() {
		return activityMapping;
	}
	public void setActivityMapping(List<String> activityMapping) {
		this.activityMapping = activityMapping;
	}
	public List<ActivitySettings> getActivityTracker() {
		return activityTracker;
	}
	public void setActivityTracker(List<ActivitySettings> activityTracker) {
		this.activityTracker = activityTracker;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	

}