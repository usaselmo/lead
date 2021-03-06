
 var local_server_url = "";

 var personService = function($http){
 
	 return {

		 findByName: function (text) {
			 var res = $http.get(local_server_url + "/persons/search/"+text);
			 return res;
		 },

		 findPersons: function (scope) {
			 var res = $http.get(local_server_url + "/persons");
			 res.then(function (response) {
				 scope.persons = response.data.persons
			 }, function (response) {
				 console.log(response)
			 });
			 return res;
		 },
 		
 		sendCantReachEmail: function (lead, person, email) {
			 var res = $http.post(local_server_url + '/persons/'+person.id+'/leads/'+lead.id+'/cantreach', email);
			 res.error( error => console.log(error))
			 return res;
		 },
 		
 		sendHiringDecisionEmail: function (lead, person, mail) {
			 var res = $http.post(local_server_url + '/persons/'+person.id+'/leads/'+lead.id+'/hiringdecision', mail);
			 res.error( error => console.log(error))
			 return res;
		 },

		 findEmailAttachments: function (lead, person) {
			 var res = $http.get(local_server_url + '/persons/emailattachments/' + person.id + lead.id );
			 res.error(error => console.log(error))
			 return res;
		 },

		 deleteAttachment: function (lead, person, f) {
			 var res = $http.delete(local_server_url + '/persons/emailattachments/' + person.id + lead.id + '/' + f );
			 res.error(error => console.log(error))
			 return res;
		 },

 		findClients: function (scope) {
 			$http.get(local_server_url + "/persons").then(function (response) {
 				scope.clients = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		update: function (client) {
 			var res = $http.put(local_server_url + "/persons", client);
 			res.error(error=>console.log(error))
 			return res;
 		},
 		
 		save: function (client) {
 			var res = $http.post(local_server_url + "/persons", client);
 			res.error(error=>console.log(error))
 			return res;
 		},
 	} 
 };

 
 export default personService