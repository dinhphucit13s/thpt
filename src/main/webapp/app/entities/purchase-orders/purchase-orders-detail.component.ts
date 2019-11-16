import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { PurchaseOrders } from './purchase-orders.model';
import { PurchaseOrdersService } from './purchase-orders.service';

@Component({
    selector: 'jhi-purchase-orders-detail',
    templateUrl: './purchase-orders-detail.component.html'
})
export class PurchaseOrdersDetailComponent implements OnInit, OnDestroy {

    purchaseOrders: PurchaseOrders;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private purchaseOrdersService: PurchaseOrdersService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPurchaseOrders();
    }

    load(id) {
        this.purchaseOrdersService.find(id)
            .subscribe((purchaseOrdersResponse: HttpResponse<PurchaseOrders>) => {
                this.purchaseOrders = purchaseOrdersResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPurchaseOrders() {
        this.eventSubscriber = this.eventManager.subscribe(
            'purchaseOrdersListModification',
            (response) => this.load(this.purchaseOrders.id)
        );
    }
}
