package com.cmacgm.mytime.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cmacgm.mytime.model.EmployeeDetails;
import com.cmacgm.mytime.model.EmployeeErrorDetails;
import com.cmacgm.mytime.repository.EmployeeDetailsRepository;

/**
 * @filename ApplicationController.java(To get application vise folder path and to upload file to respective path)
* @author Ramesh Kumar B

*/

@RestController
@RequestMapping("/api")
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	
	
	 @Autowired
	 EmployeeDetailsRepository employeeDetailsRepository;
	
	  


	    
	    @RequestMapping(value = "/uploadEmpfile", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody HashMap<String, String> UploadEmployeeExcelData(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
	   String userName="";
	    HashMap<String, String> uploadResponse=null;
	    userName=request.getParameter("userName");
	
	    
		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
					Iterator<String> fileNames = multipart.getFileNames();
	        while(fileNames.hasNext()){
	        	MultipartFile fileContents = multipart.getFile(fileNames.next());
	        	uploadResponse=processExcel(fileContents.getInputStream(),userName);	
	    			        	
	        	        }
			return uploadResponse;

	        
	        
	
	    }
	   

	   

		public HashMap<String,String> processExcel(InputStream ExcelFileToRead,String userName)        
	    {
			HashMap<String,String> data=new HashMap<>();
	    	  EmployeeDetails employeeDetails=null;
	    	List<EmployeeDetails> listEmpDetails=new ArrayList<>();
	    	List<EmployeeErrorDetails> errorlistEmpDetails=new ArrayList<>();
	    	int rowId=0;
	        try {
	        	data.put("data", "Employee Details Excel Upload");
	         
	            //définir l'objet workbook
	            XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);  
	            if(wb==null){
	            	data.put("message", "No Excel Found");
	            return data;
	            }
	            	
	            
	            if(wb.getSheetAt(0)==null){
	            	data.put("message", "No Sheet Found");
	            	
	            return data;
	            }
	            	
	         
	            
	            int FIRST_ROW_TO_GET = 1,TOTAL_ROW_COUNT=0; // 0 based

	            Sheet s = wb.getSheetAt(0);
	            if(s!=null && wb.getSheetAt(0)!=null && s.getLastRowNum()>0 && s.getLastRowNum()>1000){
	            	TOTAL_ROW_COUNT= s.getLastRowNum();
	            	data.put("message", "Sheet Exceeds Maximum rows able to upload 1000 Records only");
	            return data;
	            }
	            
	            for (int i = FIRST_ROW_TO_GET; i <= s.getLastRowNum(); i++) {
	               Row row = s.getRow(i);
	                  rowId=i;
	            	   if (row == null) {
	            		   data.put("message", "No Row Found from Excel");	
	            	   return data;
	            	   }
	 	               else if(row.getCell(0)==null)
	 	            	  errorlistEmpDetails= getError(row);
	 	               else if(row.getCell(1)==null)
	 	            	  errorlistEmpDetails=  getError(row);
	 	               else if(row.getCell(2)==null)
	 	            	  errorlistEmpDetails=  getError(row);
	 	               else if(row.getCell(3)==null)
	 	            	  errorlistEmpDetails=  getError(row);
	 	               else if(row.getCell(4)==null)
	 	            	  errorlistEmpDetails= getError(row);	 
	 	              else if(row.getCell(5)==null)
	 	            	  errorlistEmpDetails= getError(row); 
	 	             else if(row.getCell(6)==null)
	 	            	  errorlistEmpDetails= getError(row); 
	 	           
	            		   
	                  else {
	   
		                	  employeeDetails=new EmployeeDetails();
		               		Cell empId=row.getCell(0);
		            		employeeDetails.setEmpId((long) empId.getNumericCellValue());
		            		
		            		Cell emailId=row.getCell(1);
		            		employeeDetails.setEmailId(emailId.getStringCellValue());
		            	
		            		Cell empName=row.getCell(2);
		            		employeeDetails.setEmpName(empName.getStringCellValue());
		            	
		            		Cell rmId=row.getCell(3);
		            		employeeDetails.setRmId(rmId.getStringCellValue());
		            		
		            		Cell rmName=row.getCell(4);
		            		employeeDetails.setRmName(rmName.getStringCellValue());
		            	
		            		Cell isAdmin=row.getCell(5);
		            		employeeDetails.setAdmin(isAdmin.getBooleanCellValue()); 
		            		
		            		Cell activeIndicator=row.getCell(6);
		            		employeeDetails.setActiveIndicator(activeIndicator.getBooleanCellValue()); 
		            		
		            		employeeDetails.setCreatedBy(userName);
		            		employeeDetails.setUpdatedBy(userName);
		            		listEmpDetails.add(employeeDetails);
	             
	            }
	            

	           

	        }
	            if(errorlistEmpDetails.size()==0)
	                 return insert(listEmpDetails) ;
	            else
	            	 data.put("message", "Employee Details Error List");	
	                 data.put("data", errorlistEmpDetails.toString());	
	                 return data;
	            	
	        } catch (Exception e) {
	           
	           
				// TODO Auto-generated catch block
	          //  e.printStackTrace();
	            data.put("message", "Employee Details Contains Error :Row Id:"+rowId);	
	            data.put("data",  e.getMessage());
	            return data;
	        }
	      
	    }
		
		public List<EmployeeErrorDetails> getError(Row row){
			EmployeeErrorDetails employeeDetails=null;
			 employeeDetails=new EmployeeErrorDetails();
			List<EmployeeErrorDetails> errorlistEmpDetails=new ArrayList<>();
			Cell empId=row.getCell(0);
    		employeeDetails.setEmpId((long) empId.getNumericCellValue());
    		
    		Cell emailId=row.getCell(1);
    		employeeDetails.setEmailId(emailId.getStringCellValue());
    	
    		Cell empName=row.getCell(2);
    		employeeDetails.setEmpName(empName.getStringCellValue());
    	
    		Cell rmId=row.getCell(3);
    		employeeDetails.setRmId(rmId.getStringCellValue());
    		
    		Cell rmName=row.getCell(4);
    		employeeDetails.setRmName(rmName.getStringCellValue()); 	
    	
    		
    		Cell isAdmin=row.getCell(5);
    		employeeDetails.setAdmin(isAdmin.getBooleanCellValue()); 
    		
    		Cell activeIndicator=row.getCell(6);
    		employeeDetails.setActiveIndicator(activeIndicator.getBooleanCellValue()); 
    		
    		errorlistEmpDetails.add(employeeDetails);
			return errorlistEmpDetails;
		}
		
		public HashMap<String,String> insert(List<EmployeeDetails> listEmpInsertDetails){	
		   	HashMap<String,String> data=new HashMap<>();
		   	for (EmployeeDetails employeeDetails : listEmpInsertDetails) {
		   		EmployeeDetails employeeDetailsOld=new EmployeeDetails();
		   		  employeeDetailsOld=employeeDetailsRepository.findByEmailId(employeeDetails.getEmailId());
		   		 if(employeeDetailsOld!=null)
		   		  employeeDetailsRepository.setEmployee(employeeDetails.getEmpName(), employeeDetails.getEmpId(),employeeDetails.getRmId(), employeeDetails.getRmName(),  employeeDetails.isAdmin(),employeeDetails.getEmailId());	
		   		  else
		   			employeeDetailsRepository.save(employeeDetails);
		     	}	
						
			 data.put("message", "Employee Details Uploaded Successfully");
			 return data;	
		               
			}
		
		
		
	    
}


