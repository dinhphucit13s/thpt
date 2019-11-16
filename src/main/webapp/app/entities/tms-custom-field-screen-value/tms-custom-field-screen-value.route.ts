import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TMSCustomFieldScreenValueComponent } from './tms-custom-field-screen-value.component';
import { TMSCustomFieldScreenValueDetailComponent } from './tms-custom-field-screen-value-detail.component';
import { TMSCustomFieldScreenValuePopupComponent } from './tms-custom-field-screen-value-dialog.component';
import { TMSCustomFieldScreenValueDeletePopupComponent } from './tms-custom-field-screen-value-delete-dialog.component';

@Injectable()
export class TMSCustomFieldScreenValueResolvePagingParams implements Resolve<any> {

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

export const tMSCustomFieldScreenValueRoute: Routes = [
    {
        path: 'tms-custom-field-screen-value',
        component: TMSCustomFieldScreenValueComponent,
        resolve: {
            'pagingParams': TMSCustomFieldScreenValueResolvePagingParams
        },
        data: {
            
            pageTitle: 'dtmsApp.tMSCustomFieldScreenValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tms-custom-field-screen-value/:id',
        component: TMSCustomFieldScreenValueDetailComponent,
        data: {
            
            pageTitle: 'dtmsApp.tMSCustomFieldScreenValue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tMSCustomFieldScreenValuePopupRoute: Routes = [
    {
        path: 'tms-custom-field-screen-value-new',
        component: TMSCustomFieldScreenValuePopupComponent,
        data: {
            
            pageTitle: 'dtmsApp.tMSCustomFieldScreenValue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-custom-field-screen-value/:id/edit',
        component: TMSCustomFieldScreenValuePopupComponent,
        data: {
            
            pageTitle: 'dtmsApp.tMSCustomFieldScreenValue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-custom-field-screen-value/:id/delete',
        component: TMSCustomFieldScreenValueDeletePopupComponent,
        data: {
            
            pageTitle: 'dtmsApp.tMSCustomFieldScreenValue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
