import { Component, OnInit, Renderer2 } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {JhiLanguageService, JhiParseLinks} from 'ng-jhipster';

import { ProfileService } from '../profiles/profile.service';
import {JhiLanguageHelper, Account, Principal, LoginModalService, LoginService, JhiTrackerService, ITEMS_PER_PAGE} from '../../shared';
import { EmitterService } from '../../shared/services/emitter.service';
import { VERSION } from '../../app.constants';
import {NotificationService} from '../../entities/notification';
import {MailService} from '../../entities/mail/mail.service';
import {PermissionService} from '../../account/login/permission.service';
import {HttpResponse} from '@angular/common/http';
import {Permission} from '../../account/login/permission.model';
import {DataService} from '../../shared/services/data.service';
declare var jquery: any;
declare var $: any;
@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: [
        'navbar.css'
    ]
})
export class NavbarComponent implements OnInit {
    pushRightClass = 'push-right';
    mail: number;
    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    currentUserLogin: any;
    notificationList: any[];
    mailList: any;
    now: any;
    dateUTC: Date = new Date(0);
    username: any;
    allPermission: Permission;
    adminResource: number;
    /*Paging*/
    reverse: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    predicate: any;
    previousPage: any;
    page: any;

    totalNotifyUnread = 0;
    readNotify: boolean;
    unreadNotify: boolean;
    modeViewNotify: any;
    pageNotify: any;
    linksNotify: any;
    totalNotifyMail = 0;
    totalMailInbox = 0;
    constructor(
        private loginService: LoginService,
        private mailService: MailService,
        private activatedRoute: ActivatedRoute,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private profileService: ProfileService,
        private data: DataService,
        private router: Router,
        private renderer: Renderer2,
        private permissionService: PermissionService,
        private notificationService: NotificationService,
        private trackerService: JhiTrackerService,
        private parseLinks: JhiParseLinks

    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        // this.routeData = this.activatedRoute.data.subscribe((data) => {
        //     if (data.pagingParams !== undefined) {
        //         this.page = data.pagingParams.page;
        //         this.previousPage = data.pagingParams.page;
        //         this.reverse = data.pagingParams.ascending;
        //         this.predicate = data.pagingParams.predicate;
        //     } else {
        //         this.page = 1;
        //         this.previousPage = 1;
        //         this.reverse = true;
        //         this.predicate = 'id';
        //     }
        // });
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        this.languages = [];
        setInterval(() => {
            const date = new Date();
            this.now = date.getTime();
        }, 1000);
    }

    ngOnInit() {
        this.currentUserLogin = this.getUser();
        this.username = this.currentUserLogin.login;
        this.readNotify = true;
        this.unreadNotify = true;
        this.pageNotify = 0;
        this.page = 0;
        this.linksNotify = {
            last: 0
        };
        this.reverse = true;
        this.predicate = 'id';
        this.fHandleNavbar();
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });
        this.getNotificationList();
        this.getMailList();
        this.trackerService.receive().subscribe((res) => {
            console.log(res);
            const dateUTC: Date = new Date(0);
            const notificationNew = JSON.parse(res);
            if (typeof notificationNew.body === 'string') {
                notificationNew.body = JSON.parse(this.convertSpecialChar(notificationNew.body));
            }
            if (notificationNew.body.type === 'mail') {
                if (!this.mailList) {
                    this.mailList = new Array();
                }
                this.mailList.unshift(notificationNew);
                this.countMailUnseen();
            } else if (notificationNew.body.type === 'notification') {
                if (notificationNew.createdDate.epochSecond) {
                    notificationNew.createdDate = new Date(dateUTC.setUTCSeconds(notificationNew.createdDate.epochSecond));
                }
                if (notificationNew.lastModifiedDate.epochSecond) {
                    notificationNew.lastModifiedDate = new Date(dateUTC.setUTCSeconds(notificationNew.lastModifiedDate.epochSecond));
                }
                if (!this.notificationList) {
                    this.notificationList = new Array();
                }
                this.notificationList.unshift(notificationNew);
                this.countNotificationUnread();
            }
        });
        $('#header_notification_bar .dropdown-menu').click( function(e) {
                e.stopPropagation();
        });

        this.permissionService.findPermission()
            .subscribe((roleResponse: HttpResponse<Permission>) => {
                const role: Permission = roleResponse.body;
                this.data.changeUserPermission(role);
                this.data.currentUserPermission.subscribe((userPermission) => this.allPermission = userPermission);
                for (const resource of this.allPermission.resources) {
                    console.log(resource.name, resource.permission);
                    if (resource.name === 'ADMIN_RESOURCE') {
                        this.adminResource = resource.permission;
                    }else if (resource.name === 'MAIL') {
                        this.mail = resource.permission;
                    }
                }
            });

    }

    getUser() {
        if (localStorage.getItem('currentUser')) {
            return JSON.parse(localStorage.getItem('currentUser'));
        }
        return null;
    }

    getNotificationList() {
        this.countNotificationUnread();
        this.modeViewNotify = this.detechModeNotify();
        if (this.modeViewNotify === 'notView') {
            this.notificationList = new Array();
        } else {
            this.notificationService.getNotificationList(this.username.toLocaleLowerCase(), this.modeViewNotify, {
                page: this.pageNotify,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe((res) => {
                if (res.headers.get('link')) {
                    this.linksNotify = this.parseLinks.parse(res.headers.get('link'));
                }

                /*if (res.headers.get('X-Total-Count')) {
                    this.totalNotifyUnread = Number(res.headers.get('X-Total-Count'));
                }*/

                if (!this.notificationList) {
                    this.notificationList = new Array();
                }
                let newListNotify: any[] = res.body;
                newListNotify = newListNotify.map((obj) => {
                    if (typeof obj.body === 'string') {
                        obj.body = JSON.parse(this.convertSpecialChar(obj.body));
                    }
                    const index = this.notificationList.findIndex((x) => x.id === obj.id);
                    if (index >= 0) {
                        this.notificationList.splice(index, 1);
                    }
                    return obj;
                });
                this.notificationList = this.notificationList.concat(newListNotify);
                console.log(this.notificationList);
            });
        }
    }

    onModeViewChange(event) {
        const id = event.target.id;
        const checked = event.target.checked;
        if (id === 'ckb_notify_read') {
            this.readNotify = checked;
        }
        if (id === 'ckb_notify_unread') {
            this.unreadNotify = checked;
        }
        this.notificationList = new Array();
        this.pageNotify = 0;
        this.getNotificationList();
    }

    detechModeNotify() {
        if (this.readNotify) {
            if (this.unreadNotify) {
                return 'viewBoth';
            }
            return 'viewRead';
        } else {
            if (!this.unreadNotify) {
                return 'notView';
            }
            return 'viewUnread';
        }
    }

    loadPageNotify(page: any) {
        this.pageNotify = page;
        this.getNotificationList();
    }

    countNotificationUnread() {
        this.notificationService.countNotificationUnread(this.username.toLocaleLowerCase()).subscribe(
            (res) => this.totalNotifyUnread = Number(res.body)
        );
    }

    deleteMultiNotify() {
        console.log(this.notificationList);
        const notifyListClone = JSON.parse(JSON.stringify(this.notificationList));
        notifyListClone.map((obj) => {
            obj.body = JSON.stringify(obj.body);
            return obj;
        });
        this.notificationList = new Array();
        const notifyDeletePayload = {
            notifications : notifyListClone
        };
        this.notificationService.deleteMultiNotification(notifyDeletePayload).subscribe((res) => {
            if (res.statusText !== 'OK') {
                notifyListClone.map((obj) => {
                    obj.body = JSON.parse(this.convertSpecialChar(obj.body));
                    return obj;
                });
                this.notificationList = notifyListClone;
                this.countNotificationUnread();
            }
        });
    }

    /*getMailList() {
        this.mailService.queryReceiverMail({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.username.toLowerCase()).subscribe( (res) => {
            this.mailList = res.body;
            this.mailList = this.mailList.filter((mail) => {
                if (mail.status) {
                    return false;
                } else {
                    return true;
                }
            });
            this.mailList = this.mailList.map((obj) => {
                obj.body = obj.body.replace(/\n/g, ' ');
                obj.body = JSON.parse(obj.body);
                return obj;
            });
        });
    }*/

    getMailList() {
        this.mailService.queryReceiverMailUnread({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()}, this.username.toLowerCase()).subscribe( (res) => {
            if (res.headers.get('Link')) {
                const totalPagemail = this.parseLinks.parse(res.headers.get('link'));
            }

            if (res.headers.get('X-Total-Count')) {
                this.totalNotifyMail = Number(res.headers.get('X-Total-Count'));
            }

            if (!this.mailList) {
                this.mailList = [];
            }
            let newMailList = res.body;
            newMailList = newMailList.map((obj) => {
                if (typeof obj.body === 'string') {
                    obj.body = JSON.parse(this.convertSpecialChar(obj.body));
                }
                return obj;
            });

            newMailList = newMailList.map((obj) => {
                const index = this.mailList.findIndex((x) => x.id === obj.id);
                if (index >= 0) {
                    this.mailList.splice(index, 1);
                }
                return obj;
            });
            this.mailList = this.mailList.concat(newMailList);
        });
    }

    loadPageNotifyMail(page: any) {
        this.page = page;
        this.getMailList();
    }

    /*countMailUnseen() {
        return this.mailList.filter((x) => !x.status).length;
    }*/

    countMailUnseen() {
        this.mailService.countMailUnseen(this.username.toLocaleLowerCase()).subscribe(
            (res) => {
                this.totalNotifyMail = Number(res.body);
            }
        );
    }

    viewMail(idMail?: any) {
        // this.updateMailReceiver(idMail);
        const index = this.mailList.findIndex((x) => x.id === parseInt( idMail, 10));
        this.mailList.splice(index, 1);

        this.router.navigate(['/mail'], { queryParams: {id: idMail}});
    }
    updateMailReceiver(idMail?: any) {
        if (idMail !== undefined) {
            this.mailService.updateStatusMail(idMail, this.username.toLowerCase()).subscribe((res) => {
                if (res.body === true) {
                    const index = this.mailList.findIndex((x) => x.id === idMail);
                    this.mailList[index].status = true;
                    // this.mailList.splice(index, 1);
                }
            });
        }
    }
    formatDate(date) {
        // if (date.epochSecond) {
        //   return this.convertMiliseconds(this.now - this.dateUTC.setUTCSeconds(date.epochSecond));
        // }
        return this.convertMiliseconds(this.now - new Date(date).getTime());
    }

    convertMiliseconds(mil) {
        const seconds: number = parseInt((mil / 1000).toFixed(0), 10);

        const minutes: number = parseInt((mil / (1000 * 60)).toFixed(0), 10);

        const hours: number = parseInt((mil / (1000 * 60 * 60)).toFixed(0), 10);

        const days: number = parseInt((mil / (1000 * 60 * 60 * 24)).toFixed(0), 10);

        if (seconds < 60) {
            return 'Just now';
        } else if (minutes < 60) {
            return minutes + ' minutes ago';
        } else if (hours < 24) {
            return hours + ' hours ago';
        } else {
            return days + ' days ago';
        }
    }

    countNotificationUnseen() {
        return this.notificationList.filter((x) => !x.status).length;
    }

    redirectTask(notification: any) {
        const tasksId = notification.body.tasksId;
        const poId = notification.body.poId;
        if (poId) {
            sessionStorage.setItem('currentPurchaseOrderId', poId);
        }
        this.router.navigate(['/tasks/' + tasksId]);
        this.updateNotificationToSeen(notification);
        $('#header_notification_bar').removeClass('open');
    }

    deleteNotif(notification: any) {
        const index = this.notificationList.findIndex( (x) => x.id === notification.id);
        this.notificationList.splice(index, 1);
        this.notificationService.delete(notification.id).subscribe((res) => {
            if (res.statusText !== 'OK') {
                this.notificationList.splice(index, 0, notification);
                this.countNotificationUnread();
            }
        });
    }

    updateNotificationToSeen(notification: any): any {
        if (!notification.status) {
            const notificationClone = JSON.parse(JSON.stringify(notification));
            notificationClone.body = JSON.stringify(notificationClone.body);
            notificationClone.status = true;
            this.notificationService.update(notificationClone).subscribe((res) => {
                this.countNotificationUnread();
                notification = res.body;
                const index = this.notificationList.findIndex((x) => x.id === notification.id);
                if (this.modeViewNotify !== 'viewUnread' || this.modeViewNotify === 'viewBoth') {
                    if (typeof notification.body === 'string') {
                        notification.body = JSON.parse(this.convertSpecialChar(notification.body));
                    }
                    this.notificationList[index] = notification;
                } else {
                    this.notificationList.splice(index, 1);
                }
            });
        }
    }

    changeLanguage(languageKey: string) {
        this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['/login']);
    }

    fHandleNavbar() {
        $('.page-header-inner').find('.sidebar-toggler').click(() => {
            this.isNavbarCollapsed = !this.isNavbarCollapsed;
            const menu = $('.page-sidebar').find('.page-sidebar-menu');
            if (this.isNavbarCollapsed === true) {
                $('body').removeClass('page-sidebar-closed');
                menu.removeClass('page-sidebar-menu-closed');
            }else {
                menu.addClass('page-sidebar-menu-closed');
                $('body').addClass('page-sidebar-closed');
            }
        });
    }
    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
        if (this.isNavbarCollapsed === true) {
            this.renderer.removeClass(document.body, 'page-sidebar-closed');
        }else {
            this.renderer.addClass(document.body, 'page-sidebar-closed');
        }
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    getImageUrl() {
        const strURL = this.principal.getImageUrl();
        return strURL;
    }
    // =======NEW EVENT=====
    toggleSidebar() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle(this.pushRightClass);
    }

    convertSpecialChar(text: string) {
        return text.replace(/\\/g, '\\\\').replace(/"/g, '\"').replace(/\n/g, ' ');
    }
}
