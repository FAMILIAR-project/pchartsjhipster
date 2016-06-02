 'use strict';

angular.module('pchartsjhipsterApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-pchartsjhipsterApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-pchartsjhipsterApp-params')});
                }
                return response;
            }
        };
    });
