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
@Table(name="AuditHistoryActivityTrack")
public class AuditHistoryActivityTrack {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="user_id", length=100)
	private String userId;
	
	@Column(name="activity_start_time")
    private Date activityStartTime;
	
	@Column(name="activity_end_time")
    private Date activityEndTime;  	
	
	@ManyToOne
	@JoinColumn(name="activity_Id",referencedColumnName="id")
	private ActivitySettings activityId;
	
	/*@Column(name="activity_name", length=100)
	private String activityName;	
*/
	@Column(name="revised_activity_start_time")
    private Date revisedActivityStartTime;
	
	@Column(name="revised_activity_end_time")
    private Date revisedActivityEndTime;
	
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
    
    private transient String revActivityStartTime;
	

    private transient String revActivityEndTime;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
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


	public Date getRevisedActivityStartTime() {
		return revisedActivityStartTime;
	}


	public void setRevisedActivityStartTime(Date revisedActivityStartTime) {
		this.revisedActivityStartTime = revisedActivityStartTime;
	}


	public Date getRevisedActivityEndTime() {
		return revisedActivityEndTime;
	}


	public void setRevisedActivityEndTime(Date revisedActivityEndTime) {
		this.revisedActivityEndTime = revisedActivityEndTime;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
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


	public String getRevActivityStartTime() {
		return revActivityStartTime;
	}


	public void setRevActivityStartTime(String revActivityStartTime) {
		this.revActivityStartTime = revActivityStartTime;
	}


	public String getRevActivityEndTime() {
		return revActivityEndTime;
	}


	public void setRevActivityEndTime(String revActivityEndTime) {
		this.revActivityEndTime = revActivityEndTime;
	}


	public ActivitySettings getActivityId() {
		return activityId;
	}


	public void setActivityId(ActivitySettings activityId) {
		this.activityId = activityId;
	}


	/*public String getActivityName() {
		return activityName;
	}


	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}	
	*/
    
	
    
	
}
