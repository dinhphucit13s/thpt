import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ProjectBugListDefaultsComponent } from './project-bug-list-defaults.component';
import {ProjectBugListDefaultsCreatePopupComponent} from './project-bug-list-defaults-create-dialog.component';
import {ProjectBugListDefaultsDeletePopupComponent} from './project-bug-list-defaults-delete-dialog.component';
import {ProjectBugListDefaultDeletePopupComponent} from '../project-bug-list-default/project-bug-list-default-delete-dialog.component';

@Injectable()
export class ProjectBugListDefaultsResolvePagingParams implements Resolve<any> {

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

export const projectBugListDefaultsRoute: Routes = [
    {
        path: 'project-bug-list-defaults/:id/:project-name',
        component: ProjectBugListDefaultsComponent,
        resolve: {
            'pagingParams': ProjectBugListDefaultsResolvePagingParams
        },
        data: {

            pageTitle: 'Not yet translate'
        },
        canActivate: [UserRouteAccessService]
    },
];

 export const projectBugListDefaultsPopupRoute: Routes = [
     {
         path: 'project-bug-list-defaults-add/:id/:project-name',
         component: ProjectBugListDefaultsCreatePopupComponent,
         data: {

             pageTitle: 'Not yet translate'
         },
         canActivate: [UserRouteAccessService],
         outlet: 'popup'
     },
    {
        path: 'project-bug-list-defaults/:id/delete',
        component: ProjectBugListDefaultsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.projectBugListDefaults.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
 ];
