var menu = angular.module('app.module.menu', []);

menu.directive('appMenu', function() {
	return {
		restrict : 'E',
		scope : {},
		templateUrl : '/angularjs/module/menu/menu.html',
	};
});
