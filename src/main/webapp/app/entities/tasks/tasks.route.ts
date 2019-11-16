import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TasksComponent } from './tasks.component';
import { TasksDetailComponent } from './tasks-detail.component';
import { TasksPopupComponent } from './tasks-dialog.component';
import { TasksDeletePopupComponent } from './tasks-delete-dialog.component';
import {TasksImportPopupComponent} from './tasks-import-dialog.component';

import {TasksMoveDialogComponent} from './tasks-move-dialog.component';

@Injectable()
export class TasksResolvePagingParams implements Resolve<any> {

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

export const tasksRoute: Routes = [
    {
        path: 'tasks',
        component: TasksComponent,
        resolve: {
            'pagingParams': TasksResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tasks/:id',
        component: TasksDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tasks-move',
        component: TasksMoveDialogComponent,
        resolve: {
            'pagingParams': TasksResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tasks-move/:id',
        component: TasksMoveDialogComponent,
        resolve: {
            'pagingParams': TasksResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
];

export const tasksPopupRoute: Routes = [
    {
        path: 'tasks-new',
        component: TasksPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tasks/:id/edit',
        component: TasksPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'derived/tasks/:id/:name',
        component: TasksPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tasks/:id/delete',
        component: TasksDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tasks/:package_id/import',
        component: TasksImportPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
   /* {
        path: 'tasks/:package_id/move',
        component: TasksMovePopupComponent,
        resolve: {
            'pagingParams': TasksResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }*/
];
