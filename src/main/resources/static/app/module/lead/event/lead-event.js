var proposalEvent = angular.module('app.module.lead.event', []);

proposalEvent.service('leadService', leadService);

proposalEvent.directive('appLeadEvent', function() {
	return {
		restrict : 'E',
		scope : {
			lead : '=',
		},
		templateUrl : '/app/module/lead/event/lead-event.html',
	};
});


export default proposalEvent