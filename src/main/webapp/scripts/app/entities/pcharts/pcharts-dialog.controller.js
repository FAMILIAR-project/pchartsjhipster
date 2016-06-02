'use strict';

angular.module('pchartsjhipsterApp').controller('PchartsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pcharts',
        function($scope, $stateParams, $uibModalInstance, entity, Pcharts) {

        $scope.pcharts = entity;
        $scope.load = function(id) {
            Pcharts.get({id : id}, function(result) {
                $scope.pcharts = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pchartsjhipsterApp:pchartsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.pcharts.id != null) {
                Pcharts.update($scope.pcharts, onSaveSuccess, onSaveError);
            } else {
                Pcharts.save($scope.pcharts, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
