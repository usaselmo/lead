
/*
 * ANGULARJS 
 */
 var local_server_url = "";

 angular.module('company', [])

 .service('companyService', function($http) {
 	return { 
 		findCompanies: function (scope) {
 			$http.get(local_server_url + "/companies").then(function (response) {
 				scope.companies = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		

 	} 
 }
 )

 /*COMPANY CONTROLLER*/
 .controller('CompanyController', );

