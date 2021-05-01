var list = angular.module('app.module.proposal.list', []);

list.directive('proposalList', function() {
	return {
		restrict : 'E',
		scope : {
			proposals: '=',
			proposal:  '=',
		},
		templateUrl : '/app/module/lead/proposal/list/list.html',
		controller: ['$scope', function($scope){
			console.log($scope.proposal, $scope.proposals)
		}],
	};
});

export default list