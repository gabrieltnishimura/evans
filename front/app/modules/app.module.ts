import { NgModule }             from '@angular/core';
import { BrowserModule }        from '@angular/platform-browser';
import { FormsModule }          from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpModule } from '@angular/http';

import { MainComponent }  from './components/main/main.component';
import { AppRoutesModule } from './app-routes.module';

import { UserService }  from './services/user/user.service';

@NgModule({
    bootstrap:    [ MainComponent ],
    imports:      [ BrowserModule,
                    HttpModule,
                    FormsModule,
                    AppRoutesModule ],
    declarations: [ MainComponent ],
    providers: [ UserService ]
})
export class AppModule { }
