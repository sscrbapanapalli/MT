package com.cmacgm.cdrserver.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="ssc_utilization_metrics")
public class MonitoringDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="m_user_name", length=50)
	private String empUserName;	
		
	@Column(name="m_machine_name", length=50)
	private String machineName;
	
	@Column(name="m_curr_date")
	private Date currentDate;	
		
	@Column(name="m_curr_month", length=10)
	private String currentMonth;

	@Column(name="m_curr_year", length=5)
	private String currentYear;
	
//	@Column(name="active_indicator")
	//private boolean activeIndicator;
	
	
    @Column(name="m_LogOn_time")
    private Date LogOnTime;
    
	
    @Column(name="m_LogOff_time")
    private Date LogoffTime;
    
	
    @Column(name="m_active_time")
    private String activeTime;
	
	
    @Column(name="m_idle_time")
    private String idleTime;
	
	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="m_created_dt")
    private Date createdDate;
	
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="m_last_upd_dt")
    private Date updatedDate;
    
    @Column(name="created_by",length = 100)    
    private String createdBy;
    
    @Column(name="updated_by",length = 100)
    private String updatedBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmpUserName() {
		return empUserName;
	}

	public void setEmpUserName(String empUserName) {
		this.empUserName = empUserName;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

	public Date getLogOnTime() {
		return LogOnTime;
	}

	public void setLogOnTime(Date logOnTime) {
		LogOnTime = logOnTime;
	}

	public Date getLogoffTime() {
		return LogoffTime;
	}

	public void setLogoffTime(Date logoffTime) {
		LogoffTime = logoffTime;
	}

	

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}

	public String getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(String idleTime) {
		this.idleTime = idleTime;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
    
    
    
 }
