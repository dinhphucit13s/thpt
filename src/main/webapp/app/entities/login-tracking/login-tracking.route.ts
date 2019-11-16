import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { LoginTrackingComponent } from './login-tracking.component';
import { LoginTrackingDetailComponent } from './login-tracking-detail.component';
import { LoginTrackingPopupComponent } from './login-tracking-dialog.component';
import { LoginTrackingDeletePopupComponent } from './login-tracking-delete-dialog.component';

export const loginTrackingRoute: Routes = [
    {
        path: 'login-tracking',
        component: LoginTrackingComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.loginTracking.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'login-tracking/:id',
        component: LoginTrackingDetailComponent,
        data: {
            pageTitle: 'dtmsApp.loginTracking.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const loginTrackingPopupRoute: Routes = [
    {
        path: 'login-tracking-new',
        component: LoginTrackingPopupComponent,
        data: {
            pageTitle: 'dtmsApp.loginTracking.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'login-tracking/:id/edit',
        component: LoginTrackingPopupComponent,
        data: {
            pageTitle: 'dtmsApp.loginTracking.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'login-tracking/:id/delete',
        component: LoginTrackingDeletePopupComponent,
        data: {
            pageTitle: 'dtmsApp.loginTracking.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
