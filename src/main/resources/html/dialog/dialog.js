
var dialogModule = angular.module('dialog',[]);

function getDialogController()
{
    var scope = angular.element("#commonDialogDiv").scope();
    return scope;
}

dialogModule.factory('dialogSrv', function () {

    /*
     *  buttonList format: {"class": className, "text": button text, "callback": callback function}
     */
    var showDialog = function(title, message, buttonList) {
        var scope = getDialogController();
        scope.showDialog(title, message, buttonList);
    };

    var closeDialog = function(){
        var scope = getDialogController();
        scope.closeDialog()
    }

    return {
        showDialog : showDialog,
        closeDialog: closeDialog
    };
}).
    controller('dialogCtrl', ['$scope',
    function($scope) {
        $scope.dialogList = [];
        $scope.shownDialogList = new Array();
        var dialogNum = 3;
        $scope.init = function()
        {
            for(var i = 0; i < dialogNum; i++)
            {
                $scope.dialogList[i] = {
                    "id": "dialog" + i,
                    "available": true
                };
            }
        }

        /*
         *  buttonList format: {"class": className, "text": button text, "callback": callback function}
         */
        $scope.showDialog = function(title, msg, buttonList)
        {
            var availDlg = null;
            for(var i = 0; i < dialogNum; i++)
            {
                if($scope.dialogList[i].available)
                {
                    availDlg = $scope.dialogList[i];
                    $scope.dialogList[i].available = false;
                    break;
                }
            }
            if(availDlg == null)
            {
                err("dialogCtrl", "No available dialog, will force to use the first one.");
                return;
                //availDlg = $scope.dialogList[0];
            }
            log("dialogCtrl", "To show dialog: " + availDlg.id + ", title: " + title + ", msg: " + msg + ", buttonList: " + buttonList);
            availDlg.title = title;
            if(title && title.length > 0)
                availDlg.showTitle = true;
            else
                availDlg.showTitle = false;
            availDlg.message = msg;
            availDlg.buttonList = buttonList;

            $('#' + availDlg.id).modal({
                    show: true
                }
            );
            $('#' + availDlg.id).draggable({
                handle: ".modal-header",
                cursor: 'move'
            });
            /*$('#' + availDlg.id).on("hide.bs.modal",function(){
                availDlg.available = true;
                availDlg.title = "";
                availDlg.showTitle = false;
                availDlg.message = "";
                availDlg.buttonList = [];
                log("dialogCtrl", "To close dialog: " + availDlg.id );
            });*/

            $scope.shownDialogList.push(availDlg);

        }

        $scope.closeDialog = function(){
            var availDlg = $scope.shownDialogList.pop();
            if(availDlg != null)
            {
                $('#' + availDlg.id).modal('hide');
                availDlg.available = true;
                availDlg.title = "";
                availDlg.showTitle = false;
                availDlg.message = "";
                availDlg.buttonList = [];
                log("dialogCtrl", "closeDialog: " + angular.toJson(availDlg));
            }else{
                err("dialogCtrl", "failed to closeDialog: ");
            }
        }

        $scope.init();
    }]);

