// app-routes.module.ts
import { NgModule }              from '@angular/core';
import { RouterModule, Routes }  from '@angular/router';
import { FormsModule }          from '@angular/forms';

import { DashboardComponent }  from './components/dashboard/dashboard.component';
import { EvansHeaderComponent }  from './components/evans-header/evans-header.component';
import { EvansSidebarComponent }  from './components/evans-sidebar/evans-sidebar.component';
import { EvansFooterComponent }  from './components/evans-footer/evans-footer.component';
import { PageNotFoundComponent }  from './components/page-not-found/page-not-found.component';
import { LoginComponent }  from './components/login/login-form.component';

import { LoggedInGuard }  from './components/login/logged-in.guard';

const routes: Routes = [
    { path: '',  component: DashboardComponent, pathMatch: 'full' },
    { path: 'index.html',  component: DashboardComponent },
    { path: 'login',  component: LoginComponent },
    { path: '**', component: PageNotFoundComponent }
];

@NgModule({
    declarations: [
        DashboardComponent,
        EvansHeaderComponent,
        EvansSidebarComponent,
        EvansFooterComponent,
        PageNotFoundComponent,
        LoginComponent,
    ],
    imports: [
        RouterModule.forRoot(routes),
        FormsModule
    ],
    exports: [
        RouterModule,
    ]
})
export class AppRoutesModule {}
