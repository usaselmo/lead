var proposalNote = angular.module('app.module.lead.note', []);

proposalNote.service('leadService', leadService);

proposalNote.directive('appLeadNote', function() {
	return {
		restrict : 'E',
		scope : {
			lead : '=',
		},
		templateUrl : '/angularjs/module/lead/note/lead-note.html',
		controller : function($scope, leadService) {

			$scope.saveNote = function(lead, newNote) {
				leadService.saveNote($scope, lead, newNote).success(data=>$scope.newNote = '')
			}

		},
	};
});
