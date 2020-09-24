var proposalEvent = angular.module('app.module.lead.event', []);

proposalEvent.service('leadService', leadService);

proposalEvent.directive('appLeadEvent', function() {
	return {
		restrict : 'E',
		scope : {
			lead : '=',
		},
		templateUrl : '/angularjs/module/lead/event/lead-event.html',
	};
});
