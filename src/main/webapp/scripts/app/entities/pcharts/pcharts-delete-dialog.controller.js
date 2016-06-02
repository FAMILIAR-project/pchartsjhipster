'use strict';

angular.module('pchartsjhipsterApp')
	.controller('PchartsDeleteController', function($scope, $uibModalInstance, entity, Pcharts) {

        $scope.pcharts = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Pcharts.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
