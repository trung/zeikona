'use strict';

angular.module('zeikona.grid', [
    'zeikona.grid.grid-directive'
]);

angular.module('zeikona.grid.grid-directive', [])
    .factory('zeikonaGridProvider', function() {
        var minHeight = 120;
        var minWidth = 120;
        var margin = 2;
        return {
            setMinHeight : function(h) {
                minHeight = h;
            },
            setMinWidth : function(w) {
                minWidth = w;
            },
            setMargin : function(m) {
                margin = m;
            },
            minHeight : minHeight,
            minWidth: minWidth,
            margin: margin
        }
    })
    .directive('zeikonaGrid', ['$window', '$log', 'zeikonaGridProvider', '$rootScope', function($window, $log, zeikonaGridProvider, $rootScope) {
        var containerElm;
        var data = []; // current set of data
        var lastRow = []; // contains the data element of the last row

        var getHeight = function(elem) {
            elem = elem[0] || elem;
            if (isNaN(elem.offsetHeight)) {
                return getHeight(elem.document.documentElement);
            } else {
                return elem.offsetHeight;
            }
        };
        var getOffsetTop = function(elem) {
            if (!elem[0].getBoundingClientRect || elem.css('none')) {
                return;
            }
            return elem[0].getBoundingClientRect().top + getPageYOffset(elem);
        };
        var getPageYOffset = function(elem) {
            elem = elem[0] || elem;
            if (isNaN(window.pageYOffset)) {
                return elem.document.documentElement.scrollTop;
            } else {
                return elem.ownerDocument.defaultView.pageYOffset;
            }
        };

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

            var marginsOfImage = zeikonaGridProvider.margin;

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

                    // center the image
                    item.vy = Math.max(0, Math.floor(item.height - zeikonaGridProvider.minHeight) / 2);
                }
            } else {
                // all images fit in the row, set vx and vwidth
                for(var i in row) {
                    item = row[i];
                    item.vx = 0;
                    item.vy = Math.max(0, Math.floor(item.height - zeikonaGridProvider.minHeight) / 2);
                    item.vwidth = item.width;
                }
            }

            return row;
        }

        function updateDataElement(item) {
            var overflow = item.el.find("div:first");
            var img = overflow.find("img:first");

            overflow.css("width", "" + $nz(item.vwidth, zeikonaGridProvider.minWidth) + "px");
            overflow.css("height", "" + zeikonaGridProvider.minHeight +"px");

            img.css("margin-left", "" + (item.vx ? (-item.vx) : 0) + "px");
            img.css("margin-top", "" + (item.vy ? (-item.vy) : 0) + "px");
        }

        function createDataElement(item) {
            var imageContainer = angular.element('<div class="imageContainer"/>');

            var overflow = angular.element("<div/>");
            overflow.css("width", "" + $nz(item.vwidth, zeikonaGridProvider.minWidth) + "px");
            overflow.css("height", "" + zeikonaGridProvider.minHeight +"px");
            overflow.css("overflow", "hidden");

            var img = angular.element("<img/>");
            img.attr("src", item.thumbnail);
            // img.attr("title", item.title);
            img.css("width", "" + $nz(item.width, zeikonaGridProvider.minWidth) + "px");
            img.css("height", "" + $nz(item.height, zeikonaGridProvider.minWidth) + "px");
            img.css("margin-left", "" + (item.vx ? (-item.vx) : 0) + "px");
            img.css("margin-top", "" + (item.vy ? (-item.vy) : 0) + "px");
            // img.hide();

            overflow.append(img);
            imageContainer.append(overflow);
            containerElm.append(imageContainer);

            item.el = imageContainer;
            return imageContainer;
        }

        function showPhotos() {
            var containerWidth = $(containerElm).width() - 1;
            if (containerWidth == 0) {
                $log.error("Container width is 0");
                return;
            }
            var cloneData = lastRow.concat(data.slice());

            var rows = [];
            while (cloneData.length > 0) {
                rows.push(createDataRow(containerWidth, cloneData));
            }

            if (rows.length > 0) {
                lastRow = rows[rows.length - 1];
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
            var raw = element[0];

            scope.$watchCollection('dataprovider', function(updatedData){
                if (updatedData != undefined) {
                    data = updatedData;
                    showPhotos();
                }
            });

            angular.element($window).bind('scroll', function(){
                var containerBottom, elementBottom, remaining, shouldScroll;
                var container = angular.element($window);
                containerBottom = $(container).scrollTop() + $(container).height();
                elementBottom = getOffsetTop(element) + getHeight(element);
                remaining = elementBottom - containerBottom;
                shouldScroll = remaining <= 0.2 * $(container).height(); // 20% container height then load
                if (shouldScroll) {
//                    $log.debug("Should scroll as remaining is " + remaining);
                    if (scope.$$phase || $rootScope.$$phase) {
                        return scope.infiniteScroll();
                    } else {
                        return scope.$apply(scope.infiniteScroll);
                    }
                }
            });

//            angular.element($window).bind('resize', showPhotos);
        }

        return {
            restrict: 'EA',
            scope : {
                dataprovider : '=',
                infiniteScroll : '&'
            },
            link: link,
            templateUrl: 'components/grid/grid-template.html'
        };
    }]);

