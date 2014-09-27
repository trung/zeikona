'use strict';

// Declare app level module which depends on views, and components
angular.module('zeikona', [
  'ngRoute',
  'ui.bootstrap',
  'zeikona.home',
  'zeikona.view1',
  'zeikona.view2',
  'zeikona.version'
])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({redirectTo: '/home'});
}]);
