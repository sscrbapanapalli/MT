package com.cmacgm.cdrserver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.ApplicationConfig;
import com.cmacgm.cdrserver.model.ApplicationFileUploadConfig;
import com.cmacgm.cdrserver.model.EmployeeDetails;
import com.cmacgm.cdrserver.model.FolderMapping;
import com.cmacgm.cdrserver.model.Role;
import com.cmacgm.cdrserver.model.User;
import com.cmacgm.cdrserver.model.UserConfig;
import com.cmacgm.cdrserver.model.UserHomeModel;
import com.cmacgm.cdrserver.repository.AdminConfigRepository;
import com.cmacgm.cdrserver.repository.ApplicationFileUploadConfigRepository;
import com.cmacgm.cdrserver.repository.ApplicationRepository;
import com.cmacgm.cdrserver.repository.EmployeeDetailsRepository;
import com.cmacgm.cdrserver.repository.RolesRepository;
import com.cmacgm.cdrserver.repository.UserRepository;

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
	UserRepository userRepository;
	
	 @Autowired
	 AdminConfigRepository adminConfigRepository;
	 
	 @Autowired
		ApplicationRepository applicationRepository;
	 

	 @Autowired
	 EmployeeDetailsRepository employeeDetailsRepository;
	 
	 @Autowired
		RolesRepository roleRepository;
	 
	 @Autowired
	 ApplicationFileUploadConfigRepository applicationFileUploadConfigRepository;
	 
	 
	
	 @GetMapping("/getAllApplications")
		public List<Application> getAllApplications(){
		 System.out.println(" for all applications");
		 
			return applicationRepository.findAll();
		}
	 
	 //For james web client
	 @GetMapping("/batchFolders")
		public List<String> batchFolders(){
		 System.out.println(" for all batchFolders");
		 String directoryName="C:/Users/ssc.rbapanapalli/Desktop/Test";
		 File directory = new File(directoryName);
		 List<String> folderList=new ArrayList<String>();
		 File[] fList = directory.listFiles();
	        for (File file : fList){
	            if (file.isDirectory()){
	            	folderList.add(file.getAbsolutePath());
	                System.out.println(file.getAbsolutePath());
	            }
	        }
		
	   			return folderList;
		}
	 
			 
	 @GetMapping("/getAttachments")
	   	public List<String> getAttachments(HttpServletRequest req){
		 
		String folder=req.getParameter("selectedFolder");
		 System.out.println(req.getParameter("selectedFolder"));
		 File directory = new File(folder);
	        //get all the files from a directory
		 List<String> filesList=new ArrayList<String>();
	        File[] fList = directory.listFiles();
	        for (File file : fList){
	            if (file.isFile()){
	            	filesList.add(file.getAbsolutePath());
	                System.out.println(file.getAbsolutePath());
	            }
	        }
	        return filesList;
	   		
	   	}
	 
	 @GetMapping("/getAllRoles")
		public List<Role> getAllRoles(){
		 System.out.println(" for all roles");
			return roleRepository.findAll();
		}
	@GetMapping("/getAllUserDetails")
	public List<UserHomeModel> getAllUserDetails() throws Exception{
		List<UserHomeModel> allUserResultList=new ArrayList<>();
		try{
			List<User> userResult=userRepository.findAll();
			/*Predicate<User> byAge = user -> user.getId() == 1;
			User result = userResult.stream().filter(byAge).findFirst().orElse(null);*/
			User result = userResult.stream().filter(user -> user.getId() == 35).findAny().orElse(null);
			System.out.println("Test Result"+ result);
			
			for(User testObj:userResult){
				
				Collection<String> appListObj=new ArrayList<>();
				Collection<String> roleListObj=new ArrayList<>();
				
				UserHomeModel obj=new UserHomeModel();
				obj.setId(testObj.getId());
				obj.setUserId(testObj.getUserId());
				obj.setCreatedBy(testObj.getCreatedBy());
				obj.setCreatedDate(testObj.getCreatedDate());
				obj.setUserName(testObj.getUserDisplayName());
				obj.setActiveIndicator(testObj.isActiveIndicator());
				obj.setApplications(testObj.getApplications());
				obj.setRoles(testObj.getRoles());
				
				for(Application appObj:testObj.getApplications()){
					appListObj.add(appObj.getappName());
					
				}
				for(Role roleObj:testObj.getRoles()){
					roleListObj.add(roleObj.getRoleName());
					
				}
				obj.setAppList(appListObj);
				obj.setRoleList(roleListObj);
				allUserResultList.add(obj);
			}
			
		}catch(Exception e){
			throw e;
		}
		return allUserResultList;
	}
	
	/*@RequestMapping(value = "/checkUser", method = RequestMethod.POST , produces="application/json")
    public @ResponseBody UserHomeModel checkUser(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String userStatus="";
		String userName=request.getParameter("userName");
		System.out.println("in check user method" + userName);
		User userObj=userRepository.findByUserId(userName);
		
		UserHomeModel userDetailObj=new UserHomeModel();
		userDetailObj.setId(userObj.getId());
		userDetailObj.setUserId(userObj.getUserId());
		userDetailObj.setCreatedBy(userObj.getCreatedBy());
		userDetailObj.setCreatedDate(userObj.getCreatedDate());
		userDetailObj.setUserName(userObj.getUserDisplayName());
		userDetailObj.setActiveIndicator(userObj.isActiveIndicator());
		userDetailObj.setApplications(userObj.getApplications());
		userDetailObj.setRoles(userObj.getRoles());
		
		System.out.println("in check user method result" + userDetailObj);
		return userDetailObj;
	
	}*/
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST , produces="application/json")
    public @ResponseBody String deleteUser(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String deleteResponse="";
		
		Long id=Long.parseLong(request.getParameter("id"));
		String updatedBy=request.getParameter("updatedBy");
		User user=userRepository.findById(id);
		System.out.println(" in delete reverse method server");
		if(user!=null){
			try{
				user.setActiveIndicator(false);
				System.out.println(user.isActiveIndicator());
				userRepository.save(user);
				deleteResponse="User-" + user.getUserDisplayName() +" Deleted Successfully";
				
			}catch(Exception e){
				deleteResponse="User-" + user.getUserDisplayName() +" failed to Delete ";
			}
			
		}else{
		deleteResponse="User-" + user.getUserDisplayName() +" Not found in DB";
		}
		return deleteResponse;
	}
		

	@RequestMapping(value = "/setUserConfiguration", method = RequestMethod.POST ,consumes="application/json",  produces="application/json")
    public @ResponseBody String setUserConfiguration(@RequestBody UserConfig userConfig) throws IllegalStateException, IOException {
		System.out.println("In User Config method");
		 Long updatedVersion=0L;
		 String configResponse="Success";
		 String userId=userConfig.getUserId();
		 userId=userId.toUpperCase();
		 List<Application> selectedAppList=userConfig.getSelectedApplications();
		 List<Role> selectedRoleList=userConfig.getSelectedRoles();
		 boolean userStatus=userConfig.getUserStatus();
		 String createdBy=userConfig.getCreatedBy();
		 
		 String userDisplayName=null;
		 User user=new User();
		 User checkUser=new User();
		 checkUser=userRepository.findByUserId(userId);
		 Application application=null;
		 Role role=null;
		 List<Role> roleList=new ArrayList<Role>();
		 List<Application> applicationList=new ArrayList<Application>();
		  for(Application myListAppId:selectedAppList){
			 application=new Application(); 
		     application.setId(myListAppId.getId());
		 	 applicationList.add(application);
		 }
		 for(Role myListRoleId:selectedRoleList){
			 role=new Role();
			 role.setId(myListRoleId.getId());
			 roleList.add(role);
		
		 }
		 
		 String[] words = userId.split("@");
			if (words[0] != null)
				 userDisplayName = words[0];
			userDisplayName = userDisplayName.toUpperCase();
		 
		 if(checkUser==null){
	         user.setUserDisplayName(userDisplayName);
			 user.setUserId(userId);
			 user.setCreatedBy(createdBy);
			 user.setUpdatedBy(createdBy);
			 user.setApplications(applicationList);
			 user.setRoles(roleList);
			 user.setActiveIndicator(userStatus);
			 try{
			 adminConfigRepository.save(user);
			 configResponse="Config details saved successfully";
			 return configResponse;
			 }catch(Exception e){
				 configResponse="Failed to save Config details";
				 return configResponse;
			 }
		 }else{
			 
			 //updatedVersion=checkUser.getVersion()+1;
			 checkUser.setUserDisplayName(userDisplayName);
			 checkUser.setUserId(userId);
			 checkUser.setUpdatedBy(createdBy);
			 checkUser.setApplications(applicationList);
			 checkUser.setRoles(roleList);
			 checkUser.setActiveIndicator(userStatus);
			 //checkUser.setVersion(updatedVersion);
			 try{
				 
				 adminConfigRepository.save(checkUser);
				// checkUser.setCreatedBy("rame");
				 adminConfigRepository.save(checkUser);
				 configResponse="Config details Updated successfully";
				 return configResponse;
			 }catch(OptimisticLockException r){
				 configResponse="Failed to save Config details as some one locked the update row";
				 return configResponse;
				 
			 }
			 catch(Exception e){
				 configResponse="Failed to save Config details";
				 return configResponse;
			 }
		 }
	    
	 }
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
				 employee.setIsAdmin(employeeDetailsConfig.getIsAdmin());
				 employee.setRmId(employeeDetailsConfig.getRmId());
				 employee.setIsRm(employeeDetailsConfig.getIsRm());
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
				 employee.setIsAdmin(employeeDetailsConfig.getIsAdmin());
				 checkEmployee.setEmpId(employeeDetailsConfig.getEmpId());
				 employee.setEmailId(employeeDetailsConfig.getEmailId());
				 checkEmployee.setRmName(employeeDetailsConfig.getRmName());
				 checkEmployee.setIsRm(employeeDetailsConfig.getIsRm());
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
		@RequestMapping(value = "/download", method = RequestMethod.GET)
	    public void download(@RequestParam ("name") String name, final HttpServletRequest request, final HttpServletResponse response)  {
	      

	        File file = new File ("src/main/resources/" + name);
	     
	        try (InputStream fileInputStream = new FileInputStream(file);
	                OutputStream output = response.getOutputStream();) {

	            response.reset();

	            response.setContentType("application/octet-stream");
	            response.setContentLength((int) (file.length()));

	            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

	            IOUtils.copyLarge(fileInputStream, output);
	            output.flush();
	        } catch (IOException e) {
	           
	        }

	    }
	
	/* Application Configuration */
	
	@RequestMapping(value = "/deleteApplication", method = RequestMethod.POST , produces="application/json")
    public @ResponseBody String deleteApplication(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String deleteResponse="";
		
		Long id=Long.parseLong(request.getParameter("id"));
		String updatedBy=request.getParameter("updatedBy");
		Application application=applicationRepository.findById(id);
		//System.out.println(" in deleteApplication method server");
		if(application!=null){
			try{
				application.setActiveIndicator(false);
				application.setUpdatedBy(updatedBy);
				System.out.println(application.isActiveIndicator());
				applicationRepository.save(application);
				deleteResponse="Application-" + application.getappName() +" Deleted Successfully";
				
			}catch(Exception e){
				deleteResponse="Application-" + application.getappName() +" failed to Delete Please contact Application Support Team";
			}
			
		}else{
		deleteResponse="Application-" + application.getappName() +" Not found in DB";
		}
		return deleteResponse;
	}
	
	
	@RequestMapping(value="/updateAppFolderDetails", method=RequestMethod.POST , consumes="application/json", produces="application/json")
	public @ResponseBody String updateAppFolderDetails(@RequestBody List<ApplicationFileUploadConfig> applicationFileUploadConfig) throws Exception{
		String updateConfigResponse="";
		String updatedBy = (String) httpSession.getAttribute("userName");
		Application application=new Application();
		List<ApplicationFileUploadConfig> updateList=applicationFileUploadConfig;
		for(ApplicationFileUploadConfig obj:updateList){
			System.out.println("Id:" + obj.getId()+ "" + "folderName" + obj.getFolderCaption()); 
			obj.setUpdatedBy(updatedBy);
			if(obj.getId()!=null){
				application=obj.getApplication();
				try{
					applicationFileUploadConfigRepository.save(obj);
					updateConfigResponse="Application Config Updated Successfully";
				}catch(Exception e){
				updateConfigResponse="Application Config Update Fail,Please contact Application Support Team";
				}
			}else{
				ApplicationFileUploadConfig newRecord=new ApplicationFileUploadConfig();
				newRecord.setApplication(application);
				newRecord.setCreatedBy(updatedBy);
				newRecord.setUpdatedBy(updatedBy);
				newRecord.setFileAckPath(obj.getFileAckPath());
				newRecord.setFileNamePrefix(obj.getFileNamePrefix());
				newRecord.setFileTrgtPath(obj.getFileTrgtPath());
				newRecord.setFolderCaption(obj.getFolderCaption());
				newRecord.setValidationType(obj.getValidationType());
				newRecord.setActiveIndicator(true);
				try{
					applicationFileUploadConfigRepository.save(newRecord);
					updateConfigResponse="Application Config Updated Successfully";
				}catch(Exception e){
					updateConfigResponse="Application Config Update Fail,Please contact Application Support Team";
				}
			}
		}
		
		
		return updateConfigResponse;
	}
	
	
	 @RequestMapping(value = "/setApplicationConfig", method = RequestMethod.POST ,consumes="application/json", produces="application/json")
	    public @ResponseBody String setApplicationConfig(@RequestBody ApplicationConfig applicationConfig) throws IllegalStateException, IOException {
		String configResponse="";
		String fileType="";
		String applicationName=applicationConfig.getApplicationName();
		applicationName=applicationName.toUpperCase();
		String targetPath=applicationConfig.getTargetPath();
		String archivePath=applicationConfig.getArchivePath();
		String createdBy=applicationConfig.getUserName();
		List<String> selectedFileType=applicationConfig.getSelectedFileType();
		List<FolderMapping> folderMapping=applicationConfig.getFolderMapping();
		Application application=new Application();
		Application checkApplication=applicationRepository.findByAppName(applicationName);
		if(checkApplication==null){
			try{
			application.setappName(applicationName);
			application.setCreatedBy(createdBy);
			application.setUpdatedBy(createdBy);
			application.setActiveIndicator(true);
			applicationRepository.save(application);
		
		
		fileType=String.join(",", selectedFileType);
			
		for(FolderMapping obj:folderMapping){
			ApplicationFileUploadConfig applicationFileUploadConfig=new ApplicationFileUploadConfig();
			applicationFileUploadConfig.setApplication(application);
			applicationFileUploadConfig.setCreatedBy(createdBy);
			applicationFileUploadConfig.setFileAckPath(archivePath);
			applicationFileUploadConfig.setFileTrgtPath(targetPath);
			applicationFileUploadConfig.setFolderCaption(obj.getFolderName());
			applicationFileUploadConfig.setFileNamePrefix(obj.getFileName());
			applicationFileUploadConfig.setValidationType(fileType);
			applicationFileUploadConfig.setUpdatedBy(createdBy);
			applicationFileUploadConfig.setActiveIndicator(true);
			System.out.println(applicationFileUploadConfig);
			try{
			applicationFileUploadConfigRepository.save(applicationFileUploadConfig);
			configResponse="Application Configuration saved successfully";
			
			}catch(Exception e){
				configResponse="Application Configuration failed to save, Please contact Application Support Team";
			}
			
		 }
			}catch(Exception e){
				configResponse="Application Configuration failed to save, Please contact Application Support Team";
			}
		
		}else{
			configResponse="Application Configuration already exists";
		}
			
		 return configResponse;
	 }
}
