import { Component, AfterViewInit, OnInit, Renderer, ElementRef } from '@angular/core';

import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { LoginService } from '../../shared/login/login.service';
import { StateStorageService } from '../../shared/auth/state-storage.service';
import { NotificationService } from '../../shared/services/notification.service';
import { Principal } from '../../shared';
import { TranslateService } from '@ngx-translate/core';
declare var $: any;
@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})

export class LoginComponent implements OnInit, AfterViewInit {

  authenticationError: boolean;
  password: string;
  rememberMe: boolean;
  username: string;
  credentials: any;

  constructor(
    private eventManager: JhiEventManager,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private router: Router,
    private principal: Principal,
    private notification: NotificationService,
    private translate: TranslateService

  ) {
    this.credentials = {};
  }

  ngAfterViewInit() {
    this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []);
  }

  cancel() {
    this.credentials = {
      username: null,
      password: null,
      rememberMe: true
    };
    this.authenticationError = false;

  }

  login() {
    this.loginService.login({
      username: this.username,
      password: this.password,
      rememberMe: this.rememberMe
    }).then(() => {
      this.authenticationError = false;

      if (this.router.url === '/register' || (/^\/activate\//.test(this.router.url)) ||
        (/^\/reset\//.test(this.router.url))) {
        this.router.navigate(['']);
      }

      this.eventManager.broadcast({
        name: 'authenticationSuccess',
        content: 'Sending Authentication Success'
      });

      // // previousState was set in the authExpiredInterceptor before being redirected to login modal.
      // // since login is succesful, go to stored previousState and clear previousState
      // const redirect = this.stateStorageService.getUrl();
      // if (redirect) {
      //  this.stateStorageService.storeUrl(null);
      this.notification.success(this.translate.instant('login.messages.login_success'));
      this.router.navigate(['/']);
      // }
    }).catch(() => {
      this.authenticationError = true;
    });
  }

  register() {
    this.router.navigate(['/register']);
  }

  requestResetPassword() {
    this.router.navigate(['/reset', 'request']);
  }

  ngOnInit() {
    if (this.principal.isAuthenticated() === true) {
      this.router.navigate(['/']);
    }
  }

}
