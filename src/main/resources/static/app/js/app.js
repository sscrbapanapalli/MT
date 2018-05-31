/* @author Basker Ammu, Ramesh Kumar B
 * Url Routing configuration
 * $stateProvider,$urlRouterProvider
 */

'use strict';
angular.module("myTimeApp", [ "ui.router",'appConfigApp' ,'commonServiceApp','angularUtils.directives.dirPagination']);

'use strict';
angular.module('myTimeApp').config(
		[ '$stateProvider', '$urlRouterProvider',
				function($stateProvider, $urlRouterProvider) {
				
					$urlRouterProvider.otherwise("/home");
					$stateProvider.state("home", {
						url : '/home',
						templateUrl :'view/activityTrackUser.html',
						controller :"activityTrackUserController",
						controllerAs : "activityTrackUserController",
						data:{requireLogin:true}
					})
					.state("activityTrackAdmin",{
						url : '/activityTrackAdmin',
						templateUrl :'view/activityTrackAdmin.html',
						controller :"activityTrackController",
						controllerAs : "activityTrackController",
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



angular.module('myTimeApp').controller(
		'activityTrackController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','$stateParams','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,$stateParams,$location,$filter) {
					$scope.homepageContent = "Activity Track Admin page";
					$scope.pageSize = 10;
					$scope.activityMapping=[];
					$scope.allActivities=[];
					$scope.activityDetails={};
					$scope.updateActivity="false";
					$scope.addApplication="false";
					$scope.showAddActivity="true";
					
						
					 $scope.inituser = function() {
						 $rootScope.isProfilePage=true;    
						 $rootScope.currentUser = userService.getCurrentUser();
	                    	if ($rootScope.currentUser==undefined)
							 { 
								if(!appConstants.localServer){
									$rootScope.currentUser =userService.getWindowsCurrentUserName();								
								}else{
									$rootScope.currentUser =userService.getLocalTestName();									
								}								
						    }else
						    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName
					}
					
					$scope.init = function() {
						$scope.inituser();
						var activitySettingsUrl=appConstants.serverUrl+"/activity/getAllActivies/";
						$http.get(activitySettingsUrl).then(function(response) {
							
							$scope.allActivities=response.data;	
						
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
							
							var viewDetailsUrl=appConstants.serverUrl+"/activity/getselectedActivityDetails/"+id;
							
							$http.get(viewDetailsUrl).then(function(response) {
								$scope.selectedActivityDetails = response.data;
								//$scope.appFoldersSaved=angular.copy($scope.appFolderDetails);
								
								
							}, function(response) {
								$rootScope.buttonClicked = response.data;
								$rootScope.showModal = !$rootScope.showModal;
								$rootScope.contentColor = "#dd4b39";
								
								
							});
						}
					$scope.setUpdatedActDetails=function(id,activityName,activityType){
					
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
					data.append("activityType", activityType)
					data.append("updatedBy", $rootScope.currentUser.userName);
					
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
					
		
					$http.post(url,data,config).then(
							function(response){
							
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
		            
		                //Add the new item to the Array.
		            	if($scope.activityName==null || $scope.activityName==undefined ||$scope.activityName=="" ||
		            	   $scope.activityType==null || $scope.activityType==undefined ||$scope.activityType==""){
		            		$rootScope.buttonClicked = "Please provide activity Details";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
		            	}else{
		            		var activitySettingsObj = {};
		            		activitySettingsObj.activityName=$scope.activityName;
		            		activitySettingsObj.activityType=$scope.activityType;
		               	                
		                $scope.activityMapping.push(activitySettingsObj);
		                //Clear the TextBoxes.
		                $scope.activityName = "";
		                $scope.activityType="";
		            	}
		            	
		               /* //Clear the TextBoxes.
		                $scope.activityName = "";
		                $scope.activityType="";*/
		            	
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
		            	
		            	$scope.activityMapping=[];
		            	$scope.activityName = "";
		                $scope.activityType="";
	            		$scope.selectedActivityDetails.activityName="";
	            		$scope.selectedActivityDetails.activityType="";
	            		
	            		
		            }
					
					$scope.activityConfig=function(){
						
				                
				             var url =  appConstants.serverUrl+"/activity/setActivityConfig/";
				             
				             var config = {
										transformRequest : angular.identity,
										transformResponse : angular.identity,
										headers : {
											'Content-Type' : undefined
										}
									}
				             
				            
				            
				            
				             
				             if($scope.activityName==null || $scope.activityName==undefined || $scope.activityName=="" ||
				            		 $scope.activityType==null || $scope.activityType==undefined ||$scope.activityType==""	 ){
				            	 $rootScope.buttonClicked = "Please provide Activity Details";
									$rootScope.showModal = !$rootScope.showModal;
									  $rootScope.contentColor = "#dd4b39";
				             }
				             else{
				            	 var activitySettingsObj = {};
				            		activitySettingsObj.activityName=$scope.activityName;
				            		activitySettingsObj.activityType=$scope.activityType;
									
					                $scope.activityMapping.push(activitySettingsObj);
					                
					                var dataObj = {
						            	 	
						            	 	 activityMapping :$scope.activityMapping,
						            		 userName :$rootScope.currentUser.userName
						     		};
						             console.log(dataObj)
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
									
									$scope.activityMapping=[];
				            		$scope.activityName = "";
				            		$scope.activityName = "";
			                	}else{
			                		$rootScope.buttonClicked = response;
									$rootScope.showModal = !$rootScope.showModal;
									$rootScope.contentColor = "#dd4b39";
			                		
			                	}
							});
			              }	
				            		
				            		
			            }
					
					
		} ]);

angular
		.module('myTimeApp')
		.controller(
				'homeController',
				[
						'$scope',
						'$state',
						'$rootScope',
						'$window',
						'$q',
						'$http',
						'appConstants','userService','globalServices','$stateParams','$location','$filter',
						function($scope, $state, $rootScope, $window, $q,
								$http,appConstants,userService,globalServices,$stateParams,$location,$filter) {
						
		
			 $scope.dataLoading = false;						
			 $rootScope.displayUserName="";
						
			
			 
			 $scope.inituser = function() {	
			
				 $rootScope.isProfilePage=true;    
				 $rootScope.currentUser = userService.getCurrentUser();
             	if ($rootScope.currentUser==undefined)
					 { 
						if(!appConstants.localServer){
							$rootScope.currentUser =userService.getWindowsCurrentUserName();								
						}else{
							$rootScope.currentUser =userService.getLocalTestName();									
						}								
				    }else
				    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName 
						
				}
			 				
				$scope.init = function() {
					$scope.inituser();
				}
				 				
				$scope.init();
			} ]);




angular
.module('myTimeApp')
.controller(
		'loginController',
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http'
				,'appConstants','userService','$stateParams','globalServices',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,$stateParams,globalServices) {
							 $scope.dataLoading = false;						
							  $scope.user={};
							
							$scope.init=function(){	
								 $rootScope.isProfilePage=true;								
								 $rootScope.currentUser = userService.getCurrentUser();
			                    	if ($rootScope.currentUser==undefined)
									 { 
										if(!appConstants.localServer){
											$rootScope.currentUser =userService.getWindowsCurrentUserName();								
										}else{
											$rootScope.currentUser =userService.getLocalTestName();									
										}								
								    }else
								    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName
		
							  if($window.sessionStorage.getItem('isWindowsAuthName')!=undefined && $window.sessionStorage.getItem('isWindowsAuthName')!=null){
								  var url =  appConstants.serverUrl+"/login/getUserDetails?windowsUserName="+$window.sessionStorage.getItem('isWindowsAuthName');
							      
							      $http.get(url).then(function(response) {
							             $scope.user=response;
							            
							           
							                    }); 
							  }                                         
                              
                                               }

							$scope.init();
						} ]);

angular.module('myTimeApp').directive('modal', ['$timeout', function ($timeout) {
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



angular.module('myTimeApp').directive('multiselectDropdown', [function() {
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
angular.module('myTimeApp').run([
			'$state','$http','$rootScope','globalServices','userService','appConstants',function ( $state,$http,$rootScope,globalServices,userService,appConstants) {
				
	   $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams) {	    
	   
		$rootScope.currentUser = userService.getCurrentUser();	
    	if ($rootScope.currentUser==undefined)
		 { 
			if(!appConstants.localServer){
				$rootScope.currentUser =userService.getWindowsCurrentUserName();								
			}else{
				$rootScope.currentUser =userService.getLocalTestName();									
			}								
	    }else
	    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName
	    
	  });
	   

	}]);



//Activity Tracker Controller for User - activityTrackUserController

angular.module('myTimeApp').controller(
		'activityTrackUserController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','$stateParams','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,$stateParams,$location,$filter) {
					$scope.userRoles=[];
					$scope.allRoles=[];
					$scope.allActivity=[];
					$scope.userActivity=[];
					$scope.pageSize = 10;
					$scope.startTaskCount=0;
					$scope.userActivityList=[];
					$scope.windowsUser={};
					$rootScope.dataLoading = false;
					$scope.inituser = function() {
						 $rootScope.isProfilePage=true;    
                       	$rootScope.currentUser = userService.getCurrentUser();
                    	if ($rootScope.currentUser==undefined)
						 { 
							if(!appConstants.localServer){
								$rootScope.currentUser =userService.getWindowsCurrentUserName();
							}else{
								$rootScope.currentUser =userService.getLocalTestName();									
							}								
					    }  
                             
                      }
					
					$scope.init=function(){
						$scope.inituser();
						$rootScope.currentUser = userService.getCurrentUser();
                    	if ($rootScope.currentUser==undefined)
						 { 
							if(!appConstants.localServer){
								$rootScope.currentUser =userService.getWindowsCurrentUserName();								
							}else{
								$rootScope.currentUser =userService.getLocalTestName();									
							}								
					    }else
					    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName
					
							 
                          if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
  									 var url =  appConstants.serverUrl+"/login/getUserDetails?windowsUserName="+$rootScope.currentUser.userName;
                         
                         $http.get(url).then(function(response) {
                                $scope.user=response;
                             
                                       });
					}
							
	                  
                      
                          if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
  							   var userActivityUrl=appConstants.serverUrl+"/activity/getUserActivity?windowsUserName="+$rootScope.currentUser.userName;
                        var activiActivityUrl=appConstants.serverUrl+"/activity/getActiveActivity?windowsUserName="+$rootScope.currentUser.userName;
                        
                        $http.get(userActivityUrl).then(function(response) {
                        	
                        	$scope.userActivityList=response.data;	
                        	
                        });
                        $http.get(activiActivityUrl).then(function(response) {
                        	
                        	$scope.allActivityConstant=angular.copy(response.data);
                        	$scope.allActivity=response.data;	
                        	
                        });
                        }
                          
						}
					
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
				  $scope.showDialog = function(flag) {
					        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
					      };
							
					
				    
					$scope.moveItem = function(items, from, to) {

				        //console.log('Move items: ' + items + ' From: ' + from + ' To: ' + to)
				        //console.log('in user init role constant in move', $scope.allRolesConstant)
				        //Here from is returned as blank and to as undefined

				        items.forEach(function(item) {
				        	//console.log('in move item method' + item)
				          var idx = from.indexOf(item);
				        	//console.log('selected index:' +idx)
				          if (idx != -1) {
				              from.splice(idx, 1);
				              to.push(item);      
				          }
				        });
				    };

				        
			            $scope.Reset=function(){
			            	//$state.go("userSettings",{},{reload:true});
			            	$scope.userSelectedRole=[];
			            	$scope.allActivity=angular.copy($scope.allActivityConstant);
			            	$scope.userActivity=[];
								
			            }
			            
			          
			            $scope.userActivityConfig=function(userActivityList){
			            	 $scope.userActivity=userActivityList;
			            	//var duplicate=0;
			            	var addUserActivityUrl=appConstants.serverUrl+"/activity/addUserActivity/";
							var data = {
				            		 
									activityTracker : $scope.userActivity,
									userName :$rootScope.currentUser.userName
				     		};
							
												
							if( $scope.userActivity==null ||  $scope.userActivity==undefined ||  $scope.userActivity==""){
								$rootScope.buttonClicked = "Please select User Activity";
								$rootScope.showModal = !$rootScope.showModal;
								  $rootScope.contentColor = "#dd4b39";
								
							}else{
								
						
								$http.post(addUserActivityUrl,data,
											{
												headers : {
													'Accept' : 'application/json',
													'Content-Type' : 'application/json'
												}
											})
								.success(function (response) {  
								$rootScope.buttonClicked = response;
								$rootScope.showModal = !$rootScope.showModal;
								$rootScope.contentColor = "#78b266";
								 $state.go("home", {} , {reload: true} );
								 });
								
							}
						};
						
						$scope.startTask =function(id){
							//var startTaskCount=0;
							if($rootScope.currentUser!=undefined){
								
								var config = {
										transformRequest : angular.identity,
										transformResponse : angular.identity,
										headers : {
											'Content-Type' : undefined
										}
									}
								var startTaskCountUrl=appConstants.serverUrl+"/activity/getStartTaskCount?windowsUserName="+$rootScope.currentUser.userName;
								var startTaskUrl=appConstants.serverUrl+"/activity/startActivity/";
								
	                                   if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
								$http.get(startTaskCountUrl).then(function(response) {
									$scope.startTaskCount=response.data;
                                     
										var data = new FormData();
										
										data.append("userName", $rootScope.currentUser.userName);
										data.append("id", id);
									
				
                                    if($scope.startTaskCount<2){
                    					   					
                    							$http.post(startTaskUrl,data,config).then(
                    									function(response){
                    										
                    										  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
                    										      $rootScope.buttonClicked = response.data;
                    											  $rootScope.showModal = !$rootScope.showModal;
                    											  $rootScope.contentColor = "#78b266";
                    											  $state.go("home",{}, {reload: true}); 
                    											
                    									},function(response){
                    										
                    										  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
                    										      $rootScope.buttonClicked = response.data;
                    											  $rootScope.showModal = !$rootScope.showModal;
                    											  $rootScope.contentColor = "#dd4b39";
                    									});	
                    							}else{
                    								
                    							
                    								$('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
                    							      $rootScope.buttonClicked = "Already two Task In Progress! Please Complete the Active Task";
                    								  $rootScope.showModal = !$rootScope.showModal;
                    								  $rootScope.contentColor = "#dd4b39";
                    							}
                                        });
	                                   }
                      
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
							$http.post(stopTaskUrl,data,config).then(
									function(response){
										
										  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
										      $rootScope.buttonClicked = response.data;
											  $rootScope.showModal = !$rootScope.showModal;
											  $rootScope.contentColor = "#78b266";
											  $state.go("home",{}, {reload: true}); 
											
									},function(response){
										
										  $('body').removeClass().removeAttr('style');$('.modal-backdrop').remove(); 
										      $rootScope.buttonClicked = response.data;
											  $rootScope.showModal = !$rootScope.showModal;
											  $rootScope.contentColor = "#dd4b39";
									});							
							}
							
						}
						
			            
				} ]);



angular.module('myTimeApp').controller(
        'monitoringController',
        
        [
                     '$scope',
                     '$state',
                     '$rootScope',
                     '$window',
                     '$q',
                     '$http',
                      'appConstants','userService','globalServices','$stateParams','$location','$filter',
                     function($scope, $state, $rootScope, $window, $q,
                                   $http,appConstants,userService,globalServices,$stateParams,$location,$filter) {
                            $scope.pageSize = 10;
                            $scope.monitorDataList=[];    
                            $scope.pageNumber=1;
                            $scope.totalRecords = 0;
                            $scope.usersPerPage = 10; // this should match however many results your API puts on one page
                            getResultsPage(1);

                            $scope.pagination = {
                                current: 1
                            };

                            $scope.pageChanged = function(newPage) {
                            	console.log(newPage)
                                getResultsPage(newPage);
                            };

                            function getResultsPage(pageNumber) {
                            	 $scope.pageNumber=pageNumber > 0?pageNumber - 1:0;                            	
                                // this is just an example, in reality this stuff should be in a service
                            	 if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
                            		  var url= appConstants.serverUrl+"/reports/getMonitoring?windowsUserName="+$rootScope.currentUser.userName+"&pageNumber="+ $scope.pageNumber+"&size="+ $scope.usersPerPage;
                                      
                                     $http.get(url).success(function(result){                                    	
                                    	 $scope.monitorDataList = result.content;
                                         $scope.totalRecords = result.totalElements
                                     })
                                     .error(function(data,status){    
                                          console.error('Fail to load data', status, data);    
                                          $scope.monitorDataList  = { };     
                                        });    
                                     }
                              
                            }
                            
                            $scope.inituser = function() {
                            	 $rootScope.isProfilePage=true;    
                            	 $rootScope.currentUser = userService.getCurrentUser();
                             	if ($rootScope.currentUser==undefined)
         						 { 
         							if(!appConstants.localServer){
         								$rootScope.currentUser =userService.getWindowsCurrentUserName();								
         							}else{
         								$rootScope.currentUser =userService.getLocalTestName();									
         							}								
         					    }else
         					    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName
                                   
                            }
                            
                            $scope.init=function(){
                            	 $scope.inituser();
                                   $scope.userName="";                               
                                   var url= appConstants.serverUrl+"/reports/getMonitoring?windowsUserName="+$rootScope.currentUser.userName+"&pageNumber="+ $scope.pageNumber+"&size="+ $scope.usersPerPage;
                                   if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
                                   $http.get(url).success(function(result){                               	 
                                	   $scope.monitorDataList = result.content;
                                       $scope.totalRecords = result.totalElements
                                         
                                   })
                                   .error(function(data,status){    
                                        console.error('Fail to load data', status, data);    
                                        $scope.monitorDataList = { };     
                                      });    
                                   }
                                  
                                   }
                            
                            $scope.sort = function(keyname){
                                   $scope.sortKey = keyname;   //set the sortKey to the param passed
                                   $scope.reverse = !$scope.reverse; //if true make it false and vice versa
                                   
                                   
                            }
                       $scope.showDialog = function(flag) {
                                    jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
                                  };
                                          
                     
                     } ]);



angular.module('myTimeApp').controller(
		'reportsController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','$stateParams','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,$stateParams,$location,$filter) {
					$scope.isEditable=false;
					$scope.showAuditHistroyTable=false;
					$scope.revActivityStartTime="";
					$scope.revActivityEndTime="";
					$scope.userId="";
					$scope.showTable=false;
					$scope.empdDetailsList=[];	
					$scope.myAuditList=[];
					$scope.dateRange="";
					$scope.myReportList=[];
					$scope.exportHref="";
					$scope.pageSize = 10;
					$scope.pageSizeB = 10;
				
					  $scope.inituser = function() {
						  $rootScope.isProfilePage=true;    
						  $rootScope.currentUser = userService.getCurrentUser();
	                    	if ($rootScope.currentUser==undefined)
							 { 
								if(!appConstants.localServer){
									$rootScope.currentUser =userService.getWindowsCurrentUserName();								
								}else{
									$rootScope.currentUser =userService.getLocalTestName();									
								}								
						    }else
						    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName
                             
                      }
					  $scope.showDialog = function(flag) {
					        jQuery("#confirmation-dialog .modal").modal(flag ? 'show' : 'hide');
					      };
					      
					$scope.init=function(){
						$scope.inituser();
						$scope.myReportList=[];
						$scope.myAuditList=[];
						$scope.revActivityStartTime="";
						$scope.revActivityEndTime="";
						var getEmpDetailsUrl=appConstants.serverUrl+"/reports/getEmpDetails?windowsUserName="+$rootScope.currentUser.userName;
						
						if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
						$http.get(getEmpDetailsUrl).then(function(response) {
							
							$scope.empdDetailsList=response.data;
						
							
						}, function(response) {
							$rootScope.buttonClicked = response.data;
							$rootScope.showModal = !$rootScope.showModal;
							$rootScope.contentColor = "#dd4b39";
							
							
						});
						}
						
						}
					
					$scope.getMyReport=function(dateRange){
						$scope.isEditable=false;
						$scope.showTable=true;
						$scope.showAuditHistroyTable=false;
						
						var selectedRange=dateRange;
						var getMyReportUrl=appConstants.serverUrl+"/reports/getMyReport?selectedRange="+selectedRange+"&windowsUserName="+$rootScope.currentUser.userName;
						
						if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
							$http.get(getMyReportUrl).then(function(response) {
	                        	
	                        	$scope.myReportList=response.data;	
	                        });
						}
						
						
					};
					
					
					$scope.getTeamReport=function(dateRange){
						$scope.isEditable=true;
						$scope.showTable=true;
						$scope.showAuditHistroyTable=false;
						var selectedRange=dateRange;
						var getTeamReportUrl=appConstants.serverUrl+"/reports/getTeamReport?selectedRange="+selectedRange+"&windowsUserName="+$rootScope.currentUser.userName;
						if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
						$http.get(getTeamReportUrl).then(function(response) {
                        	
                        	$scope.myReportList=response.data;	
                        });
						}
					};
					
					$scope.getAuditHistoryReport=function(dateRange){
						$scope.isEditable=false;
						$scope.showTable=false;
						$scope.showAuditHistroyTable=true;
						var selectedRange=dateRange;
						var getTeamReportUrl=appConstants.serverUrl+"/reports/getAuditHistoryReport?selectedRange="+selectedRange+"&windowsUserName="+$rootScope.currentUser.userName;
						if($rootScope.currentUser.userName!=undefined && $rootScope.currentUser.userName!=null){
						$http.get(getTeamReportUrl).then(function(response) {
                        
                        	$scope.myAuditList=response.data;	
                        });
						}
					};
					
					$scope.getUserTimeReport=function(){
						$window.open(appConstants.userTimeReportUrl);
						//console.log($location.href)
					}
					
					$scope.doRevise = function(id){
						
					
						 $scope.confirmationDialogConfig = {
						      title: "Override StartTime and EndTime",
						      message: "Are you sure you want to Override StartTime and EndTime?",
						      buttons: [{
						        label: "Override",
						        action: "Override"
						      }],
						      id:id	
						    };
						    $scope.showDialog(true);			    
						
					
					}
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
					
					$scope.cancelRevise=function(){
						 $scope.showDialog(false);	
						  $( "#revstartdate" ).val("");
						   $( "#revenddate" ).val("");
						    $( "#comments" ).val("");		
					}
					
					$scope.overrideEmployeeTime=function(id){
						
						
					    $("#reviseform").validate({
					        rules: {
					            "revstartdate": {
					                required: true
					               
					            },
					            "revenddate": {
					                required: true
					               
					            },
					            "comments": {
					                required: true
					               
					            }
					        },
					        messages: {
					            "revstartdate": {
					                required: "Please Select Override StartTime"
					            },
					            "revenddate": {
					                required: "Please Select Override EndTime"
					              
					            },
					            "comments": {
					                required: "Please enter the Comments"
					              
					            }
					        },
					        submitHandler: function (form) {
					        	
					        
								var date_ini = new Date($('#revstartdate').val());
								var date_end = new Date($('#revenddate').val());	
								if (isNaN(date_ini)) {
								alert("Please Select The Start DateTime");
								return false;;
								}
								if (isNaN(date_end)) {
									alert("Please Select The End DateTime");
									return false;;
								}
								if (date_ini > date_end) {
									alert("Please Select The End DateTime greater Than Start Date Time");
									return false;
								}// for demo
					        	if($rootScope.currentUser!=undefined && $rootScope.currentUser!=null){
									var data = {
											revActivityStartTime : $( "#revstartdate" ).val(),
											revActivityEndTime :  $( "#revenddate" ).val(),								
											id : id,										            		
						            		 createdBy :$rootScope.currentUser.userName,
						            		 updatedBy :$rootScope.currentUser.userName,
						            		 comments :$( "#comments" ).val()		
						     		};           
																
										var url=appConstants.serverUrl+"/reports/overrideEmployeeTime/";						
									
									$http.post(url,data,
											{
										headers : {
											'Accept' : 'application/json',
											'Content-Type' : 'application/json'
										}}).then(
											function(response){	
												  $scope.showDialog(false);	
												  $( "#revstartdate" ).val("");
												   $( "#revenddate" ).val("");
												    $( "#comments" ).val("");									   
												$scope.getTeamReport($scope.dateRange);
													
											},function(response){									
												  $scope.showDialog(true);									
											      $rootScope.buttonClicked = response.data;
												  $rootScope.showModal = !$rootScope.showModal;
												  $rootScope.contentColor = "#dd4b39";							   
											});							
									}
					            return false; // for demo
					        }
					    });
						    	
						
					}
					
					
				
				} ]);



angular.module('myTimeApp').controller(
		'uploadEmpDataController',
		[
				'$scope',
				'$rootScope',
				'$http',
				'$window',
				'$state',
				'appConstants','userService','globalServices',
				function($scope, $rootScope, $http, $window, $state,
						appConstants,userService,globalServices) {
					$scope.myFile="";					
					$rootScope.currentUser = {						
					};

					$scope.fileList = [];
					
			
					$scope.dataLoading = false;
			
				
					  $scope.inituser = function() {
						 $rootScope.isProfilePage=true;    
						 $rootScope.currentUser = userService.getCurrentUser();
	                    	if ($rootScope.currentUser==undefined)
							 { 
								if(!appConstants.localServer){
									$rootScope.currentUser =userService.getWindowsCurrentUserName();								
								}else{
									$rootScope.currentUser =userService.getLocalTestName();									
								}								
						    }else
						    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName
                             
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
									$scope.dataLoading = false;
									  $rootScope.contentColor = "#dd4b39";
									  $rootScope.buttonClicked ="Select the Appropriate Employee Details To Upload";						
							          $rootScope.showModal = !$rootScope.showModal;	
								}
					        })
					        .error(function(){
					        });
						}					  
							//fileUpload.uploadFileToUrl(file,$rootScope.currentUser.userName);
						else{
							 $scope.dataLoading = false;
							  $rootScope.buttonClicked ="Select the Appropriate Employee Details To Upload";
							 $rootScope.showModal = !$rootScope.showModal;
					         $rootScope.contentColor = "#dd4b39";	
							
						}
					};
					}
					
					
					
					$scope.init = function() {
						$scope.inituser();
					};

				
					$scope.init();

				} ]);

angular.module('myTimeApp').controller(
		'empSettingsController',
		
		[
				'$scope',
				'$state',
				'$rootScope',
				'$window',
				'$q',
				'$http',
				'appConstants','userService','globalServices','$stateParams','$location','$filter',
				function($scope, $state, $rootScope, $window, $q,
						$http,appConstants,userService,globalServices,$stateParams,$location,$filter) {
					$scope.homepageContent = "settings dashboard page";
					$scope.allEmployeeDetails=[];	
					$scope.employeeCreate="false";
					$scope.userName="";		
					$scope.userId="";
					$scope.employee={};
					$scope.pageSize = 10;
					
					
					  $scope.inituser = function() {
						  $rootScope.isProfilePage=true;
                    	$rootScope.currentUser = userService.getCurrentUser();
                    	if ($rootScope.currentUser==undefined)
						 { 
							if(!appConstants.localServer){
								$rootScope.currentUser =userService.getWindowsCurrentUserName();								
							}else{
								$rootScope.currentUser =userService.getLocalTestName();									
							}								
					    }else
					    	$rootScope.displayUserName="Welcome "+$rootScope.currentUser.userName
                             
                      }
					
					$scope.init=function(){		
						$scope.inituser();
						$scope.userName="";					                   	
                        var allEmployeeDetailsUrl =  appConstants.serverUrl+"/admin/getAllEmployees/";                    
                    
							
                        $http.get(allEmployeeDetailsUrl).then(function(response){
                        	$scope.allEmployeeDetails=angular.copy(response.data);
                        
                        });               
                        
							
						}
					
					$scope.sort = function(keyname){
						$scope.sortKey = keyname;   //set the sortKey to the param passed
						$scope.reverse = !$scope.reverse; //if true make it false and vice versa
						
						
					}
					
					$scope.Cancel = function(){
						$scope.employeeCreate = "false";  
						
						
						
					}
					$scope.employeeConfig=function(employee){	
						
						var employeeConfigUrl=appConstants.serverUrl+"/admin/setEmployeeConfiguration/";
						var data = {
								emailId : employee.emailId,
								admin : employee.admin,
								empId : employee.empId,
								id : employee.id,
			            		 empName : employee.empName,
			            		 rmId : employee.rmId,
			            		 rmName :employee.rmName,	
			            		 teamName :employee.teamName,	
			            		 activeIndicator :employee.activeIndicator,
			            		 createdBy :$rootScope.currentUser.userName,
			            		 updatedBy :$rootScope.currentUser.userName
			     		};
																
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
							
						}else if( employee.teamName==null || employee.teamName==undefined ||  employee.teamName ==""){
							$rootScope.buttonClicked = "Please provide Team Name";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if( employee.rmName==null ||  employee.rmName==undefined ||  employee.rmName==""){
							$rootScope.buttonClicked = "Please provide Reporting Manager Name";
							$rootScope.showModal = !$rootScope.showModal;
							  $rootScope.contentColor = "#dd4b39";
							
						}else if((typeof  employee.admin !== 'boolean' &&  employee.admin !== true)||(typeof  employee.admin !== 'boolean' &&  employee.admin !== false)){
								$rootScope.buttonClicked = "Please Check Boolean value isAdmin or Not";
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
							// call $anchorScroll()
						     // anchorSmoothScroll.scrollTo('top');
							    var $target = $('html,body');
                               $target.animate({scrollTop: $target.height()}, 500);
						}
						
						
						if(methodName=="checkEmployee"){
						
							var emailId=rowId;
							for(var i=0; i<$scope.allEmployeeDetails.length; i++) {	
								
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
						
			
						$http.post(url,data,config).then(
								function(response){
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
					
						$scope.employeeUpdate($scope.employee.emailId,"checkEmployee");
					}
					
				        
			            $scope.Reset=function(){
			            	$scope.employee={};		
			            	$scope.employee.emailId='';
			            	 $scope.employeeExists="false";
							  $scope.inputReadOnly="";
			            }
			            $scope.init();
			            
				} ]);

angular.module('myTimeApp').directive('fileModel', [ '$parse', function($parse) {
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


