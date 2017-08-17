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

matrixApp.config(['$locationProvider', '$translateProvider', function ($locationProvider, $translateProvider) {
    //$locationProvider.html5Mode(true);
    $translateProvider.useStaticFilesLoader({
            prefix: '/portal/html/i18n/common_',
            suffix: '.json'
        });
    $translateProvider.preferredLanguage('zh-CN');
    $translateProvider.useLocalStorage();
}])
;

