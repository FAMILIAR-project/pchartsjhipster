'use strict';

angular.module('pchartsjhipsterApp')
    .controller('PchartDetailController', function ($scope, $rootScope, $stateParams, entity, Pchart) {
        $scope.pchart = entity;
        $scope.load = function (id) {
            Pchart.get({id: id}, function(result) {
                $scope.pchart = result;
            });
        };
        var unsubscribe = $rootScope.$on('pchartsjhipsterApp:pchartUpdate', function(event, result) {
            $scope.pchart = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
