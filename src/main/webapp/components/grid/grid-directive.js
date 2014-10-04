'use strict';

angular.module('zeikona.grid.grid-directive', [])

    .directive('zeikonaGrid', ['$window', '$log', function($window, $log) {
        var containerElm;

        $window.resize(function() {
            showPhotos();
        });

        function showPhotos() {
            $log.info(dataProvider);
        }

        function link(scope, element, attrs) {
            containerElm = element;
        }

        return {
            restrict: 'E',
            scope : {
                dataProvider: '=bindToThis'
            },
            link: link,
            templateUrl: 'grid-template.html'
        };
    }]);