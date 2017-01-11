// user.service.ts
import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

@Injectable()
export class UserService {
  private loggedIn = false;

  constructor(private http: Http) {
    this.loggedIn = !!localStorage.getItem('auth_token');
  }

  login(email: String, password: String) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    if (email === "1234" && password === "1234") {
        return true;
    }

    return false;
    
    /*return this.http
      .post(
        'localhost:9001/api/authenticate',
        JSON.stringify({ email, password }),
        { headers }
      )
      .map((res:any) => res.json())
      .map((res:any) => {
        if (res.success) {
          //localStorage.setItem('auth_token', res.auth_token);
          this.loggedIn = true;
        }
        return res.success;
    }).catch((error:any) => Observable.throw(error.json().error || 'Server error'));*/
  }

  logout() {
    //localStorage.removeItem('auth_token');
    this.loggedIn = false;
  }

  isLoggedIn() {
    return this.loggedIn;
  }
}
