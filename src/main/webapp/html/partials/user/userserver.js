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
                debugger;
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



