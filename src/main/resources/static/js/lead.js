/*
 * ANGULARJS 
 */

 angular.module('lead', [])

 .service('leadService', function($http){
 	var local_server_url = "";
 	return {
 		findLeads: function (scope, pageRange, lines, filter) {
 			$http.get(local_server_url + "/leads?pageRange=" + pageRange + "&lines=" + lines + "&eventType=" + filter).then(function (response) {
 				scope.leads = response.data.leads
 				scope.leadsTotalPrice = response.data.leadsTotalPrice
 				scope.totalLeads = response.data.leads.length
 				scope.leadTypes = response.data.leadTypes;
 				scope.eventTypes = response.data.eventTypes;
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 })


 /*MAIN CONTROLLER*/
 .controller('LeadController', function ($scope, $http, $timeout, leadService) {   

 	$scope.reload = function(filter){
 		if(filter=='ALL' || !filter) 
 			filter=''
 		leadService.findLeads($scope, 0, 10, filter)
 	}

 	var init = function(){
 		leadService.findLeads($scope, 0, 10, '');
 	}  
 	init();

 });

