import proposalCrud from '/app/module/lead/proposal/crud/crud.js';
import proposalService from '/app/service/proposal-service.js'
import list from '/app/module/lead/proposal/list/list.js'

var copy = function (obj) {
    return JSON.parse(JSON.stringify(obj))
};

var leadProposal = angular.module('app.module.lead.proposal', [
	'app.module.proposal.list',
	'app.module.lead.proposal.crud',]);

leadProposal.service('proposalService', proposalService);

leadProposal.directive('appLeadProposal', function () {
    var originalLines = [];
    return {
        retrict: 'E',
        templateUrl: '/app/module/lead/proposal/lead-proposal.html',
        scope: {
            lead: '=',
        },
        controller: ['$scope', 'proposalService', leadProposalController],
    }
})

var leadProposalController = function ($scope, proposalService) {
	
	$scope.proposal = null;

}

export default proposalCrud