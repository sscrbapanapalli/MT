 
 var config=angular.module("appConfigApp", [])
.constant("appConstants",{
	//serverUrl : "http://10.13.44.33:8080/MyTime",
	serverUrl : "http://10.13.44.80:8080/MyTimeDev",
   // serverUrl : "http://localhost:8080",
	//serverUrl : "http://10.13.56.184:8080",
	
	localServer:false,
	windowsUNUrl:"http://10.13.44.80:8080/windowsUN/Testing",
	userTimeReportUrl:"http://10.13.56.23/ReportServer/Pages/ReportViewer.aspx?%2fSSC%2fUser_Time_Report%2fUserProductivity&rs:Command=Render",
	//thresholdTime:4, //4 hours threshold limit for a task.
	//thresholdHours:3, //warning from before half an hour
	thresholdMinutes:30,//warning from before half an hour
	timeIntervalCheck:300000
//5 minutes in milli seconds

}).run(function($http, $window) {	
	//$http.defaults.headers.common['userToken'] =$window.sessionStorage["userToken"]?$window.sessionStorage["userToken"]: null;
});
 
 angular.module('appConfigApp') 
 .config(function ( $httpProvider) {        

     delete $httpProvider.defaults.headers.common['X-Requested-With'];
 });