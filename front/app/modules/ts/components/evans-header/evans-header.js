(function (app) {
    app.EvansHeader = ng.core
        .Component({
            selector: 'evans-header',
            templateUrl: 'app/modules/main/components/evans-header/evans-header.html'
        })
        .Class({
            constructor: function Main() {
                console.log("heyo from header main");
            }
        });
})(window.app || (window.app = {}));
