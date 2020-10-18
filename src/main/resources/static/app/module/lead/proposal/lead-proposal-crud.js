
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
    
    $scope.proposalEncreaseItem = function () {
        $scope.proposal.items.push({
            price: 0, title: 'ITEM ' + ($scope.proposal.items.length + 1) + ' - '
        })
    }
    $scope.proposalRemoveItem = function () {
        if ($scope.proposal.items.length > 1) {
            $scope.proposal.items.pop();
        }
    }
    $scope.proposalSave = function () {
        if ($scope.proposal.id) {
            proposalService.update($scope.proposal, $scope.lead.id).success( response => {
                $scope.lead.proposals = $scope.lead.proposals.filter(p => p.id != $scope.proposal.id)
                $scope.lead.proposals.push(response.proposal)
            });
        } else {
            proposalService.save($scope.proposal, $scope.lead.id).success(data => $scope.lead.proposals.push(data.proposal) )
        }
    }
};
export default proposalCrud