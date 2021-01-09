var fileUploader = angular.module('app.module.file-uploader', [
  'angularFileUpload',
]);


fileUploader.directive('appFileUploader', function () {
  return {
    restrict: 'E',
    scope: {
      url: '=', 
      suffixUrlWithFileName: '=',
      onCompleteAll: '&', 
    },
    templateUrl: '/app/module/file-uploader/file-uploader.html',
    controller: fileUploaderController,
  };
});

var fileUploaderController = function ($scope, FileUploader) {

  $scope.uploader = new FileUploader();

  $scope.uploadd = function (uploader) {

    uploader.onSuccessItem = function (fileItem, response, status, headers) {};

    uploader.onCompleteAll = function () {
      $scope.onCompleteAll({ 'uploader': uploader })
      uploader.clearQueue();
    }

    uploader.queue.forEach(item => {
      if (!$scope.suffixUrlWithFileName)
        item.url = $scope.url + '/' + item.file.name 
      else
        item.url = $scope.url;
      console.log(item.url)
      item.upload();
    })

  }

}

export default fileUploader