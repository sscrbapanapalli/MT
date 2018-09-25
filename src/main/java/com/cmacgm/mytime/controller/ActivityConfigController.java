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

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.mytime.model.ActivityConfig;
import com.cmacgm.mytime.model.ActivityDetails;
import com.cmacgm.mytime.model.ActivitySettings;
import com.cmacgm.mytime.model.UserActivityTrack;
import com.cmacgm.mytime.repository.ActivityConfigRepository;
import com.cmacgm.mytime.repository.ActivityTrackRepository;

@RestController
@RequestMapping("/activity")
public class ActivityConfigController {
	
	

	@Autowired
	ActivityConfigRepository activityConfigRepository;
	
	@Autowired
	ActivityTrackRepository activityTrackRepository;
	
	@GetMapping(value="/getAllActivies")
	public List<ActivitySettings> getAllActivies(){
		return activityConfigRepository.findAll();
	}
	
	@GetMapping(value="/getStartTaskCount")
	public int getStartTaskCount(HttpServletRequest request){
		int count=0;
		try {
			String windowsUserName=request.getParameter("windowsUserName");
			if ( !windowsUserName.isEmpty() && windowsUserName!=null){	
				count= activityTrackRepository.findByActivityStatus(windowsUserName);
				return count;
			}
		} catch (Exception e) {
			return count;			
		}
		
		return count;
	}
	
	 @GetMapping("/getselectedActivityDetails/{id}")
		public ActivitySettings  getselectedActivityDetails(@PathVariable(value="id") Long id){
	
			return activityConfigRepository.findById(id);
		}
	 
		@RequestMapping(value = "/updateActivity", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String updateActivity(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
			String updateResponse="";
			
			Long id=Long.parseLong(request.getParameter("id"));
			String activityName=request.getParameter("activityName");
			String activityType=request.getParameter("activityType");
			int thresholdHours=Integer.parseInt(request.getParameter("thresholdHours"));
			int thresholdMins=Integer.parseInt(request.getParameter("thresholdMins"));
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
					activity.setActivityType(activityType);
					activity.setThresholdHours(thresholdHours);
					activity.setThresholdMins(thresholdMins);
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
				deleteResponse="Activity-" + activity.getActivityName() +" Deleted Successfully";
				
			}catch(Exception e){
				deleteResponse="Activity-" + activity.getActivityName() +" failed to Delete Please contact Application Support Team";
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
	List<ActivityDetails> activityList=activityConfig.getActivityMapping();
			
	for(ActivityDetails obj:activityList){
		ActivitySettings activitySettings=new ActivitySettings();
		activitySettings.setActivityName(obj.getActivityName());
		activitySettings.setActivityType(obj.getActivityType());
		activitySettings.setThresholdHours(obj.getThresholdHours());
		activitySettings.setThresholdMins(obj.getThresholdMins());
		activitySettings.setCreatedBy(createdBy);
		activitySettings.setUpdatedBy(createdBy);
		activitySettings.setActiveIndicator(true);
		
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
	public List<ActivitySettings> getActiveActivity(HttpServletRequest request){
		
		List<UserActivityTrack> userAddedTasks=null;
		List<ActivitySettings> notSelectedTasks=null;
		List<ActivitySettings> availableTasks=null;
		try {
			String windowsUserName=request.getParameter("windowsUserName");
			if ( !windowsUserName.isEmpty() && windowsUserName!=null){	
			userAddedTasks=activityTrackRepository.findByUserId(windowsUserName);
			}
			boolean activeIndicator=true;
			List<String> availableTasksList=new ArrayList<String>();
			List<String> userAddedTasksList=new ArrayList<String>();
			List<String> notSelectedTasksList=new ArrayList<String>();
			notSelectedTasks=new ArrayList<ActivitySettings>();
			
			availableTasks=activityConfigRepository.findByActiveIndicator(activeIndicator);
			for (ActivitySettings loop1:availableTasks){
				availableTasksList.add(loop1.getActivityName());
			}
			
			
			for (UserActivityTrack loop2:userAddedTasks){
				userAddedTasksList.add(loop2.getActivityId().getActivityName());
			}
			
			for(String obj:availableTasksList){
				if(!userAddedTasksList.contains(obj)){
					notSelectedTasksList.add(obj);
				
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return availableTasks;	
	
	}
	
	
	@GetMapping(value="/getUserActivity")
	public List<UserActivityTrack> getUserActivity(HttpServletRequest request){
		List<UserActivityTrack> userActivity=null;
		  String windowsUserName=request.getParameter("windowsUserName");
		try {
			if ( !windowsUserName.isEmpty() && windowsUserName!=null)	
				return activityTrackRepository.findByUserId(windowsUserName);
			else
				return userActivity;
		} catch (Exception e) {
		          e.printStackTrace();
		}
		return userActivity;
		
	}
	
	
	@RequestMapping(value = "/addUserActivity", method = RequestMethod.POST ,consumes="application/json", produces="application/json")
    public @ResponseBody String addUserActivity(@RequestBody ActivityConfig activityConfig) throws IllegalStateException, IOException {
		
	String configResponse="";
	String activityStatus="Not Started";
	String userId=activityConfig.getUserName();
	String createdBy=activityConfig.getUserName();
	List<ActivitySettings> activityList=activityConfig.getActivityTracker();
			
	for(ActivitySettings obj:activityList){
		UserActivityTrack userActivityTrack=new UserActivityTrack();
		userActivityTrack.setUserId(userId);
		userActivityTrack.setActivityId(obj);
		//userActivityTrack.setActivityType(obj.getActivityType());
		userActivityTrack.setCreatedBy(createdBy);
		userActivityTrack.setUpdatedBy(createdBy);
		userActivityTrack.setActivityStatus(activityStatus);
		
	
		try{
			activityTrackRepository.save(userActivityTrack);
		configResponse="Activity Task added successfully";
		
		}catch(Exception e){
			configResponse="Activity Configuration failed to save, Please contact Application Support Team";
			System.out.println(e.getMessage());
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

	UserActivityTrack selectedTask=activityTrackRepository.findById(id);
	
			
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
	    
		/*totalTime=Hours.hoursBetween(dt1, dt2).getHours() % 24+":"
							+Minutes.minutesBetween(dt1, dt2).getMinutes() % 60+":"
							+Seconds.secondsBetween(dt1, dt2).getSeconds() % 60;
*/
	}catch(Exception e){
		e.printStackTrace();
	}
	

			
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
	
	@GetMapping(value="/getActiveTasks")
	public String getActiveTasks(HttpServletRequest request){
		String thresholdMsg="";
		String message="";
		int hours,mins,taskExceded;
		
		List<UserActivityTrack> userActiveTasks=null;
		  String windowsUserName=request.getParameter("windowsUserName");
		 
		try {
			if ( !windowsUserName.isEmpty() && windowsUserName!=null)	{
				userActiveTasks= activityTrackRepository.findByUserIdActivityStatus(windowsUserName);
				System.out.println("list size" + userActiveTasks.size());
			
				if(userActiveTasks.size()>0){
					
					for (UserActivityTrack obj:userActiveTasks){
						Date currentTime = new Date();
						Date activityStartTime=obj.getActivityStartTime();
						Date thresholdTime=activityStartTime;
						thresholdTime.setHours(thresholdTime.getHours()+ obj.getActivityId().getThresholdHours());
						thresholdTime.setMinutes(thresholdTime.getMinutes()+obj.getActivityId().getThresholdMins());
						taskExceded=thresholdTime.compareTo(currentTime); // date1.compareTo(date2); date1 < date2, returns less than 0
						hours=obj.getActivityId().getThresholdHours();
						mins=obj.getActivityId().getThresholdMins();
						System.out.println(taskExceded);
						if(taskExceded<0 && obj.getActivityId().getThresholdHours()>0 && obj.getActivityId().getThresholdMins()==0 ){
							message=obj.getActivityId().getActivityName() + " activity is in progress beyond " + obj.getActivityId().getThresholdHours() + "Hour(s). ";
							thresholdMsg=thresholdMsg+message;
						}
						if(taskExceded<0 && obj.getActivityId().getThresholdHours()==0 && obj.getActivityId().getThresholdMins()>0 ){
							System.out.println("In condition");
							message=obj.getActivityId().getActivityName() + " activity is in progress beyond " + obj.getActivityId().getThresholdMins() + "Mins(s). ";
							thresholdMsg=thresholdMsg+message;
						}
						if(taskExceded<0 && obj.getActivityId().getThresholdHours()>0 && obj.getActivityId().getThresholdMins()>0 ){
							message=obj.getActivityId().getActivityName() + " activity is in progress beyond " + obj.getActivityId().getThresholdHours() + "Hour(s) :" + 
									obj.getActivityId().getThresholdMins() + "Mins(s). ";
							thresholdMsg=thresholdMsg+message;
						}
						
					}
					thresholdMsg="A Gentle Reminder!! "+thresholdMsg+"Please stop the activity, if you have completed it actually!!";
					return thresholdMsg;
				}else
					return thresholdMsg;
			}
		} catch (Exception e) {
		          e.printStackTrace();
		}
		return thresholdMsg;
		
	}
	
}
