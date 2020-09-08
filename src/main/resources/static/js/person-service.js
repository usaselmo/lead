

 app.service('personService', function($http){
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
 });