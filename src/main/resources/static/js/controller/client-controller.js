
app_client.service('clientService', personService);

var initTable = function(){
	[1,2,3].forEach(function(i) {
		setTimeout(function(){ $('#94irjrn494h').DataTable(); }, i*0);
	});
};

/*COMPANY CONTROLLER*/
app_client.controller('ClientController', function ($scope, $http, $timeout, clientService) {
	var init = function(){
		clientService.findClients($scope, initTable);
	}

	$scope.crud = function(client){
		$scope.client = client; 
	}

	$scope.cancel = function(){
		$scope.client = null
		initTable();
	}

	$scope.save = function(client){
		if(client.id){
			clientService.update($scope, client)
		}else{
			clientService.save($scope, client)
		}
		$scope.cancel();
	}

	init();
});
