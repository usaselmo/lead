
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
 		
 		update: function (scope, company) {
 			$http.put(local_server_url + "/companies", company).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					company = response.data.company
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		save: function (scope, company) {
 			$http.post(local_server_url + "/companies", company).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.companies.push(response.data.company)
 					scope.successMessages = response.data.successMessages
 				}
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
 		$scope.company = company; 
 	}

 	$scope.cancel = function(){
 		$scope.company = null
 	}

 	$scope.save = function(company){
 		if(company.id){
 			companyService.update($scope, company)
 		}else{
 			companyService.save($scope, company)
 		}
 		$scope.cancel();
 	}

 	init();
 });

