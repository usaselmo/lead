var user = angular.module('app.module.user', []);

user.directive('appUser', function() {
	return {
		restrict : 'E',
		scope : {},
		template : '<h1>This is User page</h1>',
	};
});

export default user