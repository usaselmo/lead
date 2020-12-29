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

	$scope.text = '';
	$scope.search = function (text) {
		if (!text) $scope.companies = []
		else if (text.length < 3) return
		companyService.search(text).success(data=> {
			$scope.companies = data.companies;
		} )
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
}

export default company