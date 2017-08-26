var adminApp = angular.module('adminApp', []);

adminApp.controller('AdminController', function AdminController($scope,$http,$sce) {
    $scope.meetings = [];
    $http.get('/api/meetings/not-published').then(
        function(result) {
            $scope.meetings = result.data;
        });
    $scope.trust = $sce.trustAsHtml;

});