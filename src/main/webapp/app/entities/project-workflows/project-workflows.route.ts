import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ProjectWorkflowsComponent } from './project-workflows.component';
import { ProjectWorkflowsDetailComponent } from './project-workflows-detail.component';
import {ProjectWorkflowsDialogComponent, ProjectWorkflowsPopupComponent} from './project-workflows-dialog.component';
import { ProjectWorkflowsDeletePopupComponent } from './project-workflows-delete-dialog.component';

@Injectable()
export class ProjectWorkflowsResolvePagingParams implements Resolve<any> {

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

export const projectWorkflowsRoute: Routes = [
    {
        path: 'project-workflows',
        component: ProjectWorkflowsComponent,
        resolve: {
            'pagingParams': ProjectWorkflowsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectWorkflows.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-workflows/:id',
        component: ProjectWorkflowsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectWorkflows.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectWorkflowsPopupRoute: Routes = [
    {
        path: 'project-workflows-new',
        component: ProjectWorkflowsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectWorkflows.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-workflows-new/:templateId',
        component: ProjectWorkflowsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectWorkflows.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-workflows/:templateId/edit',
        component: ProjectWorkflowsDialogComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectWorkflows.home.title'
        },
        canActivate: [UserRouteAccessService],
    },
    {
        path: 'project-workflows/:id/delete',
        component: ProjectWorkflowsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectWorkflows.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
