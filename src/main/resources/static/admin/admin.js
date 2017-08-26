var adminApp = angular.module('adminApp', []);

adminApp.controller('AdminController', function AdminController($scope,$http,$sce,$location) {
    $scope.code = $location.$$absUrl.split('=')[1];
    $scope.trust = $sce.trustAsHtml;
    $scope.meetings = [];
    $http.get('/api/meetings/not-published').then(
        function(result) {
            $scope.meetings = result.data;
        });
    $scope.announce= function (id) {
        var req = {
            method: 'PUT',
            url: '/api/meetings/' + id + '/announcement',
            headers: {
                'Authorization': $scope.code
            }
        }
        $http(req).then(
            function(result) {
                alert('Meetup announced :' +  result.RegistrationID.value);
            },
            function(result) {
                alert(result.data.message);
            }
        );
    };
});