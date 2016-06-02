'use strict';

describe('Controller Tests', function() {

    describe('Pcharts Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPcharts;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPcharts = jasmine.createSpy('MockPcharts');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Pcharts': MockPcharts
            };
            createController = function() {
                $injector.get('$controller')("PchartsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pchartsjhipsterApp:pchartsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
