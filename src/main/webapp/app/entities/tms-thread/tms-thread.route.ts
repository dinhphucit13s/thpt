import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TmsThreadComponent } from './tms-thread.component';
import { TmsThreadDetailComponent } from './tms-thread-detail.component';
import { TmsThreadDeletePopupComponent } from './tms-thread-delete-dialog.component';
import {TmsThreadPopupComponent} from './tms-thread-dialog.component';
import {TmsThreadClosedPopupComponent} from './tms-thread-closed-dialog.component';

@Injectable()
export class TmsThreadResolvePagingParams implements Resolve<any> {

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

export const tmsThreadRoute: Routes = [
    {
        path: 'tms-thread',
        component: TmsThreadComponent,
        resolve: {
            'pagingParams': TmsThreadResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsThread.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tms-thread/:id',
        component: TmsThreadDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsThread.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tmsThreadPopupRoute: Routes = [
    {
        path: 'tms-thread-new/:projectId',
        component: TmsThreadPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsThread.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-thread/:id/edit',
        component: TmsThreadPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsThread.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-thread/:id/delete',
        component: TmsThreadDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsThread.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-thread-closed',
        component: TmsThreadClosedPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsThread.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
