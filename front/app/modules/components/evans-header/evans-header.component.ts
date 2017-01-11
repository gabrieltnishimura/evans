import { Component } from '@angular/core';

@Component({
  selector: 'evans-header',
  templateUrl: 'app/modules/components/evans-header/evans-header.html',
})
export class EvansHeaderComponent  {
    constructor() {
        console.log("heyo from evans header");
    }
}
