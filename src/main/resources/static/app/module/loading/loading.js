var loading = angular.module('app.module.loading', []);

loading.directive('appLoading', function () {
	return {
		restrict: 'EA',
		templateUrl: '/app/module/loading/loading.html',
	};
});

export default loading
