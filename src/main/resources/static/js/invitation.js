
/*
 * ANGULARJS 
 */

 angular.module('invitation', [])

 .service('serv', function(){

 })

 /*MAIN CONTROLLER*/
 .controller('InvitationController', function ($scope, $http, $timeout, serv) {
 	console.log($scope.test)
 });

