package com.cmacgm.mytime.model;

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
@Table(name="ActivitySettings")
public class ActivitySettings {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="activity_name", length=100)
	private String activityName;
	
	@Column(name="activity_seq_num", length=100)
	private Long activitySeqNum;
	
	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_date")
    private Date createdDate;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_date")
    private Date updatedDate;
    
    @Column(name="active_indicator",nullable = false, columnDefinition = "TINYINT(1)")
	 private boolean activeIndicator;
    
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

	public Long getActivitySeqNum() {
		return activitySeqNum;
	}

	public void setActivitySeqNum(Long activitySeqNum) {
		this.activitySeqNum = activitySeqNum;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
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

	public boolean isActiveIndicator() {
		return activeIndicator;
	}

	public void setActiveIndicator(boolean activeIndicator) {
		this.activeIndicator = activeIndicator;
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
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Application [id=").append(id).append("]").append("[activityName=").append(activityName).append("]")
        .append("[activitySeqNum=").append(activitySeqNum).append("]")
        .append("[createdDate=").append(createdDate).append("]").append("[updatedDate=").append(updatedDate).append("]")
        .append("[activeIndicator=").append(activeIndicator).append("]").append("[createdBy=").append(createdBy).append("]")
        .append("[updatedBy=").append(updatedBy).append("]");
        return builder.toString();
    }

}
