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
            'clientId': '13487503673-h0ejrqftar980f4vkk3o76q0l6mu69at.apps.googleusercontent.com',
            'scopes': 'https://www.googleapis.com/auth/plus.login ' + 'https://picasaweb.google.com/data/ ',
            'requestvisibleactions': 'http://schemas.google.com/AddActivity ' +
                'http://schemas.google.com/ReviewActivity',
            'cookiepolicy': 'single_host_origin'
        };
    })
    .factory('ZeikonaApi', function($http, Conf) {
        return {
            getAllPhotos: function(cb) {
                gapi.client.zeikona.photo.allPhotos().execute(cb);
            }
        };
    })
;