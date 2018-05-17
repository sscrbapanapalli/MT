package com.cmacgm.mytime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.cmacgm.mytime.model.ActivitySettings;

public interface ActivityConfigRepository extends JpaRepository<ActivitySettings, Long>{
	
	public ActivitySettings findById(@Param("id") Long id);
	
	public List<ActivitySettings> findByActiveIndicator(@Param("activeIndicator") boolean activeIndicator);

}
