import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { NotificationTemplateComponent } from './notification-template.component';
import { NotificationTemplateDetailComponent } from './notification-template-detail.component';
import { NotificationTemplatePopupComponent } from './notification-template-dialog.component';
import { NotificationTemplateDeletePopupComponent } from './notification-template-delete-dialog.component';

@Injectable()
export class NotificationTemplateResolvePagingParams implements Resolve<any> {

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

export const notificationTemplateRoute: Routes = [
    {
        path: 'notification-template',
        component: NotificationTemplateComponent,
        resolve: {
            'pagingParams': NotificationTemplateResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.notificationTemplate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'notification-template/:id',
        component: NotificationTemplateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.notificationTemplate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const notificationTemplatePopupRoute: Routes = [
    {
        path: 'notification-template-new',
        component: NotificationTemplatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.notificationTemplate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'notification-template/:id/edit',
        component: NotificationTemplatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.notificationTemplate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'notification-template/:id/delete',
        component: NotificationTemplateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.notificationTemplate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
