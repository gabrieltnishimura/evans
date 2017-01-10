import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { Layout }  from './components/layout/layout.component';
import { EvansHeader }  from './components/evans-header/evans-header.component';
import { EvansSidebar }  from './components/evans-sidebar/evans-sidebar.component';
import { EvansFooter }  from './components/evans-footer/evans-footer.component';

@NgModule({
  imports:      [ BrowserModule ],
  declarations: [ Layout, EvansHeader, EvansSidebar, EvansFooter ],
  bootstrap:    [ Layout ]
})
export class AppModule { }
