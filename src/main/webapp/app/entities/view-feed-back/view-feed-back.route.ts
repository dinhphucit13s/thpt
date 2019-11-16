import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ViewFeedBackComponent } from './view-feed-back.component';
import {ViewFeedBackDetailComponent} from './view-feed-back-detail.component';

@Injectable()
export class ViewFeedBackResolvePagingParams implements Resolve<any> {

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

export const viewFeedBackRoute: Routes = [
    {
        path: 'view-feed-back',
        component: ViewFeedBackComponent,
        resolve: {
            'pagingParams': ViewFeedBackResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.feedback.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'view-feed-back/:id',
        component: ViewFeedBackDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dtmsApp.feedback.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
