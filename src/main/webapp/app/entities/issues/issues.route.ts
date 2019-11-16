import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { IssuesComponent } from './issues.component';
import { IssuesDetailComponent } from './issues-detail.component';
import { IssuesPopupComponent } from './issues-dialog.component';
import { IssuesDeletePopupComponent } from './issues-delete-dialog.component';

@Injectable()
export class IssuesResolvePagingParams implements Resolve<any> {

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

export const issuesRoute: Routes = [
    {
        path: 'issues',
        component: IssuesComponent,
        resolve: {
            'pagingParams': IssuesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.issues.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'issues/:id',
        component: IssuesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.issues.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const issuesPopupRoute: Routes = [
    {
        path: 'issues-new',
        component: IssuesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.issues.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'issues/:id/edit',
        component: IssuesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.issues.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'issues/:id/delete',
        component: IssuesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.issues.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
