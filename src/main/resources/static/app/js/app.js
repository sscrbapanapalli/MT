/* @author Basker Ammu, Ramesh Kumar B
 * Url Routing configuration
 * $stateProvider,$urlRouterProvider
 */

'use strict';
angular.module("cdrApp", [ "ui.router",'appConfigApp' ,'commonServiceApp','angularUtils.directives.dirPagination']);

'use strict';
angular.module('cdrApp').config(
		[ '$stateProvider', '$urlRouterProvider',
				function($stateProvider, $urlRouterProvider) {
				
					$urlRouterProvider.otherwise("/login");
					$stateProvider.state("login", {
						url : '/login',
						templateUrl : 'view/login.html',
						controller : "loginController",
						controllerAs : "loginController",
						data:{requireLogin:true}

					}).state("upload", {
						url : '/upload',
						templateUrl : 'view/upload.html',
						controller : "uploadController",
						controllerAs : "uploadController",
						
						data:{requireLogin:true}

					}).state("home", {
						url : '/home',
						templateUrl : 'view/home.html',
						controller : "homeController",
						controllerAs : "homeController",
						data:{requireLogin:true}

					}).state("app", {
						url : '/app?',
						templateUrl : 'view/home.html',
						controller : "homeController",
						controllerAs : "homeController",
						
					    params: { 
					    	appId: null,
					    	appName: null// can initialise to default value
					      
					    },
						data:{requireLogin:true}

					}).state("userSettings", {
						url : '/userSettings',
						templateUrl : 'view/userSettings.html',
						controller : "settingsController",
						controllerAs : "settingsController",
						data:{requireLogin:true}

					}).state("applicationSettings",{
						url : '/applicationSettings',
						templateUrl :'view/applicationSettings.html',
						controller :"appSettingsController",
						controllerAs : "appSettingsController",
						data:{requireLogin:true}
					})
					.state("jamesWeb",{
						url : '/jamesWeb',
						templateUrl :'view/jamesWeb.html',
						controller :"jamesWebController",
						controllerAs : "jamesWebController",
						data:{requireLogin:true}
					})
					.state("activityTrackAdmin",{
						url : '/activityTrackAdmin',
						templateUrl :'view/activityTrackAdmin.html',
						controller :"activityTrackController",
						controllerAs : "activityTrackController",
						data:{requireLogin:true}
					})
					.state("activityTrackUser",{
						url : '/activityTrackUser',
						templateUrl :'view/activityTrackUser.html',
						controller :"activityTrackUserController",
						controllerAs : "activityTrackUserController",
						data:{requireLogin:true}
					})
					.state("monitoring",{
						url : '/monitoring',
						templateUrl :'view/monitoring.html',
						controller :"monitoringController",
						controllerAs : "monitoringController",
						data:{requireLogin:true}
					})
					.state("reports",{
						url : '/reports',
						templateUrl :'view/reports.html',
						controller :"reportsController",
						controllerAs : "reportsController",
						data:{requireLogin:true}
					}).state("empSettings", {
						url : '/empSettings',
						templateUrl : 'view/empSettings.html',
						controller : "empSettingsController",
						controllerAs : "empSettingsController",
						data:{requireLogin:true}

					}).state("uploadEmpData",{
						url : '/uploadEmpData',
						templateUrl :'view/empUpload.html',
						controller :"uploadEmpDataController",
						controllerAs : "uploadEmpDataController",
						data:{requireLogin:true}
					});
					
					

				} ]);


angular
		.module('cdrApp')
		.controller(
				'homeController',
				[
						'$scope',
						'$state',
						'$rootScope',
						'$window',
						'$q',
						'$http',
						'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
						function($scope, $state, $rootScope, $window, $q,
								$http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
							$scope.homepageContent = "landing dashboard page";
							//$scope.batchFiles={fileName:'', filePath:''};
							$scope.batchFileslist=[];
							$scope.checkStatus=false;
							$scope.welcomeMsg=false;
							$scope.userId=0;
							$scope.currentPage = 1;						
							$scope.currentPagetest = 1;
							$scope.pageSizetest = 10;
							$scope.showChild=false;		
							$scope.currentUploadDetailsResult={};							
							$scope.selectedId = undefined;
							$scope.selectedIdChild = undefined;
							$scope.resetParent=function(){
							$scope.currentPage = 1;
							$scope.pageSize = 10;
									
									  $scope.search=""
										  if ( $window.sessionStorage.getItem('appId')  != undefined
													|| $window.sessionStorage.getItem('appId')  != null) {						
								            	
										  $scope.batchHistoryDetails( $window.sessionStorage.getItem('appId'));
										  }
								}
								$scope.resetChild=function(){
									  $scope.searchBatch=""
										  $scope.currentPagetest = 1;
									  $scope.pageSizetest = 10;
										  
								}
								
								$scope.ClassOdd = function(id)
								{
									if(id === $scope.selectedId)
										return "selected";
									else 
										return "odd";
								};
								
								$scope.ClassEven = function(id)
								{
									if(id === $scope.selectedId)
										return "selected";
									else 
										return "even";
								
								};
								
								$scope.ClassOddChild = function(id)
								{
									if(id === $scope.selectedIdChild)
										return "selected";
									else 
										return "odd";
								};
								
								$scope.ClassEvenChild = function(id)
								{
									if(id === $scope.selectedIdChild)
										return "selected";
									else 
										return "even";
								
								};
								
								
								
							//$rootscope.selectedAppId=0;
							$scope.inituser = function() {
								var data = globalServices.isUserTokenAvailable();
								if (data == null || data == undefined) {
									$rootScope.isProfilePage = false;
									$state.go("login");
								} else {
									$rootScope.currentUser = userService.getCurrentUser();
									if ($rootScope.currentUser != undefined
											|| $rootScope.currentUser != null) {
										$rootScope.isProfilePage = true;
									} else {
										$rootScope.isProfilePage = false;
										$state.go("login");
									}

								}
							}
							$scope.logout = function () {		
								   AuthenticationService.ClearCredentials();  
								   $rootScope.isProfilePage=false;
								   
							   }
							
							$scope.batchFiles=function(row,id){
								 $scope.selectedIdChild = undefined;
								$scope.selectedId = row;
								$scope.checkStatus=true;
								$scope.inituser();
								
							
								//alert(s);
							  	   $scope.batchFileslist=[];								
								  for(var i=0; i<$scope.batchDetailsResult.length; i++) {									
								   if(id==$scope.batchDetailsResult[i].id){
								   $scope.batchFileslist=$scope.batchDetailsResult[i].batchFileDetailList;		
								  						   
								      // call $anchorScroll()
								      anchorSmoothScroll.scrollTo('bottom');
								   }
								  }
								/*$scope.batchFiles.fileName=$scope.batchDetailsResult[s].batchFileDetail.batchFileName;
								$scope.batchFiles.filePath=$scope.batchDetailsResult[s].batchFileDetail.batchFileTrgtPath;
								console.log('got selected batch details', $scope.batchFiles)
								console.log($scope.batchFiles.fileName)
								console.log($scope.batchFiles.filePath)*/
							}

					
							// To get application list
							
							$scope.init = function() {
								$scope.inituser();
								if ($rootScope.currentUser!=undefined )
									if($rootScope.currentUser.userId != undefined
										||$rootScope.currentUser.userId != null) {
								$scope.userId=$rootScope.currentUser.userId;
								}
								
								$scope.user={};
								$scope.welcomeMsg=false;
								var data = new FormData();
																
								var url =  appConstants.serverUrl+"/login/getUserAuthDetails/" + $window.sessionStorage.getItem('userToken');
								
								$http.get(url).then(function(response) {
									
												$scope.applications=response.data.applications;	
												$scope.roles=response.data.roles;	
											console.log(' userDetails' , response)
											console.log(' applications' , $scope.applications)
											console.log(' roles' , $scope.roles)
										});
								
								 var appId = $stateParams.appId
								 var appName=$stateParams.appName
									if(appId!=undefined )
									$scope.batchHistoryDetails(appId,appName);
								
							};
							
							$scope.selectRecordRow=function(row) {
								$scope.selectedIdChild = row;
							}
						
							$scope.batchHistoryDetails = function(appId,appName) {
								$scope.selectedAppName=appName;
								console.log('appName:' , appName)
								$scope.selectedId = undefined;
								$scope.selectedIdChild = undefined;
								$scope.checkStatus=false;
								$scope.batchDetailsResult=[];
								$scope.batchFileslist=[];
								$scope.inituser();
								$scope.welcomeMsg=true;
								$scope.appId = appId;							
								if ($scope.appId  != undefined
										||$scope.appId  != null) {									
									$window.sessionStorage.setItem('appId',appId); 
					            	console.log('user selected app id in session' , $window.sessionStorage.getItem('appId'))
					            	   if($window.sessionStorage.getItem('appId')!=undefined){
			                                var url1=appConstants.serverUrl+"/api/currentUploadDetails/"+ $window.sessionStorage.getItem('appId');
											var url= appConstants.serverUrl+"/api/batchHistoryDetails/" + $window.sessionStorage.getItem('appId');
											
											
											console.log(url)
											var data = new FormData();
											
											$http.get(url1).then(function(response) {
												$scope.currentUploadDetailsResult = response.data;

												console.log('currentUploadDetailsResult', $scope.currentUploadDetailsResult);
												
											}, function(response) {
												$rootScope.buttonClicked = response.data;
												$rootScope.showModal = !$rootScope.showModal;
												$rootScope.contentColor = "#dd4b39";
												
												
											});

											$http.get(url).then(function(response) {
												$scope.batchDetailsResult = response.data;
												 angular.forEach($scope.batchDetailsResult, function(batchDetailsResult){
													   batchDetailsResult.formattedDate = $filter('date')(batchDetailsResult['batchUploadCrDate'],'yyyy-MM-dd HH:mm:ss');
												      }) ;
												      
												       $scope.batchDetailsResult =  $scope.batchDetailsResult;
												console.log('full batch his details', $scope.batchDetailsResult);
												
											}, function(err) {
												   $rootScope.buttonClicked = response.data;
													$rootScope.showModal = !$rootScope.showModal;
													  $rootScope.contentColor = "#dd4b39";
												
												
											});
											}
					
							};
							}
							$scope.hideChildTable=function(){
								 $scope.checkStatus=false;
							}
							
							$scope.sort = function(keyname){
								$scope.sortParent = keyname;   //set the sortKey to the param passed
								$scope.reverse = !$scope.reverse; //if true make it false and vice versa
								
								
							}
							
							$scope.sortBatch = function(keyname){
								$scope.sortChild = keyname;   //set the sortKey to the param passed
								$scope.reverseBatch = !$scope.reverseBatch; //if true make it false and vice versa
								
								
							}
						
							
							$scope.doUpload = function() {
								$rootScope.isProfilePage = true;
								
								$state.go("upload");
								$scope.checkStatus=false;
								
							}

							   $scope.showDialog = function(flag) {
							        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
							      };
									$scope.doReverse = function(id,batchId){
										console.log('in  do reverese')
									
										 $scope.confirmationDialogConfig = {
										      title: "REVERSE UPLOAD",
										      message: "Are you sure you want to Reverse?",
										      buttons: [{
										        label: "Reverse",
										        action: "Reverse"
										      }],
										      id:id,
										      batchId:batchId
										    };
										    $scope.showDialog(true);			    
										
									
									}
									$scope.confirmReverse =function(id){
										   console.log($window.sessionStorage.getItem('appId'))
										     console.log( $rootScope.currentUser.userName)
										       console.log(id)
										if($rootScope.currentUser!=undefined){
											
											var config = {
													transformRequest : angular.identity,
													transformResponse : angular.identity,
													headers : {
														'Content-Type' : undefined
													}
												}
										var url=appConstants.serverUrl+"/api/reverseUpload/";
										var data = new FormData();
										data.append("applicationId" ,$window.sessionStorage.getItem('appId'));
										data.append("userName", $rootScope.currentUser.userName);
										data.append("selectedBatchUniqueId", id);
										console.log(data)
							
										$http.post(url,data,config).then(
												function(response){
													
													  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); // added by me
													      $rootScope.buttonClicked = response.data;
														  $rootScope.showModal = !$rootScope.showModal;
														  $rootScope.contentColor = "#78b266";
														  $state.go("app", {appId:$window.sessionStorage.getItem('appId')}, {reload: true}); 
														
												},function(response){
													
													  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); // added by me
													      $rootScope.buttonClicked = response.data;
														  $rootScope.showModal = !$rootScope.showModal;
														  $rootScope.contentColor = "#dd4b39";
												});							
										}
										
									}
			
						} ]);



angular.module('cdrApp').service('anchorSmoothScroll', function(){
    
    this.scrollTo = function(eID) {

        // This scrolling function 
        // is from http://www.itnewb.com/tutorial/Creating-the-Smooth-Scroll-Effect-with-JavaScript
        
        var startY = currentYPosition();
        var stopY = elmYPosition(eID);
        var distance = stopY > startY ? stopY - startY : startY - stopY;
        if (distance < 100) {
            scrollTo(0, stopY); return;
        }
        var speed = Math.round(distance / 100);
        if (speed >= 20) speed = 20;
        var step = Math.round(distance / 25);
        var leapY = stopY > startY ? startY + step : startY - step;
        var timer = 0;
        if (stopY > startY) {
            for ( var i=startY; i<stopY; i+=step ) {
                setTimeout("window.scrollTo(0, "+leapY+")", timer * speed);
                leapY += step; if (leapY > stopY) leapY = stopY; timer++;
            } return;
        }
        for ( var i=startY; i>stopY; i-=step ) {
            setTimeout("window.scrollTo(0, "+leapY+")", timer * speed);
            leapY -= step; if (leapY < stopY) leapY = stopY; timer++;
        }
        
        function currentYPosition() {
            // Firefox, Chrome, Opera, Safari
            if (self.pageYOffset) return self.pageYOffset;
            // Internet Explorer 6 - standards mode
            if (document.documentElement && document.documentElement.scrollTop)
                return document.documentElement.scrollTop;
            // Internet Explorer 6, 7 and 8
            if (document.body.scrollTop) return document.body.scrollTop;
            return 0;
        }
        
        function elmYPosition(eID) {
            var elm = document.getElementById(eID);
            var y = elm.offsetTop;
            var node = elm;
            while (node.offsetParent && node.offsetParent != document.body) {
                node = node.offsetParent;
                y += node.offsetTop;
            } return y;
        }

    };
    
});
angular.module('cdrApp')
.factory('AuthenticationService',
	    [ '$http', '$state',"$window",'$rootScope','appConstants','globalServices',
	    function ($http, $state,$window,$rootScope,appConstants,globalServices) {
	    	
	        var service = {};	     
	        service.Login = function (userName, password, callback) {     
	           $http.post(appConstants.serverUrl+"/login/loginUser", {userName:userName,password:encryptString (password)},
						{
					headers : {
						'Accept' : 'application/json',
						'Content-Type' : 'application/json'
					}})
	                .success(function (response) {  
	                
							if(response.message!='failure' && response.data.authStatus){
								service.SetCredentials(response);		                	       
							} 
							 callback(response);	
						
	                   
	                });		

	        };
	 
	        service.SetCredentials = function (response) {        	
	                 $window.sessionStorage.setItem('userToken',response.data.userToken); 
	                 $http.defaults.headers.common['userToken'] =response.data.userToken;	           
	        };
	 
	        service.ClearCredentials = function () {
	        	  
	        	$rootScope.username="";
	        	if(globalServices.isUserTokenAvailable()!=null&&globalServices.isUserTokenAvailable()!=undefined){	
	        		var logoutUrl = appConstants.serverUrl+"/login/logOut/"
					+ globalServices.isUserTokenAvailable()
			$http(
					{
						method : "POST",
						url : logoutUrl,
						headers : {
							"userToken" : globalServices.isUserTokenAvailable()
						}
					})	        	
	             .success(function (response) {                	
							if(response){
								if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null)
									$rootScope.username=$rootScope.currentUser.userName								
									 $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); // added by me
								    $rootScope.isProfilePage=false;
								    $rootScope.currentUser={};	
								    $rootScope.username="";
								 $window.sessionStorage.removeItem('userToken');
								 $window.sessionStorage.removeItem('currentUser');		
								 $window.sessionStorage.removeItem('appId');
								
								  $http.defaults.headers.common['userToken']==null;
								  $rootScope.buttonClicked = "Logout Successfully";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#78b266";
				 
								  $rootScope.isProfilePage=false;
								  $state.go("login");
																
							}else{
								 $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); // added by me
								  $rootScope.buttonClicked = "LogOut Error";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";
							}
								
						
	             	       	
	                
	             });
	        	}
	           
	        };       
	      
	 
	        return service;
	    }]);



angular.module('cdrApp').controller(
		'settingsController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
					$scope.homepageContent = "settings dashboard page";
					$scope.allUserDetails=[];	
					//$scope.userStatus="true";
					$scope.userRoles=[];
					$scope.userApplications=[];
					$scope.allRoles=[];
					$scope.allRolesConstant=[];
					$scope.checkUserDetails={};
					$scope.userId="";
					$scope.pageSize = 10;
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					
					$scope.init=function(){
						$scope.userName="";
						$scope.userStatus=true;
                        var url =  appConstants.serverUrl+"/login/getUserAuthDetails/"+$window.sessionStorage.getItem('userToken');
                        var allUsersUrl=appConstants.serverUrl+"/admin/getAllUserDetails/";
                        var masterAppUrl =  appConstants.serverUrl+"/admin/getAllApplications/";	
                        var masterRoleUrl =  appConstants.serverUrl+"/admin/getAllRoles/";
                        
                        $http.get(url).then(function(response) {
							$scope.applications=response.data.applications;	
							$scope.roles=response.data.roles;	
							/*console.log(' settings userDetails' , response)*/
						});
                        
                        $http.get(allUsersUrl).then(function(response){
                        	$scope.allUserDetails=angular.copy(response.data);
                        	/*console.log('allUserDetails' , $scope.allUserDetails)*/
                        });
                        
                        $http.get(masterAppUrl).then(function(response) {
                        	/*console.log(' allApplications ' , response)*/
                        	$scope.allApplicationsConstant=angular.copy(response.data);
                        	$scope.allApplications=response.data;
                        	
                        });
                        
                        $http.get(masterRoleUrl).then(function(response) {
                        	/*console.log(' allRoles ' , response)*/
                        	$scope.allRolesConstant=angular.copy(response.data);
                        	$scope.allRoles=response.data;	
                        	
                        });
                        
                        
							$scope.inituser();
						}
					
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
					$scope.userConfig=function(){
						
						var userCongigUrl=appConstants.serverUrl+"/admin/setUserConfiguration/";
						var data = {
			            		 userId : $scope.userId,
			            		 selectedRoles : $scope.userRoles,
			            		 selectedApplications : $scope.userApplications,
			            		 userStatus :$scope.userStatus,
			            		 createdBy :$rootScope.currentUser.userName
			     		};
						//console.log(data)
											
						if($scope.userId==null || $scope.userId==undefined || $scope.userId==""){
							$rootScope.buttonClicked = "Please provide User Id detail";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if( $scope.userRoles==null ||  $scope.userRoles==undefined ||  $scope.userRoles==""){
							$rootScope.buttonClicked = "Please select User roles";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if($scope.userApplications==null || $scope.userApplications==undefined || $scope.userApplications==""){
							$rootScope.buttonClicked = "Please select Application";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else{
							//console.log('in else method')
							
							 $http.post(userCongigUrl,data,
										{
											headers : {
												'Accept' : 'application/json',
												'Content-Type' : 'application/json'
											}
										})
							.success(function (response) {  
	                
							//console.log(response)
							$rootScope.buttonClicked = response;
							$rootScope.showModal = !$rootScope.showModal;
							$rootScope.contentColor = "#78b266";
							 $state.go("userSettings", {} , {reload: true} );
							 });
						
						}
					};
					
					$scope.userUpdate=function(rowId,methodName){
						/*console.log('in checkUser method', rowId)
												//$scope.userStatus="true";
						console.log('in user updaTE' ,$scope.allRoles)
						console.log('in user update1', $scope.allRolesConstant)*/
						$scope.userId="";
						$scope.userApplications=[];
						$scope.userUpdateObj={}
						$scope.selectedUserDetails={};
						$scope.allApplications=angular.copy($scope.allApplicationsConstant);
						$scope.allRoles=angular.copy($scope.allRolesConstant);
						/*console.log('in user updaTE' ,$scope.allRoles)
						console.log('in user update1', $scope.allRolesConstant)*/
						$scope.userId="";
						
						
						if(methodName=="updateUser"){
							for(var i=0; i<$scope.allUserDetails.length; i++) {									
								   if(rowId==$scope.allUserDetails[i].id){
									   $scope.inputReadOnly="true";
									   $scope.selectedUserDetails=$scope.allUserDetails[i];
									   $scope.userId=$scope.selectedUserDetails.userId;	
									   $scope.userApplications=angular.copy($scope.selectedUserDetails.applications);
									   $scope.userRoles=angular.copy($scope.selectedUserDetails.roles);
									   $scope.userStatus=angular.copy($scope.selectedUserDetails.activeIndicator);
								   }
							}
							
						}
						if(methodName=="checkUser"){
							
							var userId=rowId.toUpperCase();
							for(var i=0; i<$scope.allUserDetails.length; i++) {									
								   if(userId==$scope.allUserDetails[i].userId){
									   $scope.userExists="true";
									   $scope.inputReadOnly="true";
									   $scope.selectedUserDetails=$scope.allUserDetails[i];
									   $scope.userId=$scope.selectedUserDetails.userId;	
									   $scope.userApplications=angular.copy($scope.selectedUserDetails.applications);
									   $scope.userRoles=angular.copy($scope.selectedUserDetails.roles);
									   $scope.userStatus=angular.copy($scope.selectedUserDetails.activeIndicator);
									   break;
								   }
								   $scope.userId=userId;
								   $scope.userApplications=[];
								   $scope.userRoles=[];
								   $scope.userStatus=true;
								   $scope.userExists="false";
								   $scope.inputReadOnly="";
								   
							}
							
						}
					}
					
					  $scope.showDialog = function(flag) {
					        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
					      };
							
					
					$scope.userDelete=function(id,userName){
						console.log('in  delete user')
						$scope.confirmationDialogConfig = {
							      title: "DELETE USER",
							      message: "Are you sure you want to Delete User?",
							      buttons: [{
							        label: "Delete",
							        action: "Delete"
							      }],
							      id:id,
							      userName:userName
							    };
							    $scope.showDialog(true);
					
					}
					$scope.confirmUserDelete=function(id){
						
						if($rootScope.currentUser!=undefined){
							
							var config = {
									transformRequest : angular.identity,
									transformResponse : angular.identity,
									headers : {
										'Content-Type' : undefined
									}
								}
						var url=appConstants.serverUrl+"/admin/deleteUser/";
						var data = new FormData();
						data.append("id" ,id);
						data.append("updatedBy", $rootScope.currentUser.userName);
						console.log('conform delete to post' , data)
						console.log(id)
						console.log($rootScope.currentUser.userName)
			
						$http.post(url,data,config).then(
								function(response){
									console.log('in delete user post method')
									  $('body').removeClass().removeAttr('style');
									  $('.modal-backdrop').remove(); 
									  $scope.showDialog(false);
									      $rootScope.buttonClicked = response.data;
										  $rootScope.showModal = !$rootScope.showModal;
										  $rootScope.contentColor = "#78b266";
										  $state.go("userSettings", {} , {reload: true} );
										  //$state.go("app", {appId:$window.sessionStorage.getItem('appId')}, {reload: true}); 
										
								},function(response){
									
									  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
									      $rootScope.buttonClicked = response.data;
										  $rootScope.showModal = !$rootScope.showModal;
										  $rootScope.contentColor = "#dd4b39";
								});							
						}
					}
					
					//to check user availability in db
					$scope.checkUser=function(){
						console.log('in checkUser method')
						$scope.userUpdate($scope.userId,"checkUser");
					}
					// To move items from allRoles to userRoles
					$scope.moveItem = function(items, from, to) {

				        console.log('Move items: ' + items + ' From: ' + from + ' To: ' + to)
				        console.log('in user init role constant in move', $scope.allRolesConstant)
				        //Here from is returned as blank and to as undefined

				        items.forEach(function(item) {
				        	console.log('in move item method' + item)
				          var idx = from.indexOf(item);
				        	console.log('selected index:' +idx)
				          if (idx != -1) {
				              from.splice(idx, 1);
				              to.push(item);      
				          }
				        });
				    };

				        
			            $scope.Reset=function(){
			            	//$state.go("userSettings",{},{reload:true});
			            	
			            	$scope.allRoles=angular.copy($scope.allRolesConstant);
			            	
			            	$scope.allApplications=angular.copy($scope.allApplicationsConstant);
			            	
			            	$scope.userId="";
							$scope.userRoles=[];
							$scope.userApplications=[];
							$scope.inputReadOnly="";
							$scope.userExists="false";
							$scope.userStatus=true;
								
			            }
			            
				} ]);

angular.module('cdrApp').controller(
		'appSettingsController',
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','AuthenticationService',
				function($scope, $state, $rootScope, $window, $q, $http,
						appConstants,userService,globalServices,AuthenticationService) {
					$scope.applicationName="";
					$scope.targetPath="";
					$scope.archivePath="";
					$scope.selectedFileType = "";
					$scope.folderMapping = [];
					$scope.fileTypeList = [{id: 1, type: '.xls'}, {id: 2, type: '.xlsx'},{id: 2, type: '.pdf'},{id: 2, type: '.doc'}];
					$scope.appFolderDetails=[];
					$scope.pageSize = 10;
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					
					$scope.init = function() {
						$scope.inituser();
						
						var allAppUrl=appConstants.serverUrl+"/admin/getAllApplications/";
						
						$http.get(allAppUrl).then(function(response) {
                        	console.log(' allApplications ' , response)
                        	$scope.allApplications=response.data;	
                        });
					}
					
					
					$scope.viewAppDetails=function(id,appName){
						$scope.viewApplication="true";
						$scope.addApplication="true";
						$scope.updateApplication="false"
						$scope.appName=appName;
						/*console.log('row:', row)*/
						console.log('appId:', id)
						var viewDetailsUrl=appConstants.serverUrl+"/api/serverfolders/"+id;
						
						$http.get(viewDetailsUrl).then(function(response) {
							$scope.appFolderDetails = response.data;
							

							console.log('appFolderDetails result', $scope.appFolderDetails);
							
						}, function(response) {
							$rootScope.buttonClicked = response.data;
							$rootScope.showModal = !$rootScope.showModal;
							$rootScope.contentColor = "#dd4b39";
							
							
						});
						
					}
					
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
					
					$scope.sortDetail = function(keyname){
						$scope.sortChild = keyname;   //set the sortKey to the param passed
						$scope.reverseChild = !$scope.reverseChild; //if true make it false and vice versa
						
						
					}
					
					$scope.updateAppDetails=function(id,appName){
						$scope.updateApplication="true";
						$scope.viewApplication="false";
						$scope.addApplication="true";
						$scope.appName=appName;
						console.log('appId:', id)
						var viewDetailsUrl=appConstants.serverUrl+"/api/serverfolders/"+id;
						
						$http.get(viewDetailsUrl).then(function(response) {
							$scope.appFolderDetails = response.data;
							$scope.appFoldersSaved=angular.copy($scope.appFolderDetails);
							console.log('appFolderDetails result', $scope.appFolderDetails);
							
						}, function(response) {
							$rootScope.buttonClicked = response.data;
							$rootScope.showModal = !$rootScope.showModal;
							$rootScope.contentColor = "#dd4b39";
							
							
						});
					}
					
					$scope.resetAppFolder=function(){
						$scope.appFolderDetails=angular.copy($scope.appFoldersSaved);
						
					}
					
					$scope.addNewRow=function(){
						 $scope.appFolderDetails.push({
							 	'id':"",
							 	'application':"",
							 	'seqNo':"",
							 	'activeIndicator':"",
							 	'createdBy':"",
							 	'createdDate':"",
							 	'fileAckPath': "",
				                'fileNamePrefix': "",
				                'fileTrgtPath': "",
				                'folderCaption': "",
				                'updatedBy':"",
							 	'updatedDate':"",
				                'validationType': "",
				                   
				            });
						
					}
					
					
					$scope.updateAppFolderConfig=function(){
						//console.log('in updateConfig method')
						//console.log('after update' , $scope.appFolderDetails)
						var updateObj=$scope.appFolderDetails;
						//console.log('after update' , updateObj)
						var updateUrl=appConstants.serverUrl+"/admin/updateAppFolderDetails/";
						$http.post(updateUrl,updateObj,
								{
							headers : {
								'Accept' : 'application/json',
								'Content-Type' : 'application/json'
							}
						})
						.success(function (response) {  
			    
						//console.log(response)
						if(response=="Application Config Updated Successfully"){
							$rootScope.buttonClicked = response;
							$rootScope.showModal = !$rootScope.showModal;
							$rootScope.contentColor = "#78b266";
							$state.go("applicationSettings", {} , {reload: true} );
						}
						else{
							$rootScope.buttonClicked = response;
							$rootScope.showModal = !$rootScope.showModal;
							$rootScope.contentColor = "#dd4b39";
						}
						 });
										
						
					}
					
					  $scope.showDialog = function(flag) {
					        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
					      };
					
					$scope.deleteAppDetails=function(id,appName){
						console.log('in  delete Application')
						$scope.confirmationDialogConfig = {
							      title: "DELETE Application",
							      message: "Are you sure you want to Delete Application?",
							      buttons: [{
							        label: "Delete",
							        action: "Delete"
							      }],
							      id:id,
							      appName:appName
							    };
							    $scope.showDialog(true);
						
						
					}
					
					$scope.confirmUserDelete=function(id){
						
						if($rootScope.currentUser!=undefined){
							
							console.log('delete app id:' , id)
							var config = {
									transformRequest : angular.identity,
									transformResponse : angular.identity,
									headers : {
										'Content-Type' : undefined
									}
								}
						var url=appConstants.serverUrl+"/admin/deleteApplication/";
						var data = new FormData();
						data.append("id" ,id);
						data.append("updatedBy", $rootScope.currentUser.userName);
						console.log('conform delete to post' , data)
						console.log(id)
						console.log($rootScope.currentUser.userName)
			
						$http.post(url,data,config).then(
								function(response){
									console.log('in delete Application post method')
									  $('body').removeClass().removeAttr('style');
									  $('.modal-backdrop').remove(); 
									  $scope.showDialog(false);
									      $rootScope.buttonClicked = response.data;
										  $rootScope.showModal = !$rootScope.showModal;
										  $rootScope.contentColor = "#78b266";
										  $state.go("applicationSettings", {} , {reload: true} );
										 
								},function(response){
									
									  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
									      $rootScope.buttonClicked = response.data;
										  $rootScope.showModal = !$rootScope.showModal;
										  $rootScope.contentColor = "#dd4b39";
								});							
						}
					}
					
					
					$scope.Add = function () {
		            	
		                //Add the new item to the Array.
		            	if($scope.folderName==null || $scope.folderName==undefined ||$scope.folderName=="" 
		            	   ||$scope.fileName==null || $scope.fileName==undefined || $scope.fileName==""){
		            		$rootScope.buttonClicked = "Please provide Folder Mapping Details";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
		            	}else{
		                var folderMappingObj = {};
		                
		                folderMappingObj.folderName = $scope.folderName;
		                folderMappingObj.fileName = $scope.fileName;
		                $scope.folderMapping.push(folderMappingObj);
		            	}
		                //Clear the TextBoxes.
		                $scope.folderName = "";
		                $scope.fileName = "";
		            	
		            };

		            $scope.Remove = function (index) {
		                //Find the record using Index from Array.
		                var name = $scope.folderMapping[index].folderName;
		                if ($window.confirm("Do you want to delete folder: " + name)) {
		                    //Remove the item from Array using Index.
		                    $scope.folderMapping.splice(index, 1);
		                }
		            }
		            
		            $scope.Reset=function(){
		            	
		            	$scope.applicationName="";
		            	$scope.targetPath="";
		            	$scope.archivePath="";
		            	$scope.selectedFileType="";
		            	$scope.folderMapping=[];
	            		$scope.folderName = "";
	            		$scope.fileName = "";
		            	
		            }
					$scope.appConfig=function(){
		            	
		            	 var folderMappingObj = {};
			                folderMappingObj.folderName = $scope.folderName;
			                folderMappingObj.fileName = $scope.fileName;
			                $scope.folderMapping.push(folderMappingObj);
			                
			             var url =  appConstants.serverUrl+"/admin/setApplicationConfig/";
			             
			             var config = {
									transformRequest : angular.identity,
									transformResponse : angular.identity,
									headers : {
										'Content-Type' : undefined
									}
								}
			             console.log(dataObj)
			             
			             var dataObj = {
			            		 applicationName : $scope.applicationName,
			            		 targetPath : $scope.targetPath,
			            		 archivePath : $scope.archivePath,
			            		 selectedFileType :$scope.selectedFileType,
			            		 folderMapping :$scope.folderMapping,
			            		 userName :$rootScope.currentUser.userName
			     		};
			             console.log(dataObj)
			             
			             if($scope.applicationName==null || $scope.applicationName==undefined || $scope.applicationName==""){
			            	 $rootScope.buttonClicked = "Please provide Application Name";
								$rootScope.showModal = !$rootScope.showModal;
								  $rootScope.contentColor = "#dd4b39";
			             }else if($scope.targetPath==null || $scope.targetPath==undefined || $scope.targetPath==""){
			            	 $rootScope.buttonClicked = "Please provide Target Path details";
								$rootScope.showModal = !$rootScope.showModal;
								  $rootScope.contentColor = "#dd4b39";				            	 
			             }else if($scope.archivePath==null || $scope.archivePath==undefined || $scope.archivePath==""){
			            	 $rootScope.buttonClicked = "Please provide Archive Path details";
								$rootScope.showModal = !$rootScope.showModal;
								  $rootScope.contentColor = "#dd4b39";				            	 
			             }else if($scope.selectedFileType==null || $scope.selectedFileType==undefined || $scope.selectedFileType==""){
			            	 $rootScope.buttonClicked = "Please select file type";
								$rootScope.showModal = !$rootScope.showModal;
								  $rootScope.contentColor = "#dd4b39";				            	 
			             }else if($scope.folderName==null || $scope.folderName==undefined ||$scope.folderName=="" 
			            	   ||$scope.fileName==null || $scope.fileName==undefined || $scope.fileName==""){
			            		$rootScope.buttonClicked = "Please provide Folder Mapping Details";
								$rootScope.showModal = !$rootScope.showModal;
								  $rootScope.contentColor = "#dd4b39";
			            	}
			             else{
			             $http.post(url,dataObj,
									{
										headers : {
											'Accept' : 'application/json',
											'Content-Type' : 'application/json'
										}
									})
		                .success(function (response) {  
		                
								/*console.log(response)*/
		                	if(response=="Application Configuration saved successfully"){
								$rootScope.buttonClicked = response;
								$rootScope.showModal = !$rootScope.showModal;
								$rootScope.contentColor = "#78b266";
								$state.go("applicationSettings", {} , {reload: true} );
		                	}else{
		                		$rootScope.buttonClicked = response;
								$rootScope.showModal = !$rootScope.showModal;
								$rootScope.contentColor = "#dd4b39";
		                		
		                	}
						});
		              }	
			            		$scope.folderMapping=[];
			            		$scope.folderName = "";
			            		$scope.fileName = "";
		            }
					
					
				}
		]);

angular
.module('cdrApp')
.controller(
		'loginController',
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http'
				,'appConstants','AuthenticationService','userService',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,AuthenticationService,userService) {
							$rootScope.isProfilePage = false;
							 $scope.dataLoading = false;						
							$rootScope.currentUser = {
								roleType : '',
								email : '',
								userId : '',
								userName : '',
								userToken : ''
							};
							$scope.applications =[];
							/*$scope.roles =[];*/
							
							
							$scope.userLogin = function() {
                                $scope.dataLoading = true;
                                var username = $scope.username;
                                var password = $scope.password;

                                if ((username != '' && username != null && username != undefined)
                                              && (password != '' && password != null && password != undefined)) {
                                       AuthenticationService.Login($scope.username, $scope.password, function(response) {
                                                  console.log(response)
                                              if(response.message!='failure' && response.data.authStatus){ 
                                                    
                                                    var currentUser = {
                                                                                userId : response.data.userId,
                                                                                email  : response.data.userId,
                                                                                userName : response.data.userName,
                                                                                userToken : response.data.userToken
                                                                         };                                                                          
                                                      AuthenticationService.SetCredentials(response);
                                                     
                                                      $window.sessionStorage.setItem('currentUser', JSON.stringify(currentUser));                                    
                                                      $rootScope.currentUser=userService.getCurrentUser();                                     
                                                          $rootScope.isProfilePage=true; 
                                                          $rootScope.buttonClicked ="Welcome! "+$rootScope.currentUser.userName;
                                                          //$rootScope.showModal = !$rootScope.showModal;
                                                          $rootScope.contentColor = "#78b266";
                                                                                                            
                                                          $state.go("activityTrackUser", {}, {reload: true}); 
                                                            
                                        
                                      } else {                                                              
                                       
                                             $scope.dataLoading = false;
                                             $rootScope.buttonClicked ="User Not Authorized to Access Application, Please Contact Application Support Team";
                                             $rootScope.showModal = !$rootScope.showModal;
                                             $rootScope.contentColor = "#dd4b39";
                                             
                                                                                       
                                      }
                                  });
           
                                }else{
                         
                                       $scope.dataLoading = false;
                                              
                                              $rootScope.buttonClicked ="Enter Credentials";
                                              $rootScope.showModal = !$rootScope.showModal;
                                              $rootScope.contentColor = "#dd4b39";
                                       
                                }
                                ;

                         };     


						
							$scope.init=function(){
                                var url =  appConstants.serverUrl+"/login/getUserAuthDetails/"+$window.sessionStorage.getItem('userToken');
                                                      
                                                      $http.get(url).then(function(response) {
                                                             $scope.user=response;
                                                             console.log("EmpDetails  00" ,$scope.user.data)
                                                             
                                                                                 /*$scope.applications=response.data.applications;      
                                                                                 $scope.roles=response.data.roles; */
                                                                          /*console.log(' userDetails in login controller' , response)
                                                                          console.log(' applications' , $scope.applications)
                                                                          console.log(' roles' , $scope.roles)*/
                                                                          
                                                                    });
                                               }

						
						} ]);

angular.module('cdrApp').directive('modal', ['$timeout', function ($timeout) {
    return {
      template: '<div class="modal fade">' + 
          '<div class="modal-dialog" style="width: 750px; margin: auto;">' + 
            '<div class="modal-content" style="background-color:{{contentColor}}">' + 
              '<div class="modal-header">' + 
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' + 
                '<h4 class="modal-title" style="color: #FFFFFF;">{{ buttonClicked }}!!</h4>' + 
              '</div>' + 
              '</div>' + 
          '</div>' + 
        '</div>',
      restrict: 'E',
      transclude: true,
      replace:true,
      scope:true,
      link: function postLink(scope, element, attrs) {
    	  $timeout(function() { // Timeout 
    		  $(element).modal('hide');
    		  }, 3000);
          scope.$watch(attrs.visible, function(value){
          if(value == true)
            $(element).modal('show');
          else       	 
        	 $(element).modal('hide'); 
         
        });

       
    		  
        $(element).on('shown.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = true;
          });
        });

        $(element).on('hidden.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = false;
          });
        });
      }
    };
}]);

angular.module('cdrApp').controller(
		'uploadController',
		[
				'$scope',
				'$rootScope',
				'$http',
				'$window',
				'$state',
				'appConstants','userService','globalServices','AuthenticationService',
				function($scope, $rootScope, $http, $window, $state,
						appConstants,userService,globalServices,AuthenticationService) {

					$rootScope.isProfilePage = false;
					$rootScope.currentUser = {
						roleType : '',
						email : '',
						userId : '',
						userName : '',
						userToken : ''
					};

					$scope.appId = 0;
					$scope.fileList = [];
					$scope.filePath = [];
					$scope.serverFolders = [];
					$scope.serverFoldersResult = [];
					$scope.sessionAppId=$window.sessionStorage.getItem('appId');
					$scope.dataLoading = false;
					$scope.duplicateUpload=false;
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					$scope.logout = function () {		
						   AuthenticationService.ClearCredentials();  
						   $rootScope.isProfilePage=false;
						 
					   }

					$scope.setFile = function(file, index) {
						var responseObjList =$scope.serverFoldersResult;
						var availExt=responseObjList[index-1].validationType.split(",");
						var ext = file[0].name.split(".");
						var selectedExt="."+ ext[ext.length-1];
				       				        
				        const isValidType = availExt.includes(selectedExt);
				        /*console.log('availExt', availExt)
				        console.log('selectedExt', selectedExt)
				        console.log('isValidType', isValidType)*/
						$scope.duplicateUpload=false;
						if(isValidType ){
						if ($scope.fileList!=undefined && $scope.fileList.length>0) {	
							
						 for (var i = 1; i < $scope.fileList.length; i++) {
							if ($scope.fileList[i].name == file[0].name) {	  
															
								 $scope.duplicateUpload=true;
								 $rootScope.$apply(function() {	
								 $rootScope.buttonClicked ="Remove the duplicate file already selected with the name: "+$scope.fileList[i].name +  "";
								 $rootScope.showModal = !$rootScope.showModal;
						         $rootScope.contentColor = "#dd4b39";									
								});								   
								}
						 }
						}
						$scope.fileList[index] = file[0];
				        }	else{
				        	 $rootScope.$apply(function() {	
								 $rootScope.buttonClicked ="File Type Not Supported for file:'" + file[0].name + "' ,Please select "+availExt+" ext files only";
								 $rootScope.showModal = !$rootScope.showModal;
						         $rootScope.contentColor = "#dd4b39";									
								});
				        }

					}

					$scope.doUploadFile = function() {
						$scope.dataLoading = true;
						
						var selectedMonth = document.getElementById("startDate").value;
						var data = new FormData();

						for (var i = 0; i < $scope.fileList.length; i++) {

							data.append("uploadFile[" + i + "]",
									$scope.fileList[i]);

						}
						if( $window.sessionStorage.getItem('appId')!=undefined)
						data.append("applicationId", $window.sessionStorage.getItem('appId'));
						data.append("userName", $rootScope.currentUser.userName);
						data.append("selectedMonth",selectedMonth)
						console.log('selectedMonth' , selectedMonth)
					console.log('data for upload' , data)
						if (data != undefined && !$scope.duplicateUpload)
							$scope.UploadFileIndividual(data);
						else if($scope.duplicateUpload){
							 $scope.dataLoading = false;
							 $rootScope.buttonClicked ="Remove the duplicate files already selected";
					         $rootScope.showModal = !$rootScope.showModal;
					         $rootScope.contentColor = "#dd4b39";	
							
						}
					};
					$scope.UploadFileIndividual = function(data){

						var config = {
							transformRequest : angular.identity,
							transformResponse : angular.identity,
							headers : {
								'Content-Type' : undefined
							}
						}

						$http.post( appConstants.serverUrl+'/api/uploadfile', data, config).then(
								function(response) {
									$scope.dataLoading = false;
									if(response.data!="Files Uploaded Successfully"){
										  $rootScope.contentColor = "#dd4b39";
										  $rootScope.buttonClicked =response.data;
								          $rootScope.showModal = !$rootScope.showModal;	
									}
									else{							
										  $state.go("app", {appId:$window.sessionStorage.getItem('appId')}, {reload: true}); 
										  $rootScope.contentColor = "#78b266";
										  $rootScope.buttonClicked =response.data;
								          $rootScope.showModal = !$rootScope.showModal;	
									}
								}, function(response) {
									 $rootScope.buttonClicked =response.data;
							         $rootScope.showModal = !$rootScope.showModal;
							         $rootScope.contentColor = "#dd4b39";
								});

					}

					$scope.appServerFolder = function() {
						
						$scope.sessionApp=$window.sessionStorage.getItem('appId');
						if ($scope.sessionApp != undefined
									|| $scope.sessionApp != null) {
						var url =  appConstants.serverUrl+"/api/serverfolders/" + $scope.sessionApp;
						console.log('app server folder url:' , url)
						var data = new FormData();

						$http.get(url).then(function(response) {
							$scope.serverFoldersResult = response.data;
							console.log($scope.serverFoldersResult)
						}, function(response) {

							$scope.serverFoldersResult = response.data;
							 $("#upload").show();
						});
}
					};
					$scope.init = function() {
						$scope.inituser();

						var url =  appConstants.serverUrl+"/api/applications/"

						var data = new FormData();

						$http.get(url).then(function(response) {
							$scope.applications = response.data;
							
						}, function(response) {

							$scope.applications = response.data;
							
						});
					};

					$scope.showAlert = function(event) {
						alert(event.target.id);
					}
					$scope.init();

				} ]);


angular.module('cdrApp').directive('multiselectDropdown', [function() {
    return function(scope, element, attributes) {
            
        // Below setup the dropdown:
        
        element.multiselect({
            buttonClass : 'btn btn-small',
            buttonWidth : '200px',
            buttonContainer : '<div class="btn-group" />',
            maxHeight : 200,
            enableFiltering : true,
            enableCaseInsensitiveFiltering: true,
            buttonText : function(options) {
                if (options.length == 0) {
                    return element.data()['placeholder'] + ' <b class="caret"></b>';
                } else if (options.length > 1) {
                    return _.first(options).text 
                    + ' + ' + (options.length - 1)
                    + ' more selected <b class="caret"></b>';
                } else {
                    return _.first(options).text
                    + ' <b class="caret"></b>';
                }
            },
            // Replicate the native functionality on the elements so
            // that angular can handle the changes for us.
            onChange: function (optionElement, checked) {
                optionElement.removeAttr('selected');
                if (checked) {
                    optionElement.prop('selected', 'selected');
                }
                element.change();
            }
            
        });
        // Watch for any changes to the length of our select element
        scope.$watch(function () {
            return element[0].length;
        }, function () {
            element.multiselect('rebuild');
        });
        
        // Watch for any changes from outside the directive and refresh
        scope.$watch(attributes.ngModel, function () {
            element.multiselect('refresh');
        });
        
        // Below maybe some additional setup
    }
}]);

'use strict';
angular.module('cdrApp').run([
			'$state','$http','$rootScope','globalServices','userService',function ( $state,$http,$rootScope,globalServices,userService) {
				
	   $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams) {	
	    var requireLogin = toState.data.requireLogin;
	    $rootScope.currentUser=userService.getCurrentUser();	
			
	    if ((requireLogin && typeof $rootScope.currentUser === 'undefined')|| (typeof globalServices.isUserTokenAvailable()=== 'undefined'))
		 {	 
	    	$rootScope.isProfilePage=false;
	        return $state.go('login');	        
	    }else{	    	
	    	$rootScope.isProfilePage=true;
	    }	  
	    
	  });
	   

	}]);

angular.module('cdrApp').run([
                  			'$state','$http','$rootScope','$timeout','$document','AuthenticationService',function ( $state,$http,$rootScope,$timeout,$document,AuthenticationService) {
                				
    console.log('starting run');

    // Timeout timer value 8 minutes automatically logout
    var TimeOutTimerValue = 480000;

    // Start a timeout
    var TimeOut_Thread = $timeout(function(){ LogoutByTimer() } , TimeOutTimerValue);
    var bodyElement = angular.element($document);

    angular.forEach(['keydown', 'keyup', 'click', 'mousemove', 'DOMMouseScroll', 'mousewheel', 'mousedown', 'touchstart', 'touchmove', 'scroll', 'focus'], 
    function(EventName) {
         bodyElement.bind(EventName, function (e) { TimeOut_Resetter(e) });  
    });

    function LogoutByTimer(){
        console.log('Logout');
        ///////////////////////////////////////////////////
        /// redirect to another page(eg. Login.html) here
        ///////////////////////////////////////////////////
       // Timeout timer value 8 minutes automatically logout
        AuthenticationService.ClearCredentials();  
   
    }

    function TimeOut_Resetter(e){
    
        /// Stop the pending timeout
        $timeout.cancel(TimeOut_Thread);

        /// Reset the timeout
        TimeOut_Thread = $timeout(function(){ LogoutByTimer() } , TimeOutTimerValue);
    }

}]);

angular.module('cdrApp').controller(
		'jamesWebController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
					$scope.homepageContent = "James Web dashboard page";
					$scope.batchFolders=[];
					$scope.attachmentsList=[];
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					
					$scope.init = function() {
						$scope.inituser();
						
						var batchFoldersUrl=appConstants.serverUrl+"/admin/batchFolders/";
						
						$http.get(batchFoldersUrl).then(function(response) {
                        	console.log(' allBatchFolders ' , response)
                        	$scope.batchFolders=response.data;	
                        });
					}
					
					$scope.getAttachments=function(folder){
						var selectedFolder="C:/Users/ssc.rbapanapalli/Desktop/Test/1";
						
						var getAttachmentsUrl=appConstants.serverUrl+"/admin/getAttachments/?selectedFolder="+selectedFolder;
						console.log(getAttachmentsUrl)
						
						$http.get(getAttachmentsUrl).then(function(response) {
                        	console.log(' Attachments ' , response)
                        	$scope.attachmentsList=response.data;	
                        });
						
					}
					
					
		} ]);


angular.module('cdrApp').controller(
		'activityTrackController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
					$scope.homepageContent = "Activity Track Admin page";
					$scope.pageSize = 10;
					$scope.activityMapping=[];
					$scope.allActivities=[];
					$scope.activityDetails={};
					$scope.updateActivity="false";
					$scope.addApplication="false";
					$scope.showAddActivity="true";
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					
					$scope.init = function() {
						$scope.inituser();
						var activitySettingsUrl=appConstants.serverUrl+"/activity/getAllActivies/";
						$http.get(activitySettingsUrl).then(function(response) {
							
							$scope.allActivities=response.data;	
							console.log(' allActivities' , $scope.allActivities)
					});
						
					}
					
					$scope.Cancel = function() {
						$scope.updateActivity="false";
						$scope.addApplication="false";
						$scope.showAddActivity="true";
					}
					

					$scope.addNewActivity = function() {
						$scope.updateActivity="false";
						$scope.addApplication="true";						
						$scope.showAddActivity="false";
					}
					
					
					//Update Activity Details
					
					
					$scope.updateActivityDetails=function(id,activityName){
						
							$scope.updateActivity="true";
							$scope.selectedUpdateId=id;
							$scope.addApplication="false";
							$scope.showAddActivity="false";
							$scope.activityName=activityName;
							console.log('selectedUpdateId:', id)
							var viewDetailsUrl=appConstants.serverUrl+"/activity/getselectedActivityDetails/"+id;
							
							$http.get(viewDetailsUrl).then(function(response) {
								$scope.selectedActivityDetails = response.data;
								//$scope.appFoldersSaved=angular.copy($scope.appFolderDetails);
								console.log('appFolderDetails result', $scope.selectedActivityDetails);
								
							}, function(response) {
								$rootScope.buttonClicked = response.data;
								$rootScope.showModal = !$rootScope.showModal;
								$rootScope.contentColor = "#dd4b39";
								
								
							});
						}
					$scope.setUpdatedActDetails=function(id,activityName){
						
						console.log('Update activity id:' , id)
						var config = {
								transformRequest : angular.identity,
								transformResponse : angular.identity,
								headers : {
									'Content-Type' : undefined
								}
							}
					var updateActUrl=appConstants.serverUrl+"/activity/updateActivity/";
					var data = new FormData();
					data.append("id" ,id);
					data.append("activityName", activityName)
					data.append("updatedBy", $rootScope.currentUser.userName);
					console.log('conform delete to post' , data)
					console.log(id)
					console.log($rootScope.currentUser.userName)
		
					
					$http.post(updateActUrl,data,config)
					.success(function (response) {  
		    
					//console.log(response)
					if(response=="Activity Updated Successfully"){
						$rootScope.buttonClicked = response;
						$rootScope.showModal = !$rootScope.showModal;
						$rootScope.contentColor = "#78b266";
						$state.go("activityTrackAdmin", {} , {reload: true} );
					}
					else{
						$rootScope.buttonClicked = response;
						$rootScope.showModal = !$rootScope.showModal;
						$rootScope.contentColor = "#dd4b39";
					}
					 });
					
					
						
					};
					
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
					//Delete Activity
					$scope.showDialog = function(flag) {
				        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
				      };
				
				$scope.deleteActivityDetails=function(id,activityName){
					console.log('in  delete Application')
					$scope.confirmationDialogConfig = {
						      title: "DELETE Application",
						      message: "Are you sure you want to Delete Activity?",
						      buttons: [{
						        label: "Delete",
						        action: "Delete"
						      }],
						      id:id,
						      activityName:activityName
						    };
						    $scope.showDialog(true);
					
					
				}
				
				$scope.confirmActivityDelete=function(id){
					
					if($rootScope.currentUser!=undefined){
						
						console.log('delete activity id:' , id)
						var config = {
								transformRequest : angular.identity,
								transformResponse : angular.identity,
								headers : {
									'Content-Type' : undefined
								}
							}
					var url=appConstants.serverUrl+"/activity/deleteActivity/";
					var data = new FormData();
					data.append("id" ,id);
					data.append("updatedBy", $rootScope.currentUser.userName);
					console.log('conform delete to post' , data)
					console.log(id)
					console.log($rootScope.currentUser.userName)
		
					$http.post(url,data,config).then(
							function(response){
								console.log('in delete Activity post method')
								  $('body').removeClass().removeAttr('style');
								  $('.modal-backdrop').remove(); 
								  $scope.showDialog(false);
								      $rootScope.buttonClicked = response.data;
									  $rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#78b266";
									  $state.go("activityTrackAdmin", {} , {reload: true} );
									 
							},function(response){
								
								  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
								      $rootScope.buttonClicked = response.data;
									  $rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";
							});							
					}
				}
		
					
					//Add new row for Activity
					$scope.Add = function () {
		            	console.log('Activity Name:' , $scope.activityName)
		                //Add the new item to the Array.
		            	if($scope.activityName==null || $scope.activityName==undefined ||$scope.activityName=="" ){
		            		$rootScope.buttonClicked = "Please provide activity";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
		            	}else{
		               	                
		                $scope.activityMapping.push($scope.activityName);
		            	}
		            	console.log($scope.activityMapping)
		                //Clear the TextBoxes.
		                $scope.activityName = "";
		                
		            	
		            };

		            $scope.Remove = function (index) {
		                //Find the record using Index from Array.
		                var name = $scope.activityMapping[index].activityName;
		                if ($window.confirm("Do you want to delete activity name: " + name)) {
		                    //Remove the item from Array using Index.
		                    $scope.activityMapping.splice(index, 1);
		                }
		            }
		            
		            $scope.Reset=function(){
		            	
	            		
	            		$scope.selectedActivityDetails.activityName="";
	            		
	            		
		            }
					
					$scope.activityConfig=function(){
						
		                $scope.activityMapping.push($scope.activityName);
				                
				             var url =  appConstants.serverUrl+"/activity/setActivityConfig/";
				             
				             var config = {
										transformRequest : angular.identity,
										transformResponse : angular.identity,
										headers : {
											'Content-Type' : undefined
										}
									}
				             console.log(dataObj)
				             
				             var dataObj = {
				            	 	
				            	 	 activityMapping :$scope.activityMapping,
				            		 userName :$rootScope.currentUser.userName
				     		};
				             console.log(dataObj)
				             
				             if($scope.activityName==null || $scope.activityName==undefined || $scope.activityName==""){
				            	 $rootScope.buttonClicked = "Please provide Activity Name";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";
				             }
				             else{
				             $http.post(url,dataObj,
										{
											headers : {
												'Accept' : 'application/json',
												'Content-Type' : 'application/json'
											}
										})
			                .success(function (response) {  
			                
									/*console.log(response)*/
			                	if(response=="Activity Configuration saved successfully"){
									$rootScope.buttonClicked = response;
									$rootScope.showModal = !$rootScope.showModal;
									$rootScope.contentColor = "#78b266";
									$state.go("activityTrackAdmin", {} , {reload: true} );
			                	}else{
			                		$rootScope.buttonClicked = response;
									$rootScope.showModal = !$rootScope.showModal;
									$rootScope.contentColor = "#dd4b39";
			                		
			                	}
							});
			              }	
				            		$scope.activityMapping=[];
				            		$scope.activityName = "";
				            		
			            }
					
					
		} ]);

// Activity Tracker Controller for User - activityTrackUserController

angular.module('cdrApp').controller(
		'activityTrackUserController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
					$scope.userRoles=[];
					$scope.allRoles=[];
					$scope.allActivity=[];
					$scope.userActivity=[];
					$scope.pageSize = 10;
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					
					$scope.init=function(){
						$scope.userName="";
						$scope.userStatus=true;
                      
                        var activiActivityUrl=appConstants.serverUrl+"/activity/getActiveActivity/";
                        var userActivityUrl=appConstants.serverUrl+"/activity/getUserActivity/";
                       
                        $http.get(activiActivityUrl).then(function(response) {
                        	console.log(' allActivity ' , response)
                        	$scope.allActivityConstant=angular.copy(response.data);
                        	$scope.allActivity=response.data;	
                        	
                        });
                        
                        $http.get(userActivityUrl).then(function(response) {
                        	console.log(' user Activity ' , response)
                        	//$scope.allRolesConstant=angular.copy(response.data);
                        	$scope.userActivityList=response.data;	
                        	
                        });
                        
                        
							$scope.inituser();
						}
					
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
				  $scope.showDialog = function(flag) {
					        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
					      };
							
					
					// To move items from allRoles to userRoles
					$scope.moveItem = function(items, from, to) {

				        console.log('Move items: ' + items + ' From: ' + from + ' To: ' + to)
				        console.log('in user init role constant in move', $scope.allRolesConstant)
				        //Here from is returned as blank and to as undefined

				        items.forEach(function(item) {
				        	console.log('in move item method' + item)
				          var idx = from.indexOf(item);
				        	console.log('selected index:' +idx)
				          if (idx != -1) {
				              from.splice(idx, 1);
				              to.push(item);      
				          }
				        });
				    };

				        
			            $scope.Reset=function(){
			            	//$state.go("userSettings",{},{reload:true});
			            	
			            	$scope.allActivity=angular.copy($scope.allActivityConstant);
			            	$scope.userActivity=[];
								
			            }
			            
			          
			            $scope.userActivityConfig=function(){
							
							var addUserActivityUrl=appConstants.serverUrl+"/activity/addUserActivity/";
							console.log($scope.userActivity)
							var data = {
				            		 
									activityTracker : $scope.userActivity,
									userName :$rootScope.currentUser.userName
				     		};
							console.log(data)
												
							if( $scope.userActivity==null ||  $scope.userActivity==undefined ||  $scope.userActivity==""){
								$rootScope.buttonClicked = "Please select User Activity";
								$rootScope.showModal = !$rootScope.showModal;
								  $rootScope.contentColor = "#dd4b39";
								
							}else{
								//console.log('in else method')
								
								 $http.post(addUserActivityUrl,data,
											{
												headers : {
													'Accept' : 'application/json',
													'Content-Type' : 'application/json'
												}
											})
								.success(function (response) {  
		                
								//console.log(response)
								$rootScope.buttonClicked = response;
								$rootScope.showModal = !$rootScope.showModal;
								$rootScope.contentColor = "#78b266";
								 $state.go("activityTrackUser", {} , {reload: true} );
								 });
							
							}
						};
						
						$scope.startTask =function(id){
							   
							if($rootScope.currentUser!=undefined){
								
								var config = {
										transformRequest : angular.identity,
										transformResponse : angular.identity,
										headers : {
											'Content-Type' : undefined
										}
									}
								var startTaskUrl=appConstants.serverUrl+"/activity/startActivity/";
							var data = new FormData();
							
							data.append("userName", $rootScope.currentUser.userName);
							data.append("id", id);
							console.log(data)
				
							$http.post(startTaskUrl,data,config).then(
									function(response){
										
										  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
										      $rootScope.buttonClicked = response.data;
											  $rootScope.showModal = !$rootScope.showModal;
											  $rootScope.contentColor = "#78b266";
											  $state.go("activityTrackUser",{}, {reload: true}); 
											
									},function(response){
										
										  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
										      $rootScope.buttonClicked = response.data;
											  $rootScope.showModal = !$rootScope.showModal;
											  $rootScope.contentColor = "#dd4b39";
									});							
							}
							
						}
						
						$scope.stopTask =function(id){
							   
							if($rootScope.currentUser!=undefined){
								
								var config = {
										transformRequest : angular.identity,
										transformResponse : angular.identity,
										headers : {
											'Content-Type' : undefined
										}
									}
								var stopTaskUrl=appConstants.serverUrl+"/activity/stopActivity/";
							var data = new FormData();
							
							data.append("userName", $rootScope.currentUser.userName);
							data.append("id", id);
							console.log(data)
				
							$http.post(stopTaskUrl,data,config).then(
									function(response){
										
										  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
										      $rootScope.buttonClicked = response.data;
											  $rootScope.showModal = !$rootScope.showModal;
											  $rootScope.contentColor = "#78b266";
											  $state.go("activityTrackUser",{}, {reload: true}); 
											
									},function(response){
										
										  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
										      $rootScope.buttonClicked = response.data;
											  $rootScope.showModal = !$rootScope.showModal;
											  $rootScope.contentColor = "#dd4b39";
									});							
							}
							
						}
						
			            
				} ]);


angular.module('cdrApp').controller(
        'monitoringController',
        
        [
                     '$scope',
                     '$state',
                     '$rootScope',
                     '$window',
                     '$q',
                     '$http',
                      'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
                     function($scope, $state, $rootScope, $window, $q,
                                   $http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
                            $scope.pageSize = 5;
                            $scope.monitorDataList=[];
                            $scope.inituser = function() {
                                   var data = globalServices.isUserTokenAvailable();
                                   if (data == null || data == undefined) {
                                          $rootScope.isProfilePage = false;
                                          $state.go("login");
                                   } else {
                                          $rootScope.currentUser = userService.getCurrentUser();
                                          if ($rootScope.currentUser != undefined
                                                        || $rootScope.currentUser != null) {
                                                 $rootScope.isProfilePage = true;
                                          } else {
                                                 $rootScope.isProfilePage = false;
                                                 $state.go("login");
                                          }

                                   }
                            }
                            
                            $scope.init=function(){
                                   $scope.userName="";
                                   
                                   var url= appConstants.serverUrl+"/reports/getMonitoring/";
                                   $http.get(url).success(function(response){ 
                                          $scope.monitorDataList = response; 
                                          console.log('test monitoring data' , response)//ajax request to fetch data into $scope.data
                                   })
                                   .error(function(data,status){    
                                        console.error('Fail to load data', status, data);    
                                        $scope.employees = { };     
                                      });    
                                   
                                   $scope.inituser();
                                   }
                            
                            $scope.sort = function(keyname){
                                   $scope.sortKey = keyname;   //set the sortKey to the param passed
                                   $scope.reverse = !$scope.reverse; //if true make it false and vice versa
                                   
                                   
                            }
                       $scope.showDialog = function(flag) {
                                    jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
                                  };
                                          
                     
                     } ]);



angular.module('cdrApp').controller(
		'reportsController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
					$scope.userId="";
					$scope.showTable=false;
					$scope.empdDetailsList=[];	
					$scope.dateRange="";
					$scope.myReportList=[];
					$scope.exportHref="";
					$scope.pageSize = 10;
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					
					$scope.init=function(){
						$scope.inituser();
						$scope.myReportList=[];
						
						var getEmpDetailsUrl=appConstants.serverUrl+"/reports/getEmpDetails/";
						console.log(getEmpDetailsUrl)
						
						$http.get(getEmpDetailsUrl).then(function(response) {
							
							$scope.empdDetailsList=response.data;
							
							console.log('employee  length', $scope.empdDetailsList.length)
							console.log( response)
							console.log('Employee Details List', $scope.empdDetailsList)
							
						}, function(response) {
							$rootScope.buttonClicked = response.data;
							$rootScope.showModal = !$rootScope.showModal;
							$rootScope.contentColor = "#dd4b39";
							
							
						});
						
						}
					
					$scope.getMyReport=function(dateRange){
						$scope.showTable=true;
						console.log(dateRange)
						var selectedRange=dateRange;
						var getMyReportUrl=appConstants.serverUrl+"/reports/getMyReport/?selectedRange="+selectedRange;
						console.log(getMyReportUrl)
						$http.get(getMyReportUrl).then(function(response) {
                        	console.log(' My Report ' , response)
                        	$scope.myReportList=response.data;	
                        });
						
					};
					
					$scope.getTeamReport=function(dateRange){
						$scope.showTable=true;
						var selectedRange=dateRange;
						var getTeamReportUrl=appConstants.serverUrl+"/reports/getTeamReport/?selectedRange="+selectedRange;
						console.log(getTeamReportUrl)
						$http.get(getTeamReportUrl).then(function(response) {
                        	console.log(' Team Report ' , response)
                        	$scope.myReportList=response.data;	
                        });
					};
					
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
				  $scope.showDialog = function(flag) {
					        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
					      };
							
				
				} ]);

angular.module('cdrApp').controller(
		'uploadEmpDataController',
		[
				'$scope',
				'$rootScope',
				'$http',
				'$window',
				'$state',
				'appConstants','userService','globalServices','AuthenticationService',
				function($scope, $rootScope, $http, $window, $state,
						appConstants,userService,globalServices,AuthenticationService) {
					$scope.myFile="";
					$rootScope.isProfilePage = false;
					$rootScope.currentUser = {
						
					};

					$scope.fileList = [];
					
			
					$scope.dataLoading = false;
			
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					$scope.logout = function () {		
						   AuthenticationService.ClearCredentials();  
						   $rootScope.isProfilePage=false;
						 
					   }


					$scope.doUploadFile = function() {
						$scope.dataLoading = true;						
						var file = $scope.myFile;
						if(file==undefined){
							  $scope.dataLoading = false;	
							  $rootScope.contentColor = "#dd4b39";
							  $rootScope.buttonClicked ="Select the Appropriate Employee Details To Upload";
					          $rootScope.showModal = !$rootScope.showModal;	
						}else{
					
						if($rootScope.currentUser.userName!=undefined && file!=undefined){
							var fd = new FormData();
					        fd.append('file', file);
					   				
							 fd.append("userName", $rootScope.currentUser.userName);
							
					        $http.post(appConstants.serverUrl+'/api/uploadEmpfile', fd, {
					            transformRequest: angular.identity,
					            headers: {'Content-Type': undefined}
					        })
					        .success(function(response){
					        	
					        	$scope.dataLoading = false;
								if(response.message=="Employee Details Uploaded Successfully"){
									  $rootScope.contentColor = "#78b266";
									  $rootScope.buttonClicked =response.message;
							          $rootScope.showModal = !$rootScope.showModal;	
								}
								else{					
									 
									  $rootScope.contentColor = "#dd4b39";
									  $rootScope.buttonClicked =response.data+" : "+response.message;
							          $rootScope.showModal = !$rootScope.showModal;	
								}
					        })
					        .error(function(){
					        });
						}					  
							//fileUpload.uploadFileToUrl(file,$rootScope.currentUser.userName);
						else{
							 $scope.dataLoading = false;
							 $rootScope.showModal = !$rootScope.showModal;
					         $rootScope.contentColor = "#dd4b39";	
							
						}
					};
					}
					
					$scope.downloadSampleTemplate=function(fileName){
						//\EMPLOYEE_DATA.xlsx
						//alert("fileName"+fileName)
						 $window.location = appConstants.serverUrl+'/admin/download?name=' + fileName;
						    window.open(downloadPath, '_blank', '');
					
					}
					
					$scope.init = function() {
						$scope.inituser();
					};

				
					$scope.init();

				} ]);

angular.module('cdrApp').controller(
		'empSettingsController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','AuthenticationService','$stateParams','anchorSmoothScroll','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,AuthenticationService,$stateParams,anchorSmoothScroll,$location,$filter) {
					$scope.homepageContent = "settings dashboard page";
					$scope.allEmployeeDetails=[];	
					$scope.employeeCreate="false";
					$scope.userName="";		
					$scope.userId="";
					$scope.employee={};
					$scope.pageSize = 10;
					$scope.inituser = function() {
						var data = globalServices.isUserTokenAvailable();
						if (data == null || data == undefined) {
							$rootScope.isProfilePage = false;
							$state.go("login");
						} else {
							$rootScope.currentUser = userService.getCurrentUser();
							if ($rootScope.currentUser != undefined
									|| $rootScope.currentUser != null) {
								$rootScope.isProfilePage = true;
							} else {
								$rootScope.isProfilePage = false;
								$state.go("login");
							}

						}
					}
					
					$scope.init=function(){
						$scope.employee.isRm=false;
						$scope.employee.activeIndicator=false;
						$scope.userName="";					                   	
                        var allEmployeeDetailsUrl =  appConstants.serverUrl+"/admin/getAllEmployees/";
                   
                     
                      var url =  appConstants.serverUrl+"/login/getUserAuthDetails/"+$window.sessionStorage.getItem('userToken');
						
						$http.get(url).then(function(response) {						
										
										$scope.roles=response.data.roles;	
																		
								});
							
                        $http.get(allEmployeeDetailsUrl).then(function(response){
                        	$scope.allEmployeeDetails=angular.copy(response.data);
                        	console.log($scope.allEmployeeDetails);
                        });               
                        
							$scope.inituser();
						}
					
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
					
					$scope.Cancel = function(){
						$scope.employeeCreate = "false";  
						
						
						
					}
					$scope.employeeConfig=function(employee){
						console.log(employee)
						var employeeConfigUrl=appConstants.serverUrl+"/admin/setEmployeeConfiguration/";
						var data = {
								emailId : employee.emailId,
								isAdmin : employee.isAdmin,
								empId : employee.empId,
								id : employee.id,
			            		 empName : employee.empName,
			            		 rmId : employee.rmId,
			            		 rmName :employee.rmName,
			            		 isRm :employee.isRm,
			            		 activeIndicator :employee.activeIndicator,
			            		 createdBy :$rootScope.currentUser.userName,
			            		 updatedBy :$rootScope.currentUser.userName
			     		};
						//console.log(data)
											
						if(employee.empId==null || employee.empId==undefined || employee.empId==""){
							$rootScope.buttonClicked = "Please provide Employee Id";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if( employee.emailId==null || employee.emailId==undefined ||  employee.emailId==""){
							$rootScope.buttonClicked = "Please provide Employee Email Id";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if( employee.empName==null || employee.empName==undefined ||  employee.empName==""){
							$rootScope.buttonClicked = "Please provide Employee Name";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if(employee.rmId==null ||employee.rmId==undefined ||employee.rmId==""){
							$rootScope.buttonClicked = "Please provide Reporting Manager Id";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if( employee.rmName==null ||  employee.rmName==undefined ||  employee.rmName==""){
							$rootScope.buttonClicked = "Please provide Reporting Manager Name";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if((typeof  employee.isRm !== 'boolean' &&  employee.isRm !== true)||(typeof  employee.isRm !== 'boolean' &&  employee.isRm !== false)){
								$rootScope.buttonClicked = "Please Check Boolean value Reporting Manager or Not";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if((typeof  employee.activeIndicator !== 'boolean' &&  employee.activeIndicator !== true)||(typeof  employee.activeIndicator !== 'boolean' &&  employee.activeIndicator !== false)){
							$rootScope.buttonClicked = "Please Check Boolean value active or Not";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else{
							//console.log('in else method')
							
							 $http.post(employeeConfigUrl,data,
										{
											headers : {
												'Accept' : 'application/json',
												'Content-Type' : 'application/json'
											}
										})
							.success(function (response) {  
	                
							//console.log(response)
							$rootScope.buttonClicked = response;
							$rootScope.showModal = !$rootScope.showModal;
							$rootScope.contentColor = "#78b266";
							 $state.go("empSettings", {} , {reload: true} );
							 });
						
						}
					};
					
					$scope.employeeUpdate=function(rowId,methodName){
						
						$scope.emailId="";
						
						$scope.employeeCreate="true";
						
						if(methodName=="updateEmployee"){
							for(var i=0; i<$scope.allEmployeeDetails.length; i++) {									
								   if(rowId==$scope.allEmployeeDetails[i].emailId){
									   $scope.employeeExists="true";
									   $scope.inputReadOnly="true";
									   $scope.employee=$scope.allEmployeeDetails[i];
									   $scope.employee.empId=$scope.allEmployeeDetails[i].empId;
									   $scope.employee.emailId=$scope.allEmployeeDetails[i].emailId;									  
									   break;
									   
								   }
							}
							
						}
						if(methodName=="checkEmployeer"){
							console.log(rowId)
							var emailId=rowId;
							for(var i=0; i<$scope.allEmployeeDetails.length; i++) {	
								console.log($scope.allEmployeeDetails[i].emailId)
								   if(emailId==$scope.allEmployeeDetails[i].emailId){
									   $scope.employeeExists="true";
									   $scope.inputReadOnly="true";
									   $scope.employee=$scope.allEmployeeDetails[i];
									   $scope.employee.empId=$scope.allEmployeeDetails[i].empId;
									   $scope.employee.emailId=$scope.allEmployeeDetails[i].emailId;									  
									   break;
								   }
								   //$scope.empId=empId;
								   //$scope.allEmployeeDetails=[];			  
								  
								  $scope.employeeExists="false";
								  $scope.inputReadOnly="";
								   
							}
							
						}
					}
					
					  $scope.showDialog = function(flag) {
					        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
					      };
							
					
					$scope.employeeDelete=function(id,empName){
						console.log('in  delete Employee')
						$scope.confirmationDialogConfig = {
							      title: "DELETE EMPLOYEE",
							      message: "Are you sure you want to Delete Employee?",
							      buttons: [{
							        label: "Delete",
							        action: "Delete"
							      }],
							      id:id,
							      empName:empName
							    };
							    $scope.showDialog(true);
					
					}
					$scope.confirmEmployeeDelete=function(id){
						
						if($rootScope.currentUser!=undefined){
							
							var config = {
									transformRequest : angular.identity,
									transformResponse : angular.identity,
									headers : {
										'Content-Type' : undefined
									}
								}
						var url=appConstants.serverUrl+"/admin/deleteEmployee/";
						var data = new FormData();
						data.append("id" ,id);
						data.append("updatedBy", $rootScope.currentUser.userName);
						console.log('confirm delete to post' , data)
						console.log(id)
						console.log($rootScope.currentUser.userName)
			
						$http.post(url,data,config).then(
								function(response){
									console.log('in delete user post method')
									  $('body').removeClass().removeAttr('style');
									  $('.modal-backdrop').remove(); 
									  $scope.showDialog(false);
									      $rootScope.buttonClicked = response.data;
										  $rootScope.showModal = !$rootScope.showModal;
										  $rootScope.contentColor = "#78b266";
										  $state.go("empSettings", {} , {reload: true} );
										  //$state.go("app", {appId:$window.sessionStorage.getItem('appId')}, {reload: true}); 
										
								},function(response){
									
									  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
									      $rootScope.buttonClicked = response.data;
										  $rootScope.showModal = !$rootScope.showModal;
										  $rootScope.contentColor = "#dd4b39";
								});							
						}
					}
					
					//to check user availability in db
					$scope.checkEmployee=function(){
						console.log('in checkEmployee method')
						$scope.employeeUpdate($scope.employee.emailId,"checkEmployeer");
					}
					
				        
			            $scope.Reset=function(){
			            	$scope.employee={};		
			            	$scope.employee.emailId='';
			            	 $scope.employeeExists="false";
							  $scope.inputReadOnly="";
			            }
			            $scope.init();
			            
				} ]);

angular.module('cdrApp').directive('fileModel', [ '$parse', function($parse) {
    return {
        restrict : 'A',
        link : function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function() {
                scope.$apply(function() {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
} ]);


/*'use strict';
angular.module('cdrApp').run([
			'$state','$http','$rootScope','globalServices','userService',function ( $state,$http,$rootScope,globalServices,userService) {
				
	   $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams) {	
	    var requireLogin = toState.data.requireLogin;
	    $rootScope.currentUser=userService.getCurrentUser();	
			
	    if ((requireLogin && typeof $rootScope.currentUser === 'undefined')|| (typeof globalServices.isUserTokenAvailable()=== 'undefined'))
		 {	 
	    	$rootScope.isProfilePage=false;
	        return $state.go('login');	        
	    }else{	    	
	    	$rootScope.isProfilePage=true;
	    }	  
	    
	  });
	   

	}]);*/
