package com.cmacgm.cdrserver.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.cdrserver.model.EmployeeDetails;
import com.cmacgm.cdrserver.repository.EmployeeDetailsRepository;

/**
 * @filename AdminConfigController.java(To set user level access to application)
* @author Ramesh Kumar B

*/

@RestController
@RequestMapping("/admin")
public class AdminConfigController {
	
	
	@Autowired(required = true)
	private HttpSession httpSession;

	 

	 @Autowired
	 EmployeeDetailsRepository employeeDetailsRepository;
	 


	 @GetMapping("/getAllEmployees")
		public List<EmployeeDetails> getAllEmployees(){
		 System.out.println(" All Employees");		 
			return employeeDetailsRepository.findAll();
		}
	 
	 @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
			String deleteResponse="";
			
			Long id=Long.parseLong(request.getParameter("id"));
			String updatedBy=request.getParameter("updatedBy");
			EmployeeDetails employeeDetails=employeeDetailsRepository.findById(id);
			System.out.println(" in delete reverse method server");
			if(employeeDetails!=null){
				try{
					employeeDetails.setUpdatedBy(updatedBy);
					employeeDetails.setActiveIndicator(false);				
					employeeDetailsRepository.save(employeeDetails);
					deleteResponse="Employee-" + employeeDetails.getEmpName() +" Deleted Successfully";
					
				}catch(Exception e){
					deleteResponse="Employee-" + employeeDetails.getEmpName() +" failed to Delete ";
				}
				
			}else{
			deleteResponse="Employee-" + employeeDetails.getEmpName() +" Not found in DB";
			}
			return deleteResponse;
		}
		
		
		@RequestMapping(value = "/setEmployeeConfiguration", method = RequestMethod.POST ,consumes="application/json",  produces="application/json")
	    public @ResponseBody String setEmployeeConfiguration(@RequestBody EmployeeDetails employeeDetailsConfig) throws IllegalStateException, IOException {
			
			 String configResponse="Success";
			 
			 if(employeeDetailsConfig==null)
					return configResponse="Failure No Data Found";
			 String emailId=employeeDetailsConfig.getEmailId();
			 emailId=emailId.toUpperCase();
					 
			 String userDisplayName=null;
			 EmployeeDetails checkEmployee=new EmployeeDetails();
			 EmployeeDetails employee=new EmployeeDetails();
			 checkEmployee=employeeDetailsRepository.findByEmailId(emailId);
			 
			 
			 String[] words = emailId.split("@");
				if (words[0] != null)
					 userDisplayName = words[0];
				userDisplayName = userDisplayName.toUpperCase();
			
				
			 if(checkEmployee==null){
				 employee.setEmpName(employeeDetailsConfig.getEmpName());
				 employee.setEmailId(employeeDetailsConfig.getEmailId());
				 employee.setEmpId(employeeDetailsConfig.getEmpId());
				 employee.setRmName(employeeDetailsConfig.getRmName());
				 employee.setAdmin(employeeDetailsConfig.isAdmin());
				 employee.setRmId(employeeDetailsConfig.getRmId());				
				 employee.setActiveIndicator(employeeDetailsConfig.isActiveIndicator());
				 employee.setRmId(employeeDetailsConfig.getRmId());
				 employee.setCreatedBy(employeeDetailsConfig.getCreatedBy());
				 employee.setUpdatedBy(employeeDetailsConfig.getCreatedBy());
				
				 try{
					 employeeDetailsRepository.save(employee);
				 configResponse="Employee Details saved successfully";
				 return configResponse;
				 }catch(Exception e){
					 
					 configResponse="Failed to save Config Employee Details";
					 return configResponse;
				 }
			 }else{
				 
				 
				 checkEmployee.setEmpName(userDisplayName);
				 employee.setAdmin(employeeDetailsConfig.isAdmin());
				 checkEmployee.setEmpId(employeeDetailsConfig.getEmpId());
				 employee.setEmailId(employeeDetailsConfig.getEmailId());
				 checkEmployee.setRmName(employeeDetailsConfig.getRmName());				
				 checkEmployee.setActiveIndicator(employeeDetailsConfig.isActiveIndicator());
				 checkEmployee.setRmId(employeeDetailsConfig.getRmId());
				 checkEmployee.setCreatedBy(employeeDetailsConfig.getCreatedBy());
				 checkEmployee.setUpdatedBy(employeeDetailsConfig.getCreatedBy());
				 checkEmployee.setId(employeeDetailsConfig.getId());
				 
				 try{
					 
					 employeeDetailsRepository.save(checkEmployee);
					 configResponse="Config Employee Details Updated successfully";
					 return configResponse;
				 }catch(OptimisticLockException r){
					 configResponse="Failed to save Config Employee Details as some one locked the update row";
					 return configResponse;
					 
				 }
				 catch(Exception e){
					 
					 System.out.println(e.getMessage());
					 configResponse="Failed to save Config Employee Details";
					 return configResponse;
				 }
			 }
		    
		 }
		

}
