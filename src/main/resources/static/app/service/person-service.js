
 var local_server_url = "";

 var personService = function($http){
 
 	return {
 	
 		findPersons: function (scope) {
 			var res = $http.get(local_server_url + "/persons");
 			res.then(function (response) {
 				scope.persons = response.data
 			}, function (response) {
 				console.log(response)
 			});
 			return res;
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

 		findClients: function (scope) {
 			$http.get(local_server_url + "/persons").then(function (response) {
 				scope.clients = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		update: function (scope, client) {
 			var res = $http.put(local_server_url + "/persons", client);
 			res.error(error=>console.log(error))
 			return res;
 		},
 		
 		save: function (scope, client) {
 			var res = $http.post(local_server_url + "/persons", client);
 			res.error(error=>console.log(error))
 			return res;
 		},
 	} 
 };

 
 export default personService