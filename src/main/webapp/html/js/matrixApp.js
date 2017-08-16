'use strict';

/* App Module */

var matrixApp = angular.module('matrixApp', [
    'matrix.common',
    'matrix.bizModule',
    'ui.router',
    'ngAnimate',
    'ngCookies',
    'pascalprecht.translate',

    'modal',
    'dialog'
]);

matrixApp.run([ '$rootScope', '$state', '$stateParams',
    function($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
    }
]);

matrixApp.config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider,   $urlRouterProvider) {
            matrixApp.stateProvider = $stateProvider;
            matrixApp.urlRouterProvider = $urlRouterProvider;
            matrixApp.tabState = {};
            matrixApp.tabState.name = "tab";
            matrixApp.tabState.url = "";
            matrixApp.tabState.abstract = true;
            matrixApp.tabState.templateUrl = "/portal/html/partials/main.html";


            $stateProvider.state(matrixApp.tabState)
                .state("loading", {
                    url         : "/loading",
                    templateUrl :"/portal/html/partials/loading.html",
                    controller   : "LoadingController"
                });

            $urlRouterProvider.otherwise("/loading");

        }
    ]
)
    .config(function($locationProvider, $translateProvider){
        $locationProvider.html5Mode({enable:true, requireBase:false});
        $translateProvider.useStaticFilesLoader({
            prefix: '/portal/html/i18n/common_',
            suffix: '.json'
        });
        $translateProvider.preferredLanguage('zh-CN');
        $translateProvider.useLocalStorage();
    })
;


