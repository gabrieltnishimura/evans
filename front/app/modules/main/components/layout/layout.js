(function (app) {
    app.MainComponent = ng.core
        .Component({
            selector: 'layout',
            templateUrl: 'app/modules/main/components/layout/layout.html'
        })
        .Class({
            constructor: function Main() {
                console.log("heyo from main");
            }
        });
})(window.app || (window.app = {}));
