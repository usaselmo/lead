var loading = angular.module('app.module.loading', []);

loading.directive('appLoading', function() {
	return {
		restrict : 'E',
		scope : {},
		template : 'LOADING ... ',
	};
});

export default loading
