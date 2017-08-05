'use strict';

/* App Module */
var matrixApp = angular.module('matrixApp', [
    'matrix.common',
    'matrix.bizModule',
    'ui.router',
    'ngAnimate',
    'ngCookies',
    'pascalprecht.translate'
]);

matrixApp.run([ '$rootScope', '$state', '$stateParams',
		function($rootScope, $state, $stateParams) {
			$rootScope.$state = $state;
			$rootScope.$stateParams = $stateParams;
            log("current state " + angular.toJson($state));
		} 
]);

matrixApp.config(['$locationProvider', '$translateProvider', function ($locationProvider, $translateProvider) {
    $locationProvider.html5Mode(true);
    $translateProvider.useStaticFilesLoader({
            prefix: 'i18n/common_',
            suffix: '.json?v=1.0.0'
        });
    $translateProvider.preferredLanguage('zh-CN');
    $translateProvider.useLocalStorage();
}])
;

