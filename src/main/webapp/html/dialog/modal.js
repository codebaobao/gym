var modalModule = angular.module('modal',[]);
function getModalController()
{
    var scope = angular.element("#commonModalDiv").scope();
    return scope;
}
modalModule.factory("modalSrv", [function() {
    //modalSize: 1 means small window, 2 means middle window, 3 means large window
    var showModal = function(pageUrl, data, modalSize) {
        var modalScope = getModalController();
        if(!modalSize)
            modalSize = 2;
        modalScope.showModal(pageUrl,modalSize, data);
    };
    var hideModal = function() {
        var modalScope = getModalController();
        modalScope.hideModal();
    };

    var getModal = function()
    {
        var modalScope = getModalController();
        return modalScope.getModal();
    };
    var getData = function()
    {
        var modalScope = getModalController();
        return modalScope.getData();
    };

    return {
        showModal : showModal,
        hideModal:hideModal,
        getData: getData,
        getModal: getModal
    };
}])
    .controller('modalCtrl', ['$scope',
    function($scope) {
        $scope.modalList = [];
        $scope.shownModalList = new Array();

        var modalNum = 3;
        $scope.init = function()
        {
            for(var i = 0; i < modalNum; i++)
            {
                $scope.modalList[i] = {
                    "id": "modal" + i,
                    "available": true,
                    "width": "60%"
                };
            }
        }

        //modalSize: 1 means small window, 2 means middle window, 3 means large window
        $scope.showModal = function(pageUrl, modalSize, data)
        {
            var availModal = null;
            for(var i = 0; i < modalNum; i++)
            {
                if($scope.modalList[i].available)
                {
                    availModal = $scope.modalList[i];
                    $scope.modalList[i].available = false;
                    break;
                }
            }
            if(availModal == null)
            {
                log("modalCtrl", "No available modal!");
               return;
            }
            switch(modalSize)
            {
                case 1:
                    availModal.widthClass = "ultra-modal-small";
                    break;
                case 2:
                    availModal.widthClass = "ultra-modal-normal";
                    break;
                case 3:
                    availModal.widthClass = "ultra-modal-large";
                    break;
                default:
                    availModal.widthClass = "ultra-modal-normal";
                    break;
            }
            $scope.shownModalList.push(availModal);
            //log("modalCtrl", "show modal: " + angular.toJson(availModal) + ", pageUrl: " + pageUrl + ", data: " + data);
            availModal.content = pageUrl;
            availModal.data = data;
            $('#' + availModal.id).modal({
                    show: true
                }
            );
            $('#' + availModal.id).draggable({
                handle: ".modal-header",
                cursor: 'move'
            });
            return availModal;
        }

        $scope.hideModal = function()
        {
            var modal = $scope.shownModalList.pop();
            if(modal != null)
            {
                //log("modalCtrl", "hide modal: " + angular.toJson(modal));
                //log("modalCtrl", "hide modal: " + angular.toJson(modal));
                $('#' + modal.id).modal('hide');
                modal.content = "";
                modal.data = null;
                modal.available = true;
            }

        }

        $scope.getData = function()
        {
            if($scope.shownModalList.length < 1)
            {
                log("modalCtrl", "getData:  shownModalList is empty  ");
                return;
            }
            var modal = $scope.shownModalList[$scope.shownModalList.length - 1];
            log("modalCtrl", "getData:  modal: " + angular.toJson(modal));
            if(modal != null)
            {
                return modal.data;
            }
            log("modalCtrl", "getData:  not found modal");
            return null;
        }

        $scope.getModal = function()
        {
            if($scope.shownModalList.length < 1)
            {
                log("modalCtrl", "getModal:  shownModalList is empty  ");
                return;
            }
            var modal = $scope.shownModalList[$scope.shownModalList.length -1];
            if(modal != null)
            {
                log("modalCtrl", "getModal: found modal: " + angular.toJson(modal));
                return modal;
            }
            log("modalCtrl", "getModal: not found modal");
            return null;
        }

        $scope.init();
    }])
;