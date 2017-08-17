/*
 *  Service js file
 */
var matrix_url="/portal";
//matrix_url="http://localhost:8080/portal";
angular.module('matrix.bizModule')
	.factory('lanSrv', ['httpSrv', function(httpSrv) {
		log('lanSrv', "to set lan");
		var getLan = function (callback, errorCallback) {
			var req = {method: "GET", url: matrix_url + "/rest/settings/lans"};
			httpSrv.exec(req, callback, errorCallback);
		}

		var updateLan = function (lan, callback, errorCallback) {
			var req = {method: "PUT", url: matrix_url + "/rest/settings/lans",data: lan};
			httpSrv.exec(req, callback, errorCallback);
		}

		return {
			getLan: getLan,
			updateLan: updateLan
		}
	}])
	.factory('UsersSrv', ['httpSrv', function(httpSrv) {
		var updateUser = function ( user, callback, errorCallback) {
			var req = {method: "PUT", url: matrix_url + "/users", data:user};
			httpSrv.exec(req, callback, errorCallback);
		}
		return {
			updateUser: updateUser
		}
	}])

;