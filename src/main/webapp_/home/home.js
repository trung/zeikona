'use strict';

angular.module('zeikona.home', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {
    templateUrl: 'home/home.html'
  });
}])

.controller('ZHomeCtrl', ['$scope', 'Conf', 'ZeikonaApi', '$log', '$window', '$rootScope', function($scope, Conf, ZeikonaApi, $log, $window, $rootScope) {

    $scope.userProfile = {};
    $scope.isSignedIn = false;
    $scope.photos = undefined;
    $scope.albumId = "";
    $scope.nextOffset = 1;
    $scope.offsetLoading = {};
    $scope.limit = 20000;

    $scope.signIn = function(authResult) {
        $scope.$apply(function() {
            $scope.processAuth(authResult);
        });
    }

    $scope.loadMore = function(refresh) {
        if (refresh) {
            $scope.photos = undefined;
        }
        if (!$scope.offsetLoading[$scope.nextOffset]) {
            $log.info("load more, start from " + $scope.nextOffset);
            $scope.loadAllPhotos($scope.nextOffset, $scope.limit);
        } else {
            // hmmm
        }
    }

    $scope.signedIn = function (profile) {
        $scope.isSignedIn = true;
        $scope.userProfile = profile;
        $scope.hasUserProfile = true;
        $scope.loadAllPhotos($scope.nextOffset, $scope.limit);
    };

    $scope.loadAllPhotos = function(offset, limit) {
        $scope.offsetLoading[offset] = true;
        ZeikonaApi.getAllPhotos(offset, limit, $scope.albumId, function(response) {
           $log.info(response);
           $scope.offsetLoading = {};
           $scope.nextOffset = offset + limit;
           $scope.albumId = response.albumId;
           $scope.photos = response.photos;
           $scope.$apply();
        });
    };

    $scope.processAuth = function(authResult) {
        if (authResult && !authResult.error) {
            // obtain user info
            var request = gapi.client.plus.people.get({
                'userId': 'me'
            });
            request.then(function(resp) {
                var profile = resp.result;
                $scope.signedIn(profile);
                $scope.$apply();
            }, function(reason) {
                $log.error('Unable to get user profile: ' + reason.result.error.message);
            });
        } else if (authResult && authResult.error != 'immediate_failed') {
            $log.error('Authentication failed: ' + authResult['error']);
            $scope.isSignedIn = false;
        } else {
            $scope.isSignedIn = false;
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
            'accesstype': 'offline',
            'immediate': true
        });
    }

    $log.info("HomeCtrl initialization");
    $scope.renderSignIn();

}]);