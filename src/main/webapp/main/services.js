'use strict';

angular.module('zeikona.services', [])
    .factory('Conf', function($location) {
        function getRootUrl() {
            var rootUrl = $location.protocol() + '://' + $location.host();
            if ($location.port())
                rootUrl += ':' + $location.port();
            return rootUrl;
        };
        return {
            'clientId': '13487503673-fl85v9f6eket7u0u6r5pq42qnkkbmfc0.apps.googleusercontent.com',
            'apiBase': '/api/',
            'rootUrl': getRootUrl(),
            'scopes': 'https://www.googleapis.com/auth/plus.login ',
            'requestvisibleactions': 'http://schemas.google.com/AddActivity ' +
                'http://schemas.google.com/ReviewActivity',
            'cookiepolicy': 'single_host_origin',
            // If you have an android application and you want to enable
            // Over-The-Air install, remove the comment below and use the package
            // name associated to the project's Client Id for installed applications
            // in the Google API Console.
            //'apppackagename': 'YOUR_APP_PACKAGE'
        };
    })
    .factory('ZeikonaApi', function($http, Conf) {
        return {
            signIn: function(authResult) {
                return $http.post(Conf.apiBase + 'connect', authResult);
            },
            disconnect: function() {
                return $http.post(Conf.apiBase + 'disconnect');
            }
        };
    })
;