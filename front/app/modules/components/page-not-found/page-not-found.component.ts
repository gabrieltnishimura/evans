import { Component } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';

@Component({
  templateUrl: 'app/modules/components/page-not-found/page-not-found.html',
  styleUrls:  ['app/assets/pages/css/error.min.css'],
  encapsulation: ViewEncapsulation.None,
})
export class PageNotFoundComponent  {
    constructor() {
        var body = document.getElementsByTagName('body')[0];
        body.className = 'page-404-3';
        console.log("heyo from page not found");
    }
}
