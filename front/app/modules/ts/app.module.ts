import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './main/app.component';
import { EvansSidebar }  from './components/evans-sidebar/evans-sidebar.component';

@NgModule({
  imports:      [ BrowserModule ],
  declarations: [ AppComponent, EvansSidebar ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
