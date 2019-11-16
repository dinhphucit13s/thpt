import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TMSCustomFieldScreenComponent } from './tms-custom-field-screen.component';
import { TMSCustomFieldScreenDetailComponent } from './tms-custom-field-screen-detail.component';
import { TMSCustomFieldScreenPopupComponent } from './tms-custom-field-screen-dialog.component';
// import { TMSCustomFieldScreenDeletePopupComponent } from './tms-custom-field-screen-delete-dialog.component';

@Injectable()
export class TMSCustomFieldScreenResolvePagingParams implements Resolve<any> {

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

export const tMSCustomFieldScreenRoute: Routes = [
    {
        path: 'tms-custom-field-screen',
        component: TMSCustomFieldScreenComponent,
        resolve: {
            'pagingParams': TMSCustomFieldScreenResolvePagingParams
        },
        data: {

            pageTitle: 'dtmsApp.tMSCustomFieldScreen.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tms-custom-field-screen/:id',
        component: TMSCustomFieldScreenDetailComponent,
        data: {

            pageTitle: 'dtmsApp.tMSCustomFieldScreen.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tMSCustomFieldScreenPopupRoute: Routes = [
    {
        path: 'tms-custom-field-screen-new',
        component: TMSCustomFieldScreenPopupComponent,
        data: {

            pageTitle: 'dtmsApp.tMSCustomFieldScreen.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-custom-field-screen/:id/edit',
        component: TMSCustomFieldScreenPopupComponent,
        data: {

            pageTitle: 'dtmsApp.tMSCustomFieldScreen.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    /*{
        path: 'tms-custom-field-screen/:id/delete',
        component: TMSCustomFieldScreenDeletePopupComponent,
        data: {

            pageTitle: 'dtmsApp.tMSCustomFieldScreen.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-custom-field-screen/delete',
        component: TMSCustomFieldScreenDeletePopupComponent,
        data: {

            pageTitle: 'dtmsApp.tMSCustomFieldScreen.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }*/
];
