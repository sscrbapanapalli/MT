package com.cmacgm.mytime.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.mytime.model.AuditHistoryActivityTrack;
import com.cmacgm.mytime.model.EmployeeDetails;
import com.cmacgm.mytime.model.MonitoringDetails;
import com.cmacgm.mytime.model.UserActivityTrack;
import com.cmacgm.mytime.repository.ActiveMonitoringRepository;
import com.cmacgm.mytime.repository.ActivityTrackRepository;
import com.cmacgm.mytime.repository.AuditHistoryActivityTrackRepository;
import com.cmacgm.mytime.repository.ReportsRepository;



@RestController
@RequestMapping("/reports")
public class ReportsController {
	
	
	
	@Autowired
	ReportsRepository reportsRepository;
	@Autowired
	ActivityTrackRepository activityTrackRepository;
	
	@Autowired
	AuditHistoryActivityTrackRepository auditHistoryActivityTrackRepository;
	
	
	@Autowired
    ActiveMonitoringRepository activeMonitoringRepository;
	
	@RequestMapping("/getEmpDetails" )
	public List<EmployeeDetails> getEmpDetails(HttpServletRequest req){
		List<EmployeeDetails> data=null;
	
		try {
			String windowsUserName=req.getParameter("windowsUserName");
			if ( !windowsUserName.isEmpty() && windowsUserName!=null){
				 data=reportsRepository.findByRmId(windowsUserName);
				 System.out.println("RmId By Top:" + data);
			 }
		
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return data;
		
	}
	
	@GetMapping("/getMyReport")
   	public List<UserActivityTrack> getMyReport(HttpServletRequest req){
		List<UserActivityTrack> myRepList=null;
	
		try {
			   String[] splited ={};
			   Date dateFrom=null;
				Date dateTo=null;
			String datRange=req.getParameter("selectedRange");
			if(datRange!=null && datRange!=""){
			 splited = datRange.split("\\s+");
			}
			if(splited!=null){
			 //String[] parts = datRange.split("-");
			 String d1=splited[0]+" "+"00:00:00";
			 String d2=splited[2]+" "+"23:59:59";
			 
			 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
				try{
					dateFrom = format.parse(d1);
					dateTo = format.parse(d2);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
				String windowsUserName=req.getParameter("windowsUserName");
				if ( !windowsUserName.isEmpty() && windowsUserName!=null){		
				//String empId=userName + "@CMA-CGM.COM";
				myRepList=activityTrackRepository.findByUserIdAndCreatedDateBetween(windowsUserName,dateFrom,dateTo);
				}
				//List<UserActivityTrack> myRepList=new ArrayList<UserActivityTrack>();
				
		} catch (Exception e) {
			// TODO: handle exception
		}
	
        return myRepList;
   		
   	}
	
	@GetMapping(value="/getMonitoring")
    public Page<MonitoringDetails> getMonitoring(HttpServletRequest req){
		Page<MonitoringDetails> monitoringDetailsList=null;
		Integer pageNumber=0;
		Integer size=10;
	try {
		String windowsUserName=req.getParameter("windowsUserName");
		try {
			if(req.getParameter("pageNumber").trim()!="undefined")
			    pageNumber=Integer.valueOf(req.getParameter("pageNumber"));
			if(req.getParameter("size").trim()!="undefined")
			   size=Integer.valueOf(req.getParameter("size"));
		} catch (NumberFormatException e) {
			pageNumber=0;
			 size=10;
		}
		
		
		if ( !windowsUserName.isEmpty() && windowsUserName!=null){		
			
			 String rmId=windowsUserName + "@CMA-CGM.COM";
	         //  System.out.println(rmId);
	         monitoringDetailsList=activeMonitoringRepository.findAll(new PageRequest(pageNumber, size));	        
		}else{
			 monitoringDetailsList=activeMonitoringRepository.findAll(new PageRequest(pageNumber, size));
			 return monitoringDetailsList;
		}      
  
	} catch (Exception e) {
		pageNumber=0;
		 size=10;
		e.printStackTrace();
	}		
     return monitoringDetailsList;
	}
	
	@GetMapping("/getTeamReport")
	public List<UserActivityTrack> getTeamReport(HttpServletRequest req){
		List<EmployeeDetails> mgrReporteesList=null;
		List<UserActivityTrack> empTaskList=null;
		List<UserActivityTrack> myRepList=null;
		Date dateFrom=null;
		Date dateTo=null;
		try {
			String windowsUserName=req.getParameter("windowsUserName");
			if ( !windowsUserName.isEmpty() && windowsUserName!=null){
				mgrReporteesList=reportsRepository.findByRmId(windowsUserName);
			}
			String[] splited = {};
			List<String> empList=new ArrayList<String>();
			myRepList=new ArrayList<UserActivityTrack>();
			empTaskList=new ArrayList<UserActivityTrack>();
			String datRange=req.getParameter("selectedRange");
			if(datRange!=null)
			splited = datRange.split("\\s+");
			if(splited!=null){
			String d1=splited[0]+" "+"00:00:00";
			String d2=splited[2]+" "+"23:59:59";
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");			
				try{
					dateFrom = format.parse(d1);
					dateTo = format.parse(d2);					
				}catch(Exception e){
					e.printStackTrace();
				}			
			}
				
			for(EmployeeDetails obj:mgrReporteesList){
				empList.add(obj.getEmailId());
				
			}
			for(String id:empList){
			
				String[] split=id.split("@");
				String userId=split[0];
			
				 empTaskList=activityTrackRepository.getProgressAndCompleted(userId,dateFrom,dateTo);
			
				for(UserActivityTrack temp1:empTaskList){
					myRepList.add(temp1);
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return myRepList;
	}
	@GetMapping("/getAuditHistoryReport")
	public List<AuditHistoryActivityTrack> getAuditHistoryReport(HttpServletRequest req){
		 List<AuditHistoryActivityTrack> mgrReporteesList=null;
		 try {
				String windowsUserName=req.getParameter("windowsUserName");
				Date dateFrom=null;
				Date dateTo=null;
				String[] splited = {};
			 String datRange=req.getParameter("selectedRange");
				if(datRange!=null)
				splited = datRange.split("\\s+");
				if(splited!=null){
				String d1=splited[0]+" "+"00:00:00";
				String d2=splited[2]+" "+"23:59:59";
				
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			
					try{
						dateFrom = format.parse(d1);
						dateTo = format.parse(d2);
						
					}catch(Exception e){
						e.printStackTrace();
					}
				}
					if ( !windowsUserName.isEmpty() && windowsUserName!=null){
						 mgrReporteesList=auditHistoryActivityTrackRepository.findByUpdatedByAndCreatedDateBetween(windowsUserName,dateFrom,dateTo);		
						return 	mgrReporteesList;
					}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return mgrReporteesList;
	}

	
	@RequestMapping(value = "/overrideEmployeeTime", method = RequestMethod.POST ,consumes="application/json",  produces="application/json")
    public @ResponseBody String setEmployeeConfiguration(@RequestBody AuditHistoryActivityTrack auditHistoryActivityTrack) throws IllegalStateException, IOException {		
		  String overrideResponse="Employee Time override Success",totalTime="00:00:00";
			
		try {
	
		String comments=auditHistoryActivityTrack.getComments();			
		String revactivityStartTime=auditHistoryActivityTrack.getRevActivityStartTime();	
		if(revactivityStartTime!=null){
			revactivityStartTime=revactivityStartTime;
		}
		String revactivityEndTime=auditHistoryActivityTrack.getRevActivityEndTime();
        if(revactivityEndTime!=null){
        	revactivityEndTime=revactivityEndTime;
		}
		
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date dateFrom=null;
		Date dateTo=null;		
		
			try{
				dateFrom = format.parse(revactivityStartTime);
				dateTo = format.parse(revactivityEndTime);
				totalTime=getTotalActivityTime(dateFrom,dateTo);
				
			}catch(Exception e){
				e.printStackTrace();
				return overrideResponse=e.getMessage();
			}
			UserActivityTrack userActivityTrack= activityTrackRepository.findById(auditHistoryActivityTrack.getId());				 
			 activityTrackRepository.overrideEmployeeTime(dateFrom,dateTo,totalTime,comments,auditHistoryActivityTrack.getUpdatedBy(),auditHistoryActivityTrack.getId());
			 auditHistoryActivityTrack.setId(null);
			 auditHistoryActivityTrack.setRevisedActivityStartTime(dateFrom);
			 auditHistoryActivityTrack.setRevisedActivityEndTime(dateTo);
			 auditHistoryActivityTrack.setActivityStartTime(userActivityTrack.getActivityStartTime());
			 auditHistoryActivityTrack.setActivityEndTime(userActivityTrack.getActivityEndTime());
			 auditHistoryActivityTrack.setUpdatedBy(auditHistoryActivityTrack.getUpdatedBy());
			 auditHistoryActivityTrack.setCreatedBy(auditHistoryActivityTrack.getCreatedBy());
			 auditHistoryActivityTrack.setActivityId(userActivityTrack.getActivityId());
			 auditHistoryActivityTrack.setUserId(userActivityTrack.getUserId());
			 auditHistoryActivityTrack.setComments(comments);
			auditHistoryActivityTrackRepository.save(auditHistoryActivityTrack);
			
	} catch (Exception e) {
		return overrideResponse=e.getMessage();
	}
			
		      
			
			return overrideResponse;
		}

	private String getTotalActivityTime(Date revactivityStartTime, Date revactivityEndTime) {
		String totalTime="00:00:00";       
        int hours,minutes,seconds;
        String convertH="",convertM="",convertS="";

		try {
			
			DateTime dt1 = new DateTime(revactivityStartTime);
			DateTime dt2 = new DateTime(revactivityEndTime);
            hours=Hours.hoursBetween(dt1, dt2).getHours();
            convertH=String.valueOf(hours);
            minutes=Minutes.minutesBetween(dt1, dt2).getMinutes() % 60;
            convertM=String.valueOf(minutes);
            seconds=Seconds.secondsBetween(dt1, dt2).getSeconds() % 60;
            convertS=String.valueOf(seconds);
            if (hours   < 10) {convertH = "0"+hours;}
            if (minutes < 10) {convertM = "0"+minutes;}
            if (seconds < 10) {convertS = "0"+seconds;}
           
            totalTime=convertH+":"+convertM+":"+convertS;
 
		} catch (Exception e) {
			// TODO: handle exception
			return totalTime;
		}
		return totalTime;
	}
	
}
