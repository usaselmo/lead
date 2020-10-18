var local_server_url = "";

export default function leadService($http) {
	return {

		reloadLead: function (lead) {
			var res = $http.get(local_server_url + '/leads/' + lead.id);
			res.success(data =>{
				lead = data.lead
			}  )
			return res;
		},

		find_Leads: function (scope) {
			var res = $http.get(local_server_url + "/leads?pageRange=" + scope.filter.pageRange + "&lines=" + scope.filter.lines + "&event=" + scope.filter.event + "&text=" + scope.filter.searchText);
			res.error(error=>console.log(error))
			return res;
		},

		findLeads: function (scope) {
			if (scope.filter.event == 'ALL' || !scope.filter.event) scope.filter.event = '';
			if (!scope.filter.searchText) scope.filter.searchText = '';
			$http.get(local_server_url + "/leads?pageRange=" + scope.filter.pageRange + "&lines=" + scope.filter.lines + "&event=" + scope.filter.event + "&text=" + scope.filter.searchText).then(function (response) {
				scope.leads = response.data.leads
				scope.leadsTotalPrice = response.data.leadsTotalPrice
				scope.totalLeads = response.data.totalLeads
				scope.leadTypes = response.data.leadTypes;
				scope.events = response.data.events;
			}, function (response) {
				console.log(response)
			});
		},

		update: function (lead) {
			var res = $http.put(local_server_url + "/leads", lead);
			res.error(error => console.log(error))
			return res;
		},

		save: function (lead) {
			var res = $http.post(local_server_url + "/leads", lead);
			res.error( error => console.log(error))
			return res;
		},

		fireEvent: function (lead, event) {
			var res = $http.post(local_server_url + "/leads/" + lead.id + "/fireevent", event);
			res.error( error => console.log(error))
			return res;
		},

		saveNote: function (lead, note) {
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
			$http.delete(local_server_url + '/leads/' + lead.id + '/invitations/' + invitation.id).then(function (response) {
				if (!response.data.errorMessages)
					scope.lead.invitations = scope.lead.invitations.filter(i => i.id != invitation.id)
				scope.successMessages = response.data.successMessages
				scope.errorMessages = response.data.errorMessages
			}, function (response) {
				console.log(response)
			});
		},

		findLead: function (leadId) {
			var res = $http.get(local_server_url + '/leads/' + leadId);
			res.error(error => console.log(error))
			return res;
		},

		sendInvitationByEmail: function (scope, invitation) {
			$http.post(local_server_url + '/leads/' + invitation.lead.id + '/invitations/' + invitation.id + '/email', invitation).then(function (response) {
				if (!response.data.errorMessages)
					invitation.emailed++
				scope.successMessages = response.data.successMessages
				scope.errorMessages = response.data.errorMessages
			}, function (response) {
				console.log('aconteceu um erro', response)
			});
		},

		markInvitationAsEmailed: function (invitation, lead) {
			var res = $http.put(local_server_url + '/leads/' + lead.id + '/invitations/' + invitation.id + '/email');
			return res;
		},

	}
};


