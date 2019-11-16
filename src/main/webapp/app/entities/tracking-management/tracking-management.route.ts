import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TrackingManagementComponent } from './tracking-management.component';

@Injectable()
export class TrackingManagementResolvePagingParams implements Resolve<any> {

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

export const trackingManagementRoute: Routes = [
    {
        path: 'tracking-management',
        component: TrackingManagementComponent,
        resolve: {
            'pagingParams': TrackingManagementResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.trackingManagement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tracking-management/:id',
        component: TrackingManagementComponent,
        resolve: {
            'pagingParams': TrackingManagementResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.trackingManagement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const trackingManagementPopupRoute: Routes = [
    {
        path: 'projects-new',
        component: TrackingManagementComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.trackingManagement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'projects/:id/edit',
        component: TrackingManagementComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.trackingManagement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'projects/:id/delete',
        component: TrackingManagementComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.trackingManagement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
];
