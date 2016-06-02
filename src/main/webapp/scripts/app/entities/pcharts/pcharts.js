'use strict';

angular.module('pchartsjhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pcharts', {
                parent: 'entity',
                url: '/pchartss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pchartsjhipsterApp.pcharts.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pcharts/pchartss.html',
                        controller: 'PchartsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pcharts');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pcharts.detail', {
                parent: 'entity',
                url: '/pcharts/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pchartsjhipsterApp.pcharts.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pcharts/pcharts-detail.html',
                        controller: 'PchartsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pcharts');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pcharts', function($stateParams, Pcharts) {
                        return Pcharts.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pcharts.new', {
                parent: 'pcharts',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pcharts/pcharts-dialog.html',
                        controller: 'PchartsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    location: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pcharts', null, { reload: true });
                    }, function() {
                        $state.go('pcharts');
                    })
                }]
            })
            .state('pcharts.edit', {
                parent: 'pcharts',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pcharts/pcharts-dialog.html',
                        controller: 'PchartsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pcharts', function(Pcharts) {
                                return Pcharts.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pcharts', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('pcharts.delete', {
                parent: 'pcharts',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/pcharts/pcharts-delete-dialog.html',
                        controller: 'PchartsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Pcharts', function(Pcharts) {
                                return Pcharts.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pcharts', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
             .state('productchart', {
                             parent: 'entity',
                             url: '/productchart',
                             data: {
                                 authorities: ['ROLE_USER'],
                                 pageTitle: 'pchartsjhipsterApp.pcharts.home.title'
                             },
                             views: {
                                 'content@': {
                                     templateUrl: 'scripts/app/entities/pcharts/productchart.html',
                                     controller: 'PchartsController'
                                 }
                             },
                             resolve: {
                                 translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                     $translatePartialLoader.addPart('pcharts');
                                     $translatePartialLoader.addPart('global');
                                     return $translate.refresh();
                                 }]
                             }
                         })
            ;
    });
