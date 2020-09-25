import leadService from '/app/service/lead-service.js'
import proposalService from '/app/service/proposal-service.js'

var proposalCrud = angular.module('app.module.lead.proposal.crud', []);

proposalCrud.service('proposalService', proposalService);
proposalCrud.service('leadService', leadService);

proposalCrud.directive('proposalCrud', function() {
	return {
		restrict: 'E',
		scope: {
			proposal: '=', 
			lead: '=',
		},
		templateUrl: '/app/module/lead/proposal/lead-proposal-crud.html' , 
		controller: proposalCrudController,
	}; 
} );

var proposalCrudController = function($scope, proposalService, leadService) {
	
	$scope.proposalCancel = function(){
 	// $scope.proposal = null;
 }

 $scope.proposalEncreaseItem = function(proposal){
 	$scope.proposal.items.push({price: 0})
 }

 $scope.proposalRemoveItem = function(proposal){
 	if($scope.proposal.items.length > 1 ){
 		$scope.proposal.items.pop();
 	}
 }

 $scope.proposalSave = function(lead, proposal){
 	if(proposal.id){
 		proposalService.update($scope, proposal, lead.id)
 		.success( (response) => {
 			$scope.lead.proposals = $scope.lead.proposals.filter(p=>p.id != proposal.id)
 			$scope.lead.proposals.push(response.proposal)
 		} );
 	}else{
 		proposalService.save($scope, proposal, lead.id)
 	}
 }

};

export default proposalCrud