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
                var user = {};
                modalSrv.showModal("html/partials/user/newUser.html",user,3);
            }

            $scope.updatUser = function(){

            }

            $scope.delUserDialog = function(){

            }

        }
    ])

    .controller('addUserCtrl', ['$scope', '$rootScope', '$translate', '$filter', '$state', 'dialogSrv', 'modalSrv','constantsSrv', 'UsersSrv',
        function ($scope, $rootScope, $translate, $filter, $state, dialogSrv, modalSrv, constantsSrv, UsersSrv) {
            $scope.user = modalSrv.getData();
            $scope.roleList = constantsSrv.getRoleList();
            $scope.genderList = constantsSrv.getGenderList();
            $scope.tradeList = constantsSrv.getTradeList();

            $scope.saveUser = function(){
                $scope.user.status="1";
                UsersSrv.addUser($scope.user, function(){
                    modalSrv.hideModal();
                })
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



