
/*
 * ANGULARJS 
 */
 var local_server_url = "";

 angular.module('invitation', [])

 .service('leadService', function($http){
 	return {
 		findLead: function (scope, leadId) {
 			$http.get(local_server_url + "/leads/"+leadId).then(function (response) {
 				if(response.data.lead)
 					scope.lead = response.data.lead
 				scope.errorMessages = response.data.errorMessages
 				scope.successMessages = response.data.successMessages
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	}
 })

 /*MAIN CONTROLLER*/
 .controller('InvitationController', function ($scope, $http, $timeout, leadService) {

 	var initParam = function () {
 		let leadId = document.getElementById('leadId').value
 		leadService.findLead($scope, leadId)
 	}

 	$scope.crud = function(invitation){
 		$scope.invitation = invitation; 
 	}

 	$scope.cancel = function(){
 		$scope.invitation = null
 	}

 	initParam();

 });

