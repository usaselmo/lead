var fileUploader = angular.module('app.module.lead.file-uploader', [
  'angularFileUpload',
]);


fileUploader.directive('appFileUploader', function () {
  return {
    restrict: 'E',
    scope: {
      url: '=',
    },
    templateUrl: '/app/module/lead/file-uploader/file-uploader.html',
    controller: fileUploaderController,
  };
});

var fileUploaderController = function ($scope, FileUploader) {
  
  $scope.uploader = new FileUploader();

  $scope.uploadd = function (uploader, lead) {

    uploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.log('onSuccessItem called')
    };

    uploader.onCompleteAll = function () {
      uploader.clearQueue();
    }

    uploader.queue.forEach(item => {
      item.url = $scope.url
    })

  }

}

export default fileUploader