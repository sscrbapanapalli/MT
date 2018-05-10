package com.cmacgm.cdrserver.repository;

	import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.cmacgm.cdrserver.model.UserActivityTrack;

public interface ActivityTrackRepository extends JpaRepository<UserActivityTrack, Long>{
	
	public List<UserActivityTrack> findByUserId(@Param("userId") String userId);
	
	public UserActivityTrack findById(@Param("id") Long Id);
	public List<UserActivityTrack> findByUserIdAndCreatedDateBetween(String userId,Date dateFrom,Date dateTo);
	
	

}
