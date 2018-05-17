package com.cmacgm.mytime.repository;

	import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmacgm.mytime.model.UserActivityTrack;

public interface ActivityTrackRepository extends JpaRepository<UserActivityTrack, Long>{
	@Query(nativeQuery=true,value="select * from user_activity_track t where t.activity_status in('In Progress','Not Started') and t.user_id=:userId ")
	public List<UserActivityTrack> findByUserId(@Param("userId") String userId);
	
	public UserActivityTrack findById(@Param("id") Long Id);
	public List<UserActivityTrack> findByUserIdAndCreatedDateBetween(String userId,Date dateFrom,Date dateTo);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("update UserActivityTrack u set u.activityStartTime = :activityStartTime, u.activityEndTime = :activityEndTime , u.activityTotTime = :activityTotTime ,u.comments = :comments, u.updatedBy = :updatedBy where u.id = :id")
	void overrideEmployeeTime(@Param("activityStartTime") Date activityStartTime,@Param("activityEndTime") Date activityEndTime,@Param("activityTotTime") String activityTotTime,@Param("comments") String comments,@Param("updatedBy") String updatedBy,@Param("id") Long id);


	@Query(nativeQuery=true,value="select count(*) from user_activity_track t where t.activity_status='In Progress' and t.user_id=:userId ")
	public int findByActivityStatus(@Param("userId") String userId);
	
	
	
	

}
