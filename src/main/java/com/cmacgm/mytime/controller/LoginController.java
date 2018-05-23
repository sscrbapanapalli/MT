package com.cmacgm.mytime.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.mytime.model.EmployeeDetails;
import com.cmacgm.mytime.repository.EmployeeDetailsRepository;

/**
 * @filename LoginController.java(To Validate user login credentials across
 *           LDAP)
 * @author Ramesh Kumar B
 * 
 */

@RestController
@RequestMapping("/login")
public class LoginController {		
	
	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;


	@RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public EmployeeDetails  getUserDetails(HttpServletRequest request)
			throws Exception {
	  String windowsUserName=request.getParameter("windowsUserName");
		EmployeeDetails userDetail = null;		
		if ( !windowsUserName.isEmpty() && windowsUserName!=null){		
			userDetail = new EmployeeDetails();		
			userDetail=employeeDetailsRepository.findByEmailId(windowsUserName + "@CMA-CGM.COM");				
				return userDetail;
		}
		return  null;

		

	}

}


