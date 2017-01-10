import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { Layout }  from './components/layout/layout.component';
import { EvansHeader }  from './components/evans-header/evans-header.component';
import { EvansSidebar }  from './components/evans-sidebar/evans-sidebar.component';
import { EvansFooter }  from './components/evans-footer/evans-footer.component';
import { PageNotFound }  from './components/page-not-found/page-not-found.component';

const appRoutes: Routes = [
    { path: '',   redirectTo: '/heroes', pathMatch: 'full' },
    { path: '**', component: PageNotFound }
];

@NgModule({
  imports:      [ BrowserModule, RouterModule.forRoot(appRoutes) ],
  declarations: [   Layout,
                    EvansHeader,
                    EvansSidebar,
                    EvansFooter,
                    PageNotFound,

                ],
  bootstrap:    [ Layout ]
})
export class AppModule { }
