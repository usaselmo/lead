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

    var findAttachments = function () {
        personService.findEmailAttachments($scope.lead, person)
            .success(data => $scope.email.attachs = data )
            .error(error => {$scope.email.attachs = ['Error fetching attachments !']; console.log(error);}  );
    }

    var init = function(){
        personService.findPersons($scope).success( data => $scope.persons = data.persons );
        $scope.email = new Email()
        $scope.email.type = $scope.type
        $scope.email.to[0] = person;
        $scope.email.attachs = [];
        $scope.urlUpload = '/persons/' + person.id + '/' + $scope.lead.id + '/emailattachment/upload';
        findAttachments();
    }

    init();

    $scope.deleteAttachment = function(f){
        personService.deleteAttachment($scope.lead, person, f)
            .success(data=> findAttachments())
            .error(error=>{findAttachments(); console.log(error); })
    } 

    $scope.onCompleteAll = function () {
        console.log($scope.email)
        findAttachments();
    }


    $scope.sendEmail = function () {
        if ($scope.type == 'CANT_REACH') 
            personService.sendCantReachEmail($scope.lead, person, $scope.email).success(data=>findAttachments())
        else if ($scope.type == 'HIRING_DECISION')
            personService.sendHiringDecisionEmail($scope.lead, person, $scope.email).success(data => findAttachments());
        else if ($scope.type == 'PROPOSAL'){
            proposalService.sendByEmail($scope.proposal, $scope.email, person.id, $scope.lead.id).success(data => findAttachments());
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