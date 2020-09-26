import proposalCrud from '/app/module/lead/proposal/lead-proposal-crud.js';
import proposalService from '/app/service/proposal-service.js'
var copy = function (obj) {
    return JSON.parse(JSON.stringify(obj))
};
var leadProposal = angular.module('app.module.lead.proposal', ['app.module.lead.proposal.crud',]);
leadProposal.service('proposalService', proposalService);
leadProposal.directive('appLeadProposal', function () {
    var originalLines = [];
    return {
        retrict: 'E',
        templateUrl: '/app/module/lead/proposal/lead-proposal.html',
        scope: {
            lead: '=',
        },
        controller: ['$scope', 'proposalService', function ($scope, proposalService) {
            $scope.originalLines = [];
            var convertToClientFormat = function (proposal) {
                var prop = copy(proposal)
                var items = []
                prop.items.forEach(item => {
                    item.lines = item.lines.map(line => {
                        $scope.originalLines[item.id + line.description] = line.id;
                        return line.description;
                    }).join("\n")
                    items.push(item)
                })
                prop.items = items;
                return prop;
            }
            $scope.emailProposal = function (proposal) {
                if (confirm('Send Proposal #' + proposal.number + ' via E-mail ? ')) {
                    proposalService.sendByEmail($scope, proposal)
                }
            }
            $scope.proposalCopy = function (proposal) {
                var nproposal = convertToClientFormat(proposal);
                nproposal.total = 0;
                nproposal.id = null;
                nproposal.emailed = false;
                nproposal.number = null;
                nproposal.items.forEach(item => {
                    item.id = null;
                })
                originalLines = [];
                $scope.proposal = nproposal;
            }
            $scope.proposalCrud = function (proposal, lead) {
                if (!proposal.id) proposal = createProposal(lead);
                else proposal = convertToClientFormat(proposal);
                $scope.proposal = proposal;
            }
            var createProposal = function () {
                var prop = {
                    'items': [{
                        'price': 0, title: 'ITEM 1 - '
                    }],
                    'callMissUtility': true,
                    'scopeOfWork': 'Concrete work as per visit',
                    'paymentSchedule': '1. 50% down payment upon start of the project\n2. 50% final payment upon completion of the project.',
                    'workWarranty': 'All new material and labor are guaranteed for 36 months from completion date. All work to be completed in a neat and workmanlike manner. Warranty applies for concrete cracks that are 3/8" or greater in separation or height difference for flat concrete work. Warranty excludes the following: concrete damage caused by deicers such as salt or any deicer or fertilizer containing ammonium nitrate or ammonium sulfate, concrete spider cracks, hairline cracks and color variance.',
                    'total': 0,
                }
                $scope.currentProposal = prop;
                return prop;
            }
            $scope.deleteProposal = function (lead, proposal) {
                if (confirm(' Are you sure you want to delete? ')) {
                    proposalService.delete($scope, proposal, lead.id)
                }
            }
        }],
    }
})
export default proposalCrud