/*
 * ANGULARJS 
 */

 angular.module('lead', [])

 .service('leadService', function($http){
 	var local_server_url = "";
 	return {
 		findLeads: function (scope, pageRange, lines, event, text) {
 			$http.get(local_server_url + "/leads?pageRange=" + pageRange + "&lines=" + lines + "&event=" + event + "&text=" + text).then(function (response) {
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
 	$scope.search = '';

 	$scope.getNextListRange = function(numero){
 		if(numero>0 && ((pageRange+1)*lines) < $scope.totalLeads)
 			pageRange++
 		if(numero<0 && pageRange>0)
 			pageRange--
 		if($scope.event=='ALL' || !$scope.event) 
 			$scope.event=''
 		if(!$scope.search) $scope.search = ''
 		leadService.findLeads($scope, pageRange, lines, $scope.event, $scope.search)
 	} 

 	$scope.reload = function(event, search){
 		if(event=='ALL' || !event) 
 			event=''
 		if(!$scope.search) $scope.search = ''
 		pageRange = 0;
 		leadService.findLeads($scope, pageRange, lines, event, search)
 	}

 	var init = function(){
 		leadService.findLeads($scope, pageRange, lines, '', $scope.search);
 	}  

 	init();

 });

