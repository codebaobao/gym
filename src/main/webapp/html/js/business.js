'use strict';

var bizModule = angular.module('matrix.bizModule', ['ngResource','matrix.common']);
var lastAccessTime = (new Date()).getTime();
var eventSource;
var autoHideMenubars = false;
bizModule.controller('AppController', ['$scope','$rootScope', '$translate', 'resSrv', '$timeout','$interval','$filter','$state','modalSrv','constantsSrv','dialogSrv','httpSrv',
    '$location', function($scope, $rootScope, $translate, resSrv, $timeout, $interval, $filter, $state, modalSrv, constantsSrv,dialogSrv,httpSrv ) {

        var userInfo = getData("userLoginInfo");
        if(userInfo != null){
            $scope.loginUser = angular.fromJson(userInfo);
        }else{
            $scope.loginUser={"name": "ULTRA", "role": "Administrator", "id":""};
        }

        $scope.goHomePage = function(){
            $rootScope.currentMenu = $rootScope.leftMenus[0];
            $state.go("home");
        }
        $scope.updateMenu = function(menu)
        {
            if(angular.isDefined(menu.children)){
                var last = $('.sub-menu.open', $('#sidebar'));
                $('.menu-arrow').removeClass('arrow_carrot-right');
                $('.sub', last).slideUp(200);
                var sub = $("#" + menu.stateName + " a").next();
                if (sub.is(":visible")) {
                    $('.menu-arrow').addClass('arrow_carrot-right');
                    sub.slideUp(200);
                } else {
                    $('.menu-arrow').addClass('arrow_carrot-down');
                    sub.slideDown(200);
                }
                var o = ($("#" + menu.stateName + " a").offset());
                var diff = 200 - o.top;
                if(diff>0)
                    $("#sidebar").scrollTo("-="+Math.abs(diff),500);
                else
                    $("#sidebar").scrollTo("+="+Math.abs(diff),500);
                return;
            }
            $rootScope.previousMenu =  $rootScope.currentMenu;
            $rootScope.currentMenu = menu;
            log("AppController","updateMenu: set current menu to " + menu.stateName);
            $state.go(menu.stateName);
            if(autoHideMenubars)
            {
                $scope.showDynamicMenus();
            }
        }

        $scope.startSSE = function()
        {
            eventSource = new EventSource(matrix_url + "/sse/event?uid=" + currentUserId + "|" +currentUserRole);
            eventSource.onmessage = function(event) {
                log("SSE", "event from server: " + event.data );
                $scope.serverErrorCount = 0;
                var eventObj = angular.fromJson(event.data);
                $scope.$broadcast(eventObj.eventType, eventObj);
            }
            eventSource.onerror = function(){
                if($scope.stopInterval)
                    return;
                log("SSE", " onerror on " + new Date());
                if($scope.serverErrorCount > $scope.maxServerConnRetry)
                {
                    if(!$scope.stopInterval)
                    {
                        $scope.stopInterval = true;
                        $scope.$emit("show_error_event", $filter('translate')('error.serverConnectionError'), function(){
                            window.location.reload();
                        });
                        eventSource.close();
                        eventSource = null;
                        return;
                    }
                }
                $scope.serverErrorCount++;
                eventSource.close();
                eventSource = null;
                $timeout($scope.startSSE, 2000);
            }
            eventSource.onabort = function(){
                if($scope.stopInterval)
                    return;
                log("SSE", " onabort on " + new Date());
                $scope.serverErrorCount = 0;
                eventSource.close();
                eventSource = null;
                $timeout($scope.startSSE, 2000);
            }
        }
        $scope.stopSSE = function()
        {
            if(eventSource)
                eventSource.close();
            eventSource = null;
        }
        //$scope.startSSE();
        $interval(function(){
            if($scope.stopInterval)
                return;
            if(eventSource == null)
            {
                //$scope.startSSE();
            }
            $scope.$broadcast("interval_event");
        }, 1000);

        $scope.doLogout = function(isAutoCheck)
        {
            var req = {method: "POST", url: matrix_url + "/logout"};
            req.hideLoading = isAutoCheck;
            httpSrv.exec(req, function(){
                removeData("userLoginInfo");//清除用户
                if(isAutoCheck){

                }else{
                    document.location = "index.html";
                }
            });
        }

        $scope.calculatePagination = function(pageData)
        {
            if(pageData.pageCount <= 1)
            {
                pageData.hidePageBar = true;
                return;
            }
            pageData.hidePageBar = false;
            pageData.pageBarList = [];
            if(pageData.pageCount <= $scope.pageBarSize)
            {
                for(var i = 0; i < pageData.pageCount; i++)
                    pageData.pageBarList[i] = i;
            }else {
                if (pageData.pageIndex <= $scope.pageBarSize / 2) {
                    for (var i = 0; i < $scope.pageBarSize; i++)
                        pageData.pageBarList[i] = i;
                } else {
                    var pageStartIndex = pageData.pageIndex - $scope.pageBarSize / 2;

                    if (pageStartIndex + $scope.pageBarSize > pageData.pageCount) {
                        for (var i = 0; i < pageData.pageCount - pageStartIndex; i++)
                            pageData.pageBarList[i] = i + pageStartIndex;
                        ;
                    } else {
                        for (var i = 0; i < $scope.pageBarSize; i++)
                            pageData.pageBarList[i] = i + pageStartIndex;
                    }
                }
            }
            log("AppController", "calculatePagination: pageBarList: " + pageData.pageBarList );
        }
    }])
    .controller('LoadingController', ['$scope','$rootScope', '$translate', '$state', 'resSrv', 'stateSrv',
        function($scope, $rootScope, $translate, $state, resSrv, stateSrv) {

            resSrv.getMenus().then(function(menus){
                $rootScope.leftMenus = menus.leftMenus;
                angular.forEach($scope.leftMenus, function(menu) {
                    stateSrv.createState(matrixApp.tabState, menu, null, $scope.loginUser.role);
                });
                $rootScope.currentMenu = $scope.leftMenus[0];
                $rootScope.currentTab = $scope.leftMenus[0];
                $state.go($scope.leftMenus[0].stateName);
            });
            log("LoadingController","had been initialized");
        }])

    .controller('dateController', ['$scope', '$translate','resSrv','constantsSrv','httpSrv',
        function($scope,$translate, resSrv, constantsSrv, httpSrv) {
            $scope.productName = "管理系统";
            $scope.serverTimeStr = "";

        }])

    .controller('LoginController', ['$scope', '$translate','resSrv','constantsSrv','httpSrv',
        function($scope,$translate, resSrv, constantsSrv, httpSrv) {
            $scope.languages = [];
            $scope.loginInfo={};
            $scope.currentLan = {};
            $scope.productName = "接的快管理系统";

            resSrv.getLanguages().then(function(lans){
                log("LoginController", angular.toJson(lans));
                $scope.languages = lans;
            })
            var language = $translate.storage().get($translate.storageKey());
            if(!language)
                language = $translate.preferredLanguage();
            currentLocale = matchLan(language);
            $scope.currentLan.value = currentLocale;

            $scope.setLanguage = function(){
                log("LoginController", "change language to " + $scope.currentLan.value);
                $translate.use($scope.currentLan.value);
                currentLocale = $scope.currentLan.value;
            };

            $scope.myKeydown = function($event){
                var keycode = window.event?$event.keyCode:$event.which;
                if(keycode==13){
                    $scope.loginPost();
                }
            }

            //$scope.doLogin = function()
            //{
            //    $("#j_password").val(CryptoJS.MD5($scope.loginInfo.password));
            //    var options = {
            //        url:  matrix_url + "/loginPost",
            //        type: 'POST',
            //        success: function(responseText , status, xhr){
            //            if(responseText.indexOf('Login failed') > -1)
            //            {
            //                $scope.loginFailed = true;
            //            }else{
            //                currentUserId = $scope.loginInfo.loginId;
            //                $scope.getUserByLoginId($scope.loginInfo.loginId, function(userInfo){
            //                    log("LoginController", "logged in user info: " + angular.toJson(userInfo));
            //                    saveData("userLoginInfo", angular.toJson(userInfo));
            //                    $scope.loginFailed = false;
            //                    document.location = "index.html";
            //                }, function(){
            //                    $scope.loginFailed = true;
            //                })
            //
            //            }
            //        }
            //    };
            //    $("form").ajaxSubmit(options);
            //}

            $scope.loginFailed = false;
            $scope.loginFailedMessage = "";
            $scope.loginPost = function(){
                var user = {username:$scope.loginInfo.loginId, password:CryptoJS.MD5($scope.loginInfo.password)};
                $scope.doLogin(user, function(callback){
                    var data = callback.data;
                    if(data != null){
                        if(data.message == 'success'){
                            saveData("userLoginInfo", angular.toJson(data.user));
                            document.location = "index.html";
                        }else{
                            $scope.loginFailed = true;
                            $scope.loginFailedMessage = data.message;
                        }
                    }
                });
            }

            $scope.doLogin = function (user, callback, errorCallback) {
                var req = {method: "GET", url: matrix_url + "/doLogin?username="+user.username+"&password="+user.password};
                httpSrv.exec(req, callback, errorCallback);
            }
            log("LoginController", " had been initialized");
        }])