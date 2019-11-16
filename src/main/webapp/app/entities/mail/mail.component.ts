import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Mail } from './mail.model';
import { MailService } from './mail.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {AppConstants} from '../../shared/services/app-constants';
import {Lightbox} from 'ngx-lightbox';
import {PermissionService} from '../../account/login/permission.service';
import {Permission} from '../../account/login/permission.model';
import {DataService} from '../../shared/services/data.service';
declare var $: any;
@Component({
    selector: 'jhi-mail',
    templateUrl: './mail.component.html',
    styleUrls: ['./mail.component.css']
})
export class MailComponent implements OnInit, OnDestroy {
    mailChange: any;
    listMail: any;
    mailDetail: any;
    classMail: any;
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;
    sortDate: any;
    /*data collection*/
    columnDefs: any;
    rowData: any;
    frameworkComponents: any;
    allPermission: Permission;
    adminResource: number;
    mail: number;

    constructor(
        private mailService: MailService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private data: DataService,
        private _lightbox: Lightbox,
        private permissionService: PermissionService,
    ) {
        this.listMail = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
        this.frameworkComponents = {
            actionsButton: ButtonViewComponent,
        };
        this.sortDate = true;
        this.mailChange = 'to';
    }

    loadAll() {
        if (this.currentSearch) {
            this.mailService.search({
                query: this.currentSearch,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: HttpResponse<Mail[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
    }

    viewMail(id?: any) {
        const li_active = '#li_mail' + id;
        $(li_active).addClass('li-active');
        $('li').not(li_active).removeClass('li-active');
        this.mailService.find(id, this.currentAccount.login).subscribe( (res) => {
            this.mailDetail = res.body;
            this.mailDetail.body = this.mailDetail.body.replace(/\n/g, '<br/>');
            this.mailDetail.body = JSON.parse(this.mailDetail.body);
            if (this.mailDetail.status !== undefined && !this.mailDetail.status) {
                this.updateMailReceiver(this.mailDetail.id);
            }
        });
    }
    updateMailReceiver(id?: any) {
        this.mailService.updateStatusMail(id, this.currentAccount.login).subscribe((res) => {
            if (res.body === true) {
                if (this.listMail !== undefined) {
                    const index = this.listMail.findIndex((x) => x.id === parseInt(id, 10));
                    this.listMail[index].status = true;
                    $('#btn_inboxMail').click();
                }
                const numNotify = parseInt($('.mail_notify_count').html(), 10);
                if (numNotify > 0) {
                    $('.mail_notify_count').html(numNotify - 1);
                }
                const li = $('.mail_notify_list').find('li');
                this.classMail  = 'mail_' + id;
                $(li).each((i, item ) => {
                    const _item = $(item).find('a');
                    const hasClass = $(_item).hasClass(this.classMail);
                    if (hasClass !== undefined && hasClass === true) {
                        $(item).remove();
                        return false;
                    }
                });
            }
        });
    }
    sortCreateBy(sortDate) {
        if (sortDate) {
            this.sortDate = false;
            return this.listMail.sort((a, b) => {
                return <any>new Date(a.createdDate) - <any>new Date(b.createdDate);
            });
        } else {
            this.sortDate = true;
            return this.listMail.sort((a, b) => {
                return <any>new Date(b.createdDate) - <any>new Date(a.createdDate);
            });
        }
    }
    loadSendItems() {
        $('#btn_sendMail').addClass('btn-active');
        $('#btn_inboxMail').removeClass('btn-active');
        if (this.mailChange !== 'from') {
            this.page = 0;
            this.listMail.length = 0;
        }
        this.mailChange = 'from';
        this.mailService.querySendMail({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        }, this.currentAccount.login).subscribe(
            (res: HttpResponse<Mail[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadInbox() {
        $('#btn_inboxMail').addClass('btn-active');
        $('#btn_sendMail').removeClass('btn-active');
        if (this.mailChange !== 'to') {
            this.page = 0;
            this.listMail.length = 0;
        }
        this.mailChange = 'to';
        this.mailService.queryReceiverMail({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort()
        }, this.currentAccount.login).subscribe(
            (res: HttpResponse<Mail[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    lazyLoadingPage(page: any) {
        this.page = page;
        if (this.mailChange === 'from') {
            this.loadSendItems();
        } else {
            this.loadInbox();
        }
    }

    deleteMail(id?: any) {
        this.mailService.deleteMail(this.mailChange, id, this.currentAccount.login).subscribe( (res) => {
            if (res.statusText === 'OK') {
                this.mailDetail = undefined;
                const index = this.listMail.findIndex( (x) => x.id === id);
                this.listMail.splice(index, 1);
            }
        });
    }
    open(id): void {
        let attachment;
        this.mailService.findAttachment(id).subscribe((res) => {
            attachment = res.body;
            const albums: any[] = new Array();
                const albumItem: any = {
                    src: 'data:image/png' + ';base64,' + attachment.value
                };
                albums.push(albumItem);
                this._lightbox.open(albums, 0);
        });
    }
    reset() {
        this.page = 0;
        this.listMail = [];
        // this.loadAll();
        this.loadSendItems();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.listMail = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.listMail = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }
    ngOnInit() {
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
        /*this.principal.identity().then((account) => {
            this.currentAccount = account;
            if (this.currentAccount !== undefined) {
             this.loadInbox();
            }
        });
        this.activatedRoute.queryParams.subscribe((param) => {
             if (param['id'] !== undefined) {
             const id = parseInt(param['id'], 10);
             this.viewMail(id);
             //this.loadInbox();
            }
        });*/
        this.columnDefs = AppConstants.MailItems;
            this.principal.identity().then((account) => {
                this.currentAccount = account;
                if (this.currentAccount !== undefined) {
                    this.loadInbox();
                    this.activatedRoute.queryParams.subscribe((param) => {
                        if (param['id'] !== undefined) {
                            this.viewMail(parseInt(param['id'], 10));
                        }
                    });
                }
            });
        this.registerChangeInMail();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Mail) {
        return item.id;
    }
    registerChangeInMail() {
        this.eventSubscriber = this.eventManager.subscribe('mailListModification', (response) => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        if (!this.listMail) {
            this.listMail = [];
        }
        let newListMail: any[] = data;

        newListMail = newListMail.map((obj) => {
            obj.body = obj.body.replace(/\n/g, ' ');
            obj.body = JSON.parse(obj.body);
            return obj;
        });
        this.listMail = this.listMail.concat(newListMail);
        this.rowData = this.listMail;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
