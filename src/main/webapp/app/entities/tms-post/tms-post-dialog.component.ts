import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TmsPost } from './tms-post.model';
import { TmsPostPopupService } from './tms-post-popup.service';
import { TmsPostService } from './tms-post.service';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
    selector: 'jhi-tms-post-dialog',
    templateUrl: './tms-post-dialog.component.html',
    styleUrls: ['./tms-post-dialog.component.css']
})
export class TmsPostDialogComponent implements OnInit {

    tmsPost: TmsPost;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private tmsPostService: TmsPostService,
        private eventManager: JhiEventManager,
        private spinnerService: NgxSpinnerService,
    ) {
    }

    ngOnInit() {
    }

    onFileChange(event, post: TmsPost) {
        console.log(event.target.files);
        if (!post.attachmentsAppend) {
            post.attachmentsAppend = new Array();
        }
        if (!post.attachments) {
            post.attachments = new Array();
        }
        if (event.target.files && event.target.files.length) {
            for (let i = 0; i < event.target.files.length; i++) {
                const reader = new FileReader();
                const file = event.target.files[i];
                reader.readAsDataURL(file);
                reader.onloadend = () => {
                    const fileReader = {
                        filename: file.name,
                        fileType: file.type,
                        diskFile: '',
                        value: reader.result.split(',')[1],
                    };
                    post.attachments.push(fileReader);
                    post.attachmentsAppend.push(fileReader);
                };
            }
        }
        console.log(this.tmsPost);
    }

    removeAttachment(attach) {
        const objX = this.tmsPost.attachments.filter((x) => x !== attach);
        this.tmsPost.attachments = objX;

        if (!this.tmsPost.attachmentsRemove) {
            this.tmsPost.attachmentsRemove = new Array();
        }

        if (!this.tmsPost.attachmentsAppend) {
            this.tmsPost.attachmentsAppend = new Array();
        }
        const objRemove = this.tmsPost.attachmentsAppend.find((x) => x === attach);

        if (!objRemove) {
            this.tmsPost.attachmentsRemove.push(attach);
        }
        const objAppendX  = this.tmsPost.attachmentsAppend.filter((x) => x !== attach);
        this.tmsPost.attachmentsAppend = objAppendX;
        console.log(this.tmsPost);
    }

    detech() {
        if (!this.tmsPost.content || this.tmsPost.content.length === 0 ) {
            return true;
        }
        return false;
    }

    saveAnswer() {
        this.spinnerService.show();
        const formData: FormData = new FormData();
        formData.append('posts', new Blob([JSON.stringify(this.tmsPost)], {
            type: 'application/json'
        }));
        if (this.tmsPost.id) {
            // update
            this.tmsPostService.updatePosts(formData).subscribe( (res) => {
                console.log(res);
                this.spinnerService.hide();
                this.eventManager.broadcast({ name: 'tmsPostInThreadUpdateModification', content: 'OK', tmsPost: res.body});
                this.activeModal.dismiss(res.body);
            });
        } else {
            // create
            this.tmsPostService.createPosts(formData).subscribe( (res) => {
                this.spinnerService.hide();
                this.eventManager.broadcast({ name: 'tmsPostInThreadListModification', content: 'OK'});
                this.activeModal.dismiss(res.body);
            });
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }
}

@Component({
    selector: 'jhi-tms-post-popup',
    template: ''
})
export class TmsPostPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tmsPostPopupService: TmsPostPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tmsPostPopupService
                    .open(TmsPostDialogComponent as Component, params['id'], null);
            } else {
                this.tmsPostPopupService
                    .open(TmsPostDialogComponent as Component, null, params['threadId']);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
