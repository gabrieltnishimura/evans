(function (app) {
    app.MainModule = ng.core
        .NgModule({
            imports: [
                //ng.platformBrowser.DomSanitizer,
                ng.platformBrowser.BrowserModule,
                ng.http.HttpModule
            ],
            declarations: [
                app.MainComponent,
                app.EvansHeader,
                app.EvansFooter
            ],
            bootstrap: [
                app.MainComponent
            ]
        })
        .Class({
            constructor: function MainModule() {
            }
        });
})(window.app || (window.app = {}));
