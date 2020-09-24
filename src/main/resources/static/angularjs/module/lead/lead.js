var applead = angular.module('app.module.lead', []);

applead.service('companyService', companyService);
applead.service('leadService', leadService);
applead.service('personService', personService);
applead.service('proposalService', proposalService);
applead.service('userService', userService);

applead.directive('appLead', function() {
	return {
		restrict : 'E',
		scope : {},
		templateUrl : '/angularjs/module/lead/lead.html',
		controller : function($scope, leadService, companyService, personService, userService, proposalService) {
			var pageRange = 0;
			var lines = 10;

			$scope.list = function() {
				$scope.lead = null;
				$scope.crudLead = null;
				$scope.reload($scope.event, $scope.search);
			}

			$scope.getNextListRange = function(numero) {
				if (numero > 0 && ((pageRange + 1) * lines) < $scope.totalLeads)
					pageRange++
				if (numero < 0 && pageRange > 0)
					pageRange--
				leadService.findLeads($scope, pageRange, lines, $scope.event, $scope.search)
			}

			$scope.reload = function(event, search) {
				$scope.event = event;
				$scope.search = search;
				pageRange = 0;
				leadService.findLeads($scope, pageRange, lines)
			}

			var init = function() {
				leadService.findLeads($scope, pageRange, lines);
			}

			init();

		}
	};
});
