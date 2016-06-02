'use strict';

angular.module('pchartsjhipsterApp')
    .factory('Pcharts', function ($resource, DateUtils) {
        return $resource('api/pchartss/:id', {}, {
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


angular.module('pchartsjhipsterApp')
    .factory('Pcms', function ($resource, DateUtils) {
        return $resource('api/pcms/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                            method: 'GET',
                            transformResponse: function (data) {
                                data = angular.fromJson(data);
                                return data;
                            }
                        }
        });
    });
