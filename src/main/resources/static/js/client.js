
/*
 * ANGULARJS 
 */
 var local_server_url = "";

var initTable = function(){
	[1,2,3].forEach(function(i) {
		setTimeout(function(){ $('#94irjrn494h').DataTable(); }, i*0);
	});
}


 angular.module('client', [])

 .service('clientService', function($http){
 	return { 
 		findClients: function (scope) {
 			$http.get(local_server_url + "/persons").then(function (response) {
 				scope.clients = response.data
 				initTable();
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		update: function (scope, client) {
 			$http.put(local_server_url + "/persons", client).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					client = response.data.client
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		save: function (scope, client) {
 			$http.post(local_server_url + "/persons", client).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.clients.push(response.data.client)
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},

 	} 
 })

 /*COMPANY CONTROLLER*/
 .controller('ClientController', function ($scope, $http, $timeout, clientService) {
 	var init = function(){
 		clientService.findClients($scope);
 	}

 	$scope.crud = function(client){
 		$scope.client = client; 
 	}

 	$scope.cancel = function(){
 		$scope.client = null
 		initTable();
 	}

 	$scope.save = function(client){
 		if(client.id){
 			clientService.update($scope, client)
 		}else{
 			clientService.save($scope, client)
 		}
 		$scope.cancel();
 	}

 	init();
 });

