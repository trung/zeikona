'use strict';

angular.module('zeikona.services', [])
    .factory('Conf', function() {
        return {
            'clientId': '13487503673-mgiqme47e1uf23upgnqo29u1tif49up1.apps.googleusercontent.com',
            'scopes': 'https://www.googleapis.com/auth/plus.login ',
            'requestvisibleactions': 'http://schemas.google.com/AddActivity ' +
                'http://schemas.google.com/ReviewActivity',
            'cookiepolicy': 'single_host_origin'
        };
    })
    .factory('ZeikonaApi', function($http, Conf) {
        return {
            signIn: function(authResult, cb) {
                gapi.client.helloApi.say(authResult).execute(cb);
            },
            disconnect: function() {
                return $http.post(Conf.apiBase + 'disconnect');
            }
        };
    })
;