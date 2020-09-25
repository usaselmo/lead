
/*
 * ANGULARJS 
 */

 var app_client =  angular.module('client', []);

 

 app_client.service('clientService', personService);
 app_client.service('companyService', companyService);

 var initTable = function(){
 	[1,2,3].forEach(function(i) {
 		setTimeout(function(){ $('#94irjrn494h').DataTable(); }, i*0);
 	});
 };

 /*COMPANY CONTROLLER*/
 app_client.controller('ClientController', function ($scope, $http, $timeout, clientService, companyService) {
 	var init = function(){
 		clientService.findClients($scope, initTable);
 		companyService.findCompanies($scope);
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
