/*
 * ANGULARJS 
 */

 angular.module('lead', [])

 .service('leadService', function($http){
 	var local_server_url = "";
 	return {
 		findLeads: function (scope, pageRange, lines) {
 			if(scope.event=='ALL' || !scope.event) scope.event='';
 			if(!scope.search) scope.search = '';
 			$http.get(local_server_url + "/leads?pageRange=" + pageRange + "&lines=" + lines + "&event=" + scope.event + "&text=" + scope.search).then(function (response) {
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
 	/** CRUD **/
 	$scope.newLead=false;
 	$scope.crud = function(){
 		$scope.newLead = true;
 	}

 	/** DETAIL **/
 	$scope.edit = function(lead){
 		$scope.lead = lead;
 	}

 	/** LIST **/
 	var pageRange = 0;
 	var lines = 10;

 	$scope.showList = function(){
 		$scope.lead = null;
 		$scope.newLead = null;
 		$scope.reload($scope.event, $scope.search);
 	}

 	$scope.getNextListRange = function(numero){
 		if(numero>0 && ((pageRange+1)*lines) < $scope.totalLeads)
 			pageRange++
 		if(numero<0 && pageRange>0)
 			pageRange--
 		leadService.findLeads($scope, pageRange, lines, $scope.event, $scope.search)
 	} 

 	$scope.reload = function(event, search){
 		$scope.event = event;
 		$scope.search = search;
 		pageRange = 0;
 		leadService.findLeads($scope, pageRange, lines)
 	}

 	var init = function(){
 		leadService.findLeads($scope, pageRange, lines);
 	}  

 	init();

 });

