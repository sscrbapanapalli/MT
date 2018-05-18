package com.cmacgm.mytime.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.mytime.model.ActivityConfig;
import com.cmacgm.mytime.model.ActivitySettings;
import com.cmacgm.mytime.model.UserActivityTrack;
import com.cmacgm.mytime.repository.ActivityConfigRepository;
import com.cmacgm.mytime.repository.ActivityTrackRepository;

@RestController
@RequestMapping("/activity")
public class ActivityConfigController {
	
	
	@Autowired(required=true)
	private HttpSession httpSession;

	@Autowired
	ActivityConfigRepository activityConfigRepository;
	
	@Autowired
	ActivityTrackRepository activityTrackRepository;
	
	@GetMapping(value="/getAllActivies")
	public List<ActivitySettings> getAllActivies(){
		return activityConfigRepository.findAll();
	}
	
	@GetMapping(value="/getStartTaskCount")
	public int getStartTaskCount(){
		int count=0;
		String userId= (String) httpSession.getAttribute("userName");
		//System.out.println("count="+ userId);
		count= activityTrackRepository.findByActivityStatus(userId);
		//System.out.println("count="+ count);
		return count;
	}
	
	 @GetMapping("/getselectedActivityDetails/{id}")
		public ActivitySettings  getselectedActivityDetails(@PathVariable(value="id") Long id){
		 System.out.println(" for all roles" + id);
			return activityConfigRepository.findById(id);
		}
	 
		@RequestMapping(value = "/updateActivity", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String updateActivity(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
			String updateResponse="";
			
			Long id=Long.parseLong(request.getParameter("id"));
			String activityName=request.getParameter("activityName");
			String updatedBy=request.getParameter("updatedBy");
			ActivitySettings activity=activityConfigRepository.findById(id);
			/*System.out.println(" in update method server");
			System.out.println(id+ activityName + updatedBy );
			System.out.println(" from db" + activity);*/
			if(activity!=null){
				try{
					activity.setActivityName(activityName);
					activity.setActiveIndicator(true);
					activity.setUpdatedBy(updatedBy);
					//System.out.println(activity.isActiveIndicator());
					activityConfigRepository.save(activity);
					updateResponse="Activity Updated Successfully";
					
				}catch(Exception e){
					updateResponse="Activity-" + activity.getActivityName() +" failed to Update Please contact Application Support Team";
				}
				
			}else{
				updateResponse="Activity-" + activity.getActivityName()+" Not found in DB";
			}
			return updateResponse;
		}
	 
	@RequestMapping(value = "/deleteActivity", method = RequestMethod.POST , produces="application/json")
    public @ResponseBody String deleteActivity(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String deleteResponse="";
		
		Long id=Long.parseLong(request.getParameter("id"));
		String updatedBy=request.getParameter("updatedBy");
		ActivitySettings activity=activityConfigRepository.findById(id);
		//System.out.println(" in deleteApplication method server");
		if(activity!=null){
			try{
				activity.setActiveIndicator(false);
				activity.setUpdatedBy(updatedBy);
				//System.out.println(activity.isActiveIndicator());
				activityConfigRepository.save(activity);
				deleteResponse="Application-" + activity.getActivityName() +" Deleted Successfully";
				
			}catch(Exception e){
				deleteResponse="Application-" + activity.getActivityName() +" failed to Delete Please contact Application Support Team";
			}
			
		}else{
		deleteResponse="Activity-" + activity.getActivityName()+" Not found in DB";
		}
		return deleteResponse;
	}
	
	@RequestMapping(value = "/setActivityConfig", method = RequestMethod.POST ,consumes="application/json", produces="application/json")
    public @ResponseBody String setActivityConfig(@RequestBody ActivityConfig activityConfig) throws IllegalStateException, IOException {
	String configResponse="";
	
	String createdBy=activityConfig.getUserName();
	List<String> activityList=activityConfig.getActivityMapping();
			
	for(String obj:activityList){
		ActivitySettings activitySettings=new ActivitySettings();
		activitySettings.setActivityName(obj);
		activitySettings.setCreatedBy(createdBy);
		activitySettings.setUpdatedBy(createdBy);
		activitySettings.setActiveIndicator(true);
		
		System.out.println(activitySettings);
		try{
			activityConfigRepository.save(activitySettings);
		configResponse="Activity Configuration saved successfully";
		
		}catch(Exception e){
			configResponse="Activity Configuration failed to save, Please contact Application Support Team";
		}
		
	 }
		
	
	 return configResponse;
 }
	
	//Activity Tracker for user
	
	
	
	@GetMapping(value="/getActiveActivity")
	public List<ActivitySettings> getActiveActivity(){
		String userId = (String) httpSession.getAttribute("userName");
		boolean activeIndicator=true;
		List<String> availableTasksList=new ArrayList<String>();
		List<String> userAddedTasksList=new ArrayList<String>();
		List<String> notSelectedTasksList=new ArrayList<String>();
		List<ActivitySettings> notSelectedTasks=new ArrayList<ActivitySettings>();
		
		List<ActivitySettings> availableTasks=activityConfigRepository.findByActiveIndicator(activeIndicator);
		for (ActivitySettings loop1:availableTasks){
			availableTasksList.add(loop1.getActivityName());
		}
		
		List<UserActivityTrack> userAddedTasks=activityTrackRepository.findByUserId(userId);
		for (UserActivityTrack loop2:userAddedTasks){
			userAddedTasksList.add(loop2.getActivityName());
		}
		
		for(String obj:availableTasksList){
			if(!userAddedTasksList.contains(obj)){
				notSelectedTasksList.add(obj);
				System.out.println(obj);
			}
		}
		
		//System.out.println("availableTasks size:"+availableTasks.size()+","+"userAddedTasks:"+userAddedTasks.size()+","+"Notselected size:"+notSelectedTasksList.size());
		if(userAddedTasks.size()>0){
			for(String obj1:notSelectedTasksList){
				for (ActivitySettings loop1:availableTasks){
					if(obj1.equals(loop1.getActivityName())){
						notSelectedTasks.add(loop1);
						
					}
				}
			}
			return notSelectedTasks;
		}else{
		//System.out.println(" To get not selected tasks" + notSelectedTasks);
		return availableTasks;
		}
	
	}
	
	
	@GetMapping(value="/getUserActivity")
	public List<UserActivityTrack> getUserActivity(){
		String userId = (String) httpSession.getAttribute("userName");
		System.out.println("get user activity list"+ userId);
		
		return activityTrackRepository.findByUserId(userId);
	}
	
	
	@RequestMapping(value = "/addUserActivity", method = RequestMethod.POST ,consumes="application/json", produces="application/json")
    public @ResponseBody String addUserActivity(@RequestBody ActivityConfig activityConfig) throws IllegalStateException, IOException {
		System.out.println("in controller");
		System.out.println("in controller"+ activityConfig.getUserName()+ activityConfig.getActivityMapping());
	String configResponse="";
	String activityStatus="Not Started";
	String userId=activityConfig.getUserName();
	String createdBy=activityConfig.getUserName();
	List<ActivitySettings> activityList=activityConfig.getActivityTracker();
			
	for(ActivitySettings obj:activityList){
		UserActivityTrack userActivityTrack=new UserActivityTrack();
		userActivityTrack.setUserId(userId);
		userActivityTrack.setActivityName(obj.getActivityName());
		userActivityTrack.setCreatedBy(createdBy);
		userActivityTrack.setUpdatedBy(createdBy);
		userActivityTrack.setActivityStatus(activityStatus);
		
		System.out.println(userActivityTrack);
		try{
			activityTrackRepository.save(userActivityTrack);
		configResponse="Activity Task added successfully";
		
		}catch(Exception e){
			configResponse="Activity Configuration failed to save, Please contact Application Support Team";
		}
		
	 }
	 return configResponse;
 }

	//Start Activity Task
	
	@RequestMapping(value = "/startActivity", method = RequestMethod.POST, produces="application/json")
    public @ResponseBody String startActivity(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		
	String configResponse="";
	String activityStatus="In Progress";
	String updatedBy=request.getParameter("userName");
	Date date = new Date();
	Calendar c = Calendar.getInstance();
	String currentMonth=c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH );
	Long currentYear = Long.valueOf(c.get(Calendar.YEAR));
	Long id=Long.parseLong(request.getParameter("id"));
	System.out.println(updatedBy);
	System.out.println(id);
	UserActivityTrack selectedTask=activityTrackRepository.findById(id);
	System.out.println(selectedTask);
			
	if(selectedTask!=null ){
		
		selectedTask.setActivityStatus(activityStatus);
		selectedTask.setUpdatedBy(updatedBy);
		selectedTask.setActivityCurDate(date);
		selectedTask.setActivityStartTime(date);
		selectedTask.setActivityCurMon(currentMonth);
		selectedTask.setActivityCurYear(currentYear);
		
		
		try{
			activityTrackRepository.save(selectedTask);
		configResponse="Activity Started successfully";
		
		}catch(Exception e){
			configResponse="Activity Not Started, Please contact Application Support Team";
		}
		
	 }
	 return configResponse;
 }
	
	//Stop Activity Task
	
	@RequestMapping(value = "/stopActivity", method = RequestMethod.POST, produces="application/json")
    public @ResponseBody String stopActivity(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String totalTime="";
		int hours,minutes,seconds;
		String convertH="",convertM="",convertS="";
				
	String configResponse="";
	String activityStatus="Completed";
	String updatedBy=request.getParameter("userName");
	Date endTime = new Date();
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	String date2= format.format(endTime);
	
	Long id=Long.parseLong(request.getParameter("id"));
	UserActivityTrack selectedTask=activityTrackRepository.findById(id);
	Date startTime=selectedTask.getActivityStartTime();
	String date1=format.format(startTime);
	
	Date d1=null;
	Date d2=null;
	try{
		d1 = format.parse(date1);
		d2 = format.parse(date2);
		
		DateTime dt1 = new DateTime(d1);
		DateTime dt2 = new DateTime(d2);
		hours=Hours.hoursBetween(dt1, dt2).getHours() % 24;
		convertH=String.valueOf(hours);
		minutes=Minutes.minutesBetween(dt1, dt2).getMinutes() % 60;
		convertM=String.valueOf(minutes);
		seconds=Seconds.secondsBetween(dt1, dt2).getSeconds() % 60;
		convertS=String.valueOf(seconds);
		 if (hours   < 10) {convertH = "0"+hours;}
		 if (minutes < 10) {convertM = "0"+minutes;}
		 if (seconds < 10) {convertS = "0"+seconds;}
		 System.out.println(hours);
		totalTime=convertH+":"+convertM+":"+convertS;
	    
		/*totalTime=Hours.hoursBetween(dt1, dt2).getHours() % 24+":"
							+Minutes.minutesBetween(dt1, dt2).getMinutes() % 60+":"
							+Seconds.secondsBetween(dt1, dt2).getSeconds() % 60;
*/
		System.out.println(totalTime);
	}catch(Exception e){
		e.printStackTrace();
	}
	
	System.out.println(date1);
	System.out.println(date2);
			
	if(selectedTask!=null ){
		
		selectedTask.setActivityStatus(activityStatus);
		selectedTask.setUpdatedBy(updatedBy);
		selectedTask.setActivityEndTime(endTime);
		selectedTask.setActivityTotTime(totalTime);
		
		try{
			activityTrackRepository.save(selectedTask);
		configResponse="Activity Completed successfully";
		
		}catch(Exception e){
			configResponse="Activity Not Started, Please contact Application Support Team";
		}
		
	 }
	 return configResponse;
 }
}
