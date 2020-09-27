var local_server_url = "";

export default function leadService($http){
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
 			var res = $http.post(local_server_url + '/leads/' + lead.id + '/addNote', note); 
 			return res;
 		},
 		
 		createInvitation: function (scope, invitation, lead) {
 			var res = $http.post(local_server_url + '/leads/' + lead.id + '/invitations', invitation);
 			return res;
 		},
 		
 		updateInvitation: function (scope, invitation, lead) {
 			var res = $http.put(local_server_url + '/leads/' + lead.id + '/invitations', invitation);
 			return res;
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
 		
 		findLead: function (leadId) {
			var res = $http.get(local_server_url + '/leads/'+leadId);
			res.error(error=>console.log(error))
			return res; 
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
 };


