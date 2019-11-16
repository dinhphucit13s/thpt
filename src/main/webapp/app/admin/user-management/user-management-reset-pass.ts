import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import {UserModalService} from './user-modal.service';
import {UserService} from '../../shared';
import {PasswordService} from '../../account/password/password.service';
import {UserMgmtDialogComponent} from './user-management-dialog.component';
import { User } from '../../shared';

@Component({
    selector: 'jhi-user-management-reset-pass',
    templateUrl: './user-management-reset-pass.component.html'
})
export class UserManagementResetPassDialogComponent {
    isSaving: boolean;
    password: string;
    error: string;
    success: string;
    account: any;
    user: User;
    constructor(
        private passwordService: PasswordService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    changePasswordUser() {
        this.isSaving = true;
            this.passwordService.savePassUser(this.password, this.user.login).subscribe(() => {
                this.error = null;
                this.success = 'OK';
                this.isSaving = false;
                this.eventManager.broadcast({ name: 'userListModification', content: 'OK'});
                this.activeModal.dismiss(this.user);
            }, () => {
                this.success = null;
                this.error = 'ERROR';
            });
    }

}
@Component({
    selector: 'jhi-user-management-reset-pass',
    template: ''
})
export class UserManagementResetComponent implements OnInit, OnDestroy {

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
            if ( params['login'] ) {
                this.routeSub = this.route.params.subscribe((params) => {
                    this.userModalService
                        .openChange(UserManagementResetPassDialogComponent as Component, params['login']);
                });
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
