var zeikona = zeikona || {};

zeikona.appengine = zeikona.appengine || {};

zeikona.appengine.init = function(apiRoot) {
  var apisToLoad;
  var callback = function() {
      if (--apisToLoad == 0) {
          // all apis are loaded
          console.log("All Cloud Endpoints are loaded");
      }
  }

  apisToLoad = 1;
  gapi.client.load("helloApi", "v1", callback, apiRoot);
};