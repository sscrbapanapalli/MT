package com.cmacgm.mytime.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmacgm.mytime.model.EmployeeDetails;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long>{
	
	public EmployeeDetails findById(@Param("id") Long id);
	
	public EmployeeDetails findByEmailId(@Param("emailId") String emailId);
	
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("update EmployeeDetails u set u.empName = :empName, u.empId = :empId, u.rmId = :rmId, u.rmName = :rmName, u.isAdmin = :isAdmin where u.emailId = :emailId")
	void setEmployee(@Param("empName") String empName,@Param("empId") Long empId,@Param("rmId")String rm_id,@Param("rmName") String rm_name,@Param("isAdmin") Boolean isAdmin,@Param("emailId") String emailId);
	
	

}
