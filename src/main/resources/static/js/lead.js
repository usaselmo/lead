/*
 * ANGULARJS 
 */
 var local_server_url = "";

 var app = angular.module('lead', [
 	'app.module.lead',
 	'app.module.header',
 	'app.module.menu',
 	]);


 app.service('companyService', companyService);
 app.service('leadService', leadService);
 app.service('personService', personService);
 app.service('proposalService', proposalService);
 app.service('userService', userService);

 /* MAIN CONTROLLER */
 app.controller('LeadController', function ($scope, leadService, companyService, personService, userService, FileUploader) {  

  	/***************************************************************************
		 * *** CRUD ******
		 **************************************************************************/





  	/***************************************************************************
		 * ** PROPOSAL ***
		 **************************************************************************/

		 var createProposal = function(lead){
		 	var proposal = {
		 		total: 0,
		 		items: [{price:0}], 
		 		callMissUtility: true,
		 		scopeOfWork: 'Concrete work as per visit at ',
		 		workWarranty: 'All new material and labor are guaranteed for 36 months from completion date. All work to be completed in a neat and workmanlike manner. Warranty applies for concrete cracks that are 3/8" or greater in separation or height difference for flat concrete work. Warranty excludes the following: concrete damage caused by deicers such as salt or any deicer or fertilizer containing ammonium nitrate or ammonium sulfate, concrete spider cracks, hairline cracks and color variance.' ,
		 		paymentSchedule: '1. 50% down payment upon start of the project \r\n2. 50% final payment upon completion of the project.' 
		 	}
		 	return proposal;
		 }





  	/***************************************************************************
		 * *** DETAIL ****
		 **************************************************************************/


  	/***************************************************************************
		 * *** LIST *****
		 **************************************************************************/
		 var pageRange = 0;
		 var lines = 10;

		 $scope.list = function(){
		 	$scope.lead = null;
		 	$scope.crudLead = null;
		 	$scope.reload($scope.event, $scope.search);
		 }

		 $scope.getNextListRange = function(numero){
		 	if(numero>0 && ((pageRange+1)*lines) < $scope.totalLeads)
		 		pageRange++
		 	if(numero<0 && pageRange>0)
		 		pageRange--
		 	leadService.findLeads($scope, pageRange, lines, $scope.event, $scope.search)
		 } 

		 $scope.reload = function(event, search){
		 	$scope.event = event;
		 	$scope.search = search;
		 	pageRange = 0;
		 	leadService.findLeads($scope, pageRange, lines)
		 }

		 var init = function(){
		 	leadService.findLeads($scope, pageRange, lines);
		 }  

		 init();



  	/***************************************************************************
		 * *** MEDIA *****
		 **************************************************************************/
		 $scope.uploader = new FileUploader();

		 $scope.uploadd = function(uploader, lead){

		 	uploader.onSuccessItem = function(fileItem, response, status, headers) {
		 		if(!lead.medias)
		 			lead.medias = [];
		 		lead.medias.push({id:'', type:fileItem.file.type, name:fileItem.file.name})
		 		$scope.lead = lead;
		 	};

		 	uploader.onCompleteAll = function(){
		 		uploader.clearQueue();
		 		$scope.reloadLead(lead);
		 	}

		 	uploader.queue.forEach(item=>{
		 		item.url = '/main/leads/' + lead.id + '/file-upload'
		 		item.upload();
		 	})

		 }



  	/***************************************************************************
		 * * INVITATION **
		 **************************************************************************/

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
		 		leadService.createInvitation($scope, invitation, lead).success( ()=> $scope.reloadLead(lead) )
		 	else
		 		leadService.updateInvitation($scope, invitation, lead).success( ()=> $scope.reloadLead(lead) )
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
		 		$scope.reloadLead(lead);
		 	}

		 	uploader.queue.forEach(item=>{
		 		item.url = '/main/leads/' + lead.id + '/invitations/' + invitation.id
		 		item.upload();
		 	})

		 	$scope.uploading['processing'] = !$scope.uploading['processing'];

		 }

		});

