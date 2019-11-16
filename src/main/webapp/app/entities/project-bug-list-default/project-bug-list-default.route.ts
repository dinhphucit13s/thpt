import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ProjectBugListDefaultComponent } from './project-bug-list-default.component';
import { ProjectBugListDefaultDetailComponent } from './project-bug-list-default-detail.component';
import { ProjectBugListDefaultPopupComponent } from './project-bug-list-default-dialog.component';
import { ProjectBugListDefaultDeletePopupComponent } from './project-bug-list-default-delete-dialog.component';

@Injectable()
export class ProjectBugListDefaultResolvePagingParams implements Resolve<any> {

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

export const projectBugListDefaultRoute: Routes = [
    {
        path: 'project-bug-list-default',
        component: ProjectBugListDefaultComponent,
        resolve: {
            'pagingParams': ProjectBugListDefaultResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectBugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-bug-list-default/:id',
        component: ProjectBugListDefaultDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectBugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectBugListDefaultPopupRoute: Routes = [
    {
        path: 'project-bug-list-default-new',
        component: ProjectBugListDefaultPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectBugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-bug-list-default/:id/edit',
        component: ProjectBugListDefaultPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectBugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-bug-list-default/:id/delete',
        component: ProjectBugListDefaultDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectBugListDefault.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
