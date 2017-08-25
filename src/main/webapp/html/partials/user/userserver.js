angular.module('matrix.bizModule')
    .controller('userController', ['$scope', '$rootScope', '$translate', '$filter', '$state', 'dialogSrv', 'modalSrv','constantsSrv', 'UsersSrv',
        function ($scope, $rootScope, $translate, $filter, $state, dialogSrv, modalSrv, constantsSrv, UsersSrv) {
            $scope.roleList = constantsSrv.getRoleList();
            $scope.pageData = {};
            $scope.selectedRole = "Administrator";
            $scope.roleStatus = "1";
            $scope.loadUsers = function(){
                UsersSrv.getUsers($scope.pageIndex, $scope.pageSize, $scope.selectedRole, $scope.roleStatus, function(data){
                    if(data && data.details){
                        $scope.pageData = data;
                        $scope.calculatePagination($scope.pageData);
                    }
                })
            }
            $scope.loadUsers();

            $scope.roleChange = function(){
                $scope.loadUsers();
            }

            $scope.roleStatusChange = function(){
                $scope.loadUsers();
            }

            $scope.showAddUser = function(){
                var user = {"id":"", "role":"Administrator","password":"", "gender":"0", "trade":"Body", "dob":null};
                modalSrv.showModal("html/partials/user/newUser.html",user,3);
            }

            $scope.updateUser = function(user){
                modalSrv.showModal("html/partials/user/newUser.html",user,3);
            }

            $scope.blockUser = function(user){
                user.status="2";
                UsersSrv.updateUser(user, function(){

                })
            }

            $scope.deBlockUser = function(user){
                user.status="1";
                UsersSrv.updateUser(user, function(){

                })
            }

            $scope.delUserDialog = function(user){
                $scope.deleteUser = user;
                var buttons = [];
                buttons[0] = {};
                buttons[0].class = "btn-info";
                buttons[0].text =  $filter('translate')('btn.confirm');
                buttons[0].callback = $scope.deleteUserFun;
                buttons[1] = {};
                buttons[1].text =  $filter('translate')('btn.cancel');
                dialogSrv.showDialog($filter('translate')('dialog.confirm'),$filter('translate')('label.deleteConfirmMsg'), buttons);
            }

            $scope.deleteUserFun = function(){
                UsersSrv.deleteUser($scope.deleteUser.id, function(){

                })
            }

            $scope.goPage = function(pageIndex){
                if(pageIndex ==  $scope.pageData.pageIndex){
                    return;
                }
                UsersSrv.getUsers(pageIndex, $scope.pageSize, $scope.selectedRole, $scope.roleStatus, function(data){
                    if(data && data.details){
                        $scope.pageData = data;
                        $scope.calculatePagination($scope.pageData);
                    }
                })
            }

            $scope.refreshCurrentPageData = function(){
                UsersSrv.getUsers($scope.pageData.pageIndex, $scope.pageSize, $scope.selectedRole, $scope.roleStatus, function(data){
                    if(data && data.details){
                        $scope.pageData = data;
                        $scope.calculatePagination($scope.pageData);
                    }
                })
            }

            $scope.$on("UserChanged", function(event, eventData)
            {
                log("UserController", "UserChanged:  " + eventData.eventAction + ", detail: " + angular.toJson(eventData.detail));
                var userData = eventData.detail;
                switch(eventData.eventAction) {
                    case "UserAdd":
                        $scope.refreshCurrentPageData();
                        break;
                    case "UserUpdate":
                        //for(var i= 0,len=$scope.pageData.details.length; i<len; i++){
                        //    if(userData.id == $scope.pageData.details[i].id){
                        //        $scope.pageData.details[i] = userData;
                        //        break;
                        //    }
                        //}
                        $scope.refreshCurrentPageData();
                        break;
                    case "UserDelete":
                        $scope.refreshCurrentPageData();
                        break;
                }
            });

        }
    ])

    .controller('addAndEditUserCtrl', ['$scope', '$rootScope', '$translate', '$filter', '$state', 'dialogSrv', 'modalSrv','constantsSrv', 'UsersSrv',
        function ($scope, $rootScope, $translate, $filter, $state, dialogSrv, modalSrv, constantsSrv, UsersSrv) {
            $scope.user = modalSrv.getData();
            $scope.roleList = constantsSrv.getRoleList();
            $scope.genderList = constantsSrv.getGenderList();
            $scope.tradeList = constantsSrv.getTradeList();
            $scope.addFlag = $scope.user.id == ''?true:false;

            $scope.saveUser = function(){
                if($scope.addFlag){
                    var user = angular.copy($scope.user);
                    user.password = CryptoJS.MD5(user.password)+"";
                    user.status="1";
                    UsersSrv.addUser(user, function(){
                        modalSrv.hideModal();
                    })
                }else{
                    var user = angular.copy($scope.user);
                    UsersSrv.updateUser(user, function(){
                        modalSrv.hideModal();
                    })
                }
            }

            $scope.showMap = function(){
                modalSrv.showModal("html/partials/user/useMap.html", $scope.user, 3);
            }
        }
     ])

    .controller('userMapCtrl', ['$scope', '$rootScope', '$translate', '$filter', '$state', 'dialogSrv', 'modalSrv','constantsSrv', 'UsersSrv','$timeout',
        function ($scope, $rootScope, $translate, $filter, $state, dialogSrv, modalSrv, constantsSrv, UsersSrv, $timeout) {
            $scope.user = modalSrv.getData();
            $scope.addressName = $scope.user.addressName?$scope.user.addressName:"";
            $scope.customAddressName = $scope.user.customAddressName?$scope.user.customAddressName:"";
            $scope.point = $scope.user.point?$scope.user.point:"";
            $scope.pointLon = "";
            $scope.pointLat = "";

            $scope.map;
            $scope.myGeo;
            $scope.currentCity;

            $scope.loadPoints = function(){
                if($scope.point != '' && $scope.point.indexOf(',')!=-1){
                    var pointArr = $scope.point.split(",");
                    $scope.pointLon = pointArr[0];
                    $scope.pointLat = pointArr[1];
                }
            }

            $scope.loadMap = function(){
                // 百度地图API功能
                $scope.map = new BMap.Map("allmap");    // 创建Map实例
                $scope.map.centerAndZoom(new BMap.Point(116.331398, 39.897445), 11);  // 初始化地图,设置中心点坐标和地图级别
                $scope.map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
                $scope.map.setCurrentCity($scope.currentCity);          // 设置地图显示的城市 此项是必须设置的
                $scope.map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

                $scope.myGeo = new BMap.Geocoder();//创建地址解析器实例

                $scope.searchByPoint();

                $scope.map.addEventListener("click", function(e){
                    var pt = e.point;
                    $scope.pointLon = pt.lng;
                    $scope.pointLat = pt.lat;
                    $scope.myGeo.getLocation(pt, function(rs){
                        $scope.addressName = rs.address;
                    });
                });
            }

            $scope.refreshLoadMap = function(){
                $scope.point = $scope.user.point?$scope.user.point:"";
                $scope.addressName = $scope.user.addressName?$scope.user.addressName:"";
                $scope.loadPoints();
                var myCity = new BMap.LocalCity();
                myCity.get(function(result){
                    $scope.currentCity = result.name;
                    $scope.loadMap();
                });
            }

            $scope.searchByName = function(){
                $scope.pointLon = "";
                $scope.pointLat = "";
                $scope.map.clearOverlays();
                var local = new BMap.LocalSearch($scope.map, {
                    renderOptions:{map: $scope.map}
                });
                local.search($scope.addressName);
            }

            $scope.searchByPoint = function(){
                if($scope.pointLon != ''&& $scope.pointLat != ''){
                    $scope.map.clearOverlays();
                    var new_point = new BMap.Point($scope.pointLon, $scope.pointLat);
                    var marker = new BMap.Marker(new_point);  // 创建标注
                    $scope.map.addOverlay(marker);              // 将标注添加到地图中
                    $scope.map.panTo(new_point);
                    $scope.map.centerAndZoom(new BMap.Point($scope.pointLon,$scope.pointLat),16);
                }
            }

            $timeout($scope.refreshLoadMap, 200);
            //$scope.refreshLoadMap();

            $scope.savePoint = function(){
                if($scope.pointLon != ""&& $scope.pointLat != ""&& $scope.addressName!=''){
                    if($scope.customAddressName != ''){
                        $scope.user.addressName = $scope.addressName
                        $scope.user.customAddressName = $scope.customAddressName;
                        $scope.user.point = $scope.pointLon+","+$scope.pointLat;
                        modalSrv.hideModal();
                    }else{
                        var buttons = [];
                        buttons[0] = {};
                        buttons[0].class = "btn-info";
                        buttons[0].text =  $filter('translate')('btn.confirm');
                        dialogSrv.showDialog($filter('translate')('dialog.confirm'),$filter('translate')('label.inputCustomAddress'), buttons);
                    }
                }else{
                    var buttons = [];
                    buttons[0] = {};
                    buttons[0].class = "btn-info";
                    buttons[0].text =  $filter('translate')('btn.confirm');
                    dialogSrv.showDialog($filter('translate')('dialog.confirm'),$filter('translate')('label.selectPoint'), buttons);
                }
            }
        }
    ])

    .controller('ChangeUserPasswordCtrl', ['$scope', '$rootScope', '$translate', '$filter', '$state', 'dialogSrv', 'modalSrv','constantsSrv', 'UsersSrv',
        function ($scope, $rootScope, $translate, $filter, $state, dialogSrv, modalSrv, constantsSrv, UsersSrv) {
            $scope.user = modalSrv.getData();

            $scope.savePassword = function (form) {
                if($scope.passwordInfo.newPassword != $scope.passwordInfo.confirmPassword)
                {
                    form.confirmPassword.$invalid = true;
                    return;
                }
                $scope.user.password = CryptoJS.MD5($scope.passwordInfo.newPassword) + "";
                UsersSrv.updateUser($scope.user, function(){
                    dialogSrv.showDialog($filter('translate')('dialog.info'), $filter('translate')('label.userPwdUpdated'), [{
                        "text": $filter('translate')('btn.ok'),
                        "class": "btn-info",
                        "callback": $scope.closeModal
                    }])
                })
            }

            $scope.closeModal = function()
            {
                modalSrv.hideModal();
            }

            $scope.checkPassword = function(form)
            {
                if($scope.passwordInfo.newPassword != $scope.passwordInfo.confirmPassword)
                {
                    form.confirmPassword.$invalid = true;
                }else{
                    form.confirmPassword.$invalid = false;
                }
            }
        }
    ])



