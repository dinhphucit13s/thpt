import { Component, OnInit, AfterViewInit , OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Mail } from './mail.model';
import { MailPopupService } from './mail-popup.service';
import { MailService } from './mail.service';
import {MailShowAttachmentComponent} from './mail-show-attachment/mail-show-attachment.component';
import {User, UserService} from '../../shared';
import {ProjectUsers} from '../project-users/project-users.model';
declare var jquery: any;
declare var $: any;
@Component({
    selector: 'jhi-mail-dialog',
    templateUrl: './mail-dialog.component.html',
    styleUrls: ['./mail.component.css']
})
export class MailDialogComponent implements OnInit, AfterViewInit {

    mail: Mail;
    mailFrom: any = new Array();
    tamMail: any[];
    searchMail: any;
    isSaving: boolean;
    today = new Date();
    selectedItems: any;
    users: User[];
    dropdownSettings = {};
    maxDate: any;
    minDate = new Date(this.today.getFullYear(), this.today.getMonth(), this.today.getDate(), 0, 0, 0);

    dateFormat = require('dateformat');
    attachments = new Array();
    constructor(
        private userService: UserService,
        private dialogAttachment: MatDialog,
        public activeModal: NgbActiveModal,
        private mailService: MailService,
        private eventManager: JhiEventManager
    ) {
        this.dateFormat(this.minDate, 'dd, mm, yyyy, hh:MM:ss TT');
        this.dropdownSettings = {
            enableSearch: true,
        };
        this.users = [];
    }

    ngOnInit() {
        this.isSaving = false;
    }

    ngAfterViewInit() {
        /*$('.tokenize-sample-demo1').tokenize2();
        $('.tokenize-sample-demo').tokenize2();
        setTimeout(() => {
            debugger;
            let item = $('.token-search').find('input');
            $(item).attr('(input)', 'suggestionUser()');
        }, 200);*/

        const thisObj = this;
        $('ng-select').find('input').keyup(function() {
            if ($('ng-select').find('input').val().length > 1) {
                const query = $('ng-select').find('input').val();
                thisObj.userService.querySearchMail(query).subscribe(
                    (res: HttpResponse<User[]>) => {
                        thisObj.users = res.body;
                        thisObj.users = thisObj.users.filter((x) => x.activated);
                    }
                );
            } else {
                thisObj.users = [];
            }
        });
    }

    changeTime() {
        if (this.mail.startTime !== undefined) {
            this.today = new Date(this.mail.startTime);
            this.maxDate = new Date(this.today.getFullYear(), this.today.getMonth(), this.today.getDate(), 23, 59, 59);
            this.dateFormat(this.maxDate, 'dd, mm, yyyy, hh:MM:ss TT');
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.mail.startTime !== undefined) {
            this.mail.startTime = this.dateFormat(this.mail.startTime, 'yyyy-mm-dd\'T\'HH:MM:ss');
        }
        if (this.mail.endTime !== undefined) {
            this.mail.endTime = this.dateFormat(this.mail.endTime, 'yyyy-mm-dd\'T\'HH:MM:ss');
        }
        if (this.mail.id !== undefined) {
            this.subscribeToSaveResponse(
                this.mailService.update(this.mail, this.attachments));
        } else {
            this.subscribeToSaveResponse(
                this.mailService.create(this.mail, this.attachments));
        }
    }

    onFileChange(event: any) {
        const files = event.target.files;
        for (let i = 0; i < files.length; i++) {
            this.attachments.push(files[i]);
        }
    }

    deleteAttach(attach: any) {
        const index = this.attachments.findIndex((fileAttach) => fileAttach === attach);
        this.attachments.splice(index, 1);
    }

    getAndShowAttachment(mail: any) {
        this.dialogAttachment.open(MailShowAttachmentComponent, {
            data: {model: mail, mode: 'edit'}
        });
    }
    suggestionUser() {
        if (this.searchMail.length > 2) {
            this.userService.querySearchMail(this.searchMail).subscribe(
                (res: HttpResponse<User[]>) => {
                    this.users = res.body;
                    if (this.users.length > 0) {
                        this.dropdownSettings = {
                            singleSelection: false,
                            defaultOpen: true,
                            enableSearch: false,
                            clearSearchOnClose: false,
                            idField: 'id',
                            textField: 'login',
                            allowSearchFilter: false,
                            enableCheckAll: false,
                            limitSelection: 1000000
                        };
                    }
                }
            );
        } else if (this.searchMail.length < 2) {
            if (this.users !== undefined) {
                this.users.length = 0;
                this.dropdownSettings = {
                    defaultOpen: false,
                };
            }
        }
    }

    onItemSelect(item: any) {
        this.mailFrom.push(item.login);
        this.mail.from = this.mailFrom.toString().replace(/,/g, ';');
    }

    onDeSelect(item: any) {
        const index = this.mailFrom.findIndex((x) => x === item.login);
        this.mailFrom.splice(index, 1);
        this.mail.from = this.mailFrom.toString().replace(/,/g, ';');
    }

    clearTo() {
        $('.txt_search').val('');
    }

    onAdd($event) {
        this.mailFrom.push($event.login);
        this.mail.from = this.mailFrom.toString().replace(/,/g, ';');
    }

    onRemove($event) {
        const index = this.mailFrom.findIndex((x) => x === $event.value.login);
        this.mailFrom.splice(index, 1);
        this.mail.from = this.mailFrom.toString().replace(/,/g, ';');
    }

    onBlur($event: {}) {
        this.users = [];
    }

    onClear() {
        this.mailFrom = [];
        this.mail.from = null;
        this.users = [];
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Mail>>) {
        result.subscribe((res: HttpResponse<Mail>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Mail) {
        this.eventManager.broadcast({ name: 'mailListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-mail-popup',
    template: ''
})
export class MailPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private mailPopupService: MailPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.mailPopupService
                    .open(MailDialogComponent as Component, params['id']);
            } else {
                this.mailPopupService
                    .open(MailDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
