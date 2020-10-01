import leadService from '/app/service/lead-service.js'

var proposalNote = angular.module('app.module.lead.note', []);

proposalNote.service('leadService', leadService);

proposalNote.directive('appLeadNote', function() {
	return {
		restrict : 'E',
		scope : {
			lead : '=',
		},
		templateUrl : '/app/module/lead/note/lead-note.html',
		controller : function($scope, leadService) {

			$scope.saveNote = function(lead, newNote) {
				leadService.saveNote(lead, newNote)
				.success(data=>{
					$scope.lead = data.lead
					$scope.newNote = ''
				})
				.error(error=>console.log(error))
			}

		},
	};
});

export default proposalNote