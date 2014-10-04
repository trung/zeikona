'use strict';

// Declare app level module which depends on views, and components
angular.module('zeikona', [
  'ngRoute',
  'ui.bootstrap',
  'zeikona.services',
  'zeikona.home',
  'zeikona.view1',
  'zeikona.view2',
  'zeikona.version',
  'zeikona.grid.grid-directive'
], function ($locationProvider) {
    //$locationProvider.html5Mode(true);
})

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({redirectTo: '/home'});
}]);
