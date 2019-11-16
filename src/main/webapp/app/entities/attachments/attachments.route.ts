import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { AttachmentsComponent } from './attachments.component';
import { AttachmentsDetailComponent } from './attachments-detail.component';
import { AttachmentsPopupComponent } from './attachments-dialog.component';
import { AttachmentsDeletePopupComponent } from './attachments-delete-dialog.component';

@Injectable()
export class AttachmentsResolvePagingParams implements Resolve<any> {

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

export const attachmentsRoute: Routes = [
    {
        path: 'attachments',
        component: AttachmentsComponent,
        resolve: {
            'pagingParams': AttachmentsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.attachments.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'attachments/:id',
        component: AttachmentsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.attachments.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attachmentsPopupRoute: Routes = [
    {
        path: 'attachments-new',
        component: AttachmentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.attachments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attachments/:id/edit',
        component: AttachmentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.attachments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attachments/:id/delete',
        component: AttachmentsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.attachments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
