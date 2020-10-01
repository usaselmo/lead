import Modal from '/app/module/modal/modal.js';

import personService from '/app/service/person-service.js';

var email = angular.module('app.module.email', [
    'app.module.modal',
]);

email.service('personService', personService);

email.directive('appEmail', function () {
    return {
        restrict: 'E',
        scope: {
            lead: '=', 
            type: '@',
        },
        templateUrl: '/app/module/email/email.html',
        controller: ['$scope', 'personService', emailController], 
    };
});

var emailController = function ($scope, personService) {

    var person = $scope.lead.client ? $scope.lead.client : $scope.lead.contact;

    $scope.sendEmail = function () {
        if ($scope.type == 'cantreach') {
            personService.sendCantReachEmail($scope.lead, person);
        }
    }

}

export default email