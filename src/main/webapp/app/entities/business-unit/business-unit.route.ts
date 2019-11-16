import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BusinessUnitComponent } from './business-unit.component';
import { BusinessUnitDetailComponent } from './business-unit-detail.component';
import { BusinessUnitPopupComponent } from './business-unit-dialog.component';
import { BusinessUnitDeletePopupComponent } from './business-unit-delete-dialog.component';

@Injectable()
export class BusinessUnitResolvePagingParams implements Resolve<any> {

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

export const businessUnitRoute: Routes = [
    {
        path: 'business-units',
        component: BusinessUnitComponent,
        resolve: {
            'pagingParams': BusinessUnitResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'business-unit/:id',
        component: BusinessUnitDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const businessUnitPopupRoute: Routes = [
    {
        path: 'business-unit-new',
        component: BusinessUnitPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'business-unit/:id/edit',
        component: BusinessUnitPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'business-unit/:id/delete',
        component: BusinessUnitDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
