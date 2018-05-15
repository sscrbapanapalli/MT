package com.cmacgm.cdrserver.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.cdrserver.model.EmployeeDetails;
import com.cmacgm.cdrserver.model.MonitoringDetails;
import com.cmacgm.cdrserver.model.UserActivityTrack;
import com.cmacgm.cdrserver.repository.ActiveMonitoringRepository;
import com.cmacgm.cdrserver.repository.ActivityTrackRepository;
import com.cmacgm.cdrserver.repository.ReportsRepository;



@RestController
@RequestMapping("/reports")
public class ReportsController {
	
	@Autowired(required = true)
	private HttpSession httpSession;
	
	@Autowired
	ReportsRepository reportsRepository;
	@Autowired
	ActivityTrackRepository activityTrackRepository;
	

	@Autowired
    ActiveMonitoringRepository activeMonitoringRepository;
	
	@RequestMapping("/getEmpDetails" )
	public List<EmployeeDetails> getEmpDetails(){
		String userName = (String) httpSession.getAttribute("userName");
		List<EmployeeDetails> data=null;
		//String rmId=userName + "@CMA-CGM.COM";
		//String rmId="SSC.BAMMU" + "@CMA-CGM.COM";
		System.out.println(userName);
		if(userName==null){
			return data;
		}else{
		  data=reportsRepository.findByRmId(userName);
		 System.out.println(data);
		
		return data;
		}
	}
	
	@GetMapping("/getMyReport")
   	public List<UserActivityTrack> getMyReport(HttpServletRequest req){
		
	String datRange=req.getParameter("selectedRange");
	String[] splited = datRange.split("\\s+");
	 //String[] parts = datRange.split("-");
	 String d1=splited[0]+" "+"00:00:00";
	 String d2=splited[2]+" "+"23:59:59";
	 
	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	 Date dateFrom=null;
		Date dateTo=null;
		try{
			dateFrom = format.parse(d1);
			dateTo = format.parse(d2);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		 System.out.println(dateFrom);
	 System.out.println(dateTo);
	 String userId = (String) httpSession.getAttribute("userName");
		//String empId=userName + "@CMA-CGM.COM";
		List<UserActivityTrack> myRepList=activityTrackRepository.findByUserIdAndCreatedDateBetween(userId,dateFrom,dateTo);
		//List<UserActivityTrack> myRepList=new ArrayList<UserActivityTrack>();
		for(UserActivityTrack obj:myRepList){
			System.out.println(obj);
		}
        return myRepList;
   		
   	}
	

@GetMapping(value="/getMonitoring")
    public List<MonitoringDetails> getMonitoring(){
           
           String userName = (String) httpSession.getAttribute("userName");
           String rmId=userName + "@CMA-CGM.COM";
         //  System.out.println(rmId);
           List<MonitoringDetails> test=activeMonitoringRepository.findAll();
           System.out.println("monitor list"+ test);
           return test;
}


	
	@GetMapping("/getTeamReport")
	public List<UserActivityTrack> getTeamReport(HttpServletRequest req){
		
		String userName = (String) httpSession.getAttribute("userName");
		//String rmId=userName + "@CMA-CGM.COM";
		List<EmployeeDetails> mgrReporteesList=reportsRepository.findByRmId(userName);
		
		System.out.println("Employes under a manager" + mgrReporteesList.size());
		List<String> empList=new ArrayList<String>();
		List<UserActivityTrack> myRepList=new ArrayList<UserActivityTrack>();
		List<UserActivityTrack> empTaskList=new ArrayList<UserActivityTrack>();
		String datRange=req.getParameter("selectedRange");
		System.out.println(datRange);
		String[] splited = datRange.split("\\s+");
		String d1=splited[0]+" "+"00:00:00";
		String d2=splited[2]+" "+"23:59:59";
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date dateFrom=null;
		Date dateTo=null;
			try{
				dateFrom = format.parse(d1);
				dateTo = format.parse(d2);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			 System.out.println(dateFrom);
			 System.out.println(dateTo);
			
		for(EmployeeDetails obj:mgrReporteesList){
			empList.add(obj.getEmailId());
			System.out.println(obj);
			
		}
		for(String id:empList){
			System.out.println(id);
			String[] split=id.split("@");
			String userId=split[0];
			System.out.println(userId);
			 empTaskList=activityTrackRepository.findByUserIdAndCreatedDateBetween(userId,dateFrom,dateTo);
			System.out.println("task size for single user" + empTaskList.size());
			for(UserActivityTrack temp1:empTaskList){
				myRepList.add(temp1);
				System.out.println("task size total report" + myRepList.size());
			}
			
		}
		
		
		for(UserActivityTrack obj1:myRepList){
			System.out.println(obj1.getActivityName()+"-"+obj1.getActivityTotTime());
		}
		
		
		return myRepList;
	}

	
	 @RequestMapping(value = "/overrideEmployeeTime", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
			String deleteResponse="";
			
			  String updatedBy = (String) httpSession.getAttribute("userName");
			Long id=Long.parseLong(request.getParameter("id"));
			/*String activityStartTime=request.getParameter("activityStartTime");
			String comments=request.getParameter("comments");
			String activityEndTime=request.getParameter("activityEndTime");
			//String activityTotTime=request.getParameter("activityTotTime");
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date dateFrom=null;
			Date dateTo=null;
			System.out.println(updatedBy);
			System.out.println(activityStartTime);
			System.out.println(activityEndTime);
			System.out.println(id);
			System.out.println(comments);*/
			
				try{
					//dateFrom = format.parse(activityStartTime);
					//dateTo = format.parse(activityEndTime);
					
				}catch(Exception e){
					e.printStackTrace();
				}
				activityTrackRepository.overrideEmployeeTime("Approved",updatedBy,id);
		       //activityTrackRepository.overrideEmployeeTime(dateFrom,dateTo,activityTotTime,comments,updatedBy,id);
		
			
			return deleteResponse;
		}
	
}
