import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CommentsComponent } from './comments.component';
import { CommentsDetailComponent } from './comments-detail.component';
import { CommentsPopupComponent } from './comments-dialog.component';
import { CommentsDeletePopupComponent } from './comments-delete-dialog.component';

export const commentsRoute: Routes = [
    {
        path: 'comments',
        component: CommentsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.comments.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'comments/:id',
        component: CommentsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.comments.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const commentsPopupRoute: Routes = [
    {
        path: 'comments-new',
        component: CommentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.comments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'comments/:id/edit',
        component: CommentsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.comments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'comments/:id/delete',
        component: CommentsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.comments.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
