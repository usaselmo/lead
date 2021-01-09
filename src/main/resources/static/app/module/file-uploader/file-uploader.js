var fileUploader = angular.module('app.module.file-uploader', [
  'angularFileUpload',
]);


fileUploader.directive('appFileUploader', function () {
  return {
    restrict: 'E',
    scope: {
      url: '=', 
      onCompleteAll: '&', 
    },
    templateUrl: '/app/module/file-uploader/file-uploader.html',
    controller: fileUploaderController,
  };
});

var fileUploaderController = function ($scope, FileUploader) {

  $scope.uploader = new FileUploader({autoUpload: true, url: $scope.url });
  
  $scope.uploader.onCompleteAll = function () {
    $scope.onCompleteAll({ 'uploader': $scope.uploader })
    $scope.uploader.clearQueue();
  }

}

export default fileUploader