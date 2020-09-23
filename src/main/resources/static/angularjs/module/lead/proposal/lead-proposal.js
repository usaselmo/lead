var leadProposal = angular.module('app.module.lead.proposal', []);

leadProposal.service('proposalService', proposalService);

leadProposal.directive('appLeadProposal', function() {
	return {
		retrict: 'E',
		templateUrl: '/angularjs/module/lead/proposal/lead-proposal.html',
		scope: {
			lead: '=',
		},
		controller: ['$scope', 'proposalService', function($scope, proposalService) {

			$scope.originalLines = [];

			var convertToClientFormat = function(proposal) {
				prop = copy(proposal)
				items = []
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

			$scope.emailProposal = function(proposal) {
				if (confirm('Send Proposal #' + proposal.number + ' via E-mail ? ')) {
					proposalService.sendByEmail($scope, proposal)
				}
			}

			$scope.proposalCopy = function(proposal) {
				nproposal = convertToClientFormat(proposal);
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

			$scope.proposalCrud = function(proposal, lead) {
				if (!proposal.id)
					proposal = createProposal(lead);
				else
					proposal = convertToClientFormat(proposal);
				$scope.proposal = proposal;
			}

			$scope.deleteProposal = function(lead, proposal) {
				if (confirm(' Are you sure you want to delete? ')) {
					proposalService.delete($scope, proposal, lead.id)
				}
			}



		}],
	}
})