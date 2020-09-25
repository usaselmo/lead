var company = angular.module('app.module.company', []);

company.directive('appCompany', function() {
	return {
		restrict : 'E',
		scope : {},
		templateUrl : '/app/module/company/company.html',
		controller: companyController,
	};
});

var companyController = function ($scope, $http, $timeout, companyService) {
	var init = function(){
		companyService.findCompanies($scope);
	}

	$scope.crud = function(company){
		$scope.company = company; 
	}

	$scope.cancel = function(){
		$scope.company = null
	}

	$scope.save = function(company){
		if(company.id){
			companyService.update($scope, company)
		}else{
			companyService.save($scope, company)
		}
		$scope.cancel();
	}

	init();
}

export default company