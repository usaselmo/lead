var local_server_url = "";

 var companyService = function($http){
	 return {

		 search: function (text) {
			 return $http.get(local_server_url + "/companies/search/"+text);
		 },

		 findCompanies: function (scope) {
			 $http.get(local_server_url + "/companies").then(function (response) {
				 scope.companies = response.data.companies
			 }, function (response) {
				 console.log(response)
			 });
		 },
 		
 		update: function (scope, company) {
 			$http.put(local_server_url + "/companies", company).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					company = response.data.company
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},
 		
 		save: function (scope, company) {
 			$http.post(local_server_url + "/companies", company).then(function (response) {
 				if(response.data.errorMessages){
 					scope.errorMessages = response.data.errorMessages
 				}else{
 					scope.companies.push(response.data.company)
 					scope.successMessages = response.data.successMessages
 				}
 			}, function (response) {
 				console.log(response)
 			});
 		},

 		
 	} 
 }
 
 export default companyService