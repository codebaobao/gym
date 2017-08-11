'use strict';
var lastOptTime = (new Date()).getTime();
var stopHttpSrv = false;
var sessionId = "";
var _filterLanList=["zh-CN", "en-US"];
var currentLocale = "zh-CN";
var currentUserId = "";
var currentUserRole = "";
var matrixCommon = angular.module('matrix.common', []);

matrixCommon.factory("constantsSrv", ['$resource','$q', '$timeout','$cookieStore',function($resource, $q, $timeout, $cookieStore) {
    return {

    };
}])
    .filter('yesNoFilter', function() {
        return function(value) {
            if(value)
            {
                return 'dropdown.yes';
            }else{
                return 'dropdown.no';
            }
        }
    })

    .factory("resSrv", ['$resource', "$q", "$timeout", function($resource, $q, $timeout) {
    var menuRes = $resource('/html/conf/menu.json', {}, {
        load: {method:'GET', params:{}, isArray:false}
    });

    var lanRes = $resource('/html/conf/language.json', {}, {
        load: {method:'GET', params:{}, isArray:true}
    });

    var menus = menuRes.load();
    var getMenus = function() {
        var deferred = $q.defer();
        $timeout( function() {
            deferred.resolve(menus);
        }, 500);
        return deferred.promise;
    };

    var language = lanRes.load();
    var getLanguages = function() {
        var deferred = $q.defer();
        $timeout( function() {
            deferred.resolve(language);
        }, 500);
        return deferred.promise;
    };

    return {
        getMenus : getMenus,
        getLanguages : getLanguages
    };
}])
    .factory("stateSrv", ["$state", "$rootScope", function($state, $rootScope) {
        var createState = function(parentState, menu, parentMenu, userRole) {
            var state = {};
            //log("state " + stateDetails.stateName);
            state.name = menu.stateName;
            state.parent = parentState;
            state.url = menu.stateUrl;
            if(menu.abstract)
                state.abstract = menu.abstract;
            state.views = {};
            if(menu.views)
            {
                angular.forEach(menu.views, function(view) {
                    state.views[view.viewName] = {};
                    state.views[view.viewName].templateUrl = view.templateUrl;
                    state.views[view.viewName].controller = view.controller;
                });
            }else{
                state.views[menu.viewName] = {};
                state.views[menu.viewName].templateUrl = menu.templateUrl;
                state.views[menu.viewName].controller = menu.controller;
            }

            matrixApp.stateProvider.state(state);
            $rootScope.$broadcast("stateCreated", {
                "state"         : state
            });
            if(menu.role)
            {
                if(menu.role.indexOf(userRole) > -1)
                    menu.visible = true;
                else
                    menu.visible = false;
            }else{
                if(parentMenu)
                    menu.visible = parentMenu.visible;
                else
                    menu.visible = false;
            }
            if(menu.hidden)
            {
                menu.visible = false;
            }
            if(menu.children && menu.visible)
            {
                angular.forEach(menu.children, function(subMenu) {
                    createState(state, subMenu, menu, userRole);
                });
            }
        };

        return {
            createState : createState
        };
    }])
    .factory('httpSrv', ['$http', '$cookieStore', '$filter','dialogSrv', function($http, $cookieStore, $filter, dialogSrv){
        var exec = function(config, callback, errorCallback)
        {
            if(stopHttpSrv)
            {
                log("httpSrv", "You cannot send request");
                return;
            }
            if(config.hideLoading)
            {

            }else{
                if(config.method != 'GET')
                {
                    $(".loading_icon_div").show();
                    lastOptTime = (new Date()).getTime();
                }
            }
            if(config.autoOperation)
            {

            }else{
                lastAccessTime = (new Date()).getTime();
            }
            log("httpSrv", "" + angular.toJson(config));
            $http({method: config.method, url: config.url, data: config.data, headers: {'Accept-Language': currentLocale, 'Current-User':currentUserId + "|" + currentUserRole},responseType:"application/json;charset=UTF-8"}).
                then(function(data, status) {
                    $(".loading_icon_div").hide();
                    log("httpSrv", angular.toJson(data) + ",   " + status);
                    if(callback)
                        callback(data, status);
                },
                function(data, status) {
                    $(".loading_icon_div").hide();
                    log("httpSrv", "error: " + angular.toJson(data) + ",   " + status);
                    if(status == 401 || status == 403)
                    {
                        stopHttpSrv = true;
                        var buttons = [{
                            "text":  $filter('translate')("btn.ok"),
                            "class": "btn-primary",
                            "callback": function(){
                                document.location.href = "login.html";
                            }
                        }
                        ];
                        dialogSrv.showDialog($filter('translate')("dialog.error"), $filter('translate')('label.notAuthorized'), buttons);
                        return;
                    }
                    if(status == 500)
                    {
                        var buttons = [{
                            "text":  $filter('translate')("btn.ok"),
                            "class": "btn-primary"
                        }
                        ];
                        dialogSrv.showDialog($filter('translate')("dialog.error"), $filter('translate')("error." + data.code), buttons);
                    }
                    if(errorCallback)
                        errorCallback(data, status);
                });
        };

        return {
            exec : exec
        };
    }])

;


function log(msg)
{
    if(console)
        console.log(msg);
}

function log(module, msg)
{
    if(console)
        console.log(module + " --> " + msg);
}

function err(msg)
{
    if(console)
        console.error(msg);
}

function err(module, msg)
{
    if(console)
        console.error(module + " --> " + msg);
}

function saveData(key, data)
{
    if(typeof(Storage) !== "undefined") {
        localStorage.setItem(key, data);
        log("common", "Save data <" + key + ", " + data + ">");
    }else{
        err("common", "The browser does not support Storage!");
    }
}

function removeData(key)
{
    if(typeof(Storage) !== "undefined") {
        localStorage.removeItem(key);
        log("common", "removed data <" + key + ">");
    }else{
        err("common", "The browser does not support Storage!");
    }
}

function getData(key)
{
    if(typeof(Storage) !== "undefined") {
        return localStorage.getItem(key);
    }
    return null;
}

var _ipv4AddressReg = /^(\d|[1-9]\d|1\d\d|2[0-1]\d|22[0-3])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([1-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
var _numberReg = /^[0-9*]/;

function validateNumber(val)
{
    return val.match(_numberReg);
}

function validateIPv4(ipv4)
{
    return ipv4.match(_ipv4AddressReg);
}

function matchLan(locale)
{
    for(var i = 0; i < _filterLanList.length; i++)
    {
        if(_filterLanList[i].indexOf(locale) > -1)
            return _filterLanList[i];
    }
    return _filterLanList[0];
}


