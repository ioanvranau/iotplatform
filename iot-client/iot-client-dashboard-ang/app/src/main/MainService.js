(function() {
  'use strict';

  angular
      .module('main')
      .service('mainService', mainService);

  mainService.$inject = ['$q', 'apiUrl'];

  /* @ngInject */
  function mainService($q, apiUrl) {
    // Promise-based API
    return {
      loadAllDevices : function($http) {
        var getData = function() {

          var url = apiUrl + "devices";
          return $http.get(url).then(function(result){
            return result.data;
          });
        };
        return { getData: getData };
      }
    };
  }
})();