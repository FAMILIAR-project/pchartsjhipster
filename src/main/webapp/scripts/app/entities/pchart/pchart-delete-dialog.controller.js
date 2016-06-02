'use strict';

angular.module('pchartsjhipsterApp')
	.controller('PchartDeleteController', function($scope, $uibModalInstance, entity, Pchart) {

        $scope.pchart = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Pchart.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
