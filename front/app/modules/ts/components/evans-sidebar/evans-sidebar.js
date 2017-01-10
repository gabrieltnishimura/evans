(function (app) {
    app.EvansSidebar = ng.core
        .Component({
            selector: 'evans-sidebar',
            templateUrl: 'app/modules/main/components/evans-sidebar/evans-sidebar.html'
        })
        .Class({
            constructor: function Main() {
                console.log("heyo from sidebar main");
            }
        });
})(window.app || (window.app = {}));
