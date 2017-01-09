(function (app) {
    app.EvansFooter = ng.core
        .Component({
            selector: 'evans-footer',
            templateUrl: 'app/modules/main/components/evans-footer/evans-footer.html'
        })
        .Class({
            constructor: function Main() {
                console.log("heyo from footer main");
            }
        });
})(window.app || (window.app = {}));
