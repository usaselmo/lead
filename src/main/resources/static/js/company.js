
/*
 * ANGULARJS 
 */

 angular.module('company', [])

 .service('companyService', function(){

 })

 /*MAIN CONTROLLER*/
 .controller('CompanyController', function ($scope, $http, $timeout, companyService) {
 	$scope.test = ' This is Company Controller'
 });

