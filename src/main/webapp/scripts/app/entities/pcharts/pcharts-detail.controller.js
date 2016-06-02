'use strict';

angular.module('pchartsjhipsterApp')
    .controller('PchartsDetailController', function ($scope, $rootScope, $stateParams, entity, Pcharts) {
        $scope.pcharts = entity;
        $scope.load = function (id) {
            Pcharts.get({id: id}, function(result) {
                $scope.pcharts = result;
            });
        };
        var unsubscribe = $rootScope.$on('pchartsjhipsterApp:pchartsUpdate', function(event, result) {
            $scope.pcharts = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
