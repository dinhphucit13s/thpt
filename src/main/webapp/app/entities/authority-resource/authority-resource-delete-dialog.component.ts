import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AuthorityResource } from './authority-resource.model';
import { AuthorityResourcePopupService } from './authority-resource-popup.service';
import { AuthorityResourceService } from './authority-resource.service';
import {Role} from "./role.model";

@Component({
    selector: 'jhi-authority-resource-delete-dialog',
    templateUrl: './authority-resource-delete-dialog.component.html'
})
export class AuthorityResourceDeleteDialogComponent {

    authorityResource: AuthorityResource;
    role: Role;

    constructor(
        private authorityResourceService: AuthorityResourceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(name: string) {
        this.authorityResourceService.delete(name).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'authorityResourceListModification',
                content: 'Deleted an authorityResource'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-authority-resource-delete-popup',
    template: ''
})
export class AuthorityResourceDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private authorityResourcePopupService: AuthorityResourcePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.authorityResourcePopupService
                .open(AuthorityResourceDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
