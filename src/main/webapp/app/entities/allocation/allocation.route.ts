import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import {AllocationComponent} from './allocation.component';
import {AllocationDetailComponent} from './allocation-detail.component';

@Injectable()
export class AllocationResolvePagingParams implements Resolve<any> {

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

export const allocationRoute: Routes = [
    {
        path: 'allocation',
        component: AllocationComponent,
        resolve: {
            'pagingParams': AllocationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.allocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'allocation/:id',
        component: AllocationDetailComponent,
        resolve: {
            'pagingParams': AllocationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.allocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
