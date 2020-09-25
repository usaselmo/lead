/*
 * ANGULARJS 
 */

 angular.module('user', [])

 
.service('userService', userService)
 
 /* MAIN CONTROLLER */
 .controller('UserController', function ($scope, $http, $timeout, userService) {
 	$scope.test = 'ksdeofenei i p sepi fesi '
 });

