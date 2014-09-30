var zeikona = zeikona || {};

zeikona.appengine = zeikona.appengine || {};

zeikona.appengine.init = function(apiRoot, onReady) {
  var apisToLoad;
  var callback = function() {
      if (--apisToLoad == 0) {
          // all apis are loaded
          console.log("All Cloud Endpoints are loaded");
          onReady();
      }
  }

  apisToLoad = 1;
  gapi.client.load("helloApi", "v1", callback, apiRoot);
};