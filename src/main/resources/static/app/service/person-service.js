
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

 		findClients: function (scope, callBackFunction) {
 			$http.get(local_server_url + "/persons").then(function (response) {
 				scope.clients = response.data
 				callBackFunction.apply();
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		update: function (scope, client) {
 			$http.put(local_server_url + "/persons", client).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					client = response.data.client
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		save: function (scope, client) {
 			$http.post(local_server_url + "/persons", client).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.clients.push(response.data.client)
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 };
