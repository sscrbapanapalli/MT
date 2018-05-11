package com.cmacgm.cdrserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmacgm.cdrserver.model.ActivitySettings;
import com.cmacgm.cdrserver.model.MonitoringDetails;


	public interface ActiveMonitoringRepository extends JpaRepository<MonitoringDetails, Long>{

}
