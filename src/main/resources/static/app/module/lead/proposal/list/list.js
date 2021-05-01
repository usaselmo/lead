var list = angular.module('app.module.proposal.list', []);

list.directive('proposalList', function() {
	return {
		restrict: 'E',
		scope: {
			proposal: '=',
			lead: '=',
		},
		templateUrl: '/app/module/lead/proposal/list/list.html',
		controller: ['$scope', function($scope) {

			$scope.markAsAccepted = function(proposal) {
				if (confirm('Confirm change Accepted status ? ')) {
					proposalService.markAsAccepted(proposal).success(data => {
						$scope.lead.proposals = $scope.lead.proposals.map(p => p.id == data.proposal.id ? data.proposal : p)
					})
				}
			}

			$scope.markAsEmailed = function(proposal) {
				if (confirm('Confirm mark this document as e-mailed ? ')) {
					proposalService.markAsEmailed(proposal).success(data => {
						$scope.lead.proposals = $scope.lead.proposals.map(p => p.id == data.proposal.id ? data.proposal : p)
					})
				}

			}

			$scope.emailProposal = function(proposal) {
				if (confirm('Send Proposal #' + proposal.number + ' via E-mail ? ')) {
					proposalService.sendByEmail($scope, proposal)
				}
			}

			$scope.proposalCopy = function(proposal) {
				var nproposal = convertToClientFormat(proposal);
				nproposal.total = 0;
				nproposal.id = null;
				nproposal.emailed = false;
				nproposal.accepted = false;
				nproposal.number = null;
				nproposal.items.forEach(item => {
					item.id = null;
				})
				originalLines = [];
				$scope.proposal = nproposal;
				console.log($scope.proposal)
			}

			$scope.proposalCrud = function(proposal) {
				if (!proposal.id) proposal = createProposal($scope.lead);
				else proposal = convertToClientFormat(proposal);
				$scope.proposal = proposal;
			}

			$scope.deleteProposal = function(proposal) {
				if (confirm(' Are you sure you want to delete? ')) {
					proposalService.delete(proposal, $scope.lead.id).success(data => $scope.lead.proposals = $scope.lead.proposals.filter(p => p.id != proposal.id))
				}
			}
		}],
	};
});

var originalLines = [];

var createProposal = function (lead) {
    var prop = {
        'items': [{
            'price': 0, title: 'ITEM 1 - '
        }],
        'callMissUtility': true,
        'scopeOfWork': lead.title + ' at ' + lead.address + ' (Job #' + lead.id + ')\nConcrete work as per visit' ,
        'paymentSchedule': '50% down payment upon start of the project\n50% final payment upon completion of the project.',
        'workWarranty': 'All new material and labor are guaranteed for 36 months from completion date. All work to be completed in a neat and workmanlike manner. Warranty applies for concrete cracks that are 3/8" or greater in separation or height difference for flat concrete work. Warranty excludes the following: concrete damage caused by deicers such as salt or any deicer or fertilizer containing ammonium nitrate or ammonium sulfate, concrete spider cracks, hairline cracks and color variance.',
        'total': 0,
    }
    return prop;
}

var convertToClientFormat = function (proposal) {
    var prop = angular.copy(proposal)
    var items = []
    prop.items.forEach(item => {
        item.lines = item.lines.map(line => {
            originalLines[item.id + line.description] = line.id;
            return line.description;
        }).join("\n")
        items.push(item)
    })
    prop.items = items;
    return prop;
}

export default list