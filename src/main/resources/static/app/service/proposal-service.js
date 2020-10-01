
var originalLines = [];

var lineBreaker = "\n";

var local_server_url = '';

var convertToClientFormat = function (proposal) {
	var prop = copy(proposal)
	var items = []
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
	var prop = copy(proposal)
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

		markAsEmailed: function(proposal){
			var res = $http.get(local_server_url + '/proposals/' + proposal.id + '/markasemailed');
			return res;
		},

		sendByEmail: function (scope, proposal) {
			$http.get(local_server_url + "/proposals/" + proposal.id + "/email").then(function (response) {
				scope.successMessages = response.data.successMessages;
				scope.errorMessages = response.data.errorMessages;
			}, function (response) {
				console.log(response)
			});
		},
		
		update: function (proposal, lead_id) {
			var result = $http.put(local_server_url + "/proposals?leadId=" + lead_id, convertToServerFormat(proposal));
			result.error( error => console.log(error))
			return result;
		},
		
		save: function (proposal, lead_id) {
			var res = $http.post(local_server_url + "/proposals?leadId=" + lead_id, convertToServerFormat(proposal));
			res.error( error => console.log(error))
			return res;
		},
		
		delete: function (proposal, lead_id) {
			var res = $http.delete(local_server_url + "/proposals?leadId=" + lead_id + "&proposalId=" + proposal.id );
			res.error( error => console.log(error))
			return res;
		},

	} 
};


export default proposalService