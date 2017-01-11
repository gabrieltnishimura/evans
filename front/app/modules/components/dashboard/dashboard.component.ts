import { Component } from '@angular/core';

@Component({
    templateUrl: './app/modules/components/dashboard/dashboard.html'
})
export class DashboardComponent {
    constructor() {
        var body = document.getElementsByTagName('body')[0];
        body.className = 'page-header-fixed page-sidebar-closed-hide-logo page-content-white';
        console.log("heyo from dashboard");
    }
}
