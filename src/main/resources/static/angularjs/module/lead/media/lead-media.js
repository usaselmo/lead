var proposalMedia = angular.module('app.module.lead.media', [
	'angularFileUpload', 
	]);

proposalMedia.service('leadService', leadService);

proposalMedia.directive('appLeadMedia', function() {
	return {
		restrict : 'E',
		scope : {
			lead : '=',
		},
		templateUrl : '/angularjs/module/lead/media/lead-media.html',
		controller : function($scope, leadService, FileUploader) {
			   $scope.uploader = new FileUploader();

			   $scope.uploadd = function(uploader, lead){

			    uploader.onSuccessItem = function(fileItem, response, status, headers) {
			        if(!lead.medias)
			            lead.medias = [];
			        lead.medias.push({id:'', type:fileItem.file.type, name:fileItem.file.name})
			        $scope.lead = lead;
			    };

			    uploader.onCompleteAll = function(){
			        uploader.clearQueue();
			    }

			    uploader.queue.forEach(item=>{
			        item.url = '/main/leads/' + lead.id + '/file-upload'
			        item.upload();
			    })

			}

		},
	};
});