import Modal from '/app/module/modal/modal.js';
import Email from '/app/model/email.js';

import personService from '/app/service/person-service.js';
import proposalService from '/app/service/proposal-service.js';

var email = angular.module('app.module.email', [
    'app.module.modal',
]);

email.service('personService', personService);
email.service('proposalService', proposalService);

email.directive('appEmail', function () {
    return {
        restrict: 'E',
        transclude: true,
        scope: {
            lead: '=', 
            type: '=',
            proposal: '=',
        },
        templateUrl: '/app/module/email/email.html',
        controller: ['$scope', 'personService', 'proposalService', emailController], 
    };
});

var emailController = function ($scope, personService, proposalService) {

    var person = $scope.lead.client ? $scope.lead.client : $scope.lead.contact;

    var init = function(){
        personService.findPersons($scope).success( data => $scope.persons = data );

        $scope.email = new Email()
        $scope.email.type = $scope.type
        $scope.email.to[0] = person;
    }

    init();

    $scope.sendEmail = function () {
        if ($scope.type == 'CANT_REACH') 
            personService.sendCantReachEmail($scope.lead, person, $scope.email);
        else if ($scope.type == 'HIRING_DECISION')
            personService.sendHiringDecisionEmail($scope.lead, person, $scope.email);
        else if ($scope.type == 'PROPOSAL'){
            proposalService.sendByEmail($scope.proposal, $scope.email)
        } 
    }

    $scope.increaseTo = function () {
        $scope.email.to.push({})
    }

    $scope.increaseBcc = function () {
        $scope.email.bcc.push({})
    }

}

export default email