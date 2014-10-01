'use strict';

angular.module('zeikona.services', [])
    .factory('Conf', function() {
        return {
            'clientId': '13487503673-mgiqme47e1uf23upgnqo29u1tif49up1.apps.googleusercontent.com',
            'scopes': 'https://www.googleapis.com/auth/plus.login ' + 'https://picasaweb.google.com/data/ ',
            'requestvisibleactions': 'http://schemas.google.com/AddActivity ' +
                'http://schemas.google.com/ReviewActivity',
            'cookiepolicy': 'single_host_origin'
        };
    })
    .factory('ZeikonaApi', function($http, Conf) {
        return {
            signIn: function(authResult, cb) {
                gapi.client.initialization.connect(authResult).execute(cb);
            },
            getAllPhotos: function(cb) {
                gapi.client.photo.allPhotos().execute(cb);
            }
        };
    })
;