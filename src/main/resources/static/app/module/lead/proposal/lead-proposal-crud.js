
import proposalService from '/app/service/proposal-service.js'

var proposalCrud = angular.module('app.module.lead.proposal.crud', []);

proposalCrud.service('proposalService', proposalService);

proposalCrud.directive('proposalCrud', function () {
    return {
        restrict: 'E',
        scope: {
            proposal: '=',
            lead: '=',
        },
        templateUrl: '/app/module/lead/proposal/lead-proposal-crud.html',
        controller: ['$scope', 'proposalService', proposalCrudController],
    };
});
var proposalCrudController = function ($scope, proposalService) {

    $scope.proposalCancel = function () {
        // $scope.proposal = null;
    }
    
    $scope.proposalEncreaseItem = function (proposal) {
        $scope.proposal.items.push({
            price: 0, title: 'ITEM ' + (proposal.items.length + 1) + ' - '
        })
    }
    $scope.proposalRemoveItem = function (proposal) {
        if ($scope.proposal.items.length > 1) {
            $scope.proposal.items.pop();
        }
    }
    $scope.proposalSave = function (lead, proposal) {
        if (proposal.id) {
            proposalService.update(proposal, lead.id).success( response => {
                lead.proposals = lead.proposals.filter(p => p.id != proposal.id)
                lead.proposals.push(response.proposal)
            });
        } else {
            proposalService.save(proposal, lead.id).success( data => lead.proposals.push(data.proposal) )
        }
    }
};
export default proposalCrud