
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

var datatables = function(){
	$(document).ready(function () {
		$('#94irjrn494h').DataTable();
	});
}

function sleep(time) {
	return new Promise((resolve) => setTimeout(resolve, time));
}

var personController = function ($scope, $http, $timeout, personService, companyService) {

	$scope.findByName = function (text) {
		if(!text) $scope.persons = []
		else if(text.length < 3) return
		else {
			personService.findByName(text)
				.success(data => {
					$scope.persons = data.persons
				})
				.error(error => console.log(error))
		}
	}

	$scope.crud = function(person){
		$scope.person = person; 
	}

}

export default person
