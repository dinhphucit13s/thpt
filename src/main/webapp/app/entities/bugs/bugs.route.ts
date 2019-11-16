import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BugsComponent } from './bugs.component';
import { BugsDetailComponent } from './bugs-detail.component';
import { BugsPopupComponent } from './bugs-dialog.component';
import { BugsDeletePopupComponent } from './bugs-delete-dialog.component';

@Injectable()
export class BugsResolvePagingParams implements Resolve<any> {

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

export const bugsRoute: Routes = [
    {
        path: 'bugs',
        component: BugsComponent,
        resolve: {
            'pagingParams': BugsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugs.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bugs/:id',
        component: BugsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugs.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bugsPopupRoute: Routes = [
    {
        path: 'bugs-new',
        component: BugsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugs.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bugs/:id/edit',
        component: BugsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugs.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bugs/:id/delete',
        component: BugsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.bugs.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
