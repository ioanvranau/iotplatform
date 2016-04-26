(function () {

        angular
            .module('app')
            .controller('MainController', MainController);

        /**
         * Main Controller for the Angular Material Starter App
         * @param $scope
         * @param $mdSidenav
         * @param avatarsService
         * @constructor
         */

        MainController.$inject = ['mainService', '$mdSidenav', '$mdBottomSheet', '$log', '$q', '$http', '$scope', '$mdDialog', '$mdMedia'];

        function MainController(mainService, $mdSidenav, $mdBottomSheet, $log, $q, $http, $scope, $mdDialog, $mdMedia) {

            var vm = this;

            vm.devices = [];
            vm.currDevice = '';
            vm.toggleList = toggleDevicesList;
            vm.showContactOptions = showContactOptions;
            vm.showAddNewDevicePrompt = showAddNewDevicePrompt;

            // Load all registered devices
            mainService
                .loadAllDevices($http).getData()
                .then(function (devices) {
                    vm.devices = [].concat(devices);
                });

            // *********************************
            // Internal methods
            // *********************************

            /**
             * First hide the bottomsheet IF visible, then
             * hide or Show the 'left' sideNav area
             */
            function toggleDevicesList() {
                var pending = $mdBottomSheet.hide() || $q.when(true);

                pending.then(function () {
                    $mdSidenav('left').toggle();
                });
            }

            /**
             * Show the bottom sheet
             */
            function showContactOptions($event, device) {

                return $mdBottomSheet.show({
                    parent: angular.element(document.getElementById('content')),
                    templateUrl: './src/main/view/contactSheet.html',
                    controller: ['$mdBottomSheet', ContactPanelController],
                    controllerAs: "cp",
                    bindToController: true,
                    targetEvent: $event
                }).then(function (clickedItem) {
                    clickedItem && $log.debug(clickedItem.name + ' clicked!');
                });

                /**
                 * Bottom Sheet controller for the Avatar Actions
                 */
                function ContactPanelController($mdBottomSheet) {
                    this.device = device;
                    this.actions = [
                        {name: 'Phone', icon: 'phone', icon_url: 'assets/svg/phone.svg'},
                        {name: 'Twitter', icon: 'twitter', icon_url: 'assets/svg/twitter.svg'},
                        {name: 'Google+', icon: 'google_plus', icon_url: 'assets/svg/google_plus.svg'},
                        {name: 'Hangout', icon: 'hangouts', icon_url: 'assets/svg/hangouts.svg'}
                    ];
                    this.submitContact = function (action) {
                        $mdBottomSheet.hide(action);
                    };
                }
            }

            function showAddNewDevicePrompt($event) {
                $scope.status = '  ';
                $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');

                return $mdDialog.show({

                    controller: DialogController,
                    controlerAs: "dc",
                    templateUrl: './src/main/view/addNewDeviceDialog.tmpl.html',
                    parent: angular.element(document.body),
                    targetEvent: event,
                    clickOutsideToClose: true,
                    fullscreen: true

                }).then(function () {
                    $scope.status = 'OK';
                    $log.debug('OK');
                }, function () {
                    var dialogCanceled = 'You cancelled the dialog.';
                    $scope.status = dialogCanceled;
                    $log.debug(dialogCanceled);
                });

                function DialogController($scope, $mdDialog) {

                    $scope.device = {
                        ip: 'localhost'
                    };



                    $scope.hide = function () {
                        $mdDialog.hide();
                    };
                    $scope.cancel = function () {
                        $mdDialog.cancel();
                    };
                    $scope.answer = function () {
                        $mdDialog.hide();
                        var currDevice = this.device;

                        var device = JSON.stringify({ip:currDevice.ip});
                        mainService
                            .addNewDevice($http, device).getData()
                            .then(function (data) {
                                $log.debug("Success!" + data.ip)
                            });
                    };
                }

            }
        }
    })();
