import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ProjectUsersComponent } from './project-users.component';
import { ProjectUsersListComponent } from './project-users-list.component';
import { ProjectUsersDetailComponent } from './project-users-detail.component';
import { ProjectUsersPopupComponent } from './project-users-dialog.component';
import { ProjectUsersCreatePopupComponent } from './project-users-create-dialog.component';
import { ProjectUsersDeletePopupComponent } from './project-users-delete-dialog.component';
import {MembersManagementDetailComponent} from '../members-management/members-management-detail.component';
import {MembersManagementResolvePagingParams} from '../members-management/members-management.route';
import {ProjectUsersImportPopupComponent} from './project-users-import-dialog.component';

@Injectable()
export class ProjectUsersResolvePagingParams implements Resolve<any> {

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

export const projectUsersRoute: Routes = [
    {
        path: 'project-users',
        component: ProjectUsersComponent,
        resolve: {
            'pagingParams': ProjectUsersResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectUsers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-users/:id',
        component: ProjectUsersDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectUsers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-member/:userLogin',
        component: MembersManagementDetailComponent,
        resolve: {
            'pagingParams': ProjectUsersResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.packages.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-users-list/:id/:id2',
        component: ProjectUsersListComponent,
        resolve: {
            'pagingParams': ProjectUsersResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectUsers.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectUsersPopupRoute: Routes = [
    {
        path: 'project-users-new',
        component: ProjectUsersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectUsers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-users-add/:id/:id2',
        component: ProjectUsersCreatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectUsers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-users/:id/edit',
        component: ProjectUsersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectUsers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-users/:id/delete',
        component: ProjectUsersDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectUsers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-users-import/:id/:id2',
        component: ProjectUsersImportPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectUsers.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
