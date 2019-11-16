import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import {PackagesComponent} from '../packages/packages.component';
import {MembersManagementComponent} from './members-management.component';
import {MembersManagementDetailComponent} from './members-management-detail.component';

@Injectable()
export class MembersManagementResolvePagingParams implements Resolve<any> {

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

export const membersManagementRoute: Routes = [
    {
        path: 'members-management',
        component: MembersManagementComponent,
        resolve: {
            'pagingParams': MembersManagementResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.membersManagement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'members-management/:userLogin',
        component: MembersManagementDetailComponent,
        resolve: {
            'pagingParams': MembersManagementResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const membersManagementPopupRoute: Routes = [
    /*{
        path: 'members-management-new',
        component: MembersManagementPopupComponent,
        data: {

            pageTitle: 'dtmsApp.purchaseOrders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purchase-orders/:id/edit',
        component: PurchaseOrdersPopupComponent,
        data: {

            pageTitle: 'dtmsApp.purchaseOrders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purchase-orders/:id/delete',
        component: PurchaseOrdersDeletePopupComponent,
        data: {

            pageTitle: 'dtmsApp.purchaseOrders.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }*/
];
