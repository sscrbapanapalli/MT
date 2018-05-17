package com.cmacgm.mytime.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired(required = true)
	private HttpSession httpSession;
	
	@Autowired
	ReportsRepository reportsRepository;
	@Autowired
	ActivityTrackRepository activityTrackRepository;
	
	@Autowired
	AuditHistoryActivityTrackRepository auditHistoryActivityTrackRepository;
	
	
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
	@GetMapping("/getAuditHistoryReport")
	public List<AuditHistoryActivityTrack> getAuditHistoryReport(HttpServletRequest req){
		 List<AuditHistoryActivityTrack> mgrReporteesList=null;
		 String userName = (String) httpSession.getAttribute("userName");		
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
			 mgrReporteesList=auditHistoryActivityTrackRepository.findByUpdatedByAndCreatedDateBetween(userName,dateFrom,dateTo);		
		
		return mgrReporteesList;
	}

	
	@RequestMapping(value = "/overrideEmployeeTime", method = RequestMethod.POST ,consumes="application/json",  produces="application/json")
    public @ResponseBody String setEmployeeConfiguration(@RequestBody AuditHistoryActivityTrack auditHistoryActivityTrack) throws IllegalStateException, IOException {		
		  String overrideResponse="Employee Time override Success",totalTime="00:00:00";
			
		try {
				
		  String updatedBy = (String) httpSession.getAttribute("userName");
	
	
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
			 activityTrackRepository.overrideEmployeeTime(dateFrom,dateTo,totalTime,comments,updatedBy,auditHistoryActivityTrack.getId());
			 auditHistoryActivityTrack.setId(null);
			 auditHistoryActivityTrack.setRevisedActivityStartTime(dateFrom);
			 auditHistoryActivityTrack.setRevisedActivityEndTime(dateTo);
			 auditHistoryActivityTrack.setActivityStartTime(userActivityTrack.getActivityStartTime());
			 auditHistoryActivityTrack.setActivityEndTime(userActivityTrack.getActivityEndTime());
			 auditHistoryActivityTrack.setUpdatedBy(updatedBy);
			 auditHistoryActivityTrack.setCreatedBy(updatedBy);
			 auditHistoryActivityTrack.setActivityName(userActivityTrack.getActivityName());
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
            hours=Hours.hoursBetween(dt1, dt2).getHours() % 24;
            convertH=String.valueOf(hours);
            minutes=Minutes.minutesBetween(dt1, dt2).getMinutes() % 60;
            convertM=String.valueOf(minutes);
            seconds=Seconds.secondsBetween(dt1, dt2).getSeconds() % 60;
            convertS=String.valueOf(seconds);
            if (hours   < 10) {convertH = "0"+hours;}
            if (minutes < 10) {convertM = "0"+minutes;}
            if (seconds < 10) {convertS = "0"+seconds;}
           
            totalTime=convertH+":"+convertM+":"+convertS;
          System.out.println(totalTime);
		} catch (Exception e) {
			// TODO: handle exception
			return totalTime;
		}
		return totalTime;
	}
	
}
