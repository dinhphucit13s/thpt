import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TaskBiddingComponent } from './task-bidding.component';
import { TaskBiddingDetailComponent } from './task-bidding-detail.component';
import { TaskBiddingPopupComponent } from './task-bidding-dialog.component';
import { TaskBiddingDeletePopupComponent } from './task-bidding-delete-dialog.component';
import {TasksDeletePopupComponent} from '../tasks/tasks-delete-dialog.component';
import {TaskBiddingClassifyPopupUnbiddingComponent} from './task-bidding-classify/task-bidding-classify-dialog-unbidding.component';
import {TaskBiddingAssignPopupDialogComponent} from './task-bidding-assign-dialog.component';

@Injectable()
export class TaskBiddingResolvePagingParams implements Resolve<any> {

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

export const taskBiddingRoute: Routes = [
    {
        path: 'task-bidding',
        component: TaskBiddingComponent,
        resolve: {
            'pagingParams': TaskBiddingResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBidding.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'task-bidding/:id',
        component: TaskBiddingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBidding.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const taskBiddingPopupRoute: Routes = [
    {
        path: 'task-bidding/:id/assign',
        component: TaskBiddingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBidding.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tasks-bidding-unassign',
        component: TaskBiddingClassifyPopupUnbiddingComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.tasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'tasks-bidding-assign',
        component: TaskBiddingAssignPopupDialogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.taskBidding.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
