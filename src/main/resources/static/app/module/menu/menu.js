var menu = angular.module('app.module.menu', []);

menu.directive('appMenu', function() {
	return {
		restrict : 'E',
		scope : {},
		templateUrl : '/app/module/menu/menu.html',
	};
});

export default menu