import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TMSCustomFieldComponent } from './tms-custom-field.component';
import { TMSCustomFieldDetailComponent } from './tms-custom-field-detail.component';
import { TMSCustomFieldPopupComponent } from './tms-custom-field-dialog.component';
import {TMSCustomFieldCreateComponent} from './tms-custom-field-create.component';
import { TMSCustomFieldDeletePopupComponent } from './tms-custom-field-delete-dialog.component';

@Injectable()
export class TMSCustomFieldResolvePagingParams implements Resolve<any> {

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

export const tMSCustomFieldRoute: Routes = [
    {
        path: 'tms-custom-field',
        component: TMSCustomFieldComponent,
        resolve: {
            'pagingParams': TMSCustomFieldResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tMSCustomField.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tms-custom-field/:id',
        component: TMSCustomFieldDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tMSCustomField.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tMSCustomFieldPopupRoute: Routes = [
    /*{
        path: 'tms-custom-field-new',
        component: TMSCustomFieldPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tMSCustomField.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },*/
    {
        path: 'tms-custom-field-create/:id',
        component: TMSCustomFieldCreateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tMSCustomField.home.title'
        },
        canActivate: [UserRouteAccessService],
    },
    {
        path: 'tms-custom-field/:id/edit',
        component: TMSCustomFieldPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tMSCustomField.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-custom-field/screen/:screenId/edit',
        component: TMSCustomFieldPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tMSCustomField.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-custom-field/:id/:workflowId/delete',
        component: TMSCustomFieldDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tMSCustomField.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-custom-field/:id/delete',
        component: TMSCustomFieldDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tMSCustomField.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
