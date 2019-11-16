import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BugListDefaultComponent } from './bug-list-default.component';
import { BugListDefaultDetailComponent } from './bug-list-default-detail.component';
import { BugListDefaultPopupComponent } from './bug-list-default-dialog.component';
import { BugListDefaultDeletePopupComponent } from './bug-list-default-delete-dialog.component';

@Injectable()
export class BugListDefaultResolvePagingParams implements Resolve<any> {

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

export const bugListDefaultRoute: Routes = [
    {
        path: 'bug-list-default',
        component: BugListDefaultComponent,
        resolve: {
            'pagingParams': BugListDefaultResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bug-list-default/:id',
        component: BugListDefaultDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bugListDefaultPopupRoute: Routes = [
    {
        path: 'bug-list-default-new',
        component: BugListDefaultPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bug-list-default/:id/edit',
        component: BugListDefaultPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bug-list-default/:id/delete',
        component: BugListDefaultDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
