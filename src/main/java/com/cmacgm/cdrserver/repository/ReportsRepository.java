package com.cmacgm.cdrserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.cmacgm.cdrserver.model.EmployeeDetails;

public interface ReportsRepository extends JpaRepository<EmployeeDetails, Long> {

	public List<EmployeeDetails> findByRmId(@Param("rmId") String rmId);	


	
}
