import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TMSLogHistoryComponent } from './tms-log-history.component';
import { TMSLogHistoryDetailComponent } from './tms-log-history-detail.component';
import { TMSLogHistoryPopupComponent } from './tms-log-history-dialog.component';
import { TMSLogHistoryDeletePopupComponent } from './tms-log-history-delete-dialog.component';

@Injectable()
export class TMSLogHistoryResolvePagingParams implements Resolve<any> {

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

export const tMSLogHistoryRoute: Routes = [
    {
        path: 'tms-log-history',
        component: TMSLogHistoryComponent,
        resolve: {
            'pagingParams': TMSLogHistoryResolvePagingParams
        },
        data: {
            
            pageTitle: 'dtmsApp.tMSLogHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tms-log-history/:id',
        component: TMSLogHistoryDetailComponent,
        data: {
            
            pageTitle: 'dtmsApp.tMSLogHistory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tMSLogHistoryPopupRoute: Routes = [
    {
        path: 'tms-log-history-new',
        component: TMSLogHistoryPopupComponent,
        data: {
            
            pageTitle: 'dtmsApp.tMSLogHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-log-history/:id/edit',
        component: TMSLogHistoryPopupComponent,
        data: {
            
            pageTitle: 'dtmsApp.tMSLogHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-log-history/:id/delete',
        component: TMSLogHistoryDeletePopupComponent,
        data: {
            
            pageTitle: 'dtmsApp.tMSLogHistory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
