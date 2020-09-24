var invitation = angular.module('app.module.lead.invitation', [
	'angularFileUpload', 
	
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
		templateUrl : '/angularjs/module/lead/invitation/lead-invitation.html',
		controller : function($scope, leadService, companyService, personService, FileUploader) {
			   
			$scope.uploader = new FileUploader();

			$scope.uploading = [];

			$scope.invitationCrud = function(invitation){
				$scope.invitation = invitation; 
				companyService.findCompanies($scope);
				personService.findPersons($scope)
			}

			$scope.invitationCancel = function(){
				$scope.invitation = null
			}

			$scope.invitationSave = function(invitation, lead){
				if(!invitation.id)
					leadService.createInvitation($scope, invitation, lead).success( ()=> console.log('success') )
				else
					leadService.updateInvitation($scope, invitation, lead).success( ()=> console.log('success') )
				$scope.invitationCancel();
			}


			$scope.invitationDelete = function(invitation, lead){
				if(confirm(' Are you sure you want to delete invitation #'+invitation.id+'? ')){
					leadService.deleteInvitation($scope, invitation, lead);
				}
			}

			$scope.invitationEmail = function(invitation, lead){
				if(confirm(' Are you sure you want to send this invitation by e-mail ? ')){
					invitation.lead = {id: lead.id}
					leadService.sendInvitationByEmail($scope, invitation)	
				}
			}

			$scope.invitationIncludeProposal = function(invitation){
				$scope.uploading['processing'] = !$scope.uploading['processing'];
				if($scope.uploading['processing'])
					$scope.uploading['invitation'] = invitation
			}

			$scope.uploadProposal = function(uploader, invitation, lead){

				uploader.onSuccessItem = function(fileItem, response, status, headers) {
					invitation.medias.push({id:'', type:fileItem.file.type, name:fileItem.file.name})
				};

				uploader.onCompleteAll = function(){
					uploader.clearQueue();
				}

				uploader.queue.forEach(item=>{
					item.url = '/main/leads/' + lead.id + '/invitations/' + invitation.id
					item.upload();
				})

				$scope.uploading['processing'] = !$scope.uploading['processing'];

			}

		},
	};
});
