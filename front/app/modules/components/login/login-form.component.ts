// login.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ViewEncapsulation } from '@angular/core';

import { User } from '../../models/user/user';
import { UserService } from '../../services/user/user.service';

@Component({
    moduleId: module.id,
    selector: 'login',
    templateUrl: 'login-form.html',
    styleUrls:  ['../../../assets/pages/css/login.min.css'],
    encapsulation: ViewEncapsulation.None
})
export class LoginComponent {
    model = new User('', '');

    constructor(private userService: UserService, private router: Router) {
        var body = document.getElementsByTagName('body')[0];
        body.className = 'login';
        console.log('hey there, im login');
    }

    //submitted = false;
    login() {
        //this.submitted = true;
        console.log('submitted!');
    }
    get diagnostic() { return JSON.stringify(this.model); }

}
