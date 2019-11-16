import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ProjectTemplatesComponent } from './project-templates.component';
import { ProjectTemplatesDetailComponent } from './project-templates-detail.component';
import { ProjectTemplatesPopupComponent } from './project-templates-dialog.component';
import { ProjectTemplatesDeletePopupComponent } from './project-templates-delete-dialog.component';

@Injectable()
export class ProjectTemplatesResolvePagingParams implements Resolve<any> {

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

export const projectTemplatesRoute: Routes = [
    {
        path: 'project-templates',
        component: ProjectTemplatesComponent,
        resolve: {
            'pagingParams': ProjectTemplatesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectTemplates.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'project-templates/:id',
        component: ProjectTemplatesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectTemplates.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectTemplatesPopupRoute: Routes = [
    {
        path: 'project-templates-new',
        component: ProjectTemplatesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectTemplates.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-templates/:id/edit',
        component: ProjectTemplatesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectTemplates.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'project-templates/:id/delete',
        component: ProjectTemplatesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectTemplates.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
];
