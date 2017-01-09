(function (app) {
    document.addEventListener('DOMContentLoaded', function () {
        //ng.core.enableProdMode();
        ng.platformBrowserDynamic
            .platformBrowserDynamic()
            .bootstrapModule(app.MainModule);
    });
})(window.app || (window.app = {}));