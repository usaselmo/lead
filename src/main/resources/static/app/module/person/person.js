
import personCrud from '/app/module/person/person-crud.js'

import companyService from '/app/service/company-service.js'
import personService from '/app/service/person-service.js'

var person = angular.module('app.module.person', [
	'app.module.lead.person.crud',
]);


person.service('companyService', companyService);
person.service('personService', personService);

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
		personService.findPersons($scope);
	}

	$scope.crud = function(person){
		$scope.person = person; 
	}

	init();
}

export default person
