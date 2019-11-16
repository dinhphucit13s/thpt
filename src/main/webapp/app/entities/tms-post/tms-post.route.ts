import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TmsPostComponent } from './tms-post.component';
import { TmsPostDetailComponent } from './tms-post-detail.component';
import { TmsPostPopupComponent } from './tms-post-dialog.component';
import { TmsPostDeletePopupComponent } from './tms-post-delete-dialog.component';

export const tmsPostRoute: Routes = [
    {
        path: 'tms-post',
        component: TmsPostComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsPost.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'tms-post/:id',
        component: TmsPostDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsPost.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const tmsPostPopupRoute: Routes = [
    {
        path: 'tms-post-new/:threadId',
        component: TmsPostPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsPost.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-post/:id/edit',
        component: TmsPostPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsPost.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tms-post/:id/delete',
        component: TmsPostDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tmsPost.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
