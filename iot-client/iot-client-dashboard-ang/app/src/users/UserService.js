(function() {
  'use strict';

  angular
      .module('users')
      .service('userService', userService);

  userService.$inject = ['$q', 'apiUrl'];

  /* @ngInject */
  function userService($q, apiUrl) {
    // Promise-based API
    return {
      loadAllUsers : function($http) {
        var getData = function() {

          //var apiUrl = "http://localhost:9090/";
          var url = apiUrl + "users";
          return $http.get(url).then(function(result){
            return result.data;
          });
        };
        return { getData: getData };

        //return $q.when(users);
      }
    };
  }
})();