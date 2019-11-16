import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TaskTrackingTimeComponent } from './task-tracking-time.component';
import { TaskTrackingTimeDetailComponent } from './task-tracking-time-detail.component';
import { TaskTrackingTimePopupComponent } from './task-tracking-time-dialog.component';
import { TaskTrackingTimeDeletePopupComponent } from './task-tracking-time-delete-dialog.component';

export const taskTrackingTimeRoute: Routes = [
    {
        path: 'task-tracking-time',
        component: TaskTrackingTimeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'task-tracking-time/:id',
        component: TaskTrackingTimeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const taskTrackingTimePopupRoute: Routes = [
    {
        path: 'task-tracking-time-new',
        component: TaskTrackingTimePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'task-tracking-time/:id/edit',
        component: TaskTrackingTimePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'task-tracking-time/:id/delete',
        component: TaskTrackingTimeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskTrackingTime.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
