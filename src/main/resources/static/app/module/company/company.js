var company = angular.module('app.module.company', []);

company.directive('appCompany', function() {
	return {
		restrict : 'E',
		scope : {},
		templateUrl : '/app/module/company/company.html',
	};
});

export default company