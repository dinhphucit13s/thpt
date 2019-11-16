import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { PackagesComponent } from './packages.component';
import { PackagesDetailComponent } from './packages-detail.component';
import {PackagesDialogComponent, PackagesPopupComponent} from './packages-dialog.component';
import { PackagesDeletePopupComponent } from './packages-delete-dialog.component';
import {TasksDetailComponent} from '../tasks/tasks-detail.component';
import {TasksResolvePagingParams} from '../tasks/tasks.route';
import {TasksComponent} from '../tasks/tasks.component';
import {TaskCloneComponent} from './task-clone-dialog.component';
import {PackagesImportPopupComponent} from './packages-import-dialog.component';
import {PackagesDeliveryPopupComponent} from './packages-delivery-dialog.component';
import {PackagesCloneDialogComponent, PackagesClonePopupComponent} from './packages-clone-dialog.component';
import {TasksMoveDialogComponent} from '../tasks';
import {TaskBiddingAssignPopupDialogComponent} from '../task-bidding/task-bidding-assign-dialog.component';
import {TaskCloneDialogComponent} from './task-clone-dialog.component/task-clone-assign-dialog.component';
import {TaskClonePopupDialogComponent} from './task-clone-dialog.component/task-clone-assign-dialog.component';

@Injectable()
export class PackagesResolvePagingParams implements Resolve<any> {

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

export const packagesRoute: Routes = [
    {
        path: 'packages',
        component: PackagesComponent,
        resolve: {
            'pagingParams': PackagesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'packages/:id',
        component: TasksComponent,
        resolve: {
            'pagingParams': TasksResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
    , {
        path: 'tasksClone/:id',
        component: TaskCloneComponent,
        resolve: {
            'pagingParams': TasksResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const packagesPopupRoute: Routes = [
    {
        path: 'packages-new',
        component: PackagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packages/:id/edit',
        component: PackagesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packages-import',
        component: PackagesImportPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packages/:id/delete',
        component: PackagesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packages/:id/delivery',
        component: PackagesDeliveryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packages/:id/clone',
        component: PackagesClonePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tasks-clone-assign',
        component: TaskClonePopupDialogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBidding.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
