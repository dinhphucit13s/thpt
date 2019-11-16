import {Component, OnInit, OnDestroy} from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TmsThreadPopupService } from './tms-thread-popup.service';
import { TmsThreadService } from './tms-thread.service';
import {UserService} from '../../shared';
import {TmsPost} from '../tms-post';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
    selector: 'jhi-tms-thread-dialog',
    templateUrl: './tms-thread-dialog.component.html',
    styleUrls: ['./tms-thread-dialog.component.css']
})
export class TmsThreadDialogComponent implements OnInit {

    users: any[];
    projectId: any;
    questionAndAnswer: any;
    dropdownSettings = {
        singleSelection: true,
        idField: 'id',
        textField: 'userLogin',
        itemsShowLimit: 1,
        allowSearchFilter: true,
        enableCheckAll: false,
        limitSelection: 1
    };
    constructor(private userService: UserService, private spinnerService: NgxSpinnerService,
                private threadService: TmsThreadService, public activeModal: NgbActiveModal,
                private eventManager: JhiEventManager) {}

    ngOnInit() {
        this.userService.getUserActivatedByProject(this.projectId).subscribe( (res) => {
            this.users = res.body;
        });
        this.questionAndAnswer = new Object();
        this.questionAndAnswer.projectsId = this.projectId;
        this.questionAndAnswer.status = 'PUBLIC';
        this.questionAndAnswer.posts = new Array();
        this.questionAndAnswer.posts.push(new TmsPost());

    }

    onItemSelect(item: any) {
        console.log(item);
        this.questionAndAnswer.assigneeId = item.id;
    }

    onDeSelect(item: any) {
        console.log(item);
        this.questionAndAnswer.assigneeId = null;
    }

    onFileChange(event, questionAndAnswer: any) {
        console.log(event);
        if (!questionAndAnswer.posts[0].attachments) {
            questionAndAnswer.posts[0].attachments = new Array();
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
                    questionAndAnswer.posts[0].attachments.push(fileReader);
                };
            }
        }
        console.log(this.questionAndAnswer);
    }

    removeAttachment(attach) {
        const objX = this.questionAndAnswer.posts[0].attachments.filter((x) => x !== attach);
        this.questionAndAnswer.posts[0].attachments = objX;

        if (!this.questionAndAnswer.posts[0].attachmentsRemove) {
            this.questionAndAnswer.posts[0].attachmentsRemove = new Array();
        }

        if (!this.questionAndAnswer.posts[0].attachmentsAppend) {
            this.questionAndAnswer.posts[0].attachmentsAppend = new Array();
        }
        const objRemove = this.questionAndAnswer.posts[0].attachmentsAppend.find((x) => x === attach);

        if (!objRemove) {
            this.questionAndAnswer.posts[0].attachmentsRemove.push(attach);
        }
        const objAppendX  = this.questionAndAnswer.posts[0].attachmentsAppend.filter((x) => x !== attach);
        this.questionAndAnswer.posts[0].attachmentsAppend = objAppendX;
        console.log(this.questionAndAnswer.posts[0]);
    }

    saveNewQA() {
        this.spinnerService.show();
        console.log(this.questionAndAnswer);
        const formData: FormData = new FormData();
        formData.append('questionAndAnswer', new Blob([JSON.stringify(this.questionAndAnswer)], {
            type: 'application/json'
        }));
        console.log('formData ' + formData);
        this.threadService.createQuestionAndAnswer(formData).subscribe( (res) => {
                this.spinnerService.hide();
                this.activeModal.dismiss(res.body);
                this.eventManager.broadcast({ name: 'tmsThreadListModification', content: 'OK'});
                console.log(res.body);
        }
        );

    }

    detech() {
        if (!this.questionAndAnswer.title || this.questionAndAnswer.title.length === 0
            || !this.questionAndAnswer.posts[0].content || this.questionAndAnswer.posts[0].content.length === 0
            || !this.questionAndAnswer.status || this.questionAndAnswer.status.length === 0
            || !this.questionAndAnswer.assigneeId || this.questionAndAnswer.assigneeId.length === 0) {
            return true;
        }
        return false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }
}

@Component({
    selector: 'jhi-tms-thread-popup',
    template: ''
})
export class TmsThreadPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tmsThreadPopupService: TmsThreadPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tmsThreadPopupService
                    .open(TmsThreadDialogComponent as Component, params['projectId']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
