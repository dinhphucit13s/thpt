import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { PurchaseOrdersComponent } from './purchase-orders.component';
import { PurchaseOrdersDetailComponent } from './purchase-orders-detail.component';
import { PurchaseOrdersPopupComponent } from './purchase-orders-dialog.component';
import { PurchaseOrdersDeletePopupComponent } from './purchase-orders-delete-dialog.component';
import { PackagesComponent} from '../packages/packages.component';

@Injectable()
export class PurchaseOrdersResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const purchaseOrdersRoute: Routes = [
    {
        path: 'purchase-orders',
        component: PurchaseOrdersComponent,
        resolve: {
            'pagingParams': PurchaseOrdersResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.purchaseOrders.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'purchase-orders/:id',
        component: PackagesComponent,
        resolve: {
            'pagingParams': PurchaseOrdersResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchaseOrdersPopupRoute: Routes = [
    {
        path: 'purchase-orders-new',
        component: PurchaseOrdersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.purchaseOrders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purchase-orders/:id/edit',
        component: PurchaseOrdersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.purchaseOrders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purchase-orders/:id/delete',
        component: PurchaseOrdersDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.purchaseOrders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
