/*
 * ANGULARJS 
 */
 var local_server_url = "";

 var originalLines = [];

 var lineBreaker = "\n"

 var convertToClientFormat = function (proposal) {
 	prop = copy(proposal)
 	items = []
 	prop.items.forEach(item => {
 		item.lines = item.lines.map(line => {
 			originalLines[item.id + line.description] = line.id;
 			return line.description;
 		}).join(lineBreaker)
 		items.push(item)
 	})
 	prop.items = items;
 	return prop;
 }

 var copy = function (obj) {
 	return JSON.parse(JSON.stringify(obj))
 }


 var convertToServerFormat = function (proposal) {
 	prop = copy(proposal)
 	var its = [];
 	let ols = originalLines
 	prop.items.forEach(item => {
 		var lns = [];
 		item.lines.split(lineBreaker).forEach(line => {
 			lns.push({ 'description': line, 'id': ols[item.id + line] });
 		})
 		its.push({'id':item.id, 'title': item.title, 'lines': lns, 'price': item.price });
 	})
 	prop.items=[]
 	prop.items = its
 	prop.lines = null
 	prop.note = proposal.note
 	originalLines=[]
 	return prop
 }

 angular.module('lead', ['angularFileUpload'])

 .service('proposalService', function($http){
 	return { 
 		sendByEmail: function (scope, proposal) {
 			$http.get(local_server_url + "/proposals/" + proposal.id + "/email").then(function (response) {
 				scope.successMessages = response.data.successMessages;
 				scope.errorMessages = response.data.errorMessages;
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		update: function (scope, proposal, lead_id) {
 			$http.put(local_server_url + "/proposals?leadId=" + lead_id, convertToServerFormat(proposal)).then(function (response) {
 				if(response.data.proposal){
 					scope.lead.proposals = scope.lead.proposals.map(p=>p.id==response.data.proposal.id?response.data.proposal:p)
 				}
 				scope.successMessages = response.data.successMessages
 				scope.errorMessages = response.data.errorMessages
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		save: function (scope, proposal, lead_id) {
 			$http.post(local_server_url + "/proposals?leadId=" + lead_id, convertToServerFormat(proposal)).then(function (response) {
 				if(response.data.proposal){
 					scope.lead.proposals.push(response.data.proposal);
 				}
 				scope.successMessages = response.data.successMessages
 				scope.errorMessages = response.data.errorMessages
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		delete: function (scope, proposal, lead_id) {
 			$http.delete(local_server_url + "/proposals?leadId=" + lead_id + "&proposalId=" + proposal.id ).then(function (response) {
 				if(!response.data.errorMessages){
 					scope.lead.proposals = scope.lead.proposals.filter(p=>p.id != proposal.id)
 				}
 				scope.successMessages = response.data.successMessages
 				scope.errorMessages = response.data.errorMessages
 			}, function (response) {
 				console.log(response)
 			});
 		},

 	} 
 })

 .service('userService', function($http){
 	return {
 		findEstimators: function (scope) {
 			$http.get(local_server_url + "/users/estimators").then(function (response) {
 				scope.estimators = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 })

 .service('personService', function($http){
 	return {
 		findPersons: function (scope) {
 			$http.get(local_server_url + "/persons").then(function (response) {
 				scope.persons = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		sendCantReachEmail: function (lead, person) {
 			$http.get(local_server_url + '/persons/'+person.id+'/leads/'+lead.id+'/cantreach').then(function (response) {
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		sendHiringDecisionEmail: function (lead, person) {
 			$http.get(local_server_url + '/persons/'+person.id+'/leads/'+lead.id+'/hiringdecision').then(function (response) {
 			}, function (response) {
 				console.log(response)
 			});
 		},

 	} 
 })

 .service('companyService', function($http){
 	return {
 		findCompanies: function (scope) {
 			$http.get(local_server_url + "/companies").then(function (response) {
 				scope.companies = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 })

 .service('leadService', function($http){
 	return {
 		findLeads: function (scope, pageRange, lines) {
 			if(scope.event=='ALL' || !scope.event) scope.event='';
 			if(!scope.search) scope.search = '';
 			$http.get(local_server_url + "/leads?pageRange=" + pageRange + "&lines=" + lines + "&event=" + scope.event + "&text=" + scope.search).then(function (response) {
 				scope.leads = response.data.leads
 				scope.leadsTotalPrice = response.data.leadsTotalPrice
 				scope.totalLeads = response.data.totalLeads
 				scope.leadTypes = response.data.leadTypes;
 				scope.events = response.data.events;
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		update: function (scope, lead) {
 			$http.put(local_server_url + "/leads", lead).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.lead = response.data.lead
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		save: function (scope, lead) {
 			$http.post(local_server_url + "/leads", lead).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.lead = response.data.lead
 					scope.successMessages = response.data.successMessages
 				}   
 			}, function (response) {
 				console.log(response)            
 			});
 		},
 		
 		fireEvent: function (scope, lead, event) {
 			$http.post(local_server_url + "/leads/"+lead.id+"/fireevent", event).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					if(scope.leads){
 						scope.leads = scope.leads.map(lead => lead.id==response.data.lead.id?response.data.lead:lead);
 					}else{
 						scope.lead = response.data.lead
 					}
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		saveNote: function (scope, lead, note) {
 			$http.post(local_server_url + '/leads/' + lead.id + '/addNote', note).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.successMessages = response.data.successMessages
 					scope.lead = response.data.lead
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		createInvitation: function (scope, invitation, lead) {
 			$http.post(local_server_url + '/leads/' + lead.id + '/invitations', invitation).then(function (response) {
 				if(response.data.invitation)
 					scope.lead.invitations.push(response.data.invitation)
 				
 				scope.successMessages = response.data.successMessages
 				scope.errorMessages = response.data.errorMessages
 				
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		deleteInvitation: function (scope, invitation, lead) {
 			$http.delete(local_server_url + '/leads/'+lead.id+'/invitations/' + invitation.id).then(function (response) {
 				if(!response.data.errorMessages)
 					scope.lead.invitations = scope.lead.invitations.filter(i=>i.id != invitation.id)
 				scope.successMessages = response.data.successMessages
 				scope.errorMessages = response.data.errorMessages
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		findLead: function (scope, leadId) {
 			$http.get(local_server_url + '/leads/'+leadId).then(function (response) {
 				if(!response.data.errorMessages)
 					scope.lead = response.data.lead
 				scope.successMessages = response.data.successMessages
 				scope.errorMessages = response.data.errorMessages
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		sendInvitationByEmail: function (scope, invitation) {
 			$http.post(local_server_url + '/leads/'+invitation.lead.id+'/invitations/'+invitation.id+'/email', invitation).then(function (response) {
 				if(!response.data.errorMessages)
 					invitation.emailed++
 				scope.successMessages = response.data.successMessages
 				scope.errorMessages = response.data.errorMessages
 			}, function (response) {
 				console.log('aconteceu um erro', response)
 			});
 		},





 	} 
 })


 /*MAIN CONTROLLER*/
 .controller('LeadController', function ($scope, leadService, companyService, personService, userService, proposalService, FileUploader) {  

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
 			proposalService.update($scope, proposal, lead.id);
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
            $scope.lead.medias.push({id:'', type:fileItem.file.type, name:fileItem.file.name})
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

 })

