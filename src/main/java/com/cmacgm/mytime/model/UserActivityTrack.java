package com.cmacgm.mytime.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="UserActivityTrack")
public class UserActivityTrack {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="user_id", length=100)
	private String userId;
	
	@ManyToOne
    @JoinColumn( name = "activity_id" , referencedColumnName = "id")
	private ActivitySettings activityId;
	
	
	public ActivitySettings getActivityId() {
		return this.activityId;
	}

	public void setActivityId(ActivitySettings activityId) {
		this.activityId = activityId;
	}
	
	
	/*@Column(name="activity_name", length=100)
	private String activityName;
	
	@Column(name="activity_type", length=100)
	private String activityType;*/
	
	@Column(name="activity_cur_year", length=100)
	private Long activityCurYear;
	
	@Column(name="activity_cur_mon", length=100)
	private String activityCurMon;
	
	@Column(name="activity_cur_date")
    private Date activityCurDate;
	
	@Column(name="activity_status", length=100)
	private String activityStatus;
	
	@Column(name="activity_start_time")
    private Date activityStartTime;
	
	@Column(name="activity_end_time")
    private Date activityEndTime;
	
	@Column(name="activity_tot_time")
    private String activityTotTime;
	
	@Column(name="comments")
    private String comments;
	
	
	
	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_date")
    private Date createdDate;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_date")
    private Date updatedDate;
    
    @Column(name="created_by",length = 100)    
    private String createdBy;
    
    @Column(name="updated_by",length = 100)
    private String updatedBy;

	
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	/*public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}*/

	
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
	
	  
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/*public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityID) {
		this.activityId = activityID;
	}*/

	public Long getActivityCurYear() {
		return activityCurYear;
	}

	public void setActivityCurYear(Long activityCurYear) {
		this.activityCurYear = activityCurYear;
	}

	public String getActivityCurMon() {
		return activityCurMon;
	}

	public void setActivityCurMon(String activityCurMon) {
		this.activityCurMon = activityCurMon;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Date getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}



	public String getActivityTotTime() {
		return activityTotTime;
	}

	public void setActivityTotTime(String activityTotTime) {
		this.activityTotTime = activityTotTime;
	}

	public Date getActivityCurDate() {
		return activityCurDate;
	}

	public void setActivityCurDate(Date activityCurDate) {
		this.activityCurDate = activityCurDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	

	/*public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}*/

	
	
	 /*@Override
	    public String toString() {
	        final StringBuilder builder = new StringBuilder();
	        builder.append("Application [id=").append(id).append("]")
	        .append("[userId=").append(userId).append("]")
	        .append("[activityName=").append(activityName).append("]")
	        .append("[activityCurYear=").append(activityCurYear).append("]")
	        .append("[activityCurMon=").append(activityCurMon).append("]")
	        .append("[activityCurDate=").append(activityCurDate).append("]")
	        .append("[activityStatus=").append(activityStatus).append("]")
	        .append("[activityStartTime=").append(activityStartTime).append("]")
	        .append("[activityEndTime=").append(activityEndTime).append("]")
	        .append("[activityTotTime=").append(activityTotTime).append("]")
	     	        
	        .append("[createdDate=").append(createdDate).append("]").append("[updatedDate=").append(updatedDate).append("]")
	        .append("[createdBy=").append(createdBy).append("]")    .append("[updatedBy=").append(updatedBy).append("]");
	        return builder.toString();
	    }*/

}
