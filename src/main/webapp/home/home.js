'use strict';

angular.module('zeikona.home', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {
    templateUrl: 'home/home.html'
  });
}])

.controller('ZHomeCtrl', ['$scope', 'Conf', 'ZeikonaApi', '$log', function($scope, Conf, ZeikonaApi, $log) {

    $scope.userProfile = {};
    $scope.isSignedIn = false;
    $scope.photos = [ {
        "thumbnail" : "https://lh3.googleusercontent.com/-SS8mRdkQft8/Towx76LDoYI/AAAAAAAABhk/HhtTYRXBx-g/s288/IMAG0186.jpg",
        "height" : 173,
        "width" : 288
    }, {
        "thumbnail" : "https://lh4.googleusercontent.com/-NCZRXpmaqcc/TowxuN7XhQI/AAAAAAAABhg/CaQ6mTp0n2A/s288/IMAG0185.jpg",
        "height" : 288,
        "width" : 173
    }, {
        "thumbnail" : "https://lh3.googleusercontent.com/-MMBk7oISauQ/TowxoGLmUiI/AAAAAAAABhc/WSaXb8-BR0Y/s288/IMAG0184.jpg",
        "height" : 173,
        "width" : 288
    }, {
        "thumbnail" : "https://lh4.googleusercontent.com/-pqVb76zr8Dw/TowxiMeXGSI/AAAAAAAABhY/z5uN4xGokd4/s288/IMAG0183.jpg",
        "height" : 173,
        "width" : 288
    }, {
        "thumbnail" : "https://lh6.googleusercontent.com/-TNHR5zSM_CY/TowxXKqBO_I/AAAAAAAABhU/l3eNl1p42Fo/s288/IMAG0182.jpg",
        "height" : 173,
        "width" : 288
    }, {
        "thumbnail" : "https://lh5.googleusercontent.com/-ompADRam_JY/TowxRG-8aGI/AAAAAAAABhQ/xiMbZCWJY8U/s288/IMAG0181.jpg",
        "height" : 173,
        "width" : 288
    }, {
        "thumbnail" : "https://lh4.googleusercontent.com/-Jz9k6lB8nKE/TowxLOErZII/AAAAAAAABhM/V-GWOdN8PxI/s288/IMAG0180.jpg",
        "height" : 173,
        "width" : 288
    }, {
        "thumbnail" : "https://lh3.googleusercontent.com/-JF9oKQUWLZs/TohEJZ35gdI/AAAAAAAABgc/OKqmkYBT3dA/s288/IMAG0179.jpg",
        "height" : 288,
        "width" : 173
    }, {
        "thumbnail" : "https://lh3.googleusercontent.com/-4y9akr0PLzs/TohFcwj_ETI/AAAAAAAABgg/tqquClGmdsM/s288/IMAG0178.jpg",
        "height" : 288,
        "width" : 173
    }, {
        "thumbnail" : "https://lh6.googleusercontent.com/-ppKZntTTXLw/TohF2ezkqiI/AAAAAAAABgk/rSt9UMUcvZ4/s288/IMAG0177.jpg",
        "height" : 173,
        "width" : 288
    } ];

    $scope.signIn = function(authResult) {
        $scope.$apply(function() {
            $scope.processAuth(authResult);
        });
    }

    $scope.signedIn = function (profile) {
        $scope.isSignedIn = true;
        $scope.userProfile = profile;
        $scope.hasUserProfile = true;
        $scope.loadAllPhotos();
    };

    $scope.loadAllPhotos = function() {
        ZeikonaApi.getAllPhotos(function(response) {
           $log.info(response);
           $scope.photos = response.photos;
//           $scope.photos.splice(0, $scope.photos);
//           for (var i = 0; i < response.photos.length; i++) {
//               $scope.photos.push(response.photos[i]);
//           }
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