var zeikona = zeikona || {};

zeikona.appengine = zeikona.appengine || {};

zeikona.appengine.init = function(apiRoot, onReady) {
  var apisToLoad;
  var callback = function() {
      if (--apisToLoad == 0) {
          onReady();
      }
  }

  apisToLoad = 2;
  gapi.client.load("zeikona", "v1", callback, apiRoot);
  gapi.client.load("plus", "v1", callback);
};