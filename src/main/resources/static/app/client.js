
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
 app_client.controller('ClientController',  );
