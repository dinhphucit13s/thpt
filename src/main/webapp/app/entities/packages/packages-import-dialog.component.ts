import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { PackagesPopupService } from './packages-popup.service';
import { PackagesService } from './packages.service';
import {JhiTrackerService} from '../../shared';
declare var jquery: any;
declare var $: any;

@Component({
    selector: 'jhi-packages-import-dialog',
    templateUrl: './packages-import-dialog.component.html',
    styleUrls: ['./packages-import-dialog.component.css']
})
export class PackagesImportDialogComponent implements OnInit {
    files: any;
    isSaving: boolean;
    listContentPackages: any[] = new Array();
    listContentProgress: any[] = new Array();
    totalFileImport: number;
    isProgressDone = false;
    finishImport: any;
    beginTimeImport: any;

    constructor(
        private packagesService: PackagesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private trackerService: JhiTrackerService,
    ) {
    }

    ngOnInit() {
        this.trackerService.receive().subscribe((res) => {
            console.log(res);
            const data = JSON.parse(res);
            if (data.progressImport) {
                this.listContentProgress.push(data.progressImport);
            }
            if (data.totalRowsImport) {
                this.totalFileImport = parseInt(data.totalRowsImport, 10);
            }
            if (data.progressPackages) {
                this.listContentPackages.push(data.progressPackages);
            }
            if (data.finishImport) {
                let timeImport: any = new Date().getTime() - this.beginTimeImport;
                timeImport = '<div class="bold">Total times import: ' + this.convertMilliseconds(timeImport) + '</div>';
                this.finishImport = data.finishImport + timeImport;
                $('.progress-bar-import').css('background-color', '#1F9FFF');
            }
            setTimeout( function(){
                    const $content_progress_import = $('body').find('.progress-import-content');
                    $('body').find('.progress-import-content').scrollTop($content_progress_import.prop('scrollHeight')); }
                , 1000);
        });
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    import() {
        this.totalFileImport = 0;
        this.listContentPackages = new Array();
        this.listContentProgress = new Array();
        this.isSaving = true;
        const poId = sessionStorage.getItem('currentPurchaseOrderId');
        const formData: FormData = new FormData();
        formData.append('excelFile', this.files);
        formData.append('poId', new Blob([JSON.stringify(poId)], {
            type: 'application/json'
        }));
        this.beginTimeImport = new Date().getTime();
        this.subscribeToSaveResponse(
            this.packagesService.import(formData)
        );
        // this.packagesService.import(formData).subscribe((response) => {
        //     this.ngProgress.done();
        //     this.onSaveSuccess(response);
        // });
    }
    onFileChange(fileInput: any) {
        this.files = fileInput.target.files[0];
    }

    convertMilliseconds(mil: any) {

        let seconds: any = parseInt((mil / 1000).toString(), 10);
        let hours: any = parseInt( (seconds / 3600).toString(), 10 );
        seconds = parseInt((seconds % 3600).toString(), 10);
        let minutes: any = parseInt( (seconds / 60).toString(), 10 );
        seconds = parseInt((seconds % 60).toString(), 10);

        hours = (hours < 0) ? 0 : hours;
        minutes = (minutes < 0) ? 0 : minutes;
        seconds = (seconds < 0) ? 0 : seconds;

        hours = (hours < 10) ? '0' + hours : hours;
        minutes = (minutes < 10) ? '0' + minutes : minutes;
        seconds = (seconds < 10) ? '0' + seconds : seconds;

        return [hours, minutes, seconds].join(':');
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe((res: HttpResponse<any>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError(res));
    }
    private onSaveSuccess(result: any) {
        this.eventManager.broadcast({ name: 'packagesListModification', content: 'OK'});
        this.isSaving = false;
        this.isProgressDone = true;
        // this.activeModal.dismiss(result);
    }
    private onSaveError(res: any) {
        this.eventManager.broadcast({
            name: 'dtmsApp.httpError',
            content: res
        });
        this.isSaving = true;
    }

}

@Component({
    selector: 'jhi-packages-import-dialog',
    template: ''
})
export class PackagesImportPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private packagesPopupService: PackagesPopupService,
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.packagesPopupService
                .open(PackagesImportDialogComponent as Component);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
