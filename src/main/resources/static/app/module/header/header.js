var header = angular.module('app.module.header', []);

header.directive('appHeader', function() {
	return {
		restrict : 'E',
		scope : {},
		template : '<h1>Leads DC <small>Version 2.1.0</small></h1>',
	};
});

export default header