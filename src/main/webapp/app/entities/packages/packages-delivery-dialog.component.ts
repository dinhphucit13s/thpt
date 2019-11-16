import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Packages } from './packages.model';
import { PackagesPopupService } from './packages-popup.service';
import { PackagesService } from './packages.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'jhi-packages-delivery-dialog',
    templateUrl: './packages-delivery-dialog.component.html'
})
export class PackagesDeliveryDialogComponent {

    packages: Packages;
    errorMessage: any;
    constructor(
        private packagesService: PackagesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelivery(id: number) {
        this.packagesService.delivery(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'packagesListModification',
                content: 'Delivery an packages'
            });
            this.activeModal.dismiss(true);
        }, (res: HttpErrorResponse) => {
            this.eventManager.broadcast({
                name: 'dtmsApp.httpError',
                content: res
            });
        });
    }
}

@Component({
    selector: 'jhi-packages-delivery-popup',
    template: ''
})
export class PackagesDeliveryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private packagesPopupService: PackagesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.packagesPopupService
                .open(PackagesDeliveryDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
