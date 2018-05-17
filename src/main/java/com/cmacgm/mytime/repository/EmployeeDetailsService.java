package com.cmacgm.mytime.repository;

import org.springframework.data.repository.query.Param;

import com.cmacgm.mytime.model.EmployeeDetails;

public interface EmployeeDetailsService {
	
	public EmployeeDetails findById(@Param("id") Long id);
	public EmployeeDetails findByEmpId(@Param("empId") String empId);


}
