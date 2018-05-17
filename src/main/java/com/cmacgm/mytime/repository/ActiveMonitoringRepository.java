package com.cmacgm.mytime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmacgm.mytime.model.ActivitySettings;
import com.cmacgm.mytime.model.MonitoringDetails;


	public interface ActiveMonitoringRepository extends JpaRepository<MonitoringDetails, Long>{

}
