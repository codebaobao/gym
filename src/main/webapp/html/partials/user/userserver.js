angular.module('matrix.bizModule')
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
                        "class": "btn-primary",
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



