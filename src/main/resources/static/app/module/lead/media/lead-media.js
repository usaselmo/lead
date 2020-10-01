import leadService from '/app/service/lead-service.js'
import fileUploader from '/app/module/file-uploader/file-uploader.js'

var proposalMedia = angular.module('app.module.lead.media', [
  'angularFileUpload',
  'app.module.file-uploader',
]);

proposalMedia.service('leadService', leadService);

proposalMedia.directive('appLeadMedia', function () {
  return {
    restrict: 'E',
    scope: {
      lead: '=',
    },
    templateUrl: '/app/module/lead/media/lead-media.html',
    controller: ['$scope', 'FileUploader', 'leadService', mediaController],
  };
});

var mediaController = function ($scope, FileUploader, leadService) {

  $scope.url = '/main/leads/' + $scope.lead.id + '/file-upload';

  $scope.onCompleteAll = function (){leadService.reloadLead($scope.lead).success( data => $scope.lead = data.lead )}

}

export default proposalMedia