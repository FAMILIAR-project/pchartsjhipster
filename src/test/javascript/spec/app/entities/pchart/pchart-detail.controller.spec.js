'use strict';

describe('Controller Tests', function() {

    describe('Pchart Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPchart;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPchart = jasmine.createSpy('MockPchart');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Pchart': MockPchart
            };
            createController = function() {
                $injector.get('$controller')("PchartDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pchartsjhipsterApp:pchartUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
