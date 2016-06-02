'use strict';

angular.module('pchartsjhipsterApp')
    .factory('Pchart', function ($resource, DateUtils) {
        return $resource('api/pcharts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
