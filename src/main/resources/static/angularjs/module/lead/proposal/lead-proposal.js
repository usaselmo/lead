var leadProposal = angular.module('app.module.lead.proposal', []);

leadProposal.directive('appLeadProposal', function(){
	return {
		retrict: 'E',
		templateUrl: '/angularjs/module/lead/proposal/lead-proposal.html',
		scope: {
			lead:'=',
		}, 
		controller: ['$scope', function($scope){
			console.log($scope);
		}],
	}
})