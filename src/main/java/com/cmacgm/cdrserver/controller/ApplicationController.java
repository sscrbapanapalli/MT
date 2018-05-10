package com.cmacgm.cdrserver.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.ApplicationFileUploadConfig;
import com.cmacgm.cdrserver.model.BatchFileDetail;
import com.cmacgm.cdrserver.model.BatchHistoryDetail;
import com.cmacgm.cdrserver.model.EmployeeDetails;
import com.cmacgm.cdrserver.model.EmployeeErrorDetails;
import com.cmacgm.cdrserver.model.User;
import com.cmacgm.cdrserver.repository.ApplicationFileUploadConfigRepository;
import com.cmacgm.cdrserver.repository.ApplicationRepository;
import com.cmacgm.cdrserver.repository.BatchHistoryDetailsRepository;
import com.cmacgm.cdrserver.repository.EmployeeDetailsRepository;
import com.cmacgm.cdrserver.repository.UserRepository;

/**
 * @filename ApplicationController.java(To get application vise folder path and to upload file to respective path)
* @author Ramesh Kumar B

*/

@RestController
@RequestMapping("/api")
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	@Autowired
	ApplicationRepository applicationRepository;
	@Autowired
	BatchHistoryDetailsRepository batchHistoryDetailsRepository;
	
	 @Autowired
	 EmployeeDetailsRepository employeeDetailsRepository;
	
	 
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ApplicationFileUploadConfigRepository applicationFileUploadConfigRepository;
	
	@GetMapping("/applications")
	public List<Application> getServerFolders(){
		return applicationRepository.findAll();
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable(value = "id") Long id){
		return userRepository.findById(id);
	}
	
	@GetMapping("/userById/{userId}")
	public User getUSerByUserId(@PathVariable(value = "userId") String userId){
		return userRepository.findByUserId(userId);
	}

	
	@GetMapping("/serverfolders/{id}")
	public List<ApplicationFileUploadConfig> getfolderByApp(@PathVariable(value = "id") Long id) {
         Application application = applicationRepository.findById(id);
		 List<ApplicationFileUploadConfig> selectedAppFolderDetails = applicationFileUploadConfigRepository.findByApplication(application);
		 //System.out.println("list generate");
	    return selectedAppFolderDetails;
	}
	
	/* 
	 * To R
	 */
	
	@GetMapping("/batchHistoryDetails/{appId}")
	public List<BatchHistoryDetail> getBatchHistoryDetails(@PathVariable(value="appId") Long appId){
		System.out.println("in /api/batchHistoryDetails");
		List<BatchHistoryDetail> batchHistoryDetailList=batchHistoryDetailsRepository.findByAppId(appId);
		for(BatchHistoryDetail batchHistoryDetail:batchHistoryDetailList){
			System.out.println(batchHistoryDetail.getEtlProcessed());
			if(batchHistoryDetail.getEtlProcessed().equals("N")){
				batchHistoryDetail.setEtlProcessed("New");
			}else if(batchHistoryDetail.getEtlProcessed().equals("P")){
				batchHistoryDetail.setEtlProcessed("In Progress");
			}else if(batchHistoryDetail.getEtlProcessed().equals("Y")){
				batchHistoryDetail.setEtlProcessed("ETL Processed");
			}else if(batchHistoryDetail.getEtlProcessed().equals("X")){
				System.out.println(":in test X value" );
				batchHistoryDetail.setEtlProcessed("No Longer Required");
			}
			System.out.println("rame" + batchHistoryDetail.getEtlProcessed());
			System.out.println(" for testing X" +batchHistoryDetail);
		}
			
		return batchHistoryDetailList;		
		
	}
	
	
	 @RequestMapping(value = "/uploadfile", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String UploadscenarioFiles(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
	    int i=0;
	    String uploadResponse="";
	    String userName="";
	    String batchUploadMonth="";
	    String batchUploadStatus="Upload";
	    Long applicationId=0l;
	    String etlStatus="null";
	    String etlProcessed="N";
	    List<BatchFileDetail> batchFileDetailList=new ArrayList<>();
	    BatchHistoryDetail batchHistoryDetail=new BatchHistoryDetail();
	    BatchHistoryDetail batchuploadCheck=new BatchHistoryDetail();
	    BatchHistoryDetail obj=new BatchHistoryDetail();
	    List<String> fileList=new ArrayList<>();
	   // boolean activeIndicator=true;
	    
	    System.out.println("server side appID:" + request.getParameter("applicationId"));
	    
	    applicationId=Long.parseLong(request.getParameter("applicationId"));
	    userName=request.getParameter("userName");
	    batchUploadMonth=request.getParameter("selectedMonth");
	    System.out.println("Selected month" + batchUploadMonth); 
	    //System.out.println("appName in server" + applicationId);
	    
	    Application application = applicationRepository.findById(applicationId);
	    List<ApplicationFileUploadConfig> applicationFileUploadConfig = applicationFileUploadConfigRepository.findByApplication(application);
	    Date dNow = new Date();
	       SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
	       String batchId = applicationId+ft.format(dNow);
	       
	    /*Calendar c = Calendar.getInstance();
		   String batchUploadMonth=c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH );*/
		
		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
		//batchuploadCheck=batchHistoryDetailsRepository.findByAppIdAndBatchUploadMonthAndBatchUploadStatus(applicationId,batchUploadMonth,batchUploadStatus);
		//System.out.println("batchuploadCheck:" + batchuploadCheck);
		System.out.println("null check" + batchUploadMonth +  ":"+ +applicationId);
		obj=batchHistoryDetailsRepository.findByTop(batchUploadMonth,applicationId);
		System.out.println("test" + obj);
		
		/*if(obj==null || obj.getEtlProcessed()==null || obj.getEtlProcessed().equals("X") ){ //Validation check- upload already completed for selected month or not
*/		if(obj==null || obj.getEtlProcessed()==null || obj.getBatchUploadStatus().equals("Reverse") ){ //Validation check- upload already completed for selected month or not
			Iterator<String> fileNames = multipart.getFileNames();
	        while(fileNames.hasNext()){
	        	MultipartFile fileContents = multipart.getFile(fileNames.next());
	        	fileList.add(fileContents.getOriginalFilename());
	        }
	        
	        Set<String> filteredSet = new HashSet<String>(fileList);
	        if(batchUploadMonth!=null && batchUploadMonth.length()!=0)	{
	        	System.out.println("test month:" +  batchUploadMonth);
	        if(fileList.size()==applicationFileUploadConfig.size()){
	        if(fileList.size()==filteredSet.size()){
	        
	        	System.out.println("in side second while loop");
	        	Iterator<String> uploadFileNames = multipart.getFileNames();
	        	while (uploadFileNames.hasNext()) { // Get List of files from Multipart Request.
	        	 
	            MultipartFile fileContent = multipart.getFile(uploadFileNames.next());
	            String folderCaption=applicationFileUploadConfig.get(i).getFolderCaption();
	            //String folderpath=applicationFileUploadConfig.get(i).getFileTrgtPath()+folderCaption;
	            String folderpath=applicationFileUploadConfig.get(i).getFileTrgtPath();
	            String fileName=applicationFileUploadConfig.get(i).getFileNamePrefix();
	            String orgFileName=fileContent.getOriginalFilename();
	            System.out.println("check file anme format" +fileName);
	            File dir = new File(folderpath);
	            if (!dir.exists())
					dir.mkdirs();
	            
	            String fileTrgtPath= dir.getAbsolutePath() + File.separator + fileName;
				System.out.println("file target path"+ fileTrgtPath );		
	            BatchFileDetail batchFileDetail=new BatchFileDetail();
	            //batchFileDetail.setActiveIndicator(activeIndicator);
	            batchFileDetail.setBatchFileName(orgFileName);
	            batchFileDetail.setBatchFileTrgtPath(fileTrgtPath);
	            batchFileDetail.setFolderCaption(folderCaption);
	            batchFileDetail.setCreatedBy(userName);
	            batchFileDetail.setUpdatedBy(userName);
	            batchFileDetailList.add(batchFileDetail);
	            	            
	            System.out.println("Check Folder Path in server"+folderpath);
	            
	            uploadResponse=uploadMultipleFileHandler(fileContent,folderpath,fileName);
	             
	           i++;
	            
	            //uploadMultipleFileHandler(fileContent,i);
	            System.out.println("UploadscenarioFiles in loop uploadResponse:"+uploadResponse);
	        }
	        }
	        	else{
	        		uploadResponse="Please remove duplicate files ";
	        	}
	        }
	        else{
	        	uploadResponse="Please select all files ";
	        }
		}else{
			uploadResponse="Please select upload month";
		}
		}else{
			System.out.println("batchuploadCheck is null sdfsdgs"+ uploadResponse);
			uploadResponse="Batch Upload failed as selected month batch already uploaded with batch id: "+ obj.getBatchId();
			
		}
	         
	         if(uploadResponse=="Files Uploaded Successfully"){
	        	 logger.info("Upload file status to server="
							+ uploadResponse);
	        	 
	        	 batchHistoryDetail.setAppId(applicationId); 
	        	 batchHistoryDetail.setBatchFileDetailList(batchFileDetailList);
	        	 batchHistoryDetail.setBatchId(batchId);
	        	 batchHistoryDetail.setBatchUploadMonth(batchUploadMonth);
	        	// batchHistoryDetail.setActiveIndicator(activeIndicator);
	        	 batchHistoryDetail.setBatchUploadStatus(batchUploadStatus);
	        	 batchHistoryDetail.setBatchUploadUserName(userName);
	        	 batchHistoryDetail.setCreatedBy(userName);
	        	 batchHistoryDetail.setUpdatedBy(userName);
	        	 batchHistoryDetail.setEtlProcessed(etlProcessed);
	        	 batchHistoryDetailsRepository.save(batchHistoryDetail);
             }
	         System.out.println("UploadscenarioFiles uploadResponse:"+uploadResponse);
			return uploadResponse;
	    }
	    
 
	    private String uploadMultipleFileHandler( MultipartFile file,String folderPath,String fileName) throws IOException {
	    	String serverResponse="";
	    	BufferedOutputStream stream=null;
			
			try {
				byte[] bytes = file.getBytes();
				//System.out.println("dir number"+i);
				// Creating the directory to store file
				//String rootPath = "\\\\10.13.44.80\\c$\\CDR"+i;
				//String rootPath = "\\\\10.13.44.80\\c$\\CDR";
				File dir = new File(folderPath);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + fileName);
				System.out.println(serverFile);	
				stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());
				 serverResponse="Files Uploaded Successfully";
				 System.out.println("UploadscenarioFiles serverResponse:"+serverResponse);
				 
				 
			return serverResponse;
			} catch (Exception e) {
				System.out.println(e);
				e.getMessage();
				 serverResponse="Files Upload Failure,Please contact Application Support Team";
					return serverResponse; 
			}
		finally{
			
			return serverResponse;
			
		}
		
	}
	    
	    @GetMapping("/currentUploadDetails/{appId}")
		public BatchHistoryDetail getCurrentUpload(@PathVariable(value="appId") Long appId){
			System.out.println("in /api/batchHistoryDetails");
			String batchUploadStatus="Upload";
			String batchReverseStatus="Reverse";
			String etlProcessedNew="N";
	    	String etlProcessedCompleted="Y";
	    	List<BatchHistoryDetail> reverseBatchdetailsList;
	    	reverseBatchdetailsList=batchHistoryDetailsRepository.getReverseBatchDetails(appId,batchReverseStatus);
	    	for(BatchHistoryDetail list:reverseBatchdetailsList){
	    		System.out.println(" reverse ID" +list.getId());
	    	}
	    	System.out.println("size" + reverseBatchdetailsList.size());
	    	
	    	if(reverseBatchdetailsList.size()==0){
	    		
			 	return batchHistoryDetailsRepository.findByEtlProcessed(batchUploadStatus,appId,etlProcessedNew,etlProcessedCompleted);	
	    	}else
	    		return null;
			
		}
	     
	  /*  @RequestMapping(value = "/getCurrentUpload", method = RequestMethod.POST , produces="application/json")
	    public BatchHistoryDetail getCurrentUpload(String batchUploadStatus,Long appId,String etlProcessedNew,String etlProcessedCompleted){
	    	BatchHistoryDetail batchUpdate= new BatchHistoryDetail();
	    	
	    	batchUpdate=batchHistoryDetailsRepository.findByEtlProcessed(batchUploadStatus,appId,etlProcessedNew,etlProcessedCompleted);
	    	return batchUpdate;
	    }*/
	    
	    @RequestMapping(value = "/reverseUpload", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String reverseUpload(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
	    	//System.out.println("In Do Reverse Method"); 
	    	String serverResponse="";
	    	String batchUploadStatus="Upload";
	    	String batchUploadStatusModified="Reverse";
	    	String etlStatusBeforereverse="";
	    	String etlProcessedNew="N";
	    	String etlProcessedCompleted="Y";
	    	String etlModified="X";
	    	String source="";
	    	String destination="";
	    	BatchHistoryDetail batchUpdate= new BatchHistoryDetail();
	    	List<BatchHistoryDetail> reverseBatchdetailsList;
	    		    	
	    	String userName=request.getParameter("userName");
	    	Long appId=Long.parseLong(request.getParameter("applicationId"));
	    	Long selectedBatchUniqueId=Long.parseLong(request.getParameter("selectedBatchUniqueId"));
	    	
	    	reverseBatchdetailsList=batchHistoryDetailsRepository.getReverseBatchDetails(appId,batchUploadStatusModified);	    	    	
	    	batchUpdate=batchHistoryDetailsRepository.findById(selectedBatchUniqueId);
	    	//System.out.println("test" + batchUpdate);
	    	etlStatusBeforereverse=batchUpdate.getEtlProcessed();
	    	if(batchUpdate!=null && reverseBatchdetailsList.size()==0){
	    		if(batchUpdate.getEtlProcessed().equals(etlProcessedNew)){
	    			System.out.println("in batchUpdate record before "+ etlStatusBeforereverse);
	    			batchUpdate.setEtlProcessed(etlModified);
	    			System.out.println("in batchUpdate record after"+ batchUpdate.getEtlProcessed());
	    		}
	    	batchUpdate.setUpdatedBy(userName);
	    	batchHistoryDetailsRepository.save(batchUpdate);
	    	System.out.println("Reverse batch updated entry" + batchUpdate);
	    	
	    	//To move files from destination to archive path
	    	 Application application = applicationRepository.findById(appId);
	 	    List<ApplicationFileUploadConfig> applicationFileUploadConfig = applicationFileUploadConfigRepository.findByApplication(application);
	    	if(applicationFileUploadConfig!=null){
	    		source=applicationFileUploadConfig.get(1).getFileTrgtPath();
	    		destination=applicationFileUploadConfig.get(1).getFileAckPath()+batchUpdate.getBatchId();
	    		System.out.println("Sour:" + source + "destination" + destination);
	    		File destDir = new File(destination);
	    		File srcDir = new File(source);
	           /* for(ApplicationFileUploadConfig archiveProcess:applicationFileUploadConfig){
	  		 		source=archiveProcess.getFileTrgtPath();
	  		 		destination=archiveProcess.getFileAckPath()+batchId;
	  				File destDir = new File(destination);
	          		File srcDir = new File(source);
	          		String ArchiveResult=archiveFiles(destDir,srcDir);
	  			}*/
	           String ArchiveResult=archiveFiles(srcDir,destDir);
	           System.out.println("ArchiveResult" + ArchiveResult);
	    	}
	    	// To insert new record for Reverse process
	    	BatchHistoryDetail batchReverse= new BatchHistoryDetail();
	    	batchReverse.setAppId(batchUpdate.getAppId()); 
	    	//batchReverse.setBatchFileDetailList(batchUpdate.getBatchFileDetailList());
	    	batchReverse.setBatchId(batchUpdate.getBatchId());
	    	batchReverse.setBatchUploadMonth(batchUpdate.getBatchUploadMonth());
	    	// batchHistoryDetail.setActiveIndicator(activeIndicator);
	    	batchReverse.setBatchUploadStatus(batchUploadStatusModified);
	    	batchReverse.setBatchUploadUserName(batchUpdate.getBatchUploadUserName());
	    	batchReverse.setCreatedBy(userName);
	    	batchReverse.setUpdatedBy(userName);
	    	
	    	if(etlStatusBeforereverse.equals(etlProcessedNew)){
			System.out.println("in update record before "+ etlStatusBeforereverse);
			batchReverse.setEtlProcessed(etlModified);
			System.out.println("in update record after"+ batchUpdate.getEtlProcessed());
	    	}else if(etlStatusBeforereverse.equals(etlProcessedCompleted)){
	    		batchReverse.setEtlProcessed(etlProcessedNew);
	    	 }	 
	    	/*batchReverse.setEtlProcessed(batchUpdate.getEtlProcessed());*/
	    	System.out.println("Reverse batch new enrty" + batchReverse);
	    	batchHistoryDetailsRepository.save(batchReverse);
	    	serverResponse="Reverse for batch id: "+batchUpdate.getBatchId()+" for uploaded month "+batchUpdate.getBatchUploadMonth()+" completed";
	    	
	    	System.out.println(" reverseResponse:"+serverResponse);
	    	}else{
	    		serverResponse="Batch Reverse failed as Batch Reversal for current month completed"; 
	    		System.out.println("reverseResponse:"+serverResponse);
	    	}
	    	logger.info(serverResponse);
	    	
	    	return serverResponse;
	    }
	    
	    public String archiveFiles(File srcDir,File destDir){
	    	String archiveResult="";
	    	 try {
	             // Move the source directory to the destination directory.
	             // The destination directory must not exists prior to the
	             // move process.
	    		 System.out.println("In Archive method:"+srcDir);
	    		 System.out.println("In Archive method:"+destDir);
	             FileUtils.moveDirectory(srcDir, destDir);
	              archiveResult= "Files moved to Archive Folder";
	         } catch (IOException e) {
	             e.printStackTrace();
	              archiveResult="Failed to move files to archive folder";
	         }
	    	 logger.info(archiveResult);
	    	return archiveResult;
	    }
	    
	    @RequestMapping(value = "/uploadEmpfile", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody HashMap<String, String> UploadEmployeeExcelData(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
	   String userName="";
	    HashMap<String, String> uploadResponse=null;
	    userName=request.getParameter("userName");
	   System.out.println("userName:   "+userName);
	    
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
	 	              else if(!Boolean.TRUE.equals(row.getCell(4).getBooleanCellValue()) || !Boolean.FALSE.equals(row.getCell(4).getBooleanCellValue()))
	 	            	 errorlistEmpDetails=  getError(row);
	            		   
	                  else {
	   
		                	  employeeDetails=new EmployeeDetails();
		               		Cell empId=row.getCell(0);
		            		employeeDetails.setEmpId(empId.getStringCellValue());
		            	
		            		Cell empName=row.getCell(1);
		            		employeeDetails.setEmpName(empName.getStringCellValue());
		            	
		            		Cell rmId=row.getCell(2);
		            		employeeDetails.setRmId(rmId.getStringCellValue());
		            		
		            		Cell rmName=row.getCell(3);
		            		employeeDetails.setRmName(rmName.getStringCellValue());
		            	
		            		Cell isRm=row.getCell(4);
		            		employeeDetails.setIsRm(isRm.getBooleanCellValue());  
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
    		employeeDetails.setEmpId(empId.getStringCellValue());
    	
    		Cell empName=row.getCell(1);
    		employeeDetails.setEmpName(empName.getStringCellValue());
    	
    		Cell rmId=row.getCell(2);
    		employeeDetails.setRmId(rmId.getStringCellValue());
    		
    		Cell rmName=row.getCell(3);
    		employeeDetails.setRmName(rmName.getStringCellValue());
    	
    		Cell isRm=row.getCell(4);
    		employeeDetails.setIsRm(isRm.getStringCellValue());  
    		errorlistEmpDetails.add(employeeDetails);
			return errorlistEmpDetails;
		}
		
		public HashMap<String,String> insert(List<EmployeeDetails> listEmpInsertDetails){	
		   	HashMap<String,String> data=new HashMap<>();
		   	for (EmployeeDetails employeeDetails : listEmpInsertDetails) {
		   		EmployeeDetails employeeDetailsOld=new EmployeeDetails();
		   		  employeeDetailsOld=employeeDetailsRepository.findByEmpId(employeeDetails.getEmpId());
		   		 if(employeeDetailsOld!=null)
		   		  employeeDetailsRepository.setEmployee(employeeDetails.getEmpName(), employeeDetails.getRmId(), employeeDetails.getRmName(), employeeDetails.getIsRm(), employeeDetails.getEmpId());	
		   		  else
		   			employeeDetailsRepository.save(employeeDetails);
		     	}	
						
			 data.put("message", "Employee Details Uploaded Successfully");
			 return data;	
		               
			}
		
		
		
	    
}


