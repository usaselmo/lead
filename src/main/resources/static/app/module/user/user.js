var user = angular.module('app.module.user', []);

user.directive('appUser', function() {
	return {
		restrict : 'E',
		scope : {},
		templateUrl : '/app/module/user/user.html',
	};
});

export default user