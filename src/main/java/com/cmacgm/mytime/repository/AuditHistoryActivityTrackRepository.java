package com.cmacgm.mytime.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmacgm.mytime.model.AuditHistoryActivityTrack;

@Repository
public interface AuditHistoryActivityTrackRepository extends JpaRepository<AuditHistoryActivityTrack, Long>{
	
	public List<AuditHistoryActivityTrack> findByUpdatedByAndCreatedDateBetween(String userId,Date dateFrom,Date dateTo);
	
	public List<AuditHistoryActivityTrack> findByCreatedDateBetween(Date dateFrom,Date dateTo);
	

}
