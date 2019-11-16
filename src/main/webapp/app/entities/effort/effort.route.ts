import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import {EffortComponent} from './effort.component';

@Injectable()
export class EffortResolvePagingParams implements Resolve<any> {

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

export const effortRoute: Routes = [
    {
        path: 'effort',
        component: EffortComponent,
        resolve: {
            'pagingParams': EffortResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.trackingManagement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'effort/:id',
        component: EffortComponent,
        resolve: {
            'pagingParams': EffortResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.trackingManagement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
