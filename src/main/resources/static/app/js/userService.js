angular.module('myTimeApp').service('userService', ['$window', '$q','$http','appConstants','$state', function ($window, $q,$http,appConstants,$state) {
 
    this.getCurrentUser = function () {
    	
        if ($window.sessionStorage.getItem('currentUser')) {        	
            return JSON.parse($window.sessionStorage.getItem('currentUser'));
        }
    };
    this.clearCurrentUser = function () {
        $window.sessionStorage.removeItem('currentUser');
        return;
    };
    
    this.getWindowsCurrentUserName = function () {  
    	var currentUser = {};  
        $http.get(appConstants.windowsUNUrl).then(function(response) {                      
				 currentUser = {
                       userId : response.data.userName,
                       email  : response.data.userName+"@CMA-CGM.COM",
                       userName : response.data.userName,
                       userToken :""
                };  
								 
				 $window.sessionStorage.setItem('isWindowsAuthName',currentUser.userName); 
			       $window.sessionStorage.setItem('currentUser', JSON.stringify(currentUser));  
			       $state.go("home",{}, {reload: true});  
			       
			       return  currentUser;
				
        });
        return  currentUser;
    };
    this.getLocalTestName = function () {  
    	         var currName ={"userName":"SSC.BAMMU"}	
				 var currentUser = {
                       userId :currName.userName,
                       email  : currName.userName+"@CMA-CGM.COM",
                       userName : currName.userName,
                       userToken :""
                };								
				 $window.sessionStorage.setItem('isWindowsAuthName',currentUser.userName); 
			      $window.sessionStorage.setItem('currentUser', JSON.stringify(currentUser)); 
			      $state.go("home",{}, {reload: true});  
				 return currentUser;
      
    };
    
}]);