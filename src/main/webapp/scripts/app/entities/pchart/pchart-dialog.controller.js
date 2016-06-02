'use strict';

angular.module('pchartsjhipsterApp').controller('PchartDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pchart',
        function($scope, $stateParams, $uibModalInstance, entity, Pchart) {

        $scope.pchart = entity;
        $scope.load = function(id) {
            Pchart.get({id : id}, function(result) {
                $scope.pchart = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pchartsjhipsterApp:pchartUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.pchart.id != null) {
                Pchart.update($scope.pchart, onSaveSuccess, onSaveError);
            } else {
                Pchart.save($scope.pchart, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
