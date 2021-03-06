
import companyService from '/app/service/company-service.js'
import personService from '/app/service/person-service.js'
import leadService from '/app/service/lead-service.js'

var invitation = angular.module('app.module.lead.invitation', [
	'angularFileUpload', 
	'app.module.file-uploader', 
	]);

invitation.service('leadService', leadService);
invitation.service('companyService', companyService);
invitation.service('personService', personService);

invitation.directive('appLeadInvitation', function() {
	return {
		restrict : 'E',
		scope : {
			lead : '=',
		},
		templateUrl : '/app/module/lead/invitation/lead-invitation.html',
		controller: ['$scope', 'leadService', 'companyService', 'personService', leadInvitationController],
	};
});

var leadInvitationController = function ($scope, leadService, companyService, personService) {

	$scope.uploading = [];

	$scope.onCompleteAll = function () { 
		leadService.reloadLead($scope.lead).success(data => $scope.lead = data.lead) 
	}

	$scope.markInvitationAsEmailed = function (invitation, lead) {
		if (confirm(' Confirm mark invitation #' + invitation.id + ' as e-mailed ? ')) {
			leadService.markInvitationAsEmailed(invitation, lead).success(data => $scope.lead.invitations = $scope.lead.invitations.map(i => i.id == data.invitation.id ? data.invitation : i))
		}
	}

	$scope.invitationCrud = function (invitation) {
		$scope.invitation = invitation;
		companyService.findCompanies($scope);
		personService.findPersons($scope)
	}

	$scope.invitationCancel = function () {
		$scope.invitation = null
	}

	$scope.invitationSave = function (invitation, lead) {
		if (!invitation.id)
			leadService.createInvitation($scope, invitation, lead).success(data => leadService.reloadLead($scope.lead))
		else
			leadService.updateInvitation($scope, invitation, lead).success(data => leadService.reloadLead($scope.lead))
		$scope.invitationCancel();
	}


	$scope.invitationDelete = function (invitation, lead) {
		if (confirm(' Are you sure you want to delete invitation #' + invitation.id + '? ')) {
			leadService.deleteInvitation($scope, invitation, lead);
		}
	}

	$scope.invitationEmail = function (invitation, lead) {
		if (confirm(' Are you sure you want to send this invitation by e-mail ? ')) {
			invitation.lead = { id: lead.id }
			leadService.sendInvitationByEmail($scope, invitation)
		}
	}

	$scope.invitationIncludeProposal = function (invitation) {
		$scope.uploading['processing'] = !$scope.uploading['processing'];
		if ($scope.uploading['processing'])
			$scope.uploading['invitation'] = invitation
	}

}


export default invitation