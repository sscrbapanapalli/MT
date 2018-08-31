package com.cmacgm.mytime.model;

public class ActivityDetails {
	private String activityName;
	private String activityType;
	private int thresholdHours;
	private int thresholdMins;
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public int getThresholdHours() {
		return thresholdHours;
	}
	public void setThresholdHours(int thresholdHours) {
		this.thresholdHours = thresholdHours;
	}
	public int getThresholdMins() {
		return thresholdMins;
	}
	public void setThresholdMins(int thresholdMins) {
		this.thresholdMins = thresholdMins;
	}
	
	

}
