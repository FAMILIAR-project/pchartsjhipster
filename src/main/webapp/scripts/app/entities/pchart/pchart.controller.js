'use strict';

angular.module('pchartsjhipsterApp')
    .controller('PchartController', function ($scope, $state, Pchart) {

        $scope.pcharts = [];
        $scope.loadAll = function() {
            Pchart.query(function(result) {
               $scope.pcharts = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pchart = {
                name: null,
                id: null
            };
        };
    });
