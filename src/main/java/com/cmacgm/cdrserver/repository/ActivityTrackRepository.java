package com.cmacgm.cdrserver.repository;

	import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmacgm.cdrserver.model.UserActivityTrack;

public interface ActivityTrackRepository extends JpaRepository<UserActivityTrack, Long>{
	
	public List<UserActivityTrack> findByUserId(@Param("userId") String userId);
	
	public UserActivityTrack findById(@Param("id") Long Id);
	public List<UserActivityTrack> findByUserIdAndCreatedDateBetween(String userId,Date dateFrom,Date dateTo);
	
	/*@Modifying(clearAutomatically = true)
	@Transactional
	@Query("update UserActivityTrack u set u.activityStartTime = :activityStartTime, u.activityEndTime = :activityEndTime , u.activityTotTime = :activityTotTime ,u.comments = :comments, u.updatedBy = :updatedBy where u.id = :id")
	void overrideEmployeeTime(@Param("activityStartTime") Date activityStartTime,@Param("activityEndTime") Date activityEndTime,@Param("activityTotTime") String activityTotTime,@Param("comments") String comments,@Param("updatedBy") String updatedBy,@Param("id") Long id);
	*/
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("update UserActivityTrack u set u.activityStatus = :activityStatus, u.updatedBy = :updatedBy where u.id = :id")
	void overrideEmployeeTime(@Param("activityStatus") String activityStatus,@Param("updatedBy") String updatedBy,@Param("id") Long id);
	

}
