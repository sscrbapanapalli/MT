package com.cmacgm.cdrserver.repository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmacgm.cdrserver.model.EmployeeDetails;
@Service
@Transactional
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService{

	@Autowired(required=true)
	EmployeeDetailsRepository employeeDetailsRepository;
	

	@Override
	public EmployeeDetails findById(Long id) {
		// TODO Auto-generated method stub
		 return employeeDetailsRepository.findOne(id);
	}

	@Override
	public EmployeeDetails findByEmpId(String emailId) {
		// TODO Auto-generated method stub
		 return employeeDetailsRepository.findByEmailId(emailId);
	}

}
