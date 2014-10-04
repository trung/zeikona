'use strict';

angular.module('zeikona.grid.grid-directive', [])

    .directive('zeikonaGrid', ['$window', '$log', function($window, $log) {
        var containerElm;
        var data = [];

        var $nz = function(value, defaultvalue) {
            if( typeof (value) === undefined || value == null) {
                return defaultvalue;
            }
            return value;
        };

        function calculateCutOff(len, delta, items) {
            // resulting distribution
            var cutoff = [];
            var cutsum = 0;

            // distribute the delta based on the proportion of
            // thumbnail size to length of all thumbnails.
            for(var i in items) {
                var item = items[i];
                var fractOfLen = item.width / len;
                cutoff[i] = Math.floor(fractOfLen * delta);
                cutsum += cutoff[i];
            }

            // still more pixel to distribute because of decimal
            // fractions that were omitted.
            var stillToCutOff = delta - cutsum;
            while(stillToCutOff > 0) {
                for(i in cutoff) {
                    // distribute pixels evenly until done
                    cutoff[i]++;
                    stillToCutOff--;
                    if (stillToCutOff == 0) break;
                }
            }
            return cutoff;
        }

        function createDataRow(maxwidth, items  ) {
            var row = [], len = 0;

            // each image a has a 3px margin, i.e. it takes 6px additional space
            var marginsOfImage = 4;

            // Build a row of images until longer than maxwidth
            while(items.length > 0 && len < maxwidth) {
                var item = items.shift();
                row.push(item);
                len += (item.width + marginsOfImage);
            }

            // calculate by how many pixels too long?
            var delta = len - maxwidth;

            // if the line is too long, make images smaller
            if(row.length > 0 && delta > 0) {

                // calculate the distribution to each image in the row
                var cutoff = calculateCutOff(len, delta, row);

                for(var i in row) {
                    var pixelsToRemove = cutoff[i];
                    item = row[i];

                    // move the left border inwards by half the pixels
                    item.vx = Math.floor(pixelsToRemove / 2);

                    // shrink the width of the image by pixelsToRemove
                    item.vwidth = item.width - pixelsToRemove;
                }
            } else {
                // all images fit in the row, set vx and vwidth
                for(var i in row) {
                    item = row[i];
                    item.vx = 0;
                    item.vwidth = item.width;
                }
            }

            return row;
        }

        function updateDataElement(item) {

        }

        function createDataElement(item) {
            var imageContainer = angular.element('<div class="imageContainer"/>');

            var overflow = angular.element("<div/>");
            overflow.css("width", ""+$nz(item.vwidth, 120)+"px");
            overflow.css("height", ""+ 120 +"px");
            overflow.css("overflow", "hidden");

            var img = angular.element("<img/>");
            img.attr("src", item.thumbnail);
            // img.attr("title", item.title);
            img.css("width", "" + $nz(item.width, 120) + "px");
            img.css("height", "" + $nz(item.height, 120) + "px");
            img.css("margin-left", "" + (item.vx ? (-item.vx) : 0) + "px");
            img.css("margin-top", "" + 0 + "px");
            // img.hide();

            overflow.append(img);
            imageContainer.append(overflow);
            containerElm.append(imageContainer);

            item.el = imageContainer;
            return imageContainer;
        }

        function showPhotos() {
            var containerWidth = containerElm[0].offsetWidth - 1;
            var cloneData = data.slice();

            var rows = [];
            while (cloneData.length > 0) {
                rows.push(createDataRow(containerWidth, cloneData));
            }

            for(var r in rows) {
                for(var i in rows[r]) {
                    var item = rows[r][i];
                    if(item.el) {
                        updateDataElement(item);
                    } else {
                        createDataElement(item);
                    }
                }
            }
        }

        function link(scope, element, attrs) {
            containerElm = element;

            scope.$watchCollection('dataprovider', function(updatedData){
                if (updatedData != undefined) {
                    data = updatedData;
                    showPhotos();
                }
            });

            angular.element($window).bind('resize', function() {
                //showPhotos();
            });
        }

        return {
            restrict: 'EA',
            scope : {
                dataprovider : '='
            },
            link: link,
            templateUrl: 'components/grid/grid-template.html'
        };
    }]);