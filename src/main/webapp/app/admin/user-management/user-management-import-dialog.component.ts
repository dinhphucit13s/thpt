import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import {UserModalService} from './user-modal.service';
import {UserService} from '../../shared';

@Component({
    selector: 'jhi-user-management-import-dialog',
    templateUrl: './user-management-import-dialog.component.html'
})
export class UserMgmtImportDialogComponent {
    files: any;
    isSaving: boolean;

    constructor(
        private userService: UserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    import() {
        this.isSaving = true;
        const formData: FormData = new FormData();
        formData.append('excelFile', this.files);
        this.subscribeToSaveResponse(
            this.userService.import(formData)
        );
    }
    onFileChange(fileInput: any) {
        this.files = fileInput.target.files[0];
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe((res: HttpResponse<any>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError(res));
    }
    private onSaveSuccess(result: any) {
        this.eventManager.broadcast({ name: 'userListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }
    private onSaveError(res: any) {
        this.isSaving = false;
        this.eventManager.broadcast({
            name: 'dtmsApp.httpError',
            content: res
        });

    }

}

@Component({
    selector: 'jhi-user-management-import-dialog',
    template: ''
})
export class UserMgmtImportPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userModalService: UserModalService,
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.userModalService
                .open(UserMgmtImportDialogComponent as Component);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
