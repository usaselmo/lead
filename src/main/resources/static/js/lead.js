/*
 * ANGULARJS 
 */

 angular.module('lead', [])

 .service('leadService', function($http){
 	var local_server_url = "";
 	return {
 		findLeads: function (scope, pageRange, lines, event) {
 			$http.get(local_server_url + "/leads?pageRange=" + pageRange + "&lines=" + lines + "&event=" + event).then(function (response) {
 				scope.leads = response.data.leads
 				scope.leadsTotalPrice = response.data.leadsTotalPrice
 				scope.totalLeads = response.data.totalLeads
 				scope.leadTypes = response.data.leadTypes;
 				scope.events = response.data.events;
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 })


 /*MAIN CONTROLLER*/
 .controller('LeadController', function ($scope, $http, $timeout, leadService) {  

 	var pageRange = 0;
 	var lines = 10;

 	$scope.getNextListRange = function(numero){
 		if(numero>0 && ((pageRange+1)*lines) < $scope.totalLeads)
 			pageRange++
 		if(numero<0 && pageRange>0)
 			pageRange--
 		if($scope.event=='ALL' || !$scope.event) 
 			$scope.event=''
 		leadService.findLeads($scope, pageRange, lines, $scope.event)
 	} 

 	$scope.reload = function(event){
 		if(event=='ALL' || !event) 
 			event=''
 		leadService.findLeads($scope, pageRange, lines, event)
 	}

 	var init = function(){
 		leadService.findLeads($scope, pageRange, lines, '');
 	}  

 	init();

 });

