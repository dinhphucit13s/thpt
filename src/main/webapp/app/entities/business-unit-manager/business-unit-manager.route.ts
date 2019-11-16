import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { BusinessUnitManagerComponent } from './business-unit-manager.component';
import { BusinessUnitManagerDetailComponent } from './business-unit-manager-detail.component';
import { BusinessUnitManagerPopupComponent } from './business-unit-manager-dialog.component';
import { BusinessUnitManagerDeletePopupComponent } from './business-unit-manager-delete-dialog.component';

@Injectable()
export class BusinessUnitManagerResolvePagingParams implements Resolve<any> {

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

export const businessUnitManagerRoute: Routes = [
    {
        path: 'business-unit-manager',
        component: BusinessUnitManagerComponent,
        resolve: {
            'pagingParams': BusinessUnitManagerResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnitManager.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'business-unit-manager/:id',
        component: BusinessUnitManagerDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnitManager.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const businessUnitManagerPopupRoute: Routes = [
    {
        path: 'business-unit-manager-new',
        component: BusinessUnitManagerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnitManager.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'business-unit-manager/:id/edit',
        component: BusinessUnitManagerPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnitManager.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'business-unit-manager/:id/delete',
        component: BusinessUnitManagerDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.businessUnitManager.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
