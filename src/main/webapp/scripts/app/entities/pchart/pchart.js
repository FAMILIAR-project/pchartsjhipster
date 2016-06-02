'use strict';

angular.module('pchartsjhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pchart', {
                parent: 'entity',
                url: '/pcharts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pchartsjhipsterApp.pchart.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pchart/pcharts.html',
                        controller: 'PchartController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pchart');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pchart.detail', {
                parent: 'entity',
                url: '/pchart/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pchartsjhipsterApp.pchart.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pchart/pchart-detail.html',
                        controller: 'PchartDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pchart');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pchart', function($stateParams, Pchart) {
                        return Pchart.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pchart.new', {
                parent: 'pchart',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pchart/pchart-dialog.html',
                        controller: 'PchartDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pchart', null, { reload: true });
                    }, function() {
                        $state.go('pchart');
                    })
                }]
            })
            .state('pchart.edit', {
                parent: 'pchart',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pchart/pchart-dialog.html',
                        controller: 'PchartDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pchart', function(Pchart) {
                                return Pchart.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pchart', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('pchart.delete', {
                parent: 'pchart',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pchart/pchart-delete-dialog.html',
                        controller: 'PchartDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Pchart', function(Pchart) {
                                return Pchart.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pchart', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
