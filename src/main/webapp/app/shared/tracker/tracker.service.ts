import { Injectable } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable, Observer, Subscription } from 'rxjs/Rx';

import { CSRFService } from '../auth/csrf.service';
import { WindowRef } from './window.service';
import { AuthServerProvider } from '../auth/auth-jwt.service';

import * as SockJS from 'sockjs-client';
import * as Stomp from 'webstomp-client';

@Injectable()
export class JhiTrackerService {
    stompClient = null;
    subscriber = null;
    notifySubscriber = null;
    mailSubscriber = null;
    progressImportSubscriber = null;
    connection: Promise<any>;
    connectedPromise: any;
    listener: Observable<any>;
    listenerObserver: Observer<any>;
    alreadyConnectedOnce = false;
    private subscription: Subscription;

    constructor(
        private router: Router,
        private authServerProvider: AuthServerProvider,
        private $window: WindowRef,
        // tslint:disable-next-line: no-unused-variable
        private csrfService: CSRFService,
    ) {
        this.connection = this.createConnection();
        this.listener = this.createListener();
    }

    getUser() {
        if (localStorage.getItem('currentUser')) {
            const currentUser = JSON.parse(localStorage.getItem('currentUser'));
            if (currentUser && currentUser.login) {
                return currentUser.login;
            }
        }
        return null;
    }

    connect() {
        if (this.connectedPromise === null) {
          this.connection = this.createConnection();
        }
        // building absolute path so that websocket doesn't fail when deploying with a context path
        const loc = this.$window.nativeWindow.location;
        let url;
        url = '//' + loc.host + loc.pathname + 'websocket/tracker';
        const authToken = this.authServerProvider.getToken();
        if (authToken) {
            url += '?access_token=' + authToken;
        }
        const socket = new SockJS(url);
        this.stompClient = Stomp.over(socket);
        const headers = {};
        this.stompClient.connect(headers, () => {
            this.connectedPromise('success');
            this.connectedPromise = null;
            this.sendActivity();
            this.subscribe();
            if (!this.alreadyConnectedOnce) {
                this.subscription = this.router.events.subscribe((event) => {
                  if (event instanceof NavigationEnd) {
                    this.sendActivity();
                  }
                });
                this.alreadyConnectedOnce = true;
            }
        });
    }

    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
        if (this.subscription) {
            this.subscription.unsubscribe();
            this.subscription = null;
        }
        this.alreadyConnectedOnce = false;
    }

    receive() {
        return this.listener;
    }

    sendActivity() {
        if (this.stompClient !== null && this.stompClient.connected) {
            this.stompClient.send(
                '/topic/activity', // destination
                JSON.stringify({'page': this.router.routerState.snapshot.url}), // body
                {} // header
            );
        }
    }

    subscribe() {
        this.connection.then(() => {
            const notifyDestination = '/notification/private/' + this.getUser();
            this.notifySubscriber = this.stompClient.subscribe(notifyDestination, (data) => {
                this.listenerObserver.next(data.body);
            });
            const mailDestination = '/mail/private/' + this.getUser();
            this.mailSubscriber = this.stompClient.subscribe(mailDestination, (data) => {
                this.listenerObserver.next(data.body);
            });
            const progressImportDestination = '/progress-import/private/' + this.getUser();
            this.progressImportSubscriber = this.stompClient.subscribe(progressImportDestination, (data) => {
                this.listenerObserver.next(data.body);
            });
        });
    }

    unsubscribe() {
        if (this.notifySubscriber !== null) {
            this.notifySubscriber.unsubscribe();
        }
        if (this.mailSubscriber !== null) {
            this.mailSubscriber.unsubscribe();
        }
        if (this.progressImportSubscriber !== null) {
            this.progressImportSubscriber.unsubscribe();
        }
        this.listener = this.createListener();
    }

    private createListener(): Observable<any> {
        return new Observable((observer) => {
            this.listenerObserver = observer;
        });
    }

    private createConnection(): Promise<any> {
        return new Promise((resolve, reject) => this.connectedPromise = resolve);
    }
}
