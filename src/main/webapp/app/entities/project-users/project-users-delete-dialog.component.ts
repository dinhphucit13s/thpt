import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectUsers } from './project-users.model';
import { ProjectUsersPopupService } from './project-users-popup.service';
import { ProjectUsersService } from './project-users.service';

@Component({
    selector: 'jhi-project-users-delete-dialog',
    templateUrl: './project-users-delete-dialog.component.html'
})
export class ProjectUsersDeleteDialogComponent {

    projectUsers: ProjectUsers;

    constructor(
        private projectUsersService: ProjectUsersService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projectUsersService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projectUsersListModification',
                content: 'Deleted an projectUsers'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-project-users-delete-popup',
    template: ''
})
export class ProjectUsersDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projectUsersPopupService: ProjectUsersPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projectUsersPopupService
                .open(ProjectUsersDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
