'use strict';

angular.module('zeikona.home', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {
    templateUrl: 'home/home.html',
    controller: 'ZHomeCtrl'
  });
}])

.controller('ZHomeCtrl', ['$scope', 'Conf', function($scope, Conf) {

    $scope.userProfile = undefined;
    $scope.hasUserProfile = false;
    $scope.isSignedIn = false;
    $scope.immediateFailed = false;

    $scope.signIn = function(authResult) {
        $scope.$apply(function() {
            $scope.processAuth(authResult);
        });
    }

    $scope.processAuth = function(authResult) {
        $scope.immediateFailed = true;
        if ($scope.isSignedIn) {
            return 0;
        }
        if (authResult['access_token']) {
            $scope.immediateFailed = false;
            // Successfully authorized, create session
            //PhotoHuntApi.signIn(authResult).then(function(response) {
            //    $scope.signedIn(response.data);
            //});
        } else if (authResult['error']) {
            if (authResult['error'] == 'immediate_failed') {
                $scope.immediateFailed = true;
            } else {
                console.log('Error:' + authResult['error']);
            }
        }
    }

    $scope.renderSignIn = function() {
        gapi.signin.render('myGsignin', {
            'callback': $scope.signIn,
            'clientid': Conf.clientId,
            'requestvisibleactions': Conf.requestvisibleactions,
            'scope': Conf.scopes,
            //'apppackagename': 'your.photohunt.android.package.name',
            'theme': 'dark',
            'cookiepolicy': Conf.cookiepolicy,
            'accesstype': 'offline'
        });
    }

    $scope.renderSignIn();

}]);