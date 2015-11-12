'use strict';

angular.module('zeikona.version', [
  'zeikona.version.interpolate-filter',
  'zeikona.version.version-directive'
])

.value('version', '0.1');
