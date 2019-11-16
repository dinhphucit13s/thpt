import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IssuesService } from '../issues';
import { ViewFeedBackService } from './view-feed-back.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {Observable} from 'rxjs/Observable';
import {Issues} from '../issues';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-view-feed-back',
    templateUrl: './view-feed-back.component.html'
})
export class ViewFeedBackComponent implements OnInit, OnDestroy {

    currentAccount: any;
    issues: any;
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    money: any;
    trackingTM_Cam: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        public activeModal: NgbActiveModal,
        private issuesService: IssuesService,
        private viewFeedBackService: ViewFeedBackService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
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
        if (this.currentSearch) {
            this.issuesService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                (res: HttpResponse<any[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
        this.issuesService.queryCampaign({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: HttpResponse<any[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/view-feed-back'], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: this.currentSearch,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll();
    }

    changeStatusIssue(issuesAttach) {
        if (issuesAttach.status !== 'NA') {
            this.subscribeToSaveResponse(
                this.issuesService.update(issuesAttach));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Issues>>) {
        result.subscribe(
            (res: HttpResponse<Issues>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Issues) {
        this.eventManager.broadcast({ name: 'feedbackListModification', content: 'OK'});
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/view-feed-back', {
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
        this.router.navigate(['/view-feed-back', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInIssues();
    }
    trackingTM_Campaign(money: any) {
        this.viewFeedBackService.trackingTM_Campaign(money).subscribe((res) => {
            this.trackingTM_Cam = res.body;
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: any) {
        return item.id;
    }

    registerChangeInIssues() {
        this.eventSubscriber = this.eventManager.subscribe('feedbackListModification', (response) => this.loadAll());
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
        this.issues = data;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
