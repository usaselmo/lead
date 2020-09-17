

var proposalCrud = angular.module('docsRestrictDirective', []);

proposalCrud.service('proposalService', proposalService);
proposalCrud.service('leadService', leadService);

proposalCrud.directive('proposalCrud', function() {
  return {
    restrict: 'E',
    scope: {
    	proposal: '=', 
    	lead: '=',
    },
    templateUrl: '/include/lead/detail-nav-tab-proposal-crud.html' , 
    controller: function($scope, proposalService, leadService){

		$scope.proposalCancel = function(){
		 	//$scope.proposal = null;
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
		            .success( (response) => $scope.reloadLead(lead) );
		    }else{
		        proposalService.save($scope, proposal, lead.id)
		    }
		}

		$scope.reloadLead = function(lead){
		 leadService.findLead($scope, lead.id)
		}

    },
  };
});

