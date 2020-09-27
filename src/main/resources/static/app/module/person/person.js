
import companyService from '/app/service/company-service.js'
import personService from '/app/service/person-service.js'

var person = angular.module('app.module.person', []);


person.service('companyService', companyService);
person.service('personService', personService);

var initTable = function(){
	/*[1,2,3].forEach(function(i) {
		setTimeout(function(){ $('#94irjrn494h').DataTable(); }, i*0);
	});*/
};

person.directive('appPerson', function() {
	return {
		restrict : 'E',
		scope : {},
		templateUrl : '/app/module/person/person.html',
		controller: personController,
	};
});

var personController = function ($scope, $http, $timeout, personService, companyService) {
	var init = function(){
		personService.findClients($scope, initTable);
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
			personService.update($scope, client).success(data=> client = data.person )
		}else{
			personService.save($scope, client).success(data => $scope.clients.unshift(data.person) )
		}
		$scope.cancel();
	}

	init();
}

export default person
