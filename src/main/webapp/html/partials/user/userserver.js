angular.module('matrix.bizModule')
    .controller('userController', ['$scope', '$rootScope', '$translate', '$filter', '$state', 'dialogSrv', 'modalSrv','constantsSrv', 'UsersSrv',
        function ($scope, $rootScope, $translate, $filter, $state, dialogSrv, modalSrv, constantsSrv, UsersSrv) {
            $scope.roleList = constantsSrv.getRoleList();
            $scope.pageData = {};
            $scope.selectedRole = "Administrator";
            $scope.loadUsers = function(){
                UsersSrv.getUsers($scope.pageIndex, $scope.pageSize, $scope.selectedRole, function(data){
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

            $scope.showAddUser = function(){

            }

            $scope.updatUser = function(){

            }

            $scope.delUserDialog = function(){

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



