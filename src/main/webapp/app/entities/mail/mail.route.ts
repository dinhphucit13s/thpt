import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { MailComponent } from './mail.component';
import { MailDetailComponent } from './mail-detail.component';
import { MailPopupComponent } from './mail-dialog.component';
import { MailDeletePopupComponent } from './mail-delete-dialog.component';

export const mailRoute: Routes = [
    {
        path: 'mail',
        component: MailComponent,
        data: {
            pageTitle: 'dtmsApp.mail.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'mail/:id',
        component: MailDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.mail.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mailPopupRoute: Routes = [
    {
        path: 'mail-new',
        component: MailPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.mail.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mail/:id/edit',
        component: MailPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.mail.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mail/:id/delete',
        component: MailDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.mail.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
