import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import {Feedback} from './feedback.model';
import { FeedbackPopupService } from './feedback-popup.service';
import { FeedbackService } from './feedback.service';
import { PurchaseOrders, PurchaseOrdersService } from '../purchase-orders';
import { Projects, ProjectsService } from '../projects';
import {MatDialog} from "@angular/material";
import {FeedbackShowAttachmentComponent} from "./feedback-show-attachment/feedback-show-attachment.component";
import {Issues} from "../issues";

@Component({
    selector: 'jhi-feedback-dialog',
    templateUrl: './feedback-dialog.component.html',
    styleUrls: ['./feedback-dialog.component.css']
})
export class FeedbackDialogComponent implements OnInit {
    issues: Issues;
    attachments = [];
    isSaving: boolean;

    purchaseorders: PurchaseOrders[];

    projects: Projects[];
    private feedbackProjectId: number;

    constructor(
        private dialogAttachment: MatDialog,
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private feedbackService: FeedbackService,
        private purchaseOrdersService: PurchaseOrdersService,
        private projectsService: ProjectsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.getFeedbackProject();
    }

    getFeedbackProject() {
        this.projectsService.findFeedbackProject().subscribe( res => {
            console.log(res.body);
            this.feedbackProjectId = res.body.id;
            this.issues.projectsId = res.body.id;
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.issues.id !== undefined) {
            this.subscribeToSaveResponse(
                this.feedbackService.update(this.issues));
        } else {
            this.subscribeToSaveResponse(
                this.feedbackService.create(this.issues, this.attachments));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Feedback>>) {
        result.subscribe(
            (res: HttpResponse<Feedback>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Feedback) {
        this.eventManager.broadcast({ name: 'issuesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPurchaseOrdersById(index: number, item: PurchaseOrders) {
        return item.id;
    }

    trackProjectsById(index: number, item: Projects) {
        return item.id;
    }

    onFileChange(event: any, feedback?: any) {
        const files = event.target.files;
        if (!this.issues.attachments) {
            for (let i = 0; i < files.length; i++) {
                this.attachments.push(files[i]);
            }
        } else {
            if (!feedback.attachmentsAppend) {
                feedback.attachmentsAppend = [];
            }
            if (event.target.files && files.length) {
                for (let i = 0; i < files.length; i++) {
                    const reader = new FileReader();
                    const file = files[i];
                    reader.readAsDataURL(file);
                    reader.onloadend = () => {
                        const fileReader = {
                            filename: file.name,
                            fileType: file.type,
                            diskFile: '',
                            value: reader.result.split(',')[1],
                        };
                        feedback.attachments.push(fileReader);
                        feedback.attachmentsAppend.push(fileReader);
                    };
                }
            }
        }
    }

    getAndShowAttachment(feedback: any) {
        this.showAttachment(feedback);
        if (feedback.attachments.findIndex(x => x.value === null) >= 0) {
            this.feedbackService.getAttachmentByIssuesId(feedback.id).subscribe(res => {
                const newAttachmentList = res.body;
                if (feedback.attachmentsAppend) {
                    newAttachmentList.concat(feedback.attachmentsAppend);
                }
                feedback.attachments = newAttachmentList;
            });
        }
    }

    showAttachment(feedback: any) {
        this.dialogAttachment.open(FeedbackShowAttachmentComponent, {
            data: {model: feedback, mode: 'edit'}
        });
    }

    deleteAttach(attach: any) {
        const index = this.attachments.findIndex((fileAttach) => fileAttach === attach);
        this.attachments.splice(index, 1);
    }
}

@Component({
    selector: 'jhi-feedback-popup',
    template: ''
})
export class FeedbackPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private issuesPopupService: FeedbackPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.issuesPopupService
                    .open(FeedbackDialogComponent as Component, params['id']);
            } else {
                this.issuesPopupService
                    .open(FeedbackDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
