package com.cmacgm.mytime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.cmacgm.mytime.model.EmployeeDetails;

public interface ReportsRepository extends JpaRepository<EmployeeDetails, Long> {

	public List<EmployeeDetails> findByRmId(@Param("rmId") String rmId);	


	
}
