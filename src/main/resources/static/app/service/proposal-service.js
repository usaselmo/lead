
var originalLines = [];

var lineBreaker = "\n";

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
};

var copy = function (obj) {
	return JSON.parse(JSON.stringify(obj))
};


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
};

var proposalService = function($http){
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
			var result = $http.put(local_server_url + "/proposals?leadId=" + lead_id, convertToServerFormat(proposal));
			result.then(function (response) {
				if(response.data.proposal){
					scope.lead.proposals = scope.lead.proposals.map(p=>p.id==response.data.proposal.id?response.data.proposal:p)
					proposal = response.data.proposal
				}
				scope.successMessages = response.data.successMessages
				scope.errorMessages = response.data.errorMessages
			}, function (response) {
				console.log(response)
			});
			return result;
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
};


export default proposalService