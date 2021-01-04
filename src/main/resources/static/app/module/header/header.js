var header = angular.module('app.module.header', []);

header.directive('appHeader', function() {
	return {
		restrict : 'E',
		scope : {
			modalId: '='
				},
		template : '<h1>Leads DC <small>Version 2.2.9</small></h1>',
	};
});

export default header