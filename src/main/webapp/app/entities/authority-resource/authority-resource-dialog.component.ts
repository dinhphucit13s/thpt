import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Role } from './role.model';
import { RolePermission } from './role-permission.model';
import { AuthorityResource } from './authority-resource.model';
import { AuthorityResourcePopupService } from './authority-resource-popup.service';
import { AuthorityResourceService } from './authority-resource.service';
import { DataService } from '../../shared/services/data.service';
import {LOGIN_ALREADY_USED_TYPE} from "../../shared";

@Component({
    selector: 'jhi-authority-resource-dialog',
    templateUrl: './authority-resource-dialog.component.html'
})
export class AuthorityResourceDialogComponent implements OnInit {

    role: Role;
    rolePermissions: RolePermission[];
    authorityResource: AuthorityResource[];
    isSaving: boolean;
    savingMode: string;
    resources: RolePermission[];
    errorRolePattern: string;

    constructor(
        public activeModal: NgbActiveModal,
        private authorityResourceService: AuthorityResourceService,
        private eventManager: JhiEventManager,
        private data: DataService,
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.data.currentRolePermissions.subscribe(rolePermissions => this.resources = rolePermissions);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        this.data.currentRoleSavingMode.subscribe(roleSavingMode => this.savingMode = roleSavingMode);
        let auth = new Array<AuthorityResource>();
        this.data.currentRolePermissions.subscribe(rolePermissions => this.rolePermissions = rolePermissions);
        for (let item of this.rolePermissions) {
            let newAuth = new AuthorityResource();
            newAuth.name = item.resource;
            newAuth.authorityName = this.role.name;
            newAuth.permission = item.permission;
            auth.push(newAuth);
        }
        this.role.resources = auth;
        this.errorRolePattern = null;
        if (this.savingMode !== 'CREATE') {
            this.subscribeToSaveResponse(
                this.authorityResourceService.update(this.role));
        } else {
            this.subscribeToSaveResponse(
                this.authorityResourceService.create(this.role));
        }
        this.data.resetRolePermission();
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Role>>) {
        result.subscribe((res: HttpResponse<Role>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Role) {
        this.eventManager.broadcast({ name: 'authorityResourceListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    onSelectionChange(i: number, value: number) {
        this.data.currentRolePermissions.subscribe(rolePermissions => this.rolePermissions = rolePermissions);
        this.rolePermissions[i].permission = value;
        this.data.changeRolePermissions(this.rolePermissions);
    }

    private onSaveError(res) {
        this.isSaving = false;
        if (res.status === 400 && res.error.title === "Constraint Violation") {
            this.errorRolePattern = 'ERROR';
        }
    }
}

@Component({
    selector: 'jhi-authority-resource-popup',
    template: ''
})
export class AuthorityResourcePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolePopupService: AuthorityResourcePopupService,
        private data: DataService,
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                console.log('AuthorityResourcePopupComponent have ID');
                this.data.changeRoleSavingMode('UPDATE');
                this.rolePopupService
                    .open(AuthorityResourceDialogComponent as Component, params['id']);
            } else {
                console.log('AuthorityResourcePopupComponent Not have ID');
                this.data.resetRoleSavingMode();
                this.rolePopupService
                    .open(AuthorityResourceDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
