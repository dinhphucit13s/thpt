import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Issues } from '../issues/issues.model';
import { FeedbackService } from './feedback.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {IssuesService} from "../issues";
import {ProjectsService} from "../projects";
import {Feedback} from "./feedback.model";

@Component({
    selector: 'jhi-feedback',
    templateUrl: './feedback.component.html'
})
export class FeedbackComponent implements OnInit, OnDestroy {

currentAccount: any;
    issues: Issues[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    feedbackProjectId: number;
    listFeedback: Feedback[];

    constructor(
        private feedbackService: FeedbackService,
        private issuesService: IssuesService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private projectService: ProjectsService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        this.projectService.findFeedbackProject().subscribe( res => {
            console.log(res.body);
            this.feedbackProjectId = res.body.id;

            /*if (this.currentSearch) {
                this.issuesService.search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()}).subscribe(
                    (res: HttpResponse<Issues[]>) => this.onSuccess(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                return;
            }*/

            this.feedbackService.getListFeedbackOfUser({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()},
                this.currentAccount.login.toLocaleLowerCase(),
                this.feedbackProjectId).subscribe(
                (res: HttpResponse<Issues[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/issues'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/issues', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/issues', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.loadAll();
        this.registerChangeInIssues();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Issues) {
        return item.id;
    }

    registerChangeInIssues() {
        this.eventSubscriber = this.eventManager.subscribe('issuesListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.listFeedback = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    getFeedbackProject() {
        this.projectService.findFeedbackProject().subscribe( res => {
            console.log(res.body);
            this.feedbackProjectId = res.body.id;
            this.getListFeedbackOfUser();
        });
    }

    getListFeedbackOfUser() {
        const userLogin = this.currentAccount.login.toLocaleLowerCase();
        this.feedbackService.getListFeedbackOfUser({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, userLogin, this.feedbackProjectId).subscribe(
            res => {
                this.listFeedback = res.body;
                this.totalItems = res.headers.get('X-Total-Count');
                this.queryCount = this.totalItems;
                console.log(this.listFeedback);
            }
        );
    }
}
