

 app.service('userService', function($http){
 	return {
 		findEstimators: function (scope) {
 			$http.get(local_server_url + "/users/estimators").then(function (response) {
 				scope.estimators = response.data
 			}, function (response) {
 				console.log(response)
 			});
 		},
 	} 
 });