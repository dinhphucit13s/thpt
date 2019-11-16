import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DtmsMonitoringComponent } from './dtms-monitoring.component';
import { DtmsMonitoringDetailComponent } from './dtms-monitoring-detail.component';
import { DtmsMonitoringPopupComponent } from './dtms-monitoring-dialog.component';
import { DtmsMonitoringDeletePopupComponent } from './dtms-monitoring-delete-dialog.component';

export const dtmsMonitoringRoute: Routes = [
    {
        path: 'dtms-monitoring',
        component: DtmsMonitoringComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.dtmsMonitoring.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'dtms-monitoring/:id',
        component: DtmsMonitoringDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.dtmsMonitoring.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dtmsMonitoringPopupRoute: Routes = [
    {
        path: 'dtms-monitoring-new',
        component: DtmsMonitoringPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.dtmsMonitoring.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'dtms-monitoring/:id/edit',
        component: DtmsMonitoringPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.dtmsMonitoring.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'dtms-monitoring/:id/delete',
        component: DtmsMonitoringDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.dtmsMonitoring.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
