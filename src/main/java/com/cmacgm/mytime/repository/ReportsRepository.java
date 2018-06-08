package com.cmacgm.mytime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cmacgm.mytime.model.EmployeeDetails;

public interface ReportsRepository extends JpaRepository<EmployeeDetails, Long> {

	public List<EmployeeDetails> findByRmId(@Param("rmId") String rmId);	
	
	
	@Query(nativeQuery = true,value = "SELECT * FROM Employee_Details a WHERE a.rm_Id = :rmId ORDER BY a.id LIMIT 1")
	public List<EmployeeDetails> findTopByRmId(@Param("rmId") String rmId);


	
}
