import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TaskBiddingTrackingTimeComponent } from './task-bidding-tracking-time.component';
import { TaskBiddingTrackingTimeDetailComponent } from './task-bidding-tracking-time-detail.component';
import { TaskBiddingTrackingTimePopupComponent } from './task-bidding-tracking-time-dialog.component';
import { TaskBiddingTrackingTimeDeletePopupComponent } from './task-bidding-tracking-time-delete-dialog.component';

export const taskBiddingTrackingTimeRoute: Routes = [
    {
        path: 'task-bidding-tracking-time',
        component: TaskBiddingTrackingTimeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBiddingTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'task-bidding-tracking-time/:id',
        component: TaskBiddingTrackingTimeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBiddingTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const taskBiddingTrackingTimePopupRoute: Routes = [
    {
        path: 'task-bidding-tracking-time-new',
        component: TaskBiddingTrackingTimePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBiddingTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'task-bidding-tracking-time/:id/edit',
        component: TaskBiddingTrackingTimePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBiddingTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'task-bidding-tracking-time/:id/delete',
        component: TaskBiddingTrackingTimeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBiddingTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
