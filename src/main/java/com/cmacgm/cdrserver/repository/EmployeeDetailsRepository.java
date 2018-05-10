package com.cmacgm.cdrserver.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmacgm.cdrserver.model.EmployeeDetails;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long>{
	
	public EmployeeDetails findById(@Param("id") Long id);
	
	public EmployeeDetails findByEmpId(@Param("empId") String empId);
	
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("update EmployeeDetails u set u.empName = :empName, u.rmId = :rmId, u.rmName = :rmName, u.isRm = :isRm where u.empId = :empId")
	void setEmployee(@Param("empName") String empName,@Param("rmId")String rm_id,@Param("rmName") String rm_name, @Param("isRm") Boolean isRm,@Param("empId") String emp_id);
	
	

}
