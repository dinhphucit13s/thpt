import { Route } from '@angular/router';
import { Injectable } from '@angular/core';
import { HomeComponent } from './';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
@Injectable()
export class WorkFlowResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) { }

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

export const HOME_ROUTE: Route = {
    path: '',
    component: HomeComponent,
    resolve: {
        'pagingParams': WorkFlowResolvePagingParams
    },
    data: {
        authorities: [],
        pageTitle: 'home.title'
    }
};
