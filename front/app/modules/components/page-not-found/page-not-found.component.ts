import { Component } from '@angular/core';

@Component({
  templateUrl: 'app/modules/ts/components/page-not-found/page-not-found.html',
  styleUrls:  ['app/assets/pages/css/error.min.css']
})
export class PageNotFound  {
    constructor() {
        console.log("heyo from page not found");
    }
}
