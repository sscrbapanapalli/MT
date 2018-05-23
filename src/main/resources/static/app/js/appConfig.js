 
 var config=angular.module("appConfigApp", [])
.constant("appConstants",{
	serverUrl : "http://10.13.44.33:8080/MyTime",
	//serverUrl : "http://10.13.44.80:8080/MyTime",
	//serverUrl : "http://10.13.56.64:8080",
	//serverUrl : "http://localhost:8080",

}).run(function($http, $window) {	
	//$http.defaults.headers.common['userToken'] =$window.sessionStorage["userToken"]?$window.sessionStorage["userToken"]: null;
});
 
 angular.module('appConfigApp') 
 .config(function ( $httpProvider) {        

     delete $httpProvider.defaults.headers.common['X-Requested-With'];
 });