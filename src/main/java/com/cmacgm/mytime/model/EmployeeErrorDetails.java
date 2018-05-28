package com.cmacgm.mytime.model;

public class EmployeeErrorDetails {
	
	

	private Long empId;	
		

	private String empName;

	private String emailId;	
	

	private String rmId;	
		
	
	private String rmName;

	private String teamName;


	private boolean isAdmin;

	 private boolean activeIndicator;
	


	public Long getEmpId() {
		return empId;
	}



	public void setEmpId(Long empId) {
		this.empId = empId;
	}



	public String getEmpName() {
		return empName;
	}



	public void setEmpName(String empName) {
		this.empName = empName;
	}



	public String getEmailId() {
		return emailId;
	}



	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}



	public String getRmId() {
		return rmId;
	}



	public void setRmId(String rmId) {
		this.rmId = rmId;
	}



	public String getRmName() {
		return rmName;
	}



	public void setRmName(String rmName) {
		this.rmName = rmName;
	}



	public boolean isAdmin() {
		return isAdmin;
	}



	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}



	public boolean isActiveIndicator() {
		return activeIndicator;
	}



	public void setActiveIndicator(boolean activeIndicator) {
		this.activeIndicator = activeIndicator;
	}



	public String getTeamName() {
		return teamName;
	}



	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}




	
    

}
