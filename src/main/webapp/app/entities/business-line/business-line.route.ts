import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BusinessLineComponent } from './business-line.component';
import { BusinessLineDetailComponent } from './business-line-detail.component';
import { BusinessLinePopupComponent } from './business-line-dialog.component';
import { BusinessLineDeletePopupComponent } from './business-line-delete-dialog.component';

@Injectable()
export class BusinessLineResolvePagingParams implements Resolve<any> {

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

export const businessLineRoute: Routes = [
    {
        path: 'business-line',
        component: BusinessLineComponent,
        resolve: {
            'pagingParams': BusinessLineResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'business-line/:id',
        component: BusinessLineDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessLine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const businessLinePopupRoute: Routes = [
    {
        path: 'business-line-new',
        component: BusinessLinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessLine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'business-line/:id/edit',
        component: BusinessLinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessLine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'business-line/:id/delete',
        component: BusinessLineDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessLine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
