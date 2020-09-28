var loading = angular.module('app.module.loading', []);

loading.directive('appLoading', function () {
	return {
		restrict: 'E',
		scope: {},
		templateUrl: '/app/module/loading/loading.html',
		controller: function ($scope) {
			
		}
	};
});

export default loading
