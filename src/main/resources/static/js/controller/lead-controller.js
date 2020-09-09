
app.service('companyService', companyService);
app.service('leadService', leadService);
app.service('personService', personService);
app.service('proposalService', proposalService);
app.service('userService', userService);

/*MAIN CONTROLLER*/
app.controller('LeadController', function ($scope, leadService, companyService, personService, userService, proposalService, FileUploader) {  

 	/****************
 	 **** CRUD ******
 	 ****************/

   $scope.crud = function(lead){
     $scope.crudLead = lead;
     $scope.leads = null;
     $scope.lead = null;
     companyService.findCompanies($scope)
     personService.findPersons($scope)
     userService.findEstimators($scope)
 }

 $scope.save = function(lead){
     if(lead.id)
        leadService.update($scope, lead)
    else
        leadService.save($scope, lead);
    $scope.cancel(lead)
}

$scope.cancel = function(lead){
 if(lead.id)
    $scope.detail(lead)
else
    $scope.list()
}




 	/****************
 	 *** PROPOSAL ***
 	 ****************/

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

$scope.proposalCrud = function(proposal, lead){
 if(!proposal.id)
    proposal = createProposal(lead);
else
    proposal = convertToClientFormat(proposal);
$scope.proposal = proposal;
}

$scope.proposalCancel = function(){
 $scope.proposal = null;
}

$scope.proposalEncreaseItem = function(proposal){
 $scope.proposal.items.push({price: 0})
}

$scope.proposalRemoveItem = function(proposal){
 if($scope.proposal.items.length > 1 ){
    $scope.proposal.items.pop();
}
}

$scope.proposalSave = function(lead, proposal){
    if(proposal.id){
        proposalService.update($scope, proposal, lead.id)
            .success( (response) => $scope.reloadLead(lead) );
    }else{
        proposalService.save($scope, proposal, lead.id)
    }
}

$scope.deleteProposal = function(lead, proposal){
 if(confirm(' Are you sure you want to delete? ')){
    proposalService.delete($scope, proposal, lead.id)
}
}

$scope.proposalCopy = function(proposal){
 nproposal = convertToClientFormat(proposal);
 nproposal.id = null
 nproposal.total = 0  
 nproposal.emailed = false;
 nproposal.number = null;
 nproposal.items.forEach(item=>{
    item.id=null;
})
 originalLines=[];
 $scope.proposal = nproposal;
}



 	/****************
 	 **** DETAIL ****
 	 ****************/

   $scope.detail = function(lead){
     $scope.lead = lead;
     $scope.crudLead = null;
     $scope.leads = null;
 }

 $scope.fireEvent = function(lead, event){
     leadService.fireEvent($scope, lead, event)
 }

 $scope.saveNote = function(lead, newNote){
     leadService.saveNote($scope, lead, newNote)
 }

 $scope.emailProposal = function(proposal){
     if (confirm('Send Proposal #'+proposal.number + ' via E-mail ? ')) {
        proposalService.sendByEmail($scope, proposal)
    }
}

$scope.sendCantReachEmail = function(lead, person){
 if (person && confirm('Send Can\'t Reach E-mail to ' + person.name + ' ? ')) {
    personService.sendCantReachEmail(lead, person);
}
}

$scope.sendHiringDecisionEmail = function(lead, person){
 if (person && confirm('Send e-mail asking about '+person.name+'\'s  hiring decision ? ')) {
    personService.sendHiringDecisionEmail(lead, person);
}
}

$scope.reloadLead = function(lead){
 leadService.findLead($scope, lead.id)
}

 	/****************
 	 **** LIST  *****
 	 ****************/
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



 	/****************
 	 **** MEDIA *****
 	 ****************/
   $scope.uploader = new FileUploader();

   $scope.uploadd = function(uploader, lead){

    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        lead.medias.push({id:'', type:fileItem.file.type, name:fileItem.file.name})
        $scope.lead = lead;
    };

    uploader.onCompleteAll = function(){
        uploader.clearQueue();
    }

    uploader.queue.forEach(item=>{
        item.url = '/main/leads/' + lead.id + '/file-upload'
        item.upload();
    })

    $scope.reloadLead(lead);

}



 	/****************
 	 ** INVITATION **
 	 ****************/

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
  leadService.createInvitation($scope, invitation, lead);	
  $scope.reloadLead(lead)
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
    $scope.reloadLead(lead);
}

});

