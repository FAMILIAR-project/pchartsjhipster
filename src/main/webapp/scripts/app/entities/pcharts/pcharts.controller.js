'use strict';


angular.module('pchartsjhipsterApp')
    .controller('PchartsController', function ($scope, $state, Pcharts) {

        $scope.pchartss = [];

        $scope.loadAll = function() {
            Pcharts.query(function(result) {
               $scope.pchartss = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pcharts = {
                name: null,
                location: null,
                id: null
            };
        };
    });


angular.module('pchartsjhipsterApp')
    .controller('MyListCtrl', function ($scope, $state, Pcharts, Pcms) {

          $scope.pcms = [];
          $scope.currentPcm = '';

          $scope.pcmChartError = '';


          $scope.xFt = '';
          $scope.yFt = '';
          $scope.zFt = '';

          $scope.candidateFts = [];
          $scope.pcmData = [];


          $scope.loadAll = function() { Pcms.query(function(result) {
                                                     $scope.pcms = result;
                                                  });
                                        };

            $scope.updateProductChart = function() {

            console.log('DEPICTING product chart for PCM: ' + $scope.currentPcm );


                var DIV_TARGET_NAME = 'tester'; // TODO: change it / parameterize

                var x = $scope.pcmData[$scope.xFt];
                var y = $scope.pcmData[$scope.yFt];

                var marker = { size: $scope.pcmData[$scope.zFt] }; // bubble size

                var datum;
                              if (typeof marker === 'undefined') {
                                  datum = [{mode: "markers",  x, y}];
                              }
                              else {
                                 datum = [{mode: "markers",  x, y, marker}];
                              }

                              Plotly.newPlot( DIV_TARGET_NAME, datum, {
                                          margin: { t: 20 },
                                           xaxis: {
                                               title: $scope.xFt
                                          },
                                            yaxis: {
                                              title: $scope.yFt,

                                            },
                                          }, {displayModeBar: true} );

                              /* Current Plotly.js version */
                              //console.log( Plotly.BUILD );
            };

          $scope.depictProductChart = function() {

            $scope.pcmChartError = '';
            console.log('Initiating product chart for PCM: ' + $scope.currentPcm );


            // data of the PCM
              Pcms.get({id : $scope.currentPcm}, function(result) {

                          if (result["error"] != undefined) {
                            $scope.pcmChartError = result["error"];
                          }
                          else {

                              $scope.pcmData = result['pcmData'];
                              console.log('pcmData=' + $scope.pcmData);

                              $scope.xFt = result['xFeature']; //'ISO min';
                              $scope.yFt = result['yFeature']; //'ISO max';
                              $scope.zFt = result['zFeature']; //'Megapixel';

                              $scope.candidateFts = result['candidateFts'];
                              console.log('xFt=' + $scope.xFt);

                              $scope.updateProductChart();
                          }



                      });

          };

      });


