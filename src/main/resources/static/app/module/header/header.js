var header = angular.module('app.module.header', []);

header.directive('appHeader', function() {
	return {
		restrict : 'E',
		scope : {},
		template : '<h1>Leads DC <small>Version 2.0.6</small></h1>',
	};
});
