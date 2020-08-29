
/*
 * ANGULARJS 
 */
 var local_server_url = "";

 angular.module('company', [])

 .service('companyService', function($http){
 	return { 
 		findCompanies: function (scope) {
 			$http.get(local_server_url + "/companies").then(function (response) {
 				scope.companies = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 })

 /*COMPANY CONTROLLER*/
 .controller('CompanyController', function ($scope, $http, $timeout, companyService) {
 	var init = function(){
 		companyService.findCompanies($scope);
 	}

 	$scope.crud = function(company){
 		$scope.companies = null;
 		$scope.company = company; 
 	}

 	$scope.cancel = function(){
 		$scope.company = null
 		companyService.findCompanies($scope)
 	}

 	init();
 });

