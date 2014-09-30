'use strict';

angular.module('zeikona.home', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {
    templateUrl: 'home/home.html'
  });
}])

.controller('ZHomeCtrl', ['$scope', 'Conf', 'ZeikonaApi', '$log', function($scope, Conf, ZeikonaApi, $log) {

    $scope.userProfile = {'name':''};
    $scope.hasUserProfile = false;
    $scope.isSignedIn = false;
    $scope.immediateFailed = false;

    $scope.signIn = function(authResult) {
        $scope.$apply(function() {
            $scope.processAuth(authResult);
        });
    }

    $scope.signedIn = function (profile) {
        $scope.isSignedIn = true;
        $scope.userProfile = profile;
        $scope.hasUserProfile = true;
    };

    $scope.processAuth = function(authResult) {
        $scope.immediateFailed = true;
        if ($scope.isSignedIn) {
            return 0;
        }
        if (authResult['access_token']) {
            $scope.immediateFailed = false;
            // Successfully authorized, create session
            ZeikonaApi.signIn(authResult, function(response) {
                $scope.signedIn(response);
                $scope.$apply();
            });
        } else if (authResult['error']) {
            if (authResult['error'] == 'immediate_failed') {
                $scope.immediateFailed = true;
            } else {
                $log.error('Error: ' + authResult['error']);
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

    $log.info("HomeCtrl initialization");
    $scope.renderSignIn();

}]);