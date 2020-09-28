
import companyService from '/app/service/company-service.js'
import personService from '/app/service/person-service.js'

var personCrud = angular.module('app.module.lead.person.crud', []);

personCrud.service('companyService', companyService);
personCrud.service('personService', personService);

personCrud.directive('appPersonCrud', function () {
    return {
        restrict: 'E',
        scope: {
            person: '=',
            persons: "=",
        },
        templateUrl: '/app/module/person/person-crud.html',
        controller: function ($scope, personService, companyService) {

            var init = function () {
                companyService.findCompanies($scope);
            }
            init();

            $scope.cancel = function () {
                $scope.person = null
            }

            $scope.save = function (person) {
                if (person.id) {
                    personService.update($scope, person).success(data => person = data.person)
                } else {
                    personService.save($scope, person).success(data => $scope.persons.unshift(data.person))
                }
                $scope.cancel();
            }

        },
    };
});

export default personCrud