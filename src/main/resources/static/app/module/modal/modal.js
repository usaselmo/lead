var modal = angular.module('app.module.modal', []);

modal.directive('appModal', function() {
	return {
		restrict : 'E',
        transclude: true,
		scope : {
			title: '@', 
			spanClass: '='
		},
		templateUrl : '/app/module/modal/modal.html',
		controller: function($scope){

			$scope.code = (Math.random() + '_').replace(".", "")

			if(!$scope.spanClass)
				$scope.spanClass = 'glyphicon glyphicon-th'
		}
	};
});

export default modal